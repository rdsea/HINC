package sinc.hinc.clientgui.mainpanel;

import sinc.hinc.clientgui.UserSettings;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import sinc.hinc.common.API.HINCGlobalAPI;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.InfoSourceSettings;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;

public class MainPanelController implements Initializable {

    HINCGlobalAPI rest = (HINCGlobalAPI) JAXRSClientFactory.create(UserSettings.getHINCGlobalRESTEndpoint(), HINCGlobalAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    HINCManagementAPI mngAPI = (HINCManagementAPI) JAXRSClientFactory.create(UserSettings.getHINCGlobalRESTEndpoint(), HINCManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    int updateHINCTimeout = 2000;
    int queryDatapointTimeout = 3000;

    @FXML
    VBox CapabilityContainer;
    @FXML
    VBox HINCLocalContainer;
    @FXML
    VBox centerVbox;
    @FXML
    VBox leftVbox;
    @FXML
    TextArea logTextArea;
    @FXML
    Label globalMetaText;
        
    @FXML
    Slider timeoutSlider;
    @FXML
    VBox statusBar;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initiating ....");
        // load HINC Local on the left panel
        List<HincLocalMeta> metas = mngAPI.queryHINCLocal(0);
        showListOfProvider(metas);
        // load DataPoint on the right panel
        List<DataPoint> datapoints = rest.queryDataPoint(0, null);
        showListOfDatapoints(datapoints);

        switchButton.setWrapText(true);

        int m = 2;
        BorderPane.setMargin(centerVbox, new Insets(m, m, m, m));
        BorderPane.setMargin(leftVbox, new Insets(m, m, m, m));
        BorderPane.setMargin(logTextArea, new Insets(m, m, m, m));

        HINCGlobalMeta globalMeta = mngAPI.getHINCGlobalMeta();

        writeLog("HINC Global configure: \n  MOM: " + globalMeta.getBroker() + "\n  Group: " + globalMeta.getGroup() + "\n  ISP: " + globalMeta.getIsp() + "\n  Location: " + globalMeta.getCity()+"/"+globalMeta.getCountry());

        StringBuilder builder = new StringBuilder();
        builder.append("MOM: ").append(globalMeta.getBroker()).append(" -- ");
        builder.append("Group: ").append(globalMeta.getGroup()).append("\n");
        builder.append("Location: ").append(globalMeta.getCity()).append("/").append(globalMeta.getCountry()).append(" -- ");
        builder.append("ISP: ").append(globalMeta.getIsp());
        globalMetaText.setText(builder.toString());

        writeLog("Loading list of provider and HINC Local done!"); 

        // create a slider to define the timeout
        
        timeoutSlider.setMin(2);
        timeoutSlider.setMax(15);
        timeoutSlider.setValue(8);
        timeoutSlider.setShowTickLabels(true);
        timeoutSlider.setShowTickMarks(true);
        timeoutSlider.setMajorTickUnit(1);
        timeoutSlider.setMinorTickCount(0);
        timeoutSlider.setSnapToTicks(true);
        timeoutSlider.setBlockIncrement(1);        
        Tooltip tt = new Tooltip("Timeout defines the time that HINC Global waiting for HINC Local to reply.\n Higher timeout ensures a query returns all the information.");
        tt.setStyle("color:black;background-color:orange;");
        timeoutSlider.setTooltip(tt);
        timeoutSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    if (updateHINCTimeout!=new_val.intValue()*1000){
                        writeLog("Timeout changed to : " + new_val.intValue() +" seconds");
                    }
                    updateHINCTimeout = new_val.intValue() * 1000;
                    queryDatapointTimeout = new_val.intValue() * 1000;
            }
        });
    }

    boolean providerView = true;
    @FXML
    Button switchButton;

    // the switchOrNot = true will reverse the view, or false to just redraw the view
    private void showBaseOnSwitch(List<HincLocalMeta> metas, boolean switchOrNot) {
        if (providerView == switchOrNot) {
            showListOfHINCLocal(metas);
            switchButton.setText("HINC view");
            providerView = false;
        } else {
            showListOfProvider(metas);
            switchButton.setText("Provider view");
            providerView = true;
        }
    }

    @FXML
    private void handleButtonSwitchView(ActionEvent event) {
        List<HincLocalMeta> metas = mngAPI.queryHINCLocal(0);
        showBaseOnSwitch(metas, true);
    }

    @FXML
    private void handleButtonUpdateHINCLocal(ActionEvent event) {
        System.out.println("Starting to update the HINC Local list....");
        HINCLocalContainer.getChildren().clear();
        HINCLocalContainer.getChildren().add(new Text("Updating ...."));
        writeLog("Updated HINC Local services in " + (updateHINCTimeout / 1000) + " seconds.");
        List<HincLocalMeta> metas = mngAPI.queryHINCLocal(updateHINCTimeout);
        showBaseOnSwitch(metas, false);
    }

    boolean expanding = true;
    @FXML
    Button expandButton;

    @FXML
    private void handleButtonExpand(ActionEvent event) {
        if (expanding) {
            for (HINCLocalItem item : hincLocalList) {
                item.expanded.setExpanded(false);
            }
            expanding = false;
            expandButton.setText("Expand");
        } else {
            for (HINCLocalItem item : hincLocalList) {
                item.expanded.setExpanded(true);
            }
            expanding = true;
            expandButton.setText("Collapse");
        }
    }

    boolean checkedAll = true;
    @FXML
    Button checkAllButton;

    @FXML
    private void handleButtonCheckAll(ActionEvent event) {
        if (providerView) {
            return;
        }
        if (checkedAll) {
            for (HINCLocalItem item : hincLocalList) {
                item.checked.setSelected(false);
            }
            checkedAll = false;
            checkAllButton.setText("Check");
        } else {
            for (HINCLocalItem item : hincLocalList) {
                item.checked.setSelected(true);
            }
            checkedAll = true;
            checkAllButton.setText("Uncheck");
        }
    }

    @FXML
    private void handleButtonQuery(ActionEvent event) {
        List<DataPoint> datapoints = rest.queryDataPoint(queryDatapointTimeout, null);
        showListOfDatapoints(datapoints);
        writeLog("Updated the list of datapoints in " + (queryDatapointTimeout / 1000) + " seconds.");
    }

    public MainPanelController() {
        System.out.println("Construction of controller ...");
    }

    static class HINCLocalItem {

        String uuid;
        CheckBox checked;
        TitledPane expanded;

        public HINCLocalItem(String uuid, CheckBox checked, TitledPane expaneded) {
            this.uuid = uuid;
            this.checked = checked;
            this.expanded = expaneded;
        }
    }

    List<HINCLocalItem> hincLocalList = new ArrayList<>();

    public void showListOfHINCLocal(List<HincLocalMeta> metas) {
        System.out.println("Generating HINC Local list...");
        HINCLocalContainer.getChildren().clear();
        hincLocalList.clear();
        for (HincLocalMeta local : metas) {
            final TableView<BasicPropertyData> table = new TableView<>();
            System.out.println("Get HINC Local: " + local.toJson());

            HBox hincItemHbox = new HBox();
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            InfoSourceSettings sourceSettings = InfoSourceSettings.fromJson(local.getSettings());
            ObservableList<BasicPropertyData> data
                    = FXCollections.observableArrayList(
                            new BasicPropertyData("IP", local.getIp()),
                            new BasicPropertyData("City", local.getCity() + "/" + local.getCountry()),
                            new BasicPropertyData("Lat/lon", local.getLat() + "/" + local.getLon()),
                            new BasicPropertyData("ISP", local.getIsp()),
                            new BasicPropertyData("Provider", sourceSettings.getSource().size() + "")
                    );

            table.setEditable(true);
            table.setPrefHeight(130);

            TableColumn firstNameCol = new TableColumn("Name");
            firstNameCol.setCellValueFactory(new PropertyValueFactory<BasicPropertyData, String>("name"));

            TableColumn secondNameCol = new TableColumn("Value");
            secondNameCol.setCellValueFactory(new PropertyValueFactory<BasicPropertyData, String>("value"));

            table.setItems(data);
            table.getColumns().addAll(firstNameCol, secondNameCol);

            table.setBorder(Border.EMPTY);
            table.setStyle("-fx-table-cell-border-color: transparent;");
            table.setBackground(Background.EMPTY);

            table.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    // Get the table header
                    Pane header = (Pane) table.lookup("TableHeaderRow");
                    if (header != null && header.isVisible()) {
                        header.setMaxHeight(0);
                        header.setMinHeight(0);
                        header.setPrefHeight(0);
                        header.setVisible(false);
                        header.setManaged(false);
                    }
                }
            });

            TitledPane titlePanel = new TitledPane(local.getUuid().substring(0, 32), table);
            titlePanel.setExpanded(true);
            titlePanel.setStyle("-fx-pref-width: 350;");
            titlePanel.setContentDisplay(ContentDisplay.LEFT);

            hincLocalList.add(new HINCLocalItem(local.getUuid(), checkBox, titlePanel));

            hincItemHbox.getChildren().add(checkBox);
            hincItemHbox.getChildren().add(titlePanel);

            HINCLocalContainer.getChildren().add(hincItemHbox);
        }
    }

    public void showListOfProvider(List<HincLocalMeta> metas) {

        System.out.println("Generating provider list...");
        HINCLocalContainer.getChildren().clear();
        hincLocalList.clear();
        Font font = new Font(java.awt.Font.MONOSPACED, 14);

        for (HincLocalMeta local : metas) {
            System.out.println("Get HINC Local: " + local.toJson());

            InfoSourceSettings sourceSettings = InfoSourceSettings.fromJson(local.getSettings());

            for (InfoSourceSettings.InfoSource source : sourceSettings.getSource()) {
                ObservableList<BasicPropertyData> data
                        = FXCollections.observableArrayList(
                                new BasicPropertyData("Type", source.getType() + ""),
                                new BasicPropertyData("Interval", source.getInterval() + ""),
                                new BasicPropertyData("Adaptor", source.getAdaptorClass()),
                                new BasicPropertyData("Transformer", source.getTransformerClass()),
                                new BasicPropertyData("HINC/IP:", local.getIp()),
                                new BasicPropertyData("HINC/uuid:", local.getUuid())
                        );

                final TableView<BasicPropertyData> table = new TableView<>();
                table.setEditable(true);
                table.setPrefHeight(170);

                TableColumn firstNameCol = new TableColumn("Name");
                firstNameCol.setCellValueFactory(new PropertyValueFactory<BasicPropertyData, String>("name"));

                TableColumn secondNameCol = new TableColumn("Value");
                secondNameCol.setCellValueFactory(new PropertyValueFactory<BasicPropertyData, String>("value"));

                table.setItems(data);
                table.getColumns().addAll(firstNameCol, secondNameCol);

                table.setBorder(Border.EMPTY);
                table.setStyle("-fx-table-cell-border-color: transparent;");
                table.setBackground(Background.EMPTY);

                table.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                        // Get the table header
                        Pane header = (Pane) table.lookup("TableHeaderRow");
                        if (header != null && header.isVisible()) {
                            header.setMaxHeight(0);
                            header.setMinHeight(0);
                            header.setPrefHeight(0);
                            header.setVisible(false);
                            header.setManaged(false);
                        }
                    }
                });

                TitledPane titlePane = new TitledPane(source.getType() + " provider: " + source.getName(), table);
                titlePane.setExpanded(true);

                hincLocalList.add(new HINCLocalItem(local.getUuid(), null, titlePane));
                HINCLocalContainer.getChildren().add(titlePane);

            }
        }
    }

    private void showListOfDatapoints(List<DataPoint> datapoints) {
        if (datapoints != null && !datapoints.isEmpty()) {
            Map<String, String> properties = new LinkedHashMap<>();
            for (DataPoint dp : datapoints) {
                properties.put("UUID", dp.getUuid());
                properties.put("Name", dp.getName());
                properties.put("DataType", dp.getDatatype());
                properties.put("Unit", dp.getMeasurementUnit());
                properties.put("PhysicalID", dp.getResourceID());
                properties.put("Management", dp.getManagementClass());
                properties.put("Link", dp.getLink());
                properties.put("Description", dp.getDescription());

                StringBuilder text = new StringBuilder();
                for (String key : properties.keySet()) {
                    if (properties.get(key) != null && !properties.get(key).isEmpty() && !properties.get(key).equals("null")) {
                        text.append(key).append(" : ").append(properties.get(key)).append("\n");
                    }
                }
                Font font = new Font(java.awt.Font.MONOSPACED, 14);

                Text theText = new Text(text.toString().trim());
                theText.setFont(font);
                theText.setTextAlignment(TextAlignment.LEFT);

                TitledPane titlePanel = new TitledPane(dp.getName() + " | " + dp.getUuid().substring(0, 12), theText);
                titlePanel.setFont(font);
                titlePanel.setExpanded(false);
                titlePanel.setStyle("-fx-pref-width: 500;");
                titlePanel.setContentDisplay(ContentDisplay.LEFT);
                titlePanel.setAnimated(false);

                CapabilityContainer.getChildren().add(titlePanel);
            }
        }

    }

    private void writeLog(String line) {
        logTextArea.setEditable(false);
//        logTextArea.setText(logTextArea.getText() + "\n" + line);
        logTextArea.appendText("\n" + line);
        logTextArea.setScrollTop(Double.MAX_VALUE);
    }

}
