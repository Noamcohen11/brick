package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import main.BrickerGameManager;

import java.awt.event.KeyEvent;

/**
 * Class represting a gameobject - the paddle.
 */
public class SubPaddle extends Paddle {

    private final Counter collisionCount;
    private final BrickerGameManager gameManager;
    private static final int MAX_HITS = 4;
    private static final String BALL_TAG = "Ball";

    /**
     * Construct a new GameObject instance of the paddle.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in
     *                      which case
     *                      the GameObject will not be rendered.
     * @param inputListener The input listener to listen to the user's input.
     */
    public SubPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
            UserInputListener inputListener, BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener);
        collisionCount = new Counter();
        gameManager.setSubPaddleUsed(true);
        this.gameManager = gameManager;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(BALL_TAG)) {
            collisionCount.increment();
        }
        if (collisionCount.value() >= MAX_HITS) {
            this.gameManager.removeObj(this, Layer.DEFAULT);
            gameManager.setSubPaddleUsed(false);
        }
    }
}