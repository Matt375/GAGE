package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple asteroid
 *
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Asteroid extends SpaceEntity {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * random width and height size for the asteroid
     */
    private static Random r = new Random();
    private static float max = 50;
    private static  float min = 20;
    private static final float WIDTH = min + r.nextFloat() * max;
    private static final float HEIGHT = min + r.nextFloat() * max;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an asteroid
     *
     * @param startX     x location of the asteroid
     * @param startY     y location of the asteroid
     * @param gameScreen Gamescreen to which asteroid belongs
     */
    public Asteroid(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, WIDTH, HEIGHT, null, gameScreen);

        Random random = new Random();

        mBitmap = gameScreen.getGame().getAssetManager().getBitmap(random.nextBoolean() ? "Asteroid1" : "Asteroid2");

        mRadius = 20;
        mMass = 1000.0f;

        angularVelocity = random.nextFloat() * 240.0f - 20.0f;

    }
}
