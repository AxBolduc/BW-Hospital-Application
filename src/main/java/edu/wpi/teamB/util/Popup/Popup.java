package edu.wpi.teamB.util.Popup;

import javafx.scene.layout.Pane;
import lombok.Getter;


public abstract class Popup<T extends Pane, E> {

    private final Pane parent;
    private T window;

    @Getter
    protected final E data;

    public Popup(Pane parent, E data){
        this.parent = parent;
        this.data = data;
    }

    /**
     * Shows the popup in the parent node.
     */
    public void show(T node){

        parent.getChildren().add(node);
        window = node;
    }

    /**
     * Removes the popup in the parent node.
     */
    public void hide(){

        parent.getChildren().remove(window);
        window = null;
    }
}
