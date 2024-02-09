package gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import java.awt.Color;
import danogl.util.Vector2;
import danogl.util.Counter;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

/**
 * Class represting a gameobject - the heart life counter.
 */
public class LifeBar extends GameObject {
    private GameObject[] hearts;
    private Counter counter;
    private int hearts_places;
    private GameObjectCollection gameObjects;
    private TextRenderable textRenderable;
    private static final int YELLOW_COUNTER = 2;
    private static final int RED_COUNTER = 1;

    /**
     * Construct a new GameObject instance of the life counter.
     * Shows the hearts and the numbers.
     *
     * @param topLeftCorner   Position of the object, in window coordinates
     *                        (pixels). Note that (0,0) is the top-left corner of
     *                        the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderableHeart The renderable representing the object. Can be null,
     *                        in which case the GameObject will not be rendered.
     * @param gameObjects     The game objects collection to add the hearts to.
     * @param counter         The counter to represent the number of lives.
     * @param spacer          The space between the hearts and the number of lives.
     * @param maxLifeNum      The maximum number of lives to represent.
     * 
     */
    public LifeBar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderableHeart,
            GameObjectCollection gameObjects, Counter counter, int spacer, int maxLifeNum) {

        super(topLeftCorner, dimensions, null);

        this.counter = counter;
        this.hearts_places = 0;
        this.gameObjects = gameObjects;
        this.textRenderable = new TextRenderable(Integer.toString(counter.value()));
        textRenderable.setColor(Color.green);
        renderer().setRenderable(textRenderable);

        hearts = new GameObject[maxLifeNum];
        for (int i = 0; i < maxLifeNum; i++) {
            hearts[i] = new GameObject(topLeftCorner.add(new Vector2((i + 1) * (dimensions.x() + spacer),
                    0)), dimensions, renderableHeart);
        }

    }

    /**
     * Update the heart life counter and the numeric counter.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (counter.value() < hearts.length && hearts_places < counter.value()) {
            for (int i = hearts_places; i < counter.value(); i++) {
                gameObjects.addGameObject(hearts[i], Layer.UI);
            }
        } else if (counter.value() > 0 && hearts_places > counter.value()) {
            for (int i = counter.value(); i < hearts_places; i++) {
                gameObjects.removeGameObject(hearts[i], Layer.UI);
            }
        }
        hearts_places = counter.value();

        if (counter.value() >= 0) {
            textRenderable.setString(Integer.toString(counter.value()));
        }
        if (counter.value() == RED_COUNTER) {
            textRenderable.setColor(Color.red);
        } else if (counter.value() == YELLOW_COUNTER) {
            textRenderable.setColor(Color.yellow);

        } else {
            textRenderable.setColor(Color.green);
        }
        renderer().setRenderable(textRenderable);
    }

}