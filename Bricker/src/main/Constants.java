package main;

public class Constants {

    /**
     * constant for the title of the popping window
     */
    public static final String WINDOW_TITLE = "Bricker";
    /**
     * constant for the width if the popping window
     */
    public static final int WINDOW_WIDTH = 700;
    /**
     * constant for the height if the popping window
     */
    public static final int WINDOW_HEIGHT = 500;

    // Objects constants.
    /**
     * constant for default number of bricks rows
     */
    public static final int DEFAULT_BRICK_ROWS = 7;
    /**
     * constant for default number of bricks per row
     */
    public static final int DEFAULT_BRICKS_PER_ROW = 8;
    /**
     * constant for the height of each brick
     */
    public static final int BRICK_HEIGHT = 15;
    /**
     * constant for each wall width
     */
    public static final int WALL_WIDTH = 5;
    /**
     * constant for the space between to bricks
     */
    public static final int SPACER = 5;
    /**
     * constant for the main ball velocity
     */
    public static final float BALL_VELOCITY = 250;
    /**
     * constant for the main ball size
     */
    public static final float BALL_SIZE = 50;
    /**
     * constant for the collisions that the ball experienced since a chane of the camera happened
     */
    public static final int DELTA_BALL_COLLISIONS =4;
    /**
     * constant for a paddle width
     */
    public static final float PADDLE_WIDTH = 200;
    /**
     * constant for a paddle height
     */
    public static final float PADDLE_HEIGHT = 20;
    /**
     * constant for a paddle speed
     */
    public static final float PADDLE_MOVEMENT_SPEED = 300;
    /**
     * constant for a subPaddle max hits before its disappears
     */
    public static final int SUBPADDLE_MAX_HITS = 4;
    /**
     * constant for a size of puck ball added when a ballStrategy happens
     */
    public static final float PUCK_SIZE = BALL_SIZE * 0.75f;
    /**
     * constant for the tag of the main ball
     */
    public static final String BALL_TAG = "Ball";
    /**
     * constant for the main paddle tag
     */
    public static final String MAIN_PADDLE_TAG = "Main_paddle";
    /**
     * constant for the sub paddle tag that appears when a extraPaddleStrategy happens
     */
    public static final String SUB_PADDLE_TAG = "Sub_paddle";
    /**
     * constant for the puck balls tag
     */
    public static final String PUCK_TAG = "PuckBall";

    // UI constants.
    /**
     * constant for a size of a heart life
     */
    public static final int HEART_SIZE = 20;
    /**
     * constant for heart tag
     */
    public static final String HEART_TAG = "Heart";
    /**
     * constant for a heart velocity
     */
    public static final float HEART_VELOCITY = 100f;

    /**
     * constant for initial lives on the game
     */
    public static final int GAME_LIVES = 3;
    /**
     * constant for maximum life the user can get
     */
    public static final int MAX_GAME_LIVES = 4;
    /**
     * constant for a yellow numeric display of life
     */
    public static final int YELLOW_COUNTER = 2;
    /**
     * constant for a red numeric display of life
     */
    public static final int RED_COUNTER = 1;
    /**
     * constant for a yes no window dialog message
     */
    public static final String PLAY_AGAIN_PROMPT = " Play again?";
    /**
     * constant for lose prompt message
     */
    public static final String LOSING_PROMPT = "you lose!" + PLAY_AGAIN_PROMPT;
    /**
     * constant for win prompt message
     */
    public static final String WINNING_PROMPT = "you win!" + PLAY_AGAIN_PROMPT;

    // strategyFactory constants
    /**
     * constant for choosing the basic strategy
     */
    public static final int BASIC_STRATEGY = 0;
    /**
     * constant for the random chose of a strategy
     */
    public static final int STRATEGY_TYPE_NUM = 2;
    /**
     * constant for max strategies possible to one brick
     */
    public static final int MAX_STRATEGY_COUNT = 3;
    /**
     * constant for choosing the extra ball strategy
     */
    public static final int EXTRA_BALL = 0;
    /**
     * constant for choosing the extra paddle strategy
     */
    public static final int EXTRA_PADDLE = 1;
    /**
     * constant for choosing the camera change strategy
     */
    public static final int CHANGE_CAMERA = 2;
    /**
     * constant for choosing the extra life strategy
     */
    public static final int EXTRA_LIFE = 3;
    /**
     * constant for choosing the special strategy
     */
    public static final int SPECIEL_STRATEGY_TYPE_NUM = 5;

    // Assets
    /**
     * constant for the assets folder path
     */
    public static final String ASSETS_FOLDER = "Bricker/assets";
    /**
     * constant for the main ball image path
     */
    public static final String BALL_IMAGE_PATH = ASSETS_FOLDER + "/ball.png";
    /**
     * constant for a brick image path
     */
    public static final String BRICK_IMAGE_PATH = ASSETS_FOLDER + "/brick.png";
    /**
     * constant for a paddle image path
     */
    public static final String PADDLE_IMAGE_PATH = ASSETS_FOLDER + "/paddle.png";
    /**
     * constant for the background image path
     */
    public static final String BACKGROUND_IMAGE_PATH = ASSETS_FOLDER + "/DARK_BG2_small.jpeg";
    /**
     * constant for the heart image path
     */
    public static final String HEART_IMAGE_PATH = ASSETS_FOLDER + "/heart.png";
    /**
     * constant for the collision ball sound
     */
    public static final String BLOP_SILENCED = ASSETS_FOLDER + "/blop_cut_silenced.wav";
    /**
     * constant for the puck ball image path
     */
    public static final String PUCK_IMAGE_PATH = ASSETS_FOLDER + "/mockBall.png";

    // User input
    /**
     * constant for the user input of bricks rows number
     */
    public static final int BRICK_ROWS_LOCATION = 0;
    /**
     * constant for the user input of bricks per rows
     */
    public static final int BRICKS_PER_ROW_LOCATION = 1;
    /**
     * constant for the number of  user inputs
     */
    public static final int NUM_OF_USER_INPUTS = 2;
}
