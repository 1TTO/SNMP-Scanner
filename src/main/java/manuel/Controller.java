package manuel;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    static ListView<SNMPrecordItem> staticResultListView;
    public ListView<SNMPrecordItem> resultListView;
    static ListView<SNMPtrapsListenerItem> staticTrapsListView;
    public ListView<SNMPtrapsListenerItem> trapsListView;
    public Button scanButton;
    public TextField networkMaskTextField, firstAddress, secondAddress, oidTextfield, mibTextfield;
    public ToggleGroup community, scanOption, getMethod;
    public VBox mibContent, oidContent;
    public Button oidAddButton, mibAddButton;

    /**
     * Sets up the FXML settings of the GUI
     */
    public void initialize(){
        staticResultListView = resultListView;
        staticTrapsListView = trapsListView;

        updateMibScrollPane();
        updateOidScrollPane();

        scanButton.setOnAction(e->{
            String communityText = ((RadioButton)community.getSelectedToggle()).getText();
            String scanOptionText = ((RadioButton)scanOption.getSelectedToggle()).getText();
            String getMethodText = ((RadioButton)getMethod.getSelectedToggle()).getText();
            ArrayList<String> mibFileContent = null, oidFileContent = null;

            try {
                mibFileContent = File.getCSVContent(File.MIB_FILE_PATH);
                Main.scanner = new SNMPscanner(Main.getMib());
                Main.scanner.getVarbindCollections().clear();
                Main.scanner.getSNMPrecordItems().clear();
            } catch (IOException ioException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MIB");
                alert.setHeaderText("Error while loading MIBs from file");
                alert.setContentText("You quite possibly added a non existing MIB");

                alert.showAndWait();
            }

            try{
                oidFileContent = File.getCSVContent(File.OID_FILE_PATH);
            }catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("OID");
                alert.setHeaderText("Error while loading OIDs from file");
                alert.setContentText("You quite possibly added a non existing OID");

                alert.showAndWait();
            }

            if (mibFileContent != null && oidFileContent != null && Address.isAddress(firstAddress.getText()) && (!scanOptionText.equals("Range") || Address.isAddress(secondAddress.getText()))){
                Main.scanner.setScanMethod(getMethodText);
                ArrayList<String> finalOidFileContent = oidFileContent;

                new Thread(()->{
                    switch (scanOptionText) {
                        case "Address":
                            Main.scanner.scanAddress(firstAddress.getText(), communityText, finalOidFileContent);
                            break;
                        case "Network":
                            new Address(firstAddress.getText());
                            Main.scanner.scanNetwork(new Network(new Address(firstAddress.getText()), Integer.parseInt(networkMaskTextField.getText())), communityText, finalOidFileContent);
                            break;
                        case "Range":
                            Main.scanner.scanNetwork(new Network(new Address(firstAddress.getText()), new Address(secondAddress.getText())), communityText, finalOidFileContent);
                            break;
                    }
                }).start();
            }
        });

        networkMaskTextField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue){
                try{
                    if ( 0 > Integer.parseInt(networkMaskTextField.getText()) || Integer.parseInt(networkMaskTextField.getText()) > 32){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Netmask");
                        alert.setHeaderText("Incompatible netmask");
                        alert.setContentText("Your netmask needs to be between 0 and 32!");

                        alert.showAndWait();
                        networkMaskTextField.setText("24");
                    }
                }catch (Exception ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Netmask");
                    alert.setHeaderText("Incompatible netmask");
                    alert.setContentText("Your netmask needs to be between 0 and 32 and be numeric!");

                    alert.showAndWait();
                    networkMaskTextField.setText("24");
                }
            }
        });

        firstAddress.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue){
                handleWrongAddress(firstAddress);
            }
        });

        secondAddress.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue){
                handleWrongAddress(secondAddress);
            }
        });

        scanOption.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (scanOption.getSelectedToggle() != null) {
                switch (((RadioButton)scanOption.getSelectedToggle()).getText()){
                    case "Address":
                        firstAddress.setVisible(true);
                        secondAddress.setVisible(false);
                        networkMaskTextField.setVisible(false);
                        break;
                    case "Network":
                        firstAddress.setVisible(true);
                        secondAddress.setVisible(false);
                        networkMaskTextField.setVisible(true);
                        break;
                    case "Range":
                        firstAddress.setVisible(true);
                        secondAddress.setVisible(true);
                        networkMaskTextField.setVisible(false);
                        break;
                }
            }

        });

        mibAddButton.setOnAction(e->{
            try {
                ArrayList<String> mibFileContent = File.getCSVContent(File.MIB_FILE_PATH);
                mibFileContent.add(mibTextfield.getText());
                File.setCSVContent(File.MIB_FILE_PATH, mibFileContent);
                updateMibScrollPane();
                mibTextfield.setText("");
            }catch (Exception ex){
                handleFileNotFoundError();
            }
        });

        oidAddButton.setOnAction(e->{
            try {
                ArrayList<String> oidFileContent = File.getCSVContent(File.OID_FILE_PATH);
                oidFileContent.add(oidTextfield.getText());
                File.setCSVContent(File.OID_FILE_PATH, oidFileContent);
                updateOidScrollPane();
                oidTextfield.setText("");
            }catch (Exception ex){
                handleFileNotFoundError();
            }
        });
    }

    /**
     * Exception-handling if in a TextField the input doesn't verify as a valid address
     */
    void handleWrongAddress(TextField txtFld){
        if (!Address.isAddress(txtFld.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Address");
            alert.setHeaderText("Incompatible address");
            alert.setContentText("Your address doesn't consist of 3 points and only numerical characters!");

            alert.showAndWait();
            txtFld.setText("");
        }
    }

    /**
     * Exception-handling if a file could not be loaded
     */
    static void handleFileNotFoundError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File");
        alert.setHeaderText("Could not open the file");
        alert.setContentText("Check if the file has been open with an other program or if it does exist!");

        alert.showAndWait();
    }

    /**
     * Updates the scrollview of the OIDs in the settings tab
     */
    void updateOidScrollPane(){
        oidContent.getChildren().clear();

        try {
            ArrayList<String> oidFileContent  = File.getCSVContent(File.OID_FILE_PATH);

            for (String oid : oidFileContent){
                Label label = new Label(oid);

                oidContent.getChildren().add(label);

                label.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                            oidFileContent.remove(label.getText());
                            try {
                                File.setCSVContent(File.OID_FILE_PATH, oidFileContent);
                            } catch (IOException e) {
                                handleFileNotFoundError();
                            }
                            updateOidScrollPane();
                        }
                    }
                });
            }
        } catch (IOException e) {
            oidContent.getChildren().add(new Label("ERROR while reading file"));
        }
    }

    /**
     * Updates the scrollview of the MIBs in the settings tab
     */
    void updateMibScrollPane(){
        mibContent.getChildren().clear();

        try {
            ArrayList<String> mibFileContent  = File.getCSVContent(File.MIB_FILE_PATH);

            for (String mib : mibFileContent){
                Label label = new Label(mib);

                mibContent.getChildren().add(label);

                label.setOnMouseClicked(mouseEvent -> {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                            mibFileContent.remove(label.getText());
                            try {
                                File.setCSVContent(File.MIB_FILE_PATH, mibFileContent);
                            } catch (IOException e) {
                                handleFileNotFoundError();
                            }
                            updateMibScrollPane();
                        }
                    }
                });
            }
        } catch (IOException e) {
            mibContent.getChildren().add(new Label("ERROR while reading file"));
        }
    }
}
