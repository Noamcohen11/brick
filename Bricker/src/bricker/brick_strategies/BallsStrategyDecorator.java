package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import main.BrickerGameManager;
import main.Constants;
/**
 * extra ball strategy - adds two puck balls to the game when a brick disappears.
 *
 */


public class BallsStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy  {
    private final BrickerGameManager gameManager;
    /**
     * Construct a new ballsStrategy instance.
     *
     * @param gameManager  The game manager to remove the object from.
     * @param brickCounter The counter to decrement when a brick is removed.
     */
    public BallsStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter,
                                  CollisionStrategy wrapped) {
        super(gameManager, brickCounter, wrapped);
        this.gameManager = gameManager;
    }
    //adds a puckBall instance to the gameObjects list
    private void addPuck(Vector2 center) {
        Renderable ballImage = gameManager.getImageReader().readImage(Constants.PUCK_IMAGE_PATH,
                true);
        Sound collisionSound = gameManager.soundReader().readSound(Constants.BLOP_SILENCED);
        Ball puck = new Ball(Vector2.ZERO, new Vector2(Constants.PUCK_SIZE, Constants.PUCK_SIZE), ballImage,
                collisionSound, gameManager);
        puck.setBallStartingMovement(center);
        puck.setTag(Constants.PUCK_TAG);
        gameManager.addObj(puck, Layer.DEFAULT);

    }
    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        super.onCollision(fObj,sObj);
        addPuck(fObj.getCenter());
        addPuck(fObj.getCenter());
    }
}
