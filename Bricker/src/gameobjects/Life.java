package gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Life extends GameObject {
    public Life(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable){
        super(topLeftCorner, dimensions, renderable);
    }
}
