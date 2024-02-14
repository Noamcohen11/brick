package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;
import main.BrickerGameManager;

public class BallsStrategyDecorator extends BaseStrategyDecorator implements CollisionStrategy  {
    private final BrickerGameManager gameManager;
    public BallsStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter, CollisionStrategy wrapped) {
        super(gameManager, brickCounter, wrapped);
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        super.onCollision(fObj,sObj);
        this.gameManager.addPuck(fObj.getCenter());
        this.gameManager.addPuck(fObj.getCenter());
    }
}
