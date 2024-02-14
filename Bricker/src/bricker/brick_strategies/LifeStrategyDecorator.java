package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Heart;
import main.BrickerGameManager;

/**
 * PickLifeStrategy - removes the object from the game and adds a falling heart.
 * 
 */
public class LifeStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy {
    private BrickerGameManager gameManager;
    private static final float HEART_VELOCITY = 100f;
    private static final String HEART_TAG = "Heart";
    private static final String ASSETS_FOLDER = "Bricker/assets";
    private static final String HEART_IMAGE_PATH = ASSETS_FOLDER + "/heart.png";
    private static final int HEART_SIZE = 20;

    private void addFallingHeart(Vector2 center) {
        Renderable heartImage = this.gameManager.getImageReader().readImage(HEART_IMAGE_PATH, false);
        Heart heart = new Heart(Vector2.ZERO, new Vector2(HEART_SIZE, HEART_SIZE), heartImage, this.gameManager);
        heart.setCenter(center);
        heart.setVelocity(Vector2.DOWN.mult(HEART_VELOCITY));
        heart.setTag(HEART_TAG);
        this.gameManager.addObj(heart, Layer.DEFAULT);
    }
    /**
     * Construct a new PickLifeStrategy instance.
     *
     * @param gameManager  The game manager to remove the object from.
     * @param brickCounter The counter to decrement when a brick is removed.
     */
    public LifeStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter, CollisionStrategy wrapped) {
        super(gameManager, brickCounter, wrapped);
        this.gameManager = gameManager;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        super.onCollision(fObj, sObj);
        addFallingHeart(fObj.getCenter());
    }
}
