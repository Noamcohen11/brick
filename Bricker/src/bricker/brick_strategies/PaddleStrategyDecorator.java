package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.SubPaddle;
import main.BrickerGameManager;

/**
 * Basic collision strategy - removes the object from the game.
 */
public class PaddleStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy {

    private static final String ASSETS_FOLDER = "Bricker/assets";
    private static final String SUB_PADDLE_TAG = "Sub_paddle";
    private static final String PADDLE_IMAGE_PATH = ASSETS_FOLDER + "/paddle.png";
    private static final float PADDLE_WIDTH = 200;
    private static final float PADDLE_HEIGHT = 20;
    private final BrickerGameManager gameManager;

    // Adds a sub paddle to the game.
    private void addSubPaddle() {
        Renderable paddleImage = this.gameManager.getImageReader().readImage(PADDLE_IMAGE_PATH, false);
        SubPaddle subPaddle = new SubPaddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, this.gameManager.getInputListener(),
                this.gameManager);
        subPaddle.setCenter(new Vector2(this.gameManager.getWindowController().getWindowDimensions().x() / 2,
                this.gameManager.getWindowController().getWindowDimensions().y() / 2));
        subPaddle.setTag(SUB_PADDLE_TAG);
        this.gameManager.addObj(subPaddle, Layer.DEFAULT);
    }

    /**
     * Construct a new BasicCollisionStrategy instance.
     *
     * @param gameManager  The game manager to remove the object from.
     * @param brickCounter The counter to decrement when a brick is removed.
     */
    public PaddleStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter, CollisionStrategy wrapped) {
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
        if (!gameManager.isSubPaddleUsed()) {
            addSubPaddle();
        }
    }
}
