/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package sinc.hinc.apps.guibeans;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

*/
/**
 *
 * @author hungld
 *//*

//TODO Check what this class is truly responsible for
@ManagedBean(name = "GuiBeans")
@ViewScoped
public class GuiBeans {

    String group;
    String broker;
    String brokerType;

    int timeout = 7000;

    String detailsPage;

    static Logger logger = LoggerFactory.getLogger(GuiBeans.class);

    static Set<IoTUnit> iotUnits = new HashSet<>();
    static Set<HincLocalMeta> hinclocals = new HashSet<>();
    static Set<NetworkFunctionService> networkservices = new HashSet<>();
    static Set<CloudService> cloudservices = new HashSet<>();
    static Set<ControlPoint> controlpoints = new HashSet<>();

    static HINCGlobalMeta controllerMeta;

    ResourcesManagementAPI rest = (ResourcesManagementAPI) JAXRSClientFactory.create("http://localhost:9000/global-management-service-1.0/rest", ResourcesManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    HINCManagementAPI hincApi = (HINCManagementAPI) JAXRSClientFactory.create("http://localhost:9000/global-management-service-1.0/rest", HINCManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));

    TreeNode selectedNode; // the node of the Gateway tree    

    String resourceID;

    {
        if (controllerMeta == null) {
            controllerMeta = HincConfiguration.getGlobalMeta();
        }
    }

    public HINCGlobalMeta getGlobalMeta() {
        HINCGlobalMeta meta = hincApi.getHINCGlobalMeta();
        group = meta.getGroup();
        broker = meta.getBroker();
        brokerType = meta.getBrokerType();
        return meta;
    }

    public void setGlobalMeta() {
        logger.debug("Invoke set global metadata: " + group + ", " + broker + ", " + brokerType);
        HINCGlobalMeta meta = hincApi.getHINCGlobalMeta();
        meta.setBroker(broker);
        meta.setBrokerType(brokerType);
        meta.setGroup(group);
        hincApi.setHINCGlobalMeta(meta);
        getGlobalMeta();
    }

    boolean queryUpdateFlags[] = new boolean[]{true, true};

    public void queryEveryThing() {
        Arrays.fill(queryUpdateFlags, true);
        queryIoTUnits();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        queryLocalMeta();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Query resources done", "The query has finished in " + (timeout / 1000) + " seconds"));
    }

    public void queryIoTUnits() {
        Set<IoTUnit> units = rest.queryIoTUnits(timeout, null, null, 0, "false");
        iotUnits.addAll(units);
    }

    public void queryLocalMeta() {
        logger.debug("Querying the datapoint");
        List<HincLocalMeta> results = hincApi.queryHINCLocal(timeout);
        if (results != null) {
            hinclocals.addAll(results);
            logger.debug("Query " + hinclocals.size() + " items");
        }
    }

    public HincLocalMeta getLocalMetaByID() {
        logger.debug("Querying local by ID with UUID: " + resourceID);
        AbstractDAO<HincLocalMeta> metaDAO = new AbstractDAO<>(HincLocalMeta.class);
        List<HincLocalMeta> metas = metaDAO.readAll();
        for (HincLocalMeta meta : metas) {
            if (meta.getIp().trim().equals(resourceID)) {
                logger.debug(" --> Found a meta: " + meta.getUuid());
                return meta;
            }
        }
        logger.debug(" --> Could not find a meta");
        return null;
    }

    public IoTUnit getIoTUnitByResourceid() {
        return getIoTUnitByResourceid(resourceID);
    }

    public IoTUnit getIoTUnitByResourceid(String theResourceID) {
        logger.trace("Getting datapoint by Resource ID .......");
        Collection<IoTUnit> units = getIotUnits();
        logger.trace("Get datapoint by resource ID from Param: " + theResourceID);

        for (IoTUnit unit : units) {
            if (unit.getResourceID().equals(theResourceID)) {
                logger.trace("Found the datapoint: " + unit.getResourceID());
                return unit;
            }
        }
        return null;
    }

    public TreeNode getDeviceLayoutTree() {

//        TreeNode root = new DefaultTreeNode(new TreeEventView.Document("Files", "-", "Folder"), null);
//
//        TreeNode documents = new DefaultTreeNode(new TreeEventView.Document("Documents", "-", "Folder"), root);
//        TreeNode pictures = new DefaultTreeNode(new TreeEventView.Document("Pictures", "-", "Folder"), root);
//        TreeNode movies = new DefaultTreeNode(new TreeEventView.Document("Movies", "-", "Folder"), root);
//
//        TreeNode work = new DefaultTreeNode(new TreeEventView.Document("Work", "-", "Folder"), documents);
//        TreeNode primefaces = new DefaultTreeNode(new TreeEventView.Document("PrimeFaces", "-", "Folder"), documents);
//
//        //Documents
//        TreeNode expenses = new DefaultTreeNode("datapoint2", new TreeEventView.Document("Expenses.doc", "30 KB", "Word Document"), work);
//        TreeNode resume = new DefaultTreeNode("datapoint2", new TreeEventView.Document("Resume.doc", "10 KB", "Word Document"), work);
//        TreeNode refdoc = new DefaultTreeNode("datapoint2", new TreeEventView.Document("RefDoc.pages", "40 KB", "Pages Document"), primefaces);
//
//        return root;
        getHinclocals();
        getIotUnits();
//        logger.debug("Creating tree ....");
        TreeNode rootNode = new DefaultTreeNode("TreeData", null);

        for (HincLocalMeta local : hinclocals) {
            TreeNode gwNode = new DefaultTreeNode("device2", local.getIp(), rootNode);
//            rootNode.getChildren().add(gwNode);
//            logger.debug("   ----> Number of data point: " + datapoints.size());
            for (IoTUnit unit : iotUnits) {
                if (unit.getHincID().equals(local.getUuid())) {
                    TreeNode datapointNode = new DefaultTreeNode("datapoint2", unit.getResourceID(), gwNode);
//                    gwNode.getChildren().add(datapointNode);
                }
            }
        }
        return rootNode;
    }

    public void onDatapointProperties() {
        if (selectedNode != null) {
            logger.debug("Client want to see Properties of data point with resourceID: " + selectedNode.toString());
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            RequestContext.getCurrentInstance().openDialog("datapoint_properties", options, null);
        }
    }

    public void onDatapointViewData() {
        RequestContext.getCurrentInstance().execute("window.open('datapoint_dataview.xhtml?resourceid=" + selectedNode + "', '_newtab')");
    }

    public void onDatapointViewDataFull() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("datapoint_dataview_full.xhtml?resourceid=" + selectedNode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void queryNetworkServices() {
        networkservices.addAll(rest.queryNetworkService(0, null));
    }
    public void queryCloudServices() {
        cloudservices.addAll(rest.queryCloudServices(0, null));
        logger.info("Nr of services: "+cloudservices.size());
    }
    public void updateLocalTable() {
        logger.debug("Updating the Hinc Local table ...");
        RequestContext.getCurrentInstance().update("hincLocalTable");
    }

//    public DataPoint getDataPointOfSelectedNode() {
//        String resourceid = selectedNode.toString();
//        for (DataPoint d : datapoints) {
//            if (d.getResourceID().equals(resourceid)) {
//                return d;
//            }
//        }
//        return null;
//    }
//
//    public HincLocalMeta getLocalMetaOfSelectedNode() {
//        String localIP = selectedNode.toString();
//        for (HincLocalMeta localMeta : hinclocals) {
//            if (localMeta.getIp().equals(localIP)) {
//                return localMeta;
//            }
//        }
//        return null;
//    }
    public String getGroup() {
        return getGlobalMeta().getGroup();
    }

    public String getBroker() {
        return getGlobalMeta().getBroker();
    }

    public String getBrokerType() {
        return getGlobalMeta().getBrokerType();
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType;
    }

    public Collection<HincLocalMeta> getHinclocals() {
        if (queryUpdateFlags[0] == true) {
            hinclocals.clear();
            //TODO change to async call
            List<HincLocalMeta> result = hincApi.queryHINCLocal(0);
            if (result !=null)
            hinclocals.addAll(result);
            queryUpdateFlags[0] = false;
        }
        return hinclocals;
    }

    public Collection<IoTUnit> getIotUnits() {
        if (queryUpdateFlags[1] == true) {
            iotUnits.clear();
            iotUnits.addAll(rest.queryIoTUnits(0, null, null, 0, "false"));
            queryUpdateFlags[1] = false;
        }
        return iotUnits;
    }

    public Set<NetworkFunctionService> getNetworkservices() {
        if (networkservices.isEmpty()) {
            queryNetworkServices();
        }
        return networkservices;
    }
    public Set<CloudService> getCloudservices() {
        if (cloudservices.isEmpty()) {
            queryCloudServices();
        }
        return cloudservices;
    }
    public int getTimeout() {
        return timeout / 1000;
    }

    public HINCGlobalMeta getControllerMeta() {
        return controllerMeta;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public String getSelectedNodeName() {
        if (selectedNode == null) {
            return "";
        }
        return selectedNode.toString();
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public String getResourceID() {
        return resourceID;
    }

    public String getDetailsPage() {
        if (selectedNode == null) {
            logger.debug("Trying to get detail page but selected node is null");
            return null;
        }
        logger.trace("Good, selected node is: " + selectedNode);
        logger.trace("Good, selected node type: " + selectedNode.getType());
        if (selectedNode.getType().equals("device2")) {
            setDetailsPage("gateway_properties.xhtml");
        } else if (selectedNode.getType().equals("datapoint2")) {
            setDetailsPage("datapoint_properties.xhtml");
        }
        resourceID = String.valueOf(selectedNode.getData());
        logger.trace("Get detail page done: " + this.detailsPage);
        return detailsPage;
    }

    public void setDetailsPage(String detailsPage) {
        this.detailsPage = detailsPage;
    }

    public void showMessageQueryDatapoints() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Query datapoint", "Data will be filled in the table in 10s");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        String page = getDetailsPage();
        logger.debug("The function is invoked ! Selected node: " + selectedNode + ", detailed page: " + page);
        ajaxCount += 1;
        logger.debug("Ajax count: " + ajaxCount); // make sure the data is listening if possible

        if (selectedNode.getType().equals("datapoint2")) {
            IoTUnit unit = getIoTUnitByResourceid();
            String endpoint = "unknown";
            // TODO: THIS PLACE IS HACKED, we assume that a iot unit has only 1 datapoint
            DataPoint datapoint = unit.getDatapoints().iterator().next();
            if (datapoint.getDataApiSettings() != null) {
                endpoint = datapoint.getDataApiSettings().get("url");
            }
            if (!endpoint.contains("localhost")) {
                DataPointListener.makeSureListening(endpoint);
            }
        }

    }

    static int ajaxCount = 0;

    public int getAjaxCount() {
        return ajaxCount;
    }

    */
/**
     * DATA POINT VIEW
     *//*

    String selectedControlAction; // in the control point panel -> dropdown box of action
    String controlParameter;      // in the control point panel
    String lastControlResult = "unknown result";

    public ControlPoint getControlPointOfSelected() {
        logger.debug("Finding control point of the resource: " + getResourceID());
        if (getResourceID() == null || getResourceID().isEmpty()) {
            logger.debug("Trying to run control with null selected node");
            return null;
        }
        IoTUnit unit = getIoTUnitByResourceid();
        ControlPoint control = unit.findControlpointByName(selectedControlAction);
        if (control == null) {
            logger.debug("NOT FOUND a control with id: {} for resource: {}", selectedControlAction, getResourceID());
            return null;
        }
        return control;
    }

    public void invokeControlButton(ActionEvent event) {
        logger.debug("Invoke control for resource id {}, action: {}, parameter: {} " + getResourceID(), getSelectedControlAction(), getControlParameter());

        ControlPoint cp = getControlPointOfSelected();
        IoTUnit unit = getIoTUnitByResourceid();
        // TODO refactor for new control point model
        //cp.setParameters(controlParameter);

        logger.debug("Invoking a control with \n -> GatewayID: {}\n -> ResourceID: {}\n -> Name: {}\n -> Parameters: {}", unit.getHincID(), unit.getResourceID(), cp.getName(), cp.getParameters());
        String lastControlResultTmp = rest.sendControl(unit.getHincID(), unit.getResourceID(), cp.getName(), ""); //cp.getParameters()); TODO refactor for new control point model
        if (!lastControlResultTmp.startsWith("Cannot find")) {
            lastControlResult = lastControlResultTmp;
        } else {
            lastControlResult = "Action done !";
        }

        logger.debug("   -----> Result: " + lastControlResult);
        new Thread(new Runnable() {
            @Override
            public void run() {
                rest.queryIoTUnits(5000, null, null, 0, "true");
            }
        }).start();
    }

    public List<SelectItem> getControlActionSelectItemList() {
        if (getResourceID() == null || getResourceID().isEmpty()) {
            return null;
        }
        List<SelectItem> listItems = new ArrayList<>();
        for (IoTUnit unit : getIotUnits()) {
            if (unit.getResourceID().equals(getResourceID())) {
                for (ControlPoint c : unit.getControlpoints()) {
                    listItems.add(new SelectItem(c.getName(), c.getName()));
                }
            }
        }
        return listItems;
    }

    public Set<ControlPoint> getControlpoints() {
        controlpoints.clear();
        controlpoints.addAll(rest.queryControlPoint(0, null, null));
        return controlpoints;
    }

    public String getSelectedControlAction() {
        return selectedControlAction;
    }

    public String getControlParameter() {
        return controlParameter;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getLastControlResult() {
        return lastControlResult;
    }

    public void setSelectedControlAction(String selectedControlAction) {
        this.selectedControlAction = selectedControlAction;
    }

    public void setControlParameter(String controlParameter) {
        this.controlParameter = controlParameter;
    }

    public LineChartModel getUpdatedChart() {
        logger.debug("Get update char for resource:: " + resourceID);
        IoTUnit unit = getIoTUnitByResourceid();
        DataPointListener.DataSeries dataseries = DataPointListener.getUpdateDataSeries(resourceID);
        Queue queue = dataseries.getValues();
        Iterator<Float> i = queue.iterator();
        ChartSeries series = new ChartSeries();
        series.setLabel(resourceID);

        for (int counter = 0; counter <= DataPointListener.DataSeries.QUEUE_SIZE; counter++) {
            if (DataPointListener.DataSeries.QUEUE_SIZE < (counter + queue.size())) { // padding null data
                Float value = i.next();
                series.set(counter, (float) value);
            } else {
                series.set(counter, null); // padding null to the beginning of the series
            }
        }

        LineChartModel model = new LineChartModel();

        model.setTitle(unit.getResourceID());
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.setExtender("ext");

//        model.getAxes().put(AxisType.X, new CategoryAxis("Time"));
        Axis yAxis = model.getAxis(AxisType.Y);
        // TODO: HACK, to assume that the IoT Unit have a single datapoint
        DataPoint datapoint = unit.getDatapoints().iterator().next();
        yAxis.setLabel(datapoint.getDatatype());
        yAxis.setMin(dataseries.getMin());
        yAxis.setMax(dataseries.getMax());
        model.addSeries(series);
        return model;
    }

}
*/
