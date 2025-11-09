package group9.eng;

import com.badlogic.gdx.Gdx;

/**
 * A class that stores and plays an animation. Separate from the actual images, just keeps track
 * of the current frame index using a timer. Allows for different indices to be defined for
 * different directions, so if an entity is facing in a different direction it will look different.
 */
public class Animation {
    private float timer;
    private float speed;
    private int[][] indices;
    private float angle;

    /**
     * Creates a new animation with different frame indices for different directions.
     * @param speed how much the animation's timer will increment each frame (multiplied by
     *              deltatime).
     * @param indices arrays of frame indices, each for different directions. Must all be of the
     *                same length.
     */
    public Animation(float speed, int[]... indices) {
        this.speed = speed;
        this.indices = indices;
        for (int[] i: indices) {
            if (i.length != indices[0].length) {
                throw new RuntimeException(
                    "All variations of animation for different directions must be equal in length."
                );
            }
        }
        timer = 0;
    }

    /**
     * Creates a new animation.
     * @param speed how much the animation's timer will increment each frame (multiplied by
     *              deltatime).
     * @param indices an array of frame indices.
     */
    public Animation(float speed, int... indices) {
        this(speed, new int[][] {indices});
    }

    /**
     * Updates the animation timer.
     */
    public void update() {
        timer += Gdx.graphics.getDeltaTime() * speed;
        timer = (float) mod(timer, indices.length);
    }

    /**
     * Returns the current frame index based on the animation timer.
     * @return the current frame index.
     */
    public int getIndex() {
        return indices[
            (int) mod(angle / 360 * indices.length, indices.length)
        ]
        [
            (int) (timer)
        ];
    }

    /**
     * Change which set of frame indices to use given a direction in degrees.
     * @param angle an angle in degrees.
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    private double mod(double a, double b) {
        return (a % b + b) % b;
    }

    /**
     * Reset the animation timer to 0.
     */
    public void reset() {
        timer = 0;
    }
}
