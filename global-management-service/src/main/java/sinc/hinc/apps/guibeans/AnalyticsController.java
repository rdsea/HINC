/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 *
 * @author hungld
 */
//TODO Remove Class
@ManagedBean(name = "AnalyticController")
@ViewScoped
public class AnalyticsController implements Serializable {

    static Logger logger = LoggerFactory.getLogger(AnalyticsController.class);
    /**
     * START THE ANALYTICS PART
     */
    static List<String> analyticAlgos = Arrays.asList("average@local", "sum@local", "btsdataingest@mqtt-bigquery");
    public static Map<String, Thread> analyticActiveList = new HashMap<>();
    public static Map<String, AnalyticRunnable> analyticRunnable = new HashMap<>();
    String[] selectedResourceID;
    String selectedNetworkService;
    String selectedActiveAnalytics;
    String analyticSelected;  // analytic functions selected

    String analyticName;
    String lastCreatingAction = "unknown";

    public List<String> getResourceIDForAnalytics() {
        List<String> resourceNames = new ArrayList<>();
        GuiBeans guibean = new GuiBeans();
        for (IoTUnit unit : guibean.getIotUnits()) {
            DataPoint dp = unit.getDatapoints().iterator().next();
            if (dp.getDataApiSettings() != null && !dp.getDataApiSettings().isEmpty()) {
                resourceNames.add(unit.getResourceID());
            }
        }
        return resourceNames;
    }

    public List<String> getNetworkServiceIDForAnalytics() {
        List<String> nwServices = new ArrayList<>();
        GuiBeans guibean = new GuiBeans();
        for (NetworkFunctionService nw : guibean.getNetworkservices()) {
            nwServices.add(nw.getAccessPoint().getEndpoint());
        }
        return nwServices;
    }

    public void setAnalyticSelected(String analyticSelected) {
        this.analyticSelected = analyticSelected;
    }

    public static class AnalyticRunnable implements Runnable {

        String[] resourceIdList;
        String name;
        String analyticFunction;
        String networkService;

        public AnalyticRunnable(String[] resourceIdList, String name, String analyticFunction, String networkService) {
            logger.debug("An analytic is spawned. Name: " + name + ", function: " + analyticFunction + ", number of resource: " + resourceIdList.length);
            this.resourceIdList = resourceIdList;
            this.name = name;
            this.analyticFunction = analyticFunction;
            this.networkService = networkService;
        }

        @Override
        public void run() {
//            analyticMap.get(name).getValues();
            while (true) {
                List<Float> dataValues = new ArrayList<>();

                for (String sensorID : resourceIdList) {
                    DataPointListener.DataSeries dataseries = DataPointListener.getUpdateDataSeries(sensorID);
                    Queue queue = dataseries.getValues();
                    if (!queue.isEmpty()) {
                        dataValues.add((Float) queue.toArray()[queue.size() - 1]);  // we need to take the tail (not the head) of the queue
                        logger.debug("Last value from resourceid: " + sensorID + ". Queue side: " + queue.size() + "/" + dataseries.QUEUE_SIZE + " item is: " + (Float) queue.toArray()[queue.size() - 1]);
                    } else {
                        logger.debug("The queue" + sensorID + " is empty");
                    }

                }
                Float currentValue = 0f;
                if (dataValues.size() != resourceIdList.length) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        logger.debug("Get an interrupted signal, now break and stop the thread");
                        break;
                    }
                    continue; // not enough data yet, e.g. at the beginning it miss some points
                }
                if (analyticFunction.equals("average@local")) {
                    currentValue = calculateAverage(dataValues);
                } else if (analyticFunction.equals("sum@local")) {
                    currentValue = calculateSum(dataValues);
                } else {
                    logger.debug("Unknown analytic function: " + analyticFunction);
                }

                DataPointListener.DataSeries dataSeries = DataPointListener.getUpdateDataSeries(name);
                dataSeries.offer(currentValue, (new Date()).toString());

                logger.debug("Running analytic done, function: " + analyticFunction + ", value: " + currentValue);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    logger.debug("Get an interrupted signal, now break and stop the thread");
                    break;
                }
            }
        }

    }

    public void stopAnalytics() {
        Thread thread = analyticActiveList.get(selectedActiveAnalytics);
        if (thread != null) {
            thread.interrupt();
            analyticActiveList.remove(selectedActiveAnalytics);
        }
    }

    ResourcesManagementAPI rest = (ResourcesManagementAPI) JAXRSClientFactory.create("http://localhost:8080/global-management-service-1.0/rest", ResourcesManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));

    private NetworkFunctionService getNetworkServiceSelectedByEndpoint() {
        GuiBeans guibean = new GuiBeans();
        for (NetworkFunctionService nw : guibean.getNetworkservices()) {
            if (nw.getAccessPoint().getEndpoint().equals(selectedNetworkService)) {
                return nw;
            }
        }
        return null;
    }

    public void doAnalytic() {
        logger.debug("DO ANALYTIC BUTTON PRESSED !!!");
        GuiBeans guibeans = new GuiBeans();
        logger.debug("Creating analytics for " + selectedResourceID.length + " resources");
        if (selectedResourceID == null || selectedResourceID.length == 0) {
            lastCreatingAction = "No datapoint is selected for the analytic.";
        } else if (analyticName == null || analyticName.isEmpty()) {
            lastCreatingAction = "An analytic need a name.";
        } else if (DataPointListener.isListeningOrAnalyzed(analyticName)) {
            // to reconfigure all the sensor to the new network

            for (String sensorid : selectedResourceID) {
                logger.debug("Forwarding datapoint: " + sensorid);
                // get control point
                int controlFound = 0;
                IoTUnit unit = guibeans.getIoTUnitByResourceid(sensorid);
                for (ControlPoint c : guibeans.getControlpoints()) {
                    // forward all the data point to the network selected
                    if (unit.getResourceID().equals(sensorid) && c.getName().equals("connect-mqtt")) {
                        logger.debug("Found a control: {}", c.getName());
                        String parameter = getNetworkServiceSelectedByEndpoint().getAccessPoint().getEndpoint() + " mysensor1234";
                        logger.debug("Call control connect-mqtt of " + sensorid + " to endpoint: " + parameter);
                        // TODO refactor this for new control point model
                        //c.setParameters(parameter);
                        rest.sendControl(unit.getHincID(), unit.getResourceID(), c.getName(), parameter);
                        controlFound = controlFound + 1;
                    }
                }
                logger.debug("  --> We found " + controlFound + " control points for sensor: " + sensorid);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            String endpointToListen = getNetworkServiceSelectedByEndpoint().getAccessPoint().getEndpoint();
            DataPointListener.makeSureListening(endpointToListen);

            analyticActiveList.get(analyticName).interrupt();
            analyticRunnable.get(analyticName).networkService = selectedNetworkService;
            analyticActiveList.remove(analyticName);
            Thread newThread = new Thread(analyticRunnable.get(analyticName));
            newThread.start();
            analyticActiveList.put(analyticName, newThread);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    rest.queryIoTUnits(10000, null, null, 0, "true");
                }
            }).start();

            lastCreatingAction = "Analytics '" + analyticName + "' is reconfigured.";

        } else {
            for (String sensorid : selectedResourceID) {
                logger.debug("Forwarding datapoint: " + sensorid);
                // get control point
                int controlFound = 0;
                IoTUnit unit = guibeans.getIoTUnitByResourceid(sensorid);
                for (ControlPoint c : guibeans.getControlpoints()) {
                    // forward all the data point to the network selected
                    if (unit.getResourceID().equals(sensorid) && c.getName().equals("connect-mqtt")) {
                        logger.debug("Found a control: {}", c.getName());
                        String parameter = getNetworkServiceSelectedByEndpoint().getAccessPoint().getEndpoint() + " mysensor1234";
                        logger.debug("Call control connect-mqtt of " + sensorid + " to endpoint: " + parameter);
                        // TODO refactor this for new controil point model
                        //c.setParameters(parameter);
                        rest.sendControl(unit.getHincID(), unit.getResourceID(), c.getName(), parameter);
                        controlFound = controlFound + 1;
                    }
                }
                logger.debug("  --> We found " + controlFound + " control points for sensor: " + sensorid);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            String endpointToListen = getNetworkServiceSelectedByEndpoint().getAccessPoint().getEndpoint();
            DataPointListener.makeSureListening(endpointToListen);

            logger.debug("Add new active list, current have: " + analyticActiveList.size());
            AnalyticRunnable runnable = new AnalyticRunnable(selectedResourceID, analyticName, analyticSelected, selectedNetworkService);
            Thread thread = new Thread(runnable);
            analyticActiveList.put(analyticName, thread);
            analyticRunnable.put(analyticName, runnable);

            thread.start();
            lastCreatingAction = "Analytic " + analyticName + " is created, " + selectedResourceID.length + " datapoint(s) is subscribed. Number of slide: " + analyticActiveList.size();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rest.queryIoTUnits(10000, null, null, 0, "true");
                }
            }).start();
        }
        logger.debug("Create analytics done. Result: " + lastCreatingAction);
    }

    public LineChartModel getAnalyticsChart() {
        logger.debug("Current have " + analyticActiveList.size() + " active analytics");
        if (selectedActiveAnalytics == null || selectedActiveAnalytics.isEmpty()) {
            selectedActiveAnalytics = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("analytics");
        }
        if (selectedActiveAnalytics == null || selectedActiveAnalytics.isEmpty()) {
            return new LineChartModel();
        }
        logger.debug(" -- Generating analytics graph: " + selectedActiveAnalytics);

        DataPointListener.DataSeries dataSeries = DataPointListener.getUpdateDataSeries(selectedActiveAnalytics);
        logger.debug(" -- Data series. Max: " + dataSeries.getMax() + ", Min: " + dataSeries.getMin() + ", queue: " + dataSeries.getValues().size());

        ChartSeries chartSeries = new ChartSeries();
        chartSeries.setLabel(selectedActiveAnalytics);

        Iterator i = dataSeries.getValues().iterator();
        int count = 0;
        while (i.hasNext()) {
            chartSeries.set(count, (float) i.next());
            count += 1;
        }
        logger.debug(" -- Start creating a line model...");
        LineChartModel lineModel = new LineChartModel();

        lineModel.setTitle(selectedActiveAnalytics);
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.setExtender("ext");

        Axis yAxis = lineModel.getAxis(AxisType.Y);

        yAxis.setLabel(selectedActiveAnalytics);
        yAxis.setMin(dataSeries.getMin());
        yAxis.setMax(dataSeries.getMax());

        lineModel.addSeries(chartSeries);
        logger.debug(" -- Exported a line model. Title: " + lineModel.getTitle());
        return lineModel;
    }

    public static Float calculateSum(List<Float> dataValues) {
        Float sum = 0f;
        StringBuilder builder = new StringBuilder();
        for (Float f : dataValues) {
            sum += f;
            builder.append(f).append(" + ");
        }
        logger.debug("Calculate sum: " + builder.toString() + " equal: " + sum);
        return sum;
    }

    public static Float calculateAverage(List<Float> dataValues) {
        return calculateSum(dataValues) / dataValues.size();
    }

    public String[] getSelectedResourceID() {
        return selectedResourceID;
    }

    public void setSelectedResourceID(String[] selectedResourceID) {
        this.selectedResourceID = selectedResourceID;
    }

    public List<String> getAnalyticAlgos() {
        return analyticAlgos;
    }

    public String getAnalyticSelected() {
        return analyticSelected;
    }

    public String getAnalyticName() {
        return analyticName;
    }

    public void setAnalyticName(String analyticName) {
        this.analyticName = analyticName;
    }

    public String getSelectedActiveAnalytics() {
        return selectedActiveAnalytics;
    }

    public void setSelectedActiveAnalytics(String selectedActiveAnalytics) {
        this.selectedActiveAnalytics = selectedActiveAnalytics;
    }

    public Map<String, Thread> getAnalyticActiveList() {
        return analyticActiveList;
    }

    public void setAnalyticActiveList(Map<String, Thread> analyticActiveListp) {
        analyticActiveList = analyticActiveListp;
    }

    public String getLastCreatingAction() {
        return lastCreatingAction;
    }

    public String getSelectedNetworkService() {
        return selectedNetworkService;
    }

    public void setSelectedNetworkService(String selectedNetworkService) {
        this.selectedNetworkService = selectedNetworkService;
    }

    public List<SelectItem> getListOfActiveAnalytics() {
        List<SelectItem> items = new ArrayList<>();
        for (String s : analyticActiveList.keySet()) {
            items.add(new SelectItem(s));
        }
        return items;
    }

    public void testButton() {
        logger.debug("THE BUTTON IS PRESSSSSSSSSSSSSSSSSED ! LIST: " + analyticActiveList.size() + ", selected active analytics: " + selectedActiveAnalytics);
        for (String s : analyticActiveList.keySet()) {
            logger.debug("Active analytics: " + s);
        }
//        RequestContext.getCurrentInstance().execute("window.open('analytics_chart.xhtml?analytics=" + analyticSelected + "', '_newtab')");
    }

    public void clearAllAnalytics() {
        logger.debug("Cleaning analytics ...");
        for (Entry<String, Thread> entry : analyticActiveList.entrySet()) {
            logger.debug("Removing analytics: " + entry.getKey());
            entry.getValue().interrupt();
            if (entry.getValue().isInterrupted()) {
                logger.debug("Stop thread done: " + entry.getKey());
            } else {
                logger.debug("Stop thread failed: " + entry.getKey());
            }
        }
        analyticActiveList.clear();
        analyticRunnable.clear();
    }

    public void updateSelectedPanels() {
        AnalyticRunnable runnable = analyticRunnable.get(selectedActiveAnalytics);
        logger.debug("update panels. Name: " + runnable.name + ", function: " + runnable.analyticFunction + ", function: " + runnable.networkService);
        selectedResourceID = runnable.resourceIdList;
        selectedNetworkService = runnable.networkService;
        analyticName = runnable.name;
        analyticSelected = runnable.analyticFunction;
    }
}
