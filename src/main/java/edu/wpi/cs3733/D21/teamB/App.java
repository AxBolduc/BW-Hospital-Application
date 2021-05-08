package edu.wpi.cs3733.D21.teamB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.ChatBot;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.util.ExternalCommunication;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

@SuppressWarnings("deprecation")
public class App extends Application {

    private static Stage primaryStage;
    private DatabaseHandler db;
    private Thread dbThread;
    private Thread chatBotThread;

    @Override
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = false;
            Bot bot = new Bot("super", resourcesPath);
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine = "";

            while(true) {
                System.out.print("Human : ");
                textLine = IOUtils.readInputTextLine();
                if ((textLine == null) || (textLine.length() < 1))
                textLine = MagicStrings.null_input;
                if (textLine.equals("q")) {
                    System.exit(0);
                } else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                } else {
                    String request = textLine;
                    if (MagicBooleans.trace_mode)
                        System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("<"))
                    response = response.replace("<", "<");
                    while (response.contains(">"))
                    response = response.replace(">", ">");
                    System.out.println("Robot : " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Starting Up");
        db = DatabaseHandler.getHandler();
    }

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" +
                File.separator + "resources"+File.separator+"chat"+File.separator+"bots";
        return resourcesPath;
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/misc/chatBox.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            //Press F11 to escape fullscreen. Allows users to use the ESC key to go back to the previous scene
            primaryStage.setFullScreenExitKeyCombination(new KeyCombination() {
                @Override
                public boolean match(KeyEvent event) {
                    return event.getCode().equals(KeyCode.F11);
                }
            });

            // Starts the chat bot thread
            chatBotThread = new Thread(new ChatBot());
            chatBotThread.start();

//            primaryStage.setFullScreen(true);

            // If the database is uninitialized, fill it with the csv files
            try {
                db.executeSchema();
                if (!db.isInitialized()) {
                    SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/databaseInit.fxml");
                    primaryStage.show();

                    dbThread = new Thread(() -> {
                        try {
                            db.loadNodesEdges(CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv"), CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml"));
                    });
                    dbThread.start();
                } else primaryStage.show();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            try {
                HashMap<User, String> requiredUsers = new HashMap<>();

                //Required users
                requiredUsers.put(new User("admin", "admin@fakeemail.com", "Professor", "X", User.AuthenticationLevel.ADMIN, "F", null), "admin");
                requiredUsers.put(new User("staff", "bwhapplication@gmail.com", "Mike", "Bedard", User.AuthenticationLevel.STAFF, "F", null), "staff");
                requiredUsers.put(new User("patient", "patient@fakeemail.com", "T", "Goon", User.AuthenticationLevel.PATIENT, "F", null), "patient");

                for (User u : requiredUsers.keySet()) {
                    if (db.getUserByUsername(u.getUsername()) == null) {
                        db.addUser(u, requiredUsers.get(u));
                    }
                }
                db.resetTemporaryUser();
                db.deauthenticate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        if (dbThread != null)
            dbThread.stop();
        if (chatBotThread != null)
            chatBotThread.stop();
        if (ChatBoxController.userThread != null)
            ChatBoxController.userThread.stop();
        for (Thread t : ExternalCommunication.threads)
            t.stop();
        ExternalCommunication.threads.clear();
        DatabaseHandler.getHandler().shutdown();
        System.out.println("Shutting Down");
    }
}
