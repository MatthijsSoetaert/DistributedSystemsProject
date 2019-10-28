package Controllers;

import Models.Message;
import Models.MessageBuilder;
import Models.Session;
import Services.MessageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Name;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class MainController {

    @FXML
    TextField insertTextField;

    @FXML
    Text nameTextField;

    @FXML
    Button sendButton;

    @FXML
    Button receiveButton;

    @FXML
    MenuItem createSessionItem;

    @FXML
    TabPane tabPane;

    @FXML
    TextArea messagesTextField;


    private static Map<String, Session> sessions;
    public static String name;

    // Add a public no-args constructor
    public MainController()
    {
    }

    @FXML
    private void initialize()
    {
        sessions = new HashMap<>();
    }

    public TabPane getTabPane(){
        return tabPane;
    }

    //CREATE SESSION
    @FXML
    public void openCreateSessionInstance() throws IOException {
        //Stage stage = (Stage) sendButton.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../Views/NewSession.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void setName() throws IOException {
        //Stage stage = (Stage) sendButton.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../Views/SetName.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void sendMessage(){
        try {
            //Connecting to server
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 1199);
            MessageService impl = (MessageService) myRegistry.lookup("MessageService");

            //Get session info
            Session session = sessions.get("Bob");
            int currentIndex = session.getIndex();
            String currentTag = session.getTag();

            //Add message to view
            String message = insertTextField.getText();
            insertTextField.setText("");
            messagesTextField.setText(messagesTextField.getText() + "\nS" + session.getName() + ": " + message);
            System.out.println("\n#### SENDING ####");

            //Send message to server
            byte[] finalMessage = MessageBuilder.buildSendMessage(message,session);
            impl.sendMessage(currentIndex, finalMessage,currentTag);

            //TODO een betere plaats zoeken voor dit
            nameTextField.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void receiveMessage(){
        try {
            //Connecting with server
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 1199);
            MessageService impl = (MessageService) myRegistry.lookup("MessageService");

            //Get receiving mailbox with tag
            Session session = sessions.get("Bob");
            int currentIndex = session.getIndex();
            String currentTag = session.getTag();

            System.out.println("\n#### RECEIVING ####");
            //Get message on index currentIndex and tag currentTag
            byte[] s = impl.getMessage(currentIndex,currentTag);

            //Recreate secretKey
            byte[] decodedKey = Base64.getDecoder().decode(session.getSymKey());
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            //Decrypt message
            Cipher cipher1 = Cipher.getInstance("AES");
            cipher1.init(Cipher.DECRYPT_MODE,secretKey);
            byte[] decryptedMessage = cipher1.doFinal(s);

            //Rebuild Message object
            Message m = MessageBuilder.buildReceiveMessage(decryptedMessage);
            messagesTextField.setText(messagesTextField.getText() + "\nR" + session.getName() + ": " + m.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //GETTERS AND SETTERS
    public static Map<String, Session> getSessions() {
        return sessions;
    }

    public static void setSessions(Map<String, Session> sessions) {
        MainController.sessions = sessions;
    }

    public Text getNameTextField() {
        return nameTextField;
    }

    public static void setName(String nameText) {
        name = nameText;
    }
}
