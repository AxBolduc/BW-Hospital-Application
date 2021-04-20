package edu.wpi.teamB.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class RequestWrapper {

    private final Request r;
    private final Label type;
    private final Label time;
    private final Label date;
    private final JFXCheckBox complete;
    private  Label employeeName;
    private final TableView parentTable;
    private final JFXButton btnEdit;
    private final JFXButton btnDel;
    private ContextMenu contextMenu;

    public RequestWrapper(Request r, TableView parentTable) throws IOException {
        this.r = r;
        this.type = new Label(Request.RequestType.prettify(r.getRequestType()));
        this.time = new Label(r.getTime());
        this.date = new Label(r.getDate());
        this.complete = new JFXCheckBox();
        this.employeeName = new Label(r.getEmployeeName());
        if (employeeName.getText().equals("null")) employeeName.setText("Nobody");
        this.parentTable = parentTable;
        this.complete.setSelected(r.getComplete().equals("T"));

        //Setup context menu
        this.contextMenu = new ContextMenu();
        Menu assignMenu = new Menu("Assign");
        MenuItem unassignItem = new MenuItem("Unassign");

        List<MenuItem> staff = new ArrayList<>();
        for(User employee: DatabaseHandler.getDatabaseHandler("main.db").getUsersByAuthenticationLevel(User.AuthenticationLevel.STAFF)){
            MenuItem tempItem = new MenuItem(employee.getFirstName() + " " + employee.getLastName());
            tempItem.setOnAction(event -> {
                parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
                r.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());

                DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);

                try {
                    parentTable.getItems().add(0, new RequestWrapper(r, parentTable));
                } catch (IOException e){
                    e.printStackTrace();
                }
            });
            staff.add(tempItem);
        }
        assignMenu.getItems().addAll(staff);

        unassignItem.setOnAction(event -> {
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
            r.setEmployeeName("null");
            DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);

            try {
                parentTable.getItems().add(0, new RequestWrapper(r, parentTable));
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        this.contextMenu.getItems().add(assignMenu);
        this.contextMenu.getItems().add(unassignItem);

        //set context menu to the employee name label
        employeeName.setContextMenu(this.contextMenu);

        complete.setOnAction(event -> {
            r.setComplete(complete.isSelected() ? "T" : "F");
            DatabaseHandler.getDatabaseHandler("main.db").updateRequest(r);
        });

        // Set up edit button
        JFXButton btnEdit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableEditBtn.fxml")));
        btnEdit.setId(r.getRequestID() + "EditBtn");

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = App.getPrimaryStage();
                stage.setUserData(r);
                SceneSwitcher.switchScene(getClass(), "this is broken", "this is broken");
            }
        });


        this.btnEdit = btnEdit;

        // Set up delete button
        JFXButton btnDel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/tableDelBtn.fxml")));
        btnDel.setId(r.getRequestID() + "DelBtn");

        btnDel.setOnAction(event -> {
            DatabaseHandler.getDatabaseHandler("main.db").removeRequest(r);
            parentTable.getItems().removeIf((Object o) -> ((RequestWrapper) o).r.getRequestID().equals(r.getRequestID()));
        });

        this.btnDel = btnDel;
    }
}