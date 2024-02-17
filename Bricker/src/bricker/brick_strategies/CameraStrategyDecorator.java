package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.CameraHandler;
import main.BrickerGameManager;
import main.Constants;

import java.util.Objects;

/**
 * camera strategy - changes the frame ff reference of the game to be the ball's one.
 *
 */
public class CameraStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy{
    private final BrickerGameManager gameManager;

    /**
     * Construct a new cameraStrategy instance.
     *
     * @param gameManager  The game manager to remove the object from.
     * @param brickCounter The counter to decrement when a brick is removed.
     */
    public CameraStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter, CollisionStrategy wrapped) {
        super(gameManager, brickCounter, wrapped);
        this.gameManager = gameManager;
    }
    // add a cameraHandler to the game
    private void addCameraHandler() {
        if (gameManager.camera() != null) {
            gameManager.addObj(new CameraHandler(Vector2.ZERO, Vector2.ZERO, null, gameManager.ball,
                            gameManager),
                    Layer.DEFAULT);
        }
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
        if (Objects.equals(sObj.getTag(), Constants.BALL_TAG) && gameManager.camera()== null) {
            gameManager.setCamera(new Camera(
                gameManager.ball, //object to follow
                Vector2.ZERO, //follow the center of the object
                gameManager.windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                gameManager.windowController.getWindowDimensions() //share the window dimensions
        ));
            addCameraHandler();
        }
    }

}
