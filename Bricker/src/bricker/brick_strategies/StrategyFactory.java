package bricker.brick_strategies;

import danogl.util.Counter;
import main.BrickerGameManager;

import java.util.Random;

/**
 * StrategyFactory class is responsible for creating a random strategy for the
 * bricks. The probability of each strategy is determined by the
 * PROBABILITY_FACTOR
 * constant.
 *
 */
public class StrategyFactory {
    private static final int BASIC_STRATEGY = 0;
    private static final int STRATEGY_TYPE_NUM = 2;
    private static final int MAX_STRATEGY_COUNT = 3;
    private static final int EXTRA_BALL = 0;
    private static final int EXTRA_PADDLE = 1;
    private static final int CHANGE_CAMERA = 2;
    private static final int EXTRA_LIFE = 3;
    private static final int DOUBLE_STRATEGY = 4;
    private static final int SPECIEL_STRATEGY_TYPE_NUM = 5;

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

    private CollisionStrategy chooseStrategyHelper(Counter strategyCount, CollisionStrategy wrapped) {

        strategyCount.increment();
        int rand = random.nextInt(STRATEGY_TYPE_NUM);
        if (rand == BASIC_STRATEGY) {
            return new BasicCollisionStrategy(gameManager, brickCounter);
        }
        rand = random.nextInt(SPECIEL_STRATEGY_TYPE_NUM);
        if (rand == EXTRA_BALL) {
            return new BallsStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == EXTRA_PADDLE) {
            return new PaddleStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == CHANGE_CAMERA) {
            return new CameraStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (rand == EXTRA_LIFE) {
            return new LifeStrategyDecorator(gameManager, brickCounter, wrapped);
        }
        else if (strategyCount.value() < MAX_STRATEGY_COUNT){
            CollisionStrategy innerStrategy = chooseStrategyHelper(strategyCount, wrapped);
            return chooseStrategyHelper(strategyCount, innerStrategy);
        }
        return new BasicCollisionStrategy(gameManager, brickCounter);
    }
}
