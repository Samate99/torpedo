package torpedo.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerOneNameTextField.getText().isEmpty()) {
            errorLabel.setText("Enter player 1 name!");
            System.out.println("first name requested ");
        } else if (playerTwoNameTextField.getText().isEmpty()) {
            errorLabel.setText("Enter player 2 name!");
            System.out.println("Second name requested ");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerName(playerOneNameTextField.getText(), playerTwoNameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("The players 1 name is set to {}, players 2 name is set to {}, loading game scene", playerOneNameTextField.getText(), playerTwoNameTextField.getText());
        }
    }

}
