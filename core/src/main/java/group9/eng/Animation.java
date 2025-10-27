package group9.eng;

import com.badlogic.gdx.Gdx;

public class Animation {
    private float timer;
    private float speed;
    private int[][] indices;
    private float angle;

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

    public Animation(float speed, int... indices) {
        this(speed, new int[][] {indices});
    }

    public void update() {
        timer += Gdx.graphics.getDeltaTime() * speed;
        timer = (float) mod(timer, indices.length);
    }

    public int getIndex() {
        return indices[
            (int) mod(angle / 360 * indices.length, indices.length)
        ]
        [
            (int) (timer)
        ];
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private double mod(double a, double b) {
        return (a % b + b) % b;
    }

    public void reset() {
        timer = 0;
    }
}
