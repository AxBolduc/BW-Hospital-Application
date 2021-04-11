package edu.wpi.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SecurityRequestFormController {

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton SubmitB;

    @FXML
    void handleBackButton(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        if (btn.getId().equals("backBtn")) {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/serviceRequestMenu.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        }

        if (btn.getId().equals("SubmitB")) {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/formSubmitted.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            }
        }
    }

