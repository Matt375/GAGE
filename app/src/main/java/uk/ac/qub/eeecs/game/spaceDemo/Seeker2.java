package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.particle.Emitter;
import uk.ac.qub.eeecs.gage.engine.particle.ParticleSystemManager;
import uk.ac.qub.eeecs.gage.util.MathsHelper;
import uk.ac.qub.eeecs.gage.util.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.util.Vector2;

public class Seeker2 extends SpaceEntity {
    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * random size for the Seeker
     */
    private static Random r = new Random();
    private static float max = 40;
    private static float min = 30;
    private static float size = min + r.nextFloat() * max;
    /**
     * Distance at which the spaceship should avoid other game objects
     */
    private float mSeparateThreshold = 75.0f;

    /**
     * Accumulators used to build up the net steering outcome
     */
    private Vector2 mAccAccumulator = new Vector2();
    private Vector2 mAccComponent = new Vector2();

    /**
     * Particle emitter providing a thrust trail for this spaceship
     */
    private Emitter movementEmitter;

    /**
     * Offset/location variables for the movement emitter so it appears to
     * exit from the back of the spaceship.
     */
    private Vector2 movementEmitterOffset;
    private Vector2 movementEmitterLocation;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an AI controlled seeker spaceship
     *
     * @param startX        x location of the AI spaceship
     * @param startY        y location of the AI spaceship
     * @param gameScreen    Gamescreen to which AI belongs
     */
    public Seeker2(float startX, float startY, SpaceshipDemoScreen gameScreen) {
        super(startX, startY, size,size, null, gameScreen);

        // Define movement variables for the seeker
        maxAcceleration = 30.0f;
        maxVelocity = 50.0f;
        maxAngularVelocity = 150.0f;
        maxAngularAcceleration = 300.0f;

        mRadius = size;
        mMass = 10.0f;

        // Define the appearance of the seeker
        mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship3");

        // Create an offset for the movement emitter based on the size of the spaceship
        movementEmitterOffset = new Vector2(-size, 0.0f);
        movementEmitterLocation = new Vector2(position);
        movementEmitterLocation.add(movementEmitterOffset);

        // Create and add a particle effect for the movement of the ship
        ParticleSystemManager particleSystemManager =
                ((SpaceshipDemoScreen) mGameScreen).getParticleSystemManager();
        movementEmitter = new Emitter(
                particleSystemManager, "txt/particle/ThrusterEmitter.JSON",
                movementEmitterLocation);
        particleSystemManager.addEmitter(movementEmitter);
    }
    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the AI Spaceship
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Seek towards the player
        SteeringBehaviours.seek(this,
                ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position,
                acceleration);

        // Try to avoid a collision with the player ship
        SteeringBehaviours.separate(this,
                ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship(),
                mSeparateThreshold, 1.0f, mAccComponent);
        mAccAccumulator.set(mAccComponent);

        // Try to avoid a collision with the other space entities
        SteeringBehaviours.separate(this,
                ((SpaceshipDemoScreen) mGameScreen).getSpaceEntities(),
                mSeparateThreshold, 1.0f, mAccComponent);
        mAccAccumulator.add(mAccComponent);

        // If we are trying to avoid a collision then combine
        // it with the seek behaviour, placing more emphasis on
        // avoiding a collision.
        if (!mAccAccumulator.isZero()) {
            acceleration.x = 0.1f * acceleration.x + 0.9f * mAccAccumulator.x;
            acceleration.y = 0.1f * acceleration.y + 0.9f * mAccAccumulator.y;
        }

        // Make sure we point in the direction of travel.
        angularAcceleration = SteeringBehaviours.alignWithMovement(this);

        // Call the sprite's superclass to apply the determined accelerations
        super.update(elapsedTime);

        // Update the particle emitter associated with this ship to rhe new position,
        // calculating an offset so the particles emerge from the rear of the ship
        MathsHelper.rotateOffsetAboutCentre(
                position, movementEmitterOffset, orientation, movementEmitterLocation);
        movementEmitter.setPosition(movementEmitterLocation.x, movementEmitterLocation.y);
    }
}
