package gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.BrickerGameManager;
import danogl.collisions.Collision;

public class Heart extends GameObject{
    private static final String PADDLE_TAG = "Main_paddle";
    private BrickerGameManager gameManager;
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.gameManager = gameManager;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(gameManager.outOfBound(this)){
            gameManager.removeObj(this, Layer.DEFAULT);
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PADDLE_TAG);
    }

    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameManager.getLifeCounter().increment();
        gameManager.removeObj(this, Layer.DEFAULT);
    }
}
