package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import main.BrickerGameManager;

/**
 * Class represting a gameobject - the ball.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final BrickerGameManager gameManager;
    private int collisionCounter;
    private static final String PUCK_TAG = "PuckBall";
    private static final String BALL_TAG = "Ball";
    private static final int MAX_BALL_COLLISIONS = 4;




    /**
     * Construct a new GameObject instance of the ball.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in
     *                      which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound,  BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = 0;
        this.gameManager = gameManager;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param other     The object that the GameObject collided with.
     * @param collision The collision object containing information about the
     *                  collision.
     * 
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTag().equals((PUCK_TAG)) && gameManager.outOfBound(this)) {
            gameManager.removeObj(this,Layer.DEFAULT);

        }

    }

    public int getBallCollisionCounter(){
        return this.collisionCounter;
    }

}
