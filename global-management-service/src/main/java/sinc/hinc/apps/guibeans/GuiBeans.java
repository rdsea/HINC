/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
@ManagedBean(name = "GuiBeans")
@ViewScoped
public class GuiBeans {

    String group;
    String broker;
    String brokerType;

    int timeout = 7000;

    String detailsPage;

    static Logger logger = LoggerFactory.getLogger(GuiBeans.class);

    static Set<DataPoint> datapoints = new HashSet<>();
    static Set<HincLocalMeta> hinclocals = new HashSet<>();
    static Set<NetworkService> networkservices = new HashSet<>();
    static Set<ControlPoint> controlpoints = new HashSet<>();

    static HINCGlobalMeta controllerMeta;

    ResourcesManagementAPI rest = (ResourcesManagementAPI) JAXRSClientFactory.create("http://localhost:8080/global-management-service-1.0/rest", ResourcesManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    HINCManagementAPI hincApi = (HINCManagementAPI) JAXRSClientFactory.create("http://localhost:8080/global-management-service-1.0/rest", HINCManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));

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
        System.out.println("Invoke set global metadata: " + group + ", " + broker + ", " + brokerType);
        HINCGlobalMeta meta = hincApi.getHINCGlobalMeta();
        meta.setBroker(broker);
        meta.setBrokerType(brokerType);
        meta.setGroup(group);
        hincApi.setHINCGlobalMeta(meta);
        getGlobalMeta();
    }

    public void queryEveryThing() {
        queryDataPoint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        queryLocalMeta();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Query resources done", "The query has finished in " + (timeout / 1000) + " seconds"));
    }

    public void queryDataPoint() {
        Set<SoftwareDefinedGateway> gateways = rest.querySoftwareDefinedGateways(timeout, null, "false");
        for (SoftwareDefinedGateway gw : gateways) {
            datapoints.addAll(gw.getDataPoints());
            controlpoints.addAll(gw.getControlPoints());
        }
    }

    public void queryLocalMeta() {
        System.out.println("Querying the datapoint");
        hinclocals.addAll(hincApi.queryHINCLocal(timeout));
        System.out.println("Query " + hinclocals.size() + " items");
    }

    public HincLocalMeta getLocalMetaByID() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uuid = request.getParameter("uuid");
        System.out.println("Querying local by ID with UUID: " + uuid);
        AbstractDAO<HincLocalMeta> metaDAO = new AbstractDAO<>(HincLocalMeta.class);
        HincLocalMeta meta = metaDAO.read(uuid);
        if (meta != null) {
            System.out.println(" --> Found a meta: " + meta.getUuid());
        } else {
            System.out.println(" --> Could not find a meta");
        }
        return meta;
    }

    public DataPoint getDataPointByResourceid() {
        System.out.println("Getting datapoint by Resource ID .......");
        Collection<DataPoint> dps = getDatapoints();
        System.out.println("Get datapoint by resource ID from Param: " + resourceID);

        for (DataPoint dp : dps) {
            if (dp.getResourceID().equals(resourceID)) {
                System.out.println("Found the datapoint: " + dp.getResourceID());
                return dp;
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
        getDatapoints();
//        System.out.println("Creating tree ....");
        TreeNode rootNode = new DefaultTreeNode("TreeData", null);

        for (HincLocalMeta local : hinclocals) {
            TreeNode gwNode = new DefaultTreeNode("device2", local.getIp(), rootNode);
//            rootNode.getChildren().add(gwNode);
//            System.out.println("   ----> Number of data point: " + datapoints.size());
            for (DataPoint dp : datapoints) {
                if (dp.getGatewayID().equals(local.getUuid())) {
                    TreeNode datapointNode = new DefaultTreeNode("datapoint2", dp.getResourceID(), gwNode);
//                    gwNode.getChildren().add(datapointNode);
                }
            }
        }
        return rootNode;
    }

    public void onDatapointProperties() {
        if (selectedNode != null) {
            System.out.println("Client want to see Properties of data point with resourceID: " + selectedNode.toString());
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

    public void updateLocalTable() {
        System.out.println("Updating the Hinc Local table ...");
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
        hinclocals.clear();
        hinclocals.addAll(hincApi.queryHINCLocal(0));
        return hinclocals;
    }

    public Set<DataPoint> getDatapoints() {
        datapoints.clear();
        datapoints.addAll(rest.queryDataPoint(0, null));
        return datapoints;
    }

    public Set<NetworkService> getNetworkservices() {
        if (networkservices.isEmpty()) {
            queryNetworkServices();
        }
        return networkservices;
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
            System.out.println("Trying to get detail page but selected node is null");
            return null;
        }
        System.out.println("Good, selected node is: " + selectedNode);
        System.out.println("Good, selected node type: " + selectedNode.getType());
        if (selectedNode.getType().equals("device2")) {
//            setDetailsPage("hincGlobal.xhtml?uuid=" + selectedNode);
        } else if (selectedNode.getType().equals("datapoint2")) {
            setDetailsPage("datapoint_properties.xhtml");  // ?uuid=" + selectedNode)
            resourceID = String.valueOf(selectedNode.getData());
        }
        System.out.println("Get detail page done: " + this.detailsPage);
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
        System.out.println("The function is invoked ! Selected node: " + selectedNode + ", detailed page: " + page);
        ajaxCount += 1;
        System.out.println("Ajax count: " + ajaxCount);
//        RequestContext.getCurrentInstance().update(":form:detailsPanel");
    }

    static int ajaxCount = 0;

    public int getAjaxCount() {
        return ajaxCount;
    }

    /**
     * DATA POINT VIEW
     */
    String selectedControlAction; // in the control point panel -> dropdown box of action
    String controlParameter;      // in the control point panel
    String lastControlResult = "unknown result";

    public ControlPoint getControlPointOfSelected() {
        logger.debug("Finding control point of the resource: " + getResourceID());
        if (getResourceID() == null || getResourceID().isEmpty()) {
            logger.debug("Trying to run control with null selected node");
            return null;
        }
        for (ControlPoint c : getControlpoints()) {
            if (c.getResourceID().equals(getResourceID()) && c.getName().equals(selectedControlAction)) {
                logger.debug("Found a control: {}", c.getName());
                return c;
            }
        }
        logger.debug("NOT FOUND a control with id: {} for resource: {}", selectedControlAction, getResourceID());
        return null;
    }

    public void invokeControlButton(ActionEvent event) {
        logger.debug("Invoke control for resource id {}, action: {}, parameter: {} " + getResourceID(), getSelectedControlAction(), getControlParameter());

        ControlPoint cp = getControlPointOfSelected();
        cp.setParameters(controlParameter);

        logger.debug("Invoking a control with \n -> GatewayID: {}\n -> ResourceID: {}\n -> Name: {}\n -> Parameters: {}", cp.getGatewayID(), cp.getResourceID(), cp.getName(), cp.getParameters());
        lastControlResult = rest.sendControl(cp.getGatewayID(), cp.getResourceID(), cp.getName(), cp.getParameters());
        logger.debug("   -----> Result: " + lastControlResult);
    }

    public List<SelectItem> getControlActionSelectItemList() {
        if (getResourceID() == null || getResourceID().isEmpty()) {
            return null;
        }
        List<SelectItem> listItems = new ArrayList<>();
        for (ControlPoint c : getControlpoints()) {
            if (c.getResourceID().equals(getResourceID())) {
                listItems.add(new SelectItem(c.getName(), c.getName()));
            }
        }
        return listItems;
    }

    public Set<ControlPoint> getControlpoints() {
        controlpoints.clear();
        controlpoints.addAll(rest.queryControlPoint(0, null));
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
}
