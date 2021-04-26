package edu.wpi.cs3733.D21.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FloorSwitcher {
    private final MapDrawer mapDrawer;
    private final MapCache mapCache;
    private final ImageView map;

    private final JFXButton btnF3;
    private final JFXButton btnF2;
    private final JFXButton btnF1;
    private final JFXButton btnFL1;
    private final JFXButton btnFL2;

    private JFXButton currentlySelected;

    private final static String floor3Path = "/edu/wpi/cs3733/D21/teamB/images/maps/03_thethirdfloor.png";
    private final static String floor2Path = "/edu/wpi/cs3733/D21/teamB/images/maps/02_thesecondfloor.png";
    private final static String floor1Path = "/edu/wpi/cs3733/D21/teamB/images/maps/01_thefirstfloor-Edited.png";
    private final static String floorL1Path = "/edu/wpi/cs3733/D21/teamB/images/maps/00_thelowerlevel1.png";
    private final static String floorL2Path = "/edu/wpi/cs3733/D21/teamB/images/maps/00_thelowerlevel2.png";

    public final static String floor3ID = "3";
    public final static String floor2ID = "2";
    public final static String floor1ID = "1";
    public final static String floorL1ID = "L1";
    public final static String floorL2ID = "L2";

    public FloorSwitcher(MapDrawer mapDrawer, MapCache mapCache, ImageView map, JFXButton btnF3, JFXButton btnF2, JFXButton btnF1, JFXButton btnFL1, JFXButton btnFL2) {
        this.mapDrawer = mapDrawer;
        this.mapCache = mapCache;
        this.map = map;
        this.btnF3 = btnF3;
        this.btnF2 = btnF2;
        this.btnF1 = btnF1;
        this.btnFL1 = btnFL1;
        this.btnFL2 = btnFL2;
        this.currentlySelected = btnF1;
    }

    /**
     * Switches the map image and has the map drawer place new nodes for that floor
     *
     * @param floor String of the floor to switch to
     */
    public void switchFloor(String floor) {
        switch (floor) {
            case floor3ID:
                map.setImage(new Image(floor3Path));
                mapCache.setCurrentFloor(floor3ID);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor3ID);
                if(mapCache.getFinalPath()!=null){
                    mapDrawer.drawPath(mapCache.getFinalPath());
                }
                break;
            case floor2ID:
                map.setImage(new Image(floor2Path));
                mapCache.setCurrentFloor(floor2ID);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor2ID);
                if(mapCache.getFinalPath()!=null){
                    mapDrawer.drawPath(mapCache.getFinalPath());
                }
                break;
            case floor1ID:
                map.setImage(new Image(floor1Path));
                mapCache.setCurrentFloor(floor1ID);
                mapDrawer.drawAllElements();
                highlightFloorButton(floor1ID);
                if(mapCache.getFinalPath()!=null){
                    mapDrawer.drawPath(mapCache.getFinalPath());
                }
                break;
            case floorL1ID:
                map.setImage(new Image(floorL1Path));
                mapCache.setCurrentFloor(floorL1ID);
                mapDrawer.drawAllElements();
                highlightFloorButton(floorL1ID);
                if(mapCache.getFinalPath()!=null){
                    mapDrawer.drawPath(mapCache.getFinalPath());
                }
                break;
            case floorL2ID:
                map.setImage(new Image(floorL2Path));
                mapCache.setCurrentFloor(floorL2ID);
                mapDrawer.drawAllElements();
                highlightFloorButton(floorL2ID);
                if(mapCache.getFinalPath()!=null){
                    mapDrawer.drawPath(mapCache.getFinalPath());
                }
                break;
            default:
                System.err.println("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
                break;
        }
    }

    /**
     * Changes the border color of the currently selected floor button.
     *
     * @param floor the floor id of the floor
     */
    private void highlightFloorButton(String floor) {
        currentlySelected.setStyle("-fx-border-color: BLACK;-fx-border-width: 1;-fx-background-color:  F6BD39;");

        switch (floor) {
            case floor3ID:
                currentlySelected = btnF3;
                break;
            case floor2ID:
                currentlySelected = btnF2;
                break;
            case floor1ID:
                currentlySelected = btnF1;
                break;
            case floorL1ID:
                currentlySelected = btnFL1;
                break;
            case floorL2ID:
                currentlySelected = btnFL2;
                break;
            default:
                System.err.println("NO FLOOR! AAAAAAAAAAAAHHHHHHHHHHH!!!!!!!!!!!!!");
                break;
        }

        currentlySelected.setStyle("-fx-border-color: RED; -fx-border-width: 3; -fx-background-color:  F6BD39;");
    }
}
