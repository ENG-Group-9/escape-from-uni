package group9.eng;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.BodyComponent;

/**
 * Represents a camera system that tracks a target entity or stationary positon.
 * Works by having the camera smoothly transition to the target.
 */
public class Camera {
    private Viewport viewport;

    private Vector3 targetPosVector3;
    private Vector2 targetPos;
    private BodyComponent targetBodyComponent;

    /**
     * Creates a Camera with a specified viewport.
     * @param viewport the {@link Viewport} object used to control how the camera is managed.
     */
    public Camera(Viewport viewport) {
        this.viewport = viewport;
        targetPos = new Vector2();
        targetPosVector3 = new Vector3();
    }

    /**
     * Sets an entity for the camera to follow.
     * @param target the {@link Entity} the camera follows.
     */
    public void setTarget(Entity target) {
        targetBodyComponent = (BodyComponent)target.getComponent(BodyComponent.class);
    }

    /**
     * Sets a fixed positon for the camera to focus on.
     * @param targetPos the coordinates in the world for the camera to focus on.
     */
    public void setTargetPos(Vector2 targetPos) {
        this.targetPos.set(targetPos);
        targetBodyComponent = null;
    }

    /**
     * Updates the cameras positon. If a target entity is set the camera moves gradually towards its position.
     * Otherwise, the camera moves towards the fixed positon set by {@link #setTargetPos(Vector2)}
     */
    public void update() {
        if (targetBodyComponent != null) {
            targetPos.set(targetBodyComponent.getPosition());
        }
        targetPosVector3.set(targetPos.x, targetPos.y, 0);
        viewport.getCamera().position.lerp(targetPosVector3, 0.1f);
    }
}
