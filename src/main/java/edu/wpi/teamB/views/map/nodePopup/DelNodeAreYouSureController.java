package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DelNodeAreYouSureController implements Initializable {

    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private GraphicalNodePopupData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalNodePopupData) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnYes":
                try {
                    DatabaseHandler.getDatabaseHandler("main.db").removeNode(data.getData().getNodeID());
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
                data.getData().getPfmc().refreshEditor();

                data.getData().getMapStack().getChildren().remove(data.getParent().getRoot());
                GesturePane thePane = (GesturePane) data.getData().getMapStack().getChildren().get(0);
                thePane.setGestureEnabled(true);
                break;
            case "btnNo":
                data.getParent().areYouSureToMain();
                break;
        }
    }
}
