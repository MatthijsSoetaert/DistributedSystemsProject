package Controllers;

import Models.KeyGenerator;
import Models.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class NewSessionController {
    @FXML
    ComboBox<String> symEncryptionTypeDropdown;

    @FXML
    ComboBox<String> hashingAlgorithmDropDown;

    @FXML
    ComboBox<String> symEncryptionLengthDropDown;

    @FXML
    TextArea symmetricKeyField;

    @FXML
    Button generateKeyButton;

    @FXML
    TextField firstIndexTextField;

    @FXML
    TextField firstTagTextField;

    @FXML
    Button indexAndTagGenerateButton;

    @FXML
    Button createSessionButton;

    @FXML
    TextField nameField;

    ObservableList<String> aesLengthOptions =
            FXCollections.observableArrayList(
                    "128",
                    "192",
                    "256"
            );

    ObservableList<String> desLengthOptions =
            FXCollections.observableArrayList(
                    "64"
            );

    ObservableList<String> symKeyTypeOptions =
            FXCollections.observableArrayList(
                    "AES",
                    "DES"
            );

    ObservableList<String> hashingOptions =
            FXCollections.observableArrayList(
                  //  "MD5",
                  //  "SHA1",
                    "SHA256"
            );

    @FXML
    private void initialize() {
        symEncryptionTypeDropdown.setItems(symKeyTypeOptions);
        hashingAlgorithmDropDown.setItems(hashingOptions);
    }

    @FXML
    public void generateSymmetricKey() throws NoSuchAlgorithmException {
        String symKeyType = symEncryptionTypeDropdown.getValue();
        int symKeyLength = Integer.parseInt(symEncryptionLengthDropDown.getValue());
        SecretKey secretKey = KeyGenerator.generateSymmetricKey(symKeyType, symKeyLength);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        symmetricKeyField.setText(encodedKey);
    }

    @FXML
    public void generateTagAndIndex() {
        generateTag();
        generateIndex(Integer.MAX_VALUE);
    }

    private void generateTag(){
        StringBuilder sb = new StringBuilder();
        int length = 100;
        switch (hashingAlgorithmDropDown.getValue()) {
            case "MD5":
                length = 128;
                break;
            case "SHA1":
                length = 160;
                break;
            case "SHA256":
                length = 256;
                break;
            default:
                length = 256;
                break;
        }

        for (int i = 0; i < length; i++) {
            int j = 97 + (int) (Math.random() * 25);
            sb.append((char) j);
        }
        firstTagTextField.setText(sb.toString());

    }

    private void generateIndex(int max){
        firstIndexTextField.setText(Integer.toString((int)(Math.random() * max)));
    }

    @FXML
    public void setSymKeyLengths() {
        switch (symEncryptionTypeDropdown.getValue()) {
            case "AES":
                symEncryptionLengthDropDown.setItems(aesLengthOptions);
                break;
            case "DES":
                symEncryptionLengthDropDown.setItems(desLengthOptions);
                break;

        }
    }

    @FXML
    public void createSession() throws IOException, NotBoundException {

        //TODO meerdere tabs toevoegen zodat met meerdere personen gechat kan worden
        /*Tab t = (Tab) FXMLLoader.load(this.getClass().getResource("../Views/TabTemplate.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Sample.fxml"));
        Parent root = loader.load();
        MainController m = loader.getController();
        m.getTabPane().getTabs().add(t);*/


        if(!firstIndexTextField.getText().equals("") && !firstTagTextField.getText().equals("") && !symmetricKeyField.getText().equals("") ) {
            Session s = new Session(symmetricKeyField.getText(),firstTagTextField.getText(),Integer.parseInt(firstIndexTextField.getText()),nameField.getText());
            MainController.getSessions().put(nameField.getText(),s);
            Stage stage = (Stage) generateKeyButton.getScene().getWindow();
            stage.close();
        }

    }

}
