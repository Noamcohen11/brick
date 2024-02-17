package bricker.brick_strategies;

import danogl.util.Counter;
import main.BrickerGameManager;
import main.Constants;

import java.util.Random;

/**
 * StrategyFactory class is responsible for creating a random strategy for the
 * bricks. The probability of each strategy is determined by the
 * PROBABILITY_FACTOR
 * constant.
 *
 */
public class StrategyFactory {


    private final Random random = new Random();
    private final BrickerGameManager gameManager;
    private final Counter brickCounter;

    private boolean useDoubleStrategy;

    /**
     * Constructor for the StrategyFactory class.
     *
     * @param gameManager  the game manager
     * @param brickCounter the counter for the bricks
     */
    public StrategyFactory(BrickerGameManager gameManager, Counter brickCounter) {
        this.gameManager = gameManager;
        this.brickCounter = brickCounter;
    }

    /**
     * Chooses a strategy based on the probability factor.
     *
     * @return the chosen strategy
     */
    public CollisionStrategy chooseStrategy() {

        Counter strategyCount = new Counter();
        CollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameManager, brickCounter);
        return chooseStrategyHelper(strategyCount, basicCollisionStrategy);
    }

    // a method to choose which strategy to put for a brick according to some conditions
    private CollisionStrategy chooseStrategyHelper(Counter strategyCount, CollisionStrategy wrapped) {

        strategyCount.increment();
        int rand = random.nextInt(Constants.STRATEGY_TYPE_NUM);
        if (rand == Constants.BASIC_STRATEGY) {
            System.out.println("BASIC_STRATEGY");
            return new BasicCollisionStrategy(gameManager, brickCounter);
        }
        rand = random.nextInt(Constants.SPECIEL_STRATEGY_TYPE_NUM);
        if (rand == Constants.EXTRA_BALL) {
            System.out.println("EXTRA_BALL");
            return new BallsStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == Constants.EXTRA_PADDLE) {
            System.out.println("EXTRA_PADDLE");
            return new PaddleStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == Constants.CHANGE_CAMERA) {
            System.out.println("CHANGE_CAMERA");
            return new CameraStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == Constants.EXTRA_LIFE) {
            System.out.println("EXTRA_LIFE");
            return new LifeStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (strategyCount.value() < Constants.MAX_STRATEGY_COUNT){
            System.out.println("extra");
            CollisionStrategy innerStrategy = chooseStrategyHelper(strategyCount, wrapped);
            return chooseStrategyHelper(strategyCount, innerStrategy);
        }
        System.out.println("none");
        return new BasicCollisionStrategy(gameManager, brickCounter);

    }
}
