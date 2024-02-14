package main;

import bricker.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.*;

import java.util.Random;
import danogl.util.Counter;

/**
 * The bricker ball game manager.
 * It manages every part of the game and it's objects.
 */
public class BrickerGameManager extends GameManager {

    //////////////////////////////////////////
    // Game Constants ///////////////////////
    //////////////////////////////////////////

    // Window constants.
    private static final String WINDOW_TITLE = "Bricker";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;

    // Objects constants.
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    private static final int BRICK_HEIGHT = 15;
    private static final int WALL_WIDTH = 5;
    private static final int SPACER = 5;
    private static final float BALL_VELOCITY = 250;
    private static final float BALL_SIZE = 50;
    private static final float PADDLE_WIDTH = 200;
    private static final float PADDLE_HEIGHT = 20;
    private static final int MAX_SUBPADDLE_COLLISIONS = 4;
    private static final float PUCK_SIZE = BALL_SIZE * 0.75f;
    private static final String BALL_TAG = "Ball";
    private static final String MAIN_PADDLE_TAG = "Main_paddle";

    private static final String SUB_PADDLE_TAG = "Sub_paddle";

    private static final String PUCK_TAG = "PuckBall";

    // UI constants.
    private static final int HEART_SIZE = 20;
    private static final int GAME_LIVES = 3;
    private static final int MAX_GAME_LIVES = 4;

    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String LOSING_PROMPT = "you lose!" + PLAY_AGAIN_PROMPT;
    private static final String WINNING_PROMPT = "you win!" + PLAY_AGAIN_PROMPT;

    // Assets
    private static final String ASSETS_FOLDER = "Bricker/assets";
    private static final String BALL_IMAGE_PATH = ASSETS_FOLDER + "/ball.png";

    private static final String BRICK_IMAGE_PATH = ASSETS_FOLDER + "/brick.png";
    private static final String PADDLE_IMAGE_PATH = ASSETS_FOLDER + "/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = ASSETS_FOLDER + "/DARK_BG2_small.jpeg";
    private static final String HEART_IMAGE_PATH = ASSETS_FOLDER + "/heart.png";
    private static final String BLOP_SILENCED = ASSETS_FOLDER + "/blop_cut_silenced.wav";
    private static final String PUCK_IMAGE_PATH = ASSETS_FOLDER + "/mockBall.png";

    // User input
    public static final int BRICK_ROWS_LOCATION = 0;
    public static final int BRICKS_PER_ROW_LOCATION = 1;
    public static final int NUM_OF_USER_INPUTS = 2;
    private static final float HEART_VELOCITY = 100f;
    private static final String HEART_TAG = "Heart";

    //////////////////////////////////////////
    // Game Privates /////////////////////////
    //////////////////////////////////////////

    // Game state.
    private LifeCounter livesCounter;
    private Counter brickCounter;
    private int numOfBrickRows = DEFAULT_BRICK_ROWS;
    private int bricksPerRow = DEFAULT_BRICKS_PER_ROW;
    private StrategyFactory strategyFactory;

    // The objects.
    public Ball ball;
    public WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    private boolean subPaddleUsed;

    // Add wall
    private void addWall(Vector2 topLeftCorner, Vector2 dimensions) {
        GameObject wall = new GameObject(topLeftCorner, dimensions, null);
        this.gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
    }

    // Set a puck's starting point and velocity.
    private void setBallStartingMovement(Vector2 center, Ball ball) {
        ball.setCenter(center);
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float ballVelX = (float) Math.cos(angle) * BALL_VELOCITY;
        float ballVelY = (float) Math.sin(angle) * BALL_VELOCITY;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    // Add Background
    private void addBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(windowController.getWindowDimensions().x(), windowController.getWindowDimensions().y()),
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    // Add Bricks
    private void addBricks() {

        int rand = 0;
        Renderable brickRenderer = imageReader.readImage(BRICK_IMAGE_PATH, false);

        int brickWidth = (int) (windowController.getWindowDimensions().x() - WALL_WIDTH) / this.bricksPerRow;
        for (int col = 0; col < this.bricksPerRow; col++) {
            for (int row = 0; row < this.numOfBrickRows; row++) {
                this.strategyFactory = new StrategyFactory(this, brickCounter);
                CollisionStrategy collisionStrategy = this.strategyFactory.chooseStrategy();

                Brick brick = new Brick(
                        new Vector2(WALL_WIDTH + col * (brickWidth + SPACER),
                                WALL_WIDTH + row * (SPACER + BRICK_HEIGHT)),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickRenderer, collisionStrategy);

                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increment();
            }
        }
    }

    // Add Ball.
    private void addBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SILENCED);
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound, this);
        setBallStartingMovement(windowController.getWindowDimensions().mult(0.5f), this.ball);
        ball.setTag(BALL_TAG);
        this.gameObjects().addGameObject(ball, Layer.DEFAULT);
    }

    public void addPuck(Vector2 center) {
        Renderable ballImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SILENCED);
        Ball puck = new Ball(Vector2.ZERO, new Vector2(PUCK_SIZE, PUCK_SIZE), ballImage,
                collisionSound, this);
        setBallStartingMovement(center, puck);
        puck.setTag(PUCK_TAG);
        this.gameObjects().addGameObject(puck, Layer.DEFAULT);

    }

    // Add Paddle
    private void addPaddle(Vector2 center, String tag) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                this.inputListener);
        paddle.setCenter(center);
        paddle.setTag(tag);
        this.gameObjects().addGameObject(paddle, Layer.DEFAULT);
    }

    public void addCameraHandler() {
        // TODO: check if need to delete it from gameObjects list
        if (this.camera() != null) {
            addObj(new CameraHandler(Vector2.ZERO, Vector2.ZERO, null, ball, this),
                    Layer.DEFAULT);
        }
    }

    // Add Life Counter
    private void addLifeCounter() {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, false);
        GameObjectCollection gameObject = gameObjects();
        LifeBar lifeCounter = new LifeBar(new Vector2(0, windowController.getWindowDimensions().y() - HEART_SIZE),
                new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage,
                this, this.livesCounter, SPACER, MAX_GAME_LIVES);
        gameObjects().addGameObject(lifeCounter, Layer.UI);
    }

    /**
     * Add an object from gameObjects list
     *
     * @param gameObject the object to add.
     * @param layer      the layer to add to.
     */
    public void addObj(GameObject gameObject, int layer) {
        this.gameObjects().addGameObject(gameObject, layer);
    }

    /**
     * Remove an object from gameObjects list
     *
     * @param gameObject the object to remove.
     * @param layer      the layer to remove from.
     */
    public boolean removeObj(GameObject gameObject, int layer) {
        return this.gameObjects().removeGameObject(gameObject, layer);
    }

    /**
     * Getter for the image reader.
     *
     * @return The image reader.
     */
    public ImageReader getImageReader() {
        return imageReader;
    }

    /**
     * Getter for the input reader.
     *
     * @return The input reader.
     */
    public UserInputListener getInputListener() {
        return inputListener;
    }

    public WindowController getWindowController(){
        return windowController;
    }

    /**
     * Getter for isSubPaddleUsed.
     *
     * @return isSubPaddleUsed.
     */
    public boolean isSubPaddleUsed() {
        return subPaddleUsed;
    }

    /**
     * Setter for subPaddleUsed.
     *
     * @param subPaddleUsed
     */
    public void setSubPaddleUsed(boolean subPaddleUsed) {
        this.subPaddleUsed = subPaddleUsed;
    }

    public LifeCounter getLifeCounter(){
        return livesCounter;
    }
    /**
     * checks if an object is on the screen or fell down from the floor.
     *
     * @param object The game object we want to check it's bounds.
     */
    public boolean outOfBound(GameObject object) {
        double objHeight = object.getCenter().y();
        float gameBottom = windowController.getWindowDimensions().y();
        if (objHeight > gameBottom) {
            return true;
        }
        return false;
    }

    /**
     * Update the game state, and check for game end.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (outOfBound(ball)) {
            this.livesCounter.decrement();
            setBallStartingMovement(windowController.getWindowDimensions().mult(0.5f), this.ball);
        }
        checkForGameEnd();
    }

    /**
     * Construct a new BrickerGameManager instance.
     *
     * @param windowTitle      The title of the window.
     * @param windowDimensions The dimensions of the window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * change brick constants.
     *
     * @param numOfBrickRows The number of brick rows.
     * @param bricksPerRow   The number of bricks per row.
     */
    public void brickUpdate(int numOfBrickRows, int bricksPerRow) {
        this.numOfBrickRows = numOfBrickRows;
        this.bricksPerRow = bricksPerRow;
    }

    // Check for game end. If the ball is out of the game, check if the player has
    // more lives. If not, ask the player if he wants to play again.
    private void checkForGameEnd() {
        String prompt = "";
        if (this.livesCounter.value() == 0) {
            prompt = LOSING_PROMPT;
        }
        if (brickCounter.value() == 0) {
            prompt = WINNING_PROMPT;
        }
        if (!prompt.isEmpty()) {
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Initialize the game.
     * Builds the game's objects and sets the game's window.
     *
     * @param imageReader      The image reader to read the game's images.
     * @param soundReader      The sound reader to read the game's sounds.
     * @param inputListener    The input listener to listen to the user's input.
     * @param windowController The window controller to control the game's window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
            UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        Vector2 windowDimensions = windowController.getWindowDimensions();

        // add Objects
        addBackground();

        // leftWall
        addWall(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()));
        // rightWall
        addWall(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()));
        // upperWall
        addWall(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH));

        brickCounter = new Counter();
        addBricks();
        addBall();
        addPaddle(new Vector2(windowController.getWindowDimensions().x() / 2,
                (int) windowController.getWindowDimensions().y() - 30), MAIN_PADDLE_TAG);

        this.livesCounter = new LifeCounter(MAX_GAME_LIVES,GAME_LIVES);
        addLifeCounter();
        this.subPaddleUsed = false;
    }

    /**
     * The main function.
     * 
     * @param args The command line arguments. - numOfBrickRows, bricksPerRow.
     */
    public static void main(String[] args) {
        BrickerGameManager bouncingBallGameManager = new BrickerGameManager(WINDOW_TITLE,
                new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
        if (args.length == NUM_OF_USER_INPUTS) {
            int numOfBrickRows = Integer.parseInt(args[BRICK_ROWS_LOCATION]);
            int bricksPerRow = Integer.parseInt(args[BRICKS_PER_ROW_LOCATION]);
            bouncingBallGameManager.brickUpdate(numOfBrickRows, bricksPerRow);
        }
        bouncingBallGameManager.run();
    }
}
