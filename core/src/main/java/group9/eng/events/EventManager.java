package group9.eng.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import group9.eng.Entity;

/**
 * Manages the triggering of game events by detecting collisions.
 * It attaches a ContactListener to the physics World to identify when
 * an Entity enters or leaves an EventTrigger.
 */
public class EventManager {
    /**
     * Reference to the physics world to listen for contacts.
     */
    private World world;
    /**
     * A list of event triggers that are currently in contact with an entity.
     * Only these triggers are updated each frame.
     */
    private List<EventTrigger> activeEventTriggers;

    /**
     * Constructs an EventManager and sets up the Box2D ContactListener.
     *
     * @param world The Box2D physics world.
     */
    public EventManager(World world) {
        this.world = world;
        this.activeEventTriggers = new ArrayList<EventTrigger>();

        // Set up the contact listener to detect entity-event collisions
        this.world.setContactListener(new ContactListener() {
            /**
             * Called when two physics fixtures begin to overlap.
             * Checks if one is an Entity and the other is an EventTrigger.
             * If so, adds the entity to the trigger and adds the trigger to the
             * active list.
             *
             * @param contact The contact object describing the collision.
             */
            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null)
                    return; // Ignore fixtures without user data

                // Ensure 'a' is the Entity and 'b' is the EventTrigger
                if (a.getClass() == EventTrigger.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                // Check for the specific collision pair we care about
                if (a.getClass() == Entity.class && b.getClass() == EventTrigger.class) {
                    ((EventTrigger) b).addEntity((Entity) a);
                    activeEventTriggers.add((EventTrigger) b);
                }
            }

            /**
             * Called when two physics fixtures cease to overlap.
             * Checks if one was an Entity and the other an EventTrigger.
             * If so, removes the entity from the trigger and removes the trigger
             * from the active list.
             *
             * @param contact The contact object describing the collision.
             */
            @Override
            public void endContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a == null || b == null)
                    return;

                // Ensure 'a' is the Entity and 'b' is the EventTrigger
                if (a.getClass() == EventTrigger.class) {
                    Object temp = a;
                    a = b;
                    b = temp;
                }

                // Check for the specific collision pair
                if (a.getClass() == Entity.class && b.getClass() == EventTrigger.class) {
                    ((EventTrigger) b).removeEntity((Entity) a);
                    activeEventTriggers.remove((EventTrigger) b);
                }
            }

            /**
             * Not used in this implementation.
             */
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            /**
             * Not used in this implementation.
             */
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    /**
     * Updates all event triggers that are currently active (i.e., in contact
     * with an entity). This should be called once per game frame.
     */
    public void update() {
        // Iterate over a copy to avoid ConcurrentModificationException if list changes
        for (EventTrigger t : new ArrayList<>(activeEventTriggers)) {
            t.update();
        }
    }
}