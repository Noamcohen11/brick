package gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.BrickerGameManager;
import main.Constants;
/**
 * Class representing a gameobject - an objects that handle the with the camera behaviour once it created.
 */

public class CameraHandler extends GameObject {
    private final Ball ball;
    private final BrickerGameManager gameManager;
    private final int curCollisionsCnt;
    /**
     * Construct a new GameObject instance of a camera handler.

     *
     * @param topLeftCorner   Position of the object, in window coordinates
     *                        (pixels). Note that (0,0) is the top-left corner of
     *                        the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderable  The renderable representing the object. Can be null,
     *                        in which case the GameObject will not be rendered.
     * @param ball      a ball to follow
     * @param gameManager an instance of the game manager

     */
    public CameraHandler(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Ball ball,
                         BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.ball = ball;
        this.gameManager = gameManager;
        this.curCollisionsCnt = ball.getBallCollisionCounter();
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * Note that the time that passes between subsequent calls to this method is not constant.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(ball.getBallCollisionCounter() >= this.curCollisionsCnt+ Constants.DELTA_BALL_COLLISIONS)
        {
            gameManager.setCamera(null);
            gameManager.removeObj(this, Layer.DEFAULT);

        }
    }
}
