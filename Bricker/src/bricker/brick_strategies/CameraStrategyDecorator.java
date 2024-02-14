package bricker.brick_strategies;

import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import main.BrickerGameManager;

import java.util.Objects;


public class CameraStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy{
    private BrickerGameManager gameManager;
    private static final String BALL_TAG = "Ball";

    private static final int MAX_BALL_COLLISIONS = 4;
    public CameraStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter, CollisionStrategy wrapped) {
        super(gameManager, brickCounter, wrapped);
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        super.onCollision(fObj, sObj);
        if (Objects.equals(sObj.getTag(), BALL_TAG) && gameManager.camera()== null) {
            gameManager.setCamera(new Camera(
                gameManager.ball, //object to follow
                Vector2.ZERO, //follow the center of the object
                gameManager.windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                gameManager.windowController.getWindowDimensions() //share the window dimensions
        ));
            gameManager.addCameraHandler();
        }
    }

}
