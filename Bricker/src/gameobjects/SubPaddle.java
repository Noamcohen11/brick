package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import main.BrickerGameManager;
import main.Constants;

import java.awt.event.KeyEvent;
import java.lang.constant.Constable;

/**
 * Class represting a gameobject - the paddle.
 */


public class SubPaddle extends Paddle {

    private final Counter collisionCount;
    private final BrickerGameManager gameManager;

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
     * Called on the first frame of a collision.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Constants.BALL_TAG)||other.getTag().equals(Constants.PUCK_TAG)) {
            collisionCount.increment();
        }
        if (collisionCount.value() >= Constants.SUBPADDLE_MAX_HITS) {
            this.gameManager.removeObj(this, Layer.DEFAULT);
            gameManager.setSubPaddleUsed(false);
        }
    }
}