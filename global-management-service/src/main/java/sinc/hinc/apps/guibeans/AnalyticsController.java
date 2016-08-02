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
import java.util.logging.Level;
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
import sinc.hinc.model.VirtualNetworkResource.NetworkService;

/**
 *
 * @author hungld
 */
@ManagedBean(name = "AnalyticController")
@ViewScoped
public class AnalyticsController implements Serializable {

    static Logger logger = LoggerFactory.getLogger(AnalyticsController.class);
    /**
     * START THE ANALYTICS PART
     */
    static List<String> analyticAlgos = Arrays.asList("average", "sum");
    public static Map<String, Thread> analyticActiveList = new HashMap<>();
    String[] selectedResourceID;
    String selectedNetworkService;
    String selectedActiveAnalytics;
    String analyticSelected;  // analytic functions selected

    String analyticName;
    String lastCreatingAction = "unknown";

    public List<String> getResourceIDForAnalytics() {
        List<String> resourceNames = new ArrayList<>();
        GuiBeans guibean = new GuiBeans();
        for (DataPoint dp : guibean.getDatapoints()) {
            resourceNames.add(dp.getResourceID());
        }
        return resourceNames;
    }

    public List<String> getNetworkServiceIDForAnalytics() {
        List<String> nwServices = new ArrayList<>();
        GuiBeans guibean = new GuiBeans();
        for (NetworkService nw : guibean.getNetworkservices()) {
            nwServices.add(nw.getAccessPoint().getEndpoint());
        }
        return nwServices;
    }

    public void setAnalyticSelected(String analyticSelected) {
        this.analyticSelected = analyticSelected;
    }

    public static class AnalyticThread implements Runnable {

        String[] resourceIdList;
        String name;
        String analyticFunction;

        public AnalyticThread(String[] resourceIdList, String name, String analyticFunction) {
            System.out.println("An analytic is spawned. Name: " + name + ", function: " + analyticFunction + ", number of resource: " + resourceIdList.length);
            this.resourceIdList = resourceIdList;
            this.name = name;
            this.analyticFunction = analyticFunction;
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
                        System.out.println("Last value from resourceid: " + sensorID + ". Queue side: " + queue.size() + "/" + dataseries.QUEUE_SIZE + " item is: " + (Float) queue.toArray()[queue.size() - 1]);
                    }

                }
                Float currentValue = 0f;
                if (dataValues.size() != resourceIdList.length) {
                    continue; // not enough data yet, e.g. at the beginning it miss some points
                }
                if (analyticFunction.equals("average")) {
                    currentValue = calculateAverage(dataValues);
                } else if (analyticFunction.equals("sum")) {
                    currentValue = calculateSum(dataValues);
                } else {
                    System.out.println("Unknown analytic function: " + analyticFunction);
                }

                DataPointListener.DataSeries dataSeries = DataPointListener.getUpdateDataSeries(name);
                dataSeries.offer(currentValue, (new Date()).toString());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Running analytic done, function: " + analyticFunction + ", value: " + currentValue);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(AnalyticsController.class.getName()).log(Level.SEVERE, null, ex);
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

    private NetworkService getNetworkServiceSelectedByEndpoint() {
        GuiBeans guibean = new GuiBeans();
        for (NetworkService nw : guibean.getNetworkservices()) {
            if (nw.getAccessPoint().getEndpoint().equals(selectedNetworkService)) {
                return nw;
            }
        }
        return null;
    }

    public void doAnalytic() {
        System.out.println("DO ANALYTIC BUTTON PRESSED !!!");
        GuiBeans guibeans = new GuiBeans();
        System.out.println("Creating analytics for " + selectedResourceID.length + " resources");
        if (selectedResourceID == null || selectedResourceID.length == 0) {
            lastCreatingAction = "No datapoint is selected for the analytic.";
        } else if (analyticName == null || analyticName.isEmpty()) {
            lastCreatingAction = "An analytic need a name.";
        } else if (DataPointListener.isListeningOrAnalyzed(analyticName)) {
            lastCreatingAction = "Analytic name '" + analyticName + "' is existed.";
        } else {
            for (String sensorid : selectedResourceID) {
                System.out.println("Forwarding datapoint: " + sensorid);
                // get control point
                int controlFound = 0;
                for (ControlPoint c : guibeans.getControlpoints()) {
                    // forward all the data point to the network selected
                    if (c.getResourceID().equals(sensorid) && c.getName().equals("connect-mqtt")) {
                        logger.debug("Found a control: {}", c.getName());
                        String parameter = getNetworkServiceSelectedByEndpoint().getAccessPoint().getEndpoint() + " mysensor1234";
                        System.out.println("Call control connect-mqtt of " + sensorid + " to endpoint: " + parameter);
                        c.setParameters(parameter);
                        rest.sendControl(c.getGatewayID(), c.getResourceID(), c.getName(), parameter);
                        controlFound = controlFound + 1;                        
                    }
                }
                System.out.println("  --> We found " + controlFound + " control points for sensor: " + sensorid);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Add new active list, current have: " + analyticActiveList.size());            
            Thread thread = new Thread(new AnalyticThread(selectedResourceID, analyticName, analyticSelected));
            analyticActiveList.put(analyticName, thread);
            thread.start();
            lastCreatingAction = "Analytic created, " + selectedResourceID.length + " datapoint(s) is subscribed.";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rest.querySoftwareDefinedGateways(10000, null, "true");
                }
            }).start();
        }
        System.out.println("Create analytics done. Result: " + lastCreatingAction);
    }

    public LineChartModel getAnalyticsChart() {
        System.out.println("Current have " + analyticActiveList.size() + " active analytics");
        if (selectedActiveAnalytics == null || selectedActiveAnalytics.isEmpty()) {
            selectedActiveAnalytics = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("analytics");
        }
        if (selectedActiveAnalytics == null || selectedActiveAnalytics.isEmpty()) {
            return new LineChartModel();
        }
        System.out.println("Generating analytics graph: " + selectedActiveAnalytics);

        DataPointListener.DataSeries dataSeries = DataPointListener.getUpdateDataSeries(selectedActiveAnalytics);

        ChartSeries chartSeries = new ChartSeries();
        chartSeries.setLabel(selectedActiveAnalytics);

        Iterator i = dataSeries.getValues().iterator();
        int count = 0;
        while (i.hasNext()) {
            chartSeries.set(count, (float) i.next());
            count += 1;
        }
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
        return lineModel;
    }

    public static Float calculateSum(List<Float> dataValues) {
        Float sum = 0f;
        StringBuilder builder = new StringBuilder();
        for (Float f : dataValues) {
            sum += f;
            builder.append(f).append(" + ");
        }
        System.out.println("Calculate sum: " + builder.toString() + " equal: " + sum);
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
        System.out.println("THE BUTTON IS PRESSSSSSSSSSSSSSSSSED ! LIST: " + analyticActiveList.size() + ", selected active analytics: " + selectedActiveAnalytics);
        for (String s : analyticActiveList.keySet()) {
            System.out.println("Active analytics: " + s);
        }
//        RequestContext.getCurrentInstance().execute("window.open('analytics_chart.xhtml?analytics=" + analyticSelected + "', '_newtab')");
    }

    public void clearAllAnalytics() {
        System.out.println("Cleaning analytics ...");
        for (Entry<String, Thread> entry : analyticActiveList.entrySet()) {
            System.out.println("Removing analytics: " + entry.getKey());
            entry.getValue().interrupt();
            if (entry.getValue().isInterrupted()) {
                System.out.println("Stop thread done: " + entry.getKey());
            } else {
                System.out.println("Stop thread failed: " + entry.getKey());
            }
        }
        analyticActiveList.clear();
    }
}
