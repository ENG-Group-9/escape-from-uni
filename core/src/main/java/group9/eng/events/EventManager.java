package group9.eng.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import group9.eng.Entity;

public class EventManager {
    private World world;
    private List<EventTrigger> activeEventTriggers;

    public EventManager(World world) {
        this.world = world;

        this.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null) return;

                if (a.getClass() == EventTrigger.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                if (a.getClass() == Entity.class && b.getClass() == EventTrigger.class) {
                    ((EventTrigger) b).addEntity((Entity) a);
                    activeEventTriggers.add((EventTrigger) b);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null) return;

                if (a.getClass() == EventTrigger.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                if (a.getClass() == Entity.class && b.getClass() == EventTrigger.class) {
                    ((EventTrigger) b).removeEntity((Entity) a);
                    activeEventTriggers.remove((EventTrigger) b);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

        activeEventTriggers = new ArrayList<EventTrigger>();
    }

    public void update() {
        for (EventTrigger t: activeEventTriggers) {
            t.update();
        }
    }
}
