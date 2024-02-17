package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.SubPaddle;
import main.BrickerGameManager;
import main.Constants;

/**
 * Basic collision strategy - removes the object from the game.
 */
public class PaddleStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy {
    private final BrickerGameManager gameManager;

    // Adds a sub paddle to the game.
    private void addSubPaddle() {
        Renderable paddleImage = this.gameManager.getImageReader().readImage(Constants.PADDLE_IMAGE_PATH,
                false);
        SubPaddle subPaddle = new SubPaddle(Vector2.ZERO,
                new Vector2(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT), paddleImage,
                this.gameManager.getInputListener(),
                this.gameManager);
        subPaddle.setCenter(
                new Vector2(this.gameManager.getWindowController().getWindowDimensions().x() / 2,
                this.gameManager.getWindowController().getWindowDimensions().y() / 2));
        subPaddle.setTag(Constants.SUB_PADDLE_TAG);
        this.gameManager.addObj(subPaddle, Layer.DEFAULT);
    }

    /**
     * Construct a new BasicCollisionStrategy instance.
     *
     * @param gameManager  The game manager to remove the object from.
     * @param brickCounter The counter to decrement when a brick is removed.
     */
    public PaddleStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter,
                                   CollisionStrategy wrapped) {
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
