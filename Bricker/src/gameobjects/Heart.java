package gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.BrickerGameManager;
import danogl.collisions.Collision;
import main.Constants;
/**
 * Class representing a gameobject - the heart life counter.
 */
public class Heart extends GameObject{

    private final BrickerGameManager gameManager;
    /**
     * Construct a new GameObject instance of a heart.

     *
     * @param topLeftCorner   Position of the object, in window coordinates
     *                        (pixels). Note that (0,0) is the top-left corner of
     *                        the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderable  The renderable representing the object. Can be null,
     *                        in which case the GameObject will not be rendered.
     * @param gameManager an instance of the game manager

     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.gameManager = gameManager;
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
        if(gameManager.outOfBound(this)){
            gameManager.removeObj(this, Layer.DEFAULT);
        }
    }

    /**
     * Should this object be allowed to collide the the specified other object.
     * If both this object returns true for the other, and the other returns true
     * for this one, the collisions may occur when they overlap, meaning that their
     * respective onCollisionEnter/onCollisionStay/onCollisionExit will be called.
     * Note that this assumes that both objects have been added to the same
     * GameObjectCollection, and that its handleCollisions() method is invoked.
     * @param other The other GameObject.
     * @return true if the objects should collide. This does not guarantee a collision
     * would actually collide if they overlap, since the other object has to confirm
     * this one as well.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Constants.MAIN_PADDLE_TAG);
    }

    /**
     * Called on the first frame of a collision.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameManager.getLifeCounter().increment();
        gameManager.removeObj(this, Layer.DEFAULT);
    }
}
