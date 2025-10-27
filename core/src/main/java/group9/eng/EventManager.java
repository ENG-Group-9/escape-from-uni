package group9.eng;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class EventManager {
    private World world;
    private List<Event> activeEvents;

    public EventManager(World world) {
        this.world = world;

        this.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null) return;

                if (a.getClass() == Event.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                if (a.getClass() == Entity.class && b.getClass() == Event.class) {
                    ((Event) b).addEntity((Entity) a);
                    activeEvents.add((Event) b);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null) return;

                if (a.getClass() == Event.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                if (a.getClass() == Entity.class && b.getClass() == Event.class) {
                    ((Event) b).removeEntity((Entity) a);
                    activeEvents.remove((Event) b);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

        activeEvents = new ArrayList<Event>();
    }

    public void update() {
        for (Event e: activeEvents) {
            e.update();
        }
    }
}
