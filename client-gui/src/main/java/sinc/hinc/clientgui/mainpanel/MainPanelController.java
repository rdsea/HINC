package sinc.hinc.clientgui.mainpanel;

import sinc.hinc.clientgui.UserSettings;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    HINCGlobalAPI rest = (HINCGlobalAPI) JAXRSClientFactory.create(UserSettings.getDefaultEndpoint(), HINCGlobalAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    HINCManagementAPI mngAPI = (HINCManagementAPI) JAXRSClientFactory.create(UserSettings.getDefaultEndpoint(), HINCManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
    final int updateHINCTimeout = 2000;
    final int queryDatapointTimeout = 3000;

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

        writeLog("HINC Global configure: \n  MOM: " + globalMeta.getBroker() + "\n  Com. group: " + globalMeta.getGroup());

        StringBuilder builder = new StringBuilder();
        builder.append("MOM: ").append(globalMeta.getBroker()).append(". ");
        builder.append("Com. group: ").append(globalMeta.getGroup());
        globalMetaText.setText(builder.toString());

        writeLog("Loading list of provider and HINC Local done.");
    }

    boolean providerView = true;
    @FXML
    Button switchButton;

    // the switchOrNot = true will reverse the view, or false to just redraw the view
    private void showBaseOnSwitch(List<HincLocalMeta> metas, boolean switchOrNot) {
        if (providerView == switchOrNot) {
            showListOfHINCLocal(metas);
            switchButton.setText("Provider view");
            providerView = false;
        } else {
            showListOfProvider(metas);
            switchButton.setText("HINC view");
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
            System.out.println("Get HINC Local: " + local.toJson());
            String ip = local.getIp();
            String settingsJson = local.getSettings();
            String uuid = local.getUuid();
            String group = local.getGroupName();

            HBox hincItemHbox = new HBox();
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            InfoSourceSettings sourceSettings = InfoSourceSettings.fromJson(settingsJson);
            StringBuilder settingsText = new StringBuilder();
            for (InfoSourceSettings.InfoSource source : sourceSettings.getSource()) {
                String adaptorClass = source.getAdaptorClass().substring(source.getAdaptorClass().lastIndexOf(".") + 1);
                String transformerClass = source.getTransformerClass().substring(source.getTransformerClass().lastIndexOf(".") + 1);
                settingsText.append("Source       : ").append(source.getName()).append("\n");
                settingsText.append("  Type       : ").append(source.getType()).append("\n");
                settingsText.append("  Interval   : ").append(source.getInterval()).append("\n");
                settingsText.append("  Adaptor    : ").append(adaptorClass).append("\n");
                settingsText.append("  Transformer: ").append(transformerClass).append("\n");
                for (String s : source.getSettings().keySet()) {
                    settingsText.append("  ").append(s).append(": ").append(source.getSettings().get(s));
                }
            }
            Font font = new Font(java.awt.Font.MONOSPACED, 14);

            Text theText = new Text(settingsText.toString());
            theText.setFont(font);
            theText.setTextAlignment(TextAlignment.LEFT);

            TitledPane titlePanel = new TitledPane(ip + " | " + uuid.substring(0, 12), theText);
            titlePanel.setFont(font);
            titlePanel.setExpanded(true);
            titlePanel.setStyle("-fx-pref-width: 350;");
            titlePanel.setContentDisplay(ContentDisplay.LEFT);

            hincLocalList.add(new HINCLocalItem(uuid, checkBox, titlePanel));

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
            String ip = local.getIp();
            String settingsJson = local.getSettings();
            String uuid = local.getUuid();
            String group = local.getGroupName();

            InfoSourceSettings sourceSettings = InfoSourceSettings.fromJson(settingsJson);
            StringBuilder settingsText = new StringBuilder();
            for (InfoSourceSettings.InfoSource source : sourceSettings.getSource()) {
                String adaptorClass = source.getAdaptorClass().substring(source.getAdaptorClass().lastIndexOf(".") + 1);
                String transformerClass = source.getTransformerClass().substring(source.getTransformerClass().lastIndexOf(".") + 1);
                settingsText.append("Type       : ").append(source.getType()).append("\n");
                settingsText.append("Interval   : ").append(source.getInterval()).append("\n");
                settingsText.append("Adaptor    : ").append(adaptorClass).append("\n");
                settingsText.append("Transformer: ").append(transformerClass).append("\n");
                settingsText.append("HINC Local:").append(ip).append("\n");
                for (String s : source.getSettings().keySet()) {
                    settingsText.append("  ").append(s).append(": ").append(source.getSettings().get(s));
                }

                Text theText = new Text(settingsText.toString());
                theText.setFont(font);
                theText.setTextAlignment(TextAlignment.LEFT);
                TitledPane titlePane = new TitledPane(source.getType() + " provider: " + source.getName(), theText);
                titlePane.setExpanded(true);

                hincLocalList.add(new HINCLocalItem(uuid, null, titlePane));
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
