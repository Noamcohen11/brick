package main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Paddle;
import java.util.Random;
import danogl.util.Counter;
import gameobjects.LifeBar;

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

    // UI constants.
    private static final int HEART_SIZE = 20;
    private static final int GAME_LIVES = 3;
    private static final int MAX_GAME_LIVES = 10;
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String LOSING_PROMPT = "you lose!" + PLAY_AGAIN_PROMPT;
    private static final String WINNING_PROMPT = "you win!" + PLAY_AGAIN_PROMPT;

    // Assets
    private static final String ASSETS_FOLDER = "assets";
    private static final String BALL_IMAGE_PATH = ASSETS_FOLDER + "/ball.png";
    private static final String BRICK_IMAGE_PATH = ASSETS_FOLDER + "/brick.png";
    private static final String PADDLE_IMAGE_PATH = ASSETS_FOLDER + "/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = ASSETS_FOLDER + "/DARK_BG2_small.jpeg";
    private static final String HEART_IMAGE_PATH = ASSETS_FOLDER + "/heart.png";
    private static final String BLOP_SILENCED = ASSETS_FOLDER + "/blop_cut_silenced.wav";

    // User input
    public static final int BRICK_ROWS_LOCATION = 0;
    public static final int BRICKS_PER_ROW_LOCATION = 1;
    public static final int NUM_OF_USER_INPUTS = 2;

    //////////////////////////////////////////
    // Game Privates /////////////////////////
    //////////////////////////////////////////

    // Game state.
    private Counter livesCounter;
    private Counter brickCounter;
    private int numOfBrickRows = DEFAULT_BRICK_ROWS;
    private int bricksPerRow = DEFAULT_BRICKS_PER_ROW;

    // The objects.
    private Ball ball;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    // Add wall
    private void addWall(Vector2 topLeftCorner, Vector2 dimensions) {
        GameObject wall = new GameObject(topLeftCorner, dimensions, null);
        this.gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
    }

    // Set the ball starting point and velocity.
    private void set_ball_starting_movement() {
        ball.setCenter(windowController.getWindowDimensions().mult(0.5f));
        Random rand = new Random();
        double angle = rand.nextDouble() * 2 * Math.PI;
        float ballVelX = (float) (BALL_VELOCITY * Math.cos(angle));
        float ballVelY = (float) (BALL_VELOCITY * Math.sin(angle));
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
        Renderable brickRenderer = imageReader.readImage(BRICK_IMAGE_PATH, false);
        int brickWidth = (int) (windowController.getWindowDimensions().x() - WALL_WIDTH) / this.bricksPerRow;
        for (int col = 0; col < this.bricksPerRow; col++) {
            for (int row = 0; row < this.numOfBrickRows; row++) {
                CollisionStrategy collisionStrategy = new BasicCollisionStrategy(gameObjects(), brickCounter);
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
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        set_ball_starting_movement();
        this.gameObjects().addGameObject(ball, Layer.DEFAULT);
    }

    // Add Paddle
    private void addPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        GameObject Paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener);
        Paddle.setCenter(new Vector2(windowController.getWindowDimensions().x() / 2,
                (int) windowController.getWindowDimensions().y() - 30));
        this.gameObjects().addGameObject(Paddle, Layer.DEFAULT);
    }

    // Add Life Counter
    private void addLifeCounter() {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, false);
        LifeBar lifeCounter = new LifeBar(new Vector2(0, windowController.getWindowDimensions().y() - HEART_SIZE),
                new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage,
                gameObjects(), this.livesCounter, SPACER, MAX_GAME_LIVES);
        gameObjects().addGameObject(lifeCounter, Layer.UI);
    }

    /**
     * Update the game state, and check for game end.
     * 
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Check if the ball is out of the game. if so, decrease the lives counter.
        // And bring the ball back to the game.
        double ballHeight = ball.getCenter().y();
        float gameBottom = windowController.getWindowDimensions().y();
        if (ballHeight > gameBottom) {
            this.livesCounter.decrement();
            set_ball_starting_movement();

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
        addPaddle();

        this.livesCounter = new Counter(GAME_LIVES);
        addLifeCounter();
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
