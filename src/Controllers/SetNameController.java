package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetNameController {

    @FXML
    public Button confirmNameButton;

    @FXML
    public TextField nameField;

    @FXML
    public void confirmName(){
        MainController.setName(nameField.getText());
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
