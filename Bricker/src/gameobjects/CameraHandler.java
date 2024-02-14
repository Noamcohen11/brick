package gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.BrickerGameManager;

public class CameraHandler extends GameObject {
    private Ball ball;
    private BrickerGameManager gameManager;
    private int curCollisionsCnt;
    private static final int DELTA_COLLISIONS =5;
    public CameraHandler(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Ball ball,
                         BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.ball = ball;
        this.gameManager = gameManager;
        this.curCollisionsCnt = ball.getBallCollisionCounter();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(ball.getBallCollisionCounter() == this.curCollisionsCnt+DELTA_COLLISIONS)
        {
            gameManager.setCamera(null);
        }
    }
}
