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
    // Game Privates /////////////////////////
    //////////////////////////////////////////

    // Game state.
    private LifeCounter livesCounter;
    private Counter brickCounter;
    private int numOfBrickRows = Constants.DEFAULT_BRICK_ROWS;
    private int bricksPerRow = Constants.DEFAULT_BRICKS_PER_ROW;
    private StrategyFactory strategyFactory;

    // The objects.
    /**
     * the main ball of the game
     */
    public Ball ball;
    /**
     * the window controller of the game
     */
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

    // Add Background
    private void addBackground() {
        Renderable backgroundImage = imageReader.readImage(Constants.BACKGROUND_IMAGE_PATH,
                false);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(windowController.getWindowDimensions().x(),
                        windowController.getWindowDimensions().y()),
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    // Add Bricks
    private void addBricks() {

        Renderable brickRenderer = imageReader.readImage(Constants.BRICK_IMAGE_PATH,
                false);

        int brickWidth = (int) (windowController.getWindowDimensions().x() -
                Constants.WALL_WIDTH) / this.bricksPerRow;
        for (int col = 0; col < this.bricksPerRow; col++) {
            for (int row = 0; row < this.numOfBrickRows; row++) {
                this.strategyFactory = new StrategyFactory(this, brickCounter);
                CollisionStrategy collisionStrategy = this.strategyFactory.chooseStrategy();

                Brick brick = new Brick(
                        new Vector2(Constants.WALL_WIDTH + col * (brickWidth + Constants.SPACER),
                                Constants.WALL_WIDTH + row * (Constants.SPACER + Constants.BRICK_HEIGHT)),
                        new Vector2(brickWidth, Constants.BRICK_HEIGHT), brickRenderer, collisionStrategy);

                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increment();
            }
        }
    }

    // Add Ball.
    private void addBall() {
        Renderable ballImage = imageReader.readImage(Constants.BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(Constants.BLOP_SILENCED);
        this.ball = new Ball(Vector2.ZERO, new Vector2(Constants.BALL_SIZE, Constants.BALL_SIZE),
                ballImage, collisionSound, this);
        ball.setBallStartingMovement(windowController.getWindowDimensions().mult(0.5f));
        ball.setTag(Constants.BALL_TAG);
        this.gameObjects().addGameObject(ball, Layer.DEFAULT);
    }

    // Add Paddle
    private void addPaddle(Vector2 center, String tag) {
        Renderable paddleImage = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, false);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT), paddleImage,
                this.inputListener);
        paddle.setCenter(center);
        paddle.setTag(tag);
        this.gameObjects().addGameObject(paddle, Layer.DEFAULT);
    }

//    public void addCameraHandler() {
//        // TODO: check if need to delete it from gameObjects list
//        if (this.camera() != null) {
//            addObj(new CameraHandler(Vector2.ZERO, Vector2.ZERO, null, ball, this),
//                    Layer.DEFAULT);
//        }
//    }

    // Add Life Counter
    private void addLifeCounter() {
        Renderable heartImage = imageReader.readImage(Constants.HEART_IMAGE_PATH, true);
        GameObjectCollection gameObject = gameObjects();
        LifeBar lifeCounter = new LifeBar(new Vector2(0,
                windowController.getWindowDimensions().y() - Constants.HEART_SIZE),
                new Vector2(Constants.HEART_SIZE, Constants.HEART_SIZE),
                heartImage,
                this, this.livesCounter, Constants.SPACER, Constants.MAX_GAME_LIVES);
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
     * Getter for the soundReader.
     *
     * @return The sound reader.
     */
    public SoundReader soundReader() {
        return soundReader;
    }

    /**
     * Getter for the input reader.
     *
     * @return The input reader.
     */
    public UserInputListener getInputListener() {
        return inputListener;
    }
    /**
     * Getter for the windowController.
     *
     * @return window controller.
     */

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
     * @param subPaddleUsed boolean checks if there is already subPaddle on the game
     */
    public void setSubPaddleUsed(boolean subPaddleUsed) {
        this.subPaddleUsed = subPaddleUsed;
    }

    /**
     * Getter for the lifeCounter
     *
     * @return The life counter.
     */
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
            ball.setBallStartingMovement(windowController.getWindowDimensions().mult(0.5f));
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
            prompt = Constants.LOSING_PROMPT;
        }
        if (brickCounter.value() == 0) {
            prompt = Constants.WINNING_PROMPT;
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
        addWall(Vector2.ZERO, new Vector2(Constants.WALL_WIDTH, windowDimensions.y()));
        // rightWall
        addWall(new Vector2(windowDimensions.x() - Constants.WALL_WIDTH, 0),
                new Vector2(Constants.WALL_WIDTH, windowDimensions.y()));
        // upperWall
        addWall(Vector2.ZERO, new Vector2(windowDimensions.x(), Constants.WALL_WIDTH));

        brickCounter = new Counter();
        addBricks();
        addBall();
        addPaddle(new Vector2(windowController.getWindowDimensions().x() / 2,
                (int) windowController.getWindowDimensions().y() - 30), Constants.MAIN_PADDLE_TAG);

        this.livesCounter = new LifeCounter(Constants.MAX_GAME_LIVES,Constants.GAME_LIVES);
        addLifeCounter();
        this.subPaddleUsed = false;
    }

    /**
     * The main function.
     * 
     * @param args The command line arguments. - numOfBrickRows, bricksPerRow.
     */
    public static void main(String[] args) {
        BrickerGameManager bouncingBallGameManager = new BrickerGameManager(Constants.WINDOW_TITLE,
                new Vector2(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        if (args.length == Constants.NUM_OF_USER_INPUTS) {
            int numOfBrickRows = Integer.parseInt(args[Constants.BRICK_ROWS_LOCATION]);
            int bricksPerRow = Integer.parseInt(args[Constants.BRICKS_PER_ROW_LOCATION]);
            bouncingBallGameManager.brickUpdate(numOfBrickRows, bricksPerRow);
        }
        bouncingBallGameManager.run();
    }
}
