package group9.eng;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.BodyComponent;

public class Camera {
    private Viewport viewport;
    
    private Vector3 targetPosVector3;
    private Vector2 targetPos;
    private BodyComponent targetBodyComponent;

    public Camera(Viewport viewport) {
        this.viewport = viewport;
        targetPos = new Vector2();
        targetPosVector3 = new Vector3();
    }

    public void setTarget(Entity target) {
        targetBodyComponent = (BodyComponent)target.getComponent(BodyComponent.class);
    }

    public void setTargetPos(Vector2 targetPos) {
        this.targetPos.set(targetPos);
        targetBodyComponent = null;
    }

    public void update() {
        if (targetBodyComponent != null) {
            targetPos.set(targetBodyComponent.getPosition());
        }
        targetPosVector3.set(targetPos.x, targetPos.y, 0);
        viewport.getCamera().position.lerp(targetPosVector3, 0.1f);
    }
}
