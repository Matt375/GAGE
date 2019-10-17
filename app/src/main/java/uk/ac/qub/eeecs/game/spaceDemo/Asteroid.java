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
    private static final float DEFAULT_RADIUS = 20;
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
        super(startX, startY, DEFAULT_RADIUS * 2.0f, DEFAULT_RADIUS * 2.0f, null, gameScreen);

        Random random = new Random();

        String[] asteroids = {"Asteroid1","Asteroid2","Asteroid3","Asteroid4"};
        String pickedAsteroid = asteroids[random.nextInt(asteroids.length)];
        mBitmap = gameScreen.getGame().getAssetManager().getBitmap(pickedAsteroid);

        mRadius = 20;
        mMass = 1000.0f;

        angularVelocity = random.nextFloat() * 240.0f - 20.0f;

        float max = 50;
        float min = 20;
        float ranWidthVal = min + random.nextFloat() * max;
        float ranHeightVal = min + random.nextFloat() * max;

        setWidth(ranWidthVal);
        setHeight(ranHeightVal);
    }
}
