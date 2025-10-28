package group9.eng;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import group9.eng.events.GameEvent;
import group9.eng.events.TimeTableEvent;

/**
 * Manages all the games events within the world.
 * Keeps track of any active events, updates them depending on if their active or not,
 * and handles any specific interactions to do with the events.
 */
public class EventManager {
    private List<GameEvent> events;

    private final float csBuildingWidth;
    private final float csBuildingHeight;
    private final float csBuildingX;
    private final float csBuildingY;

    /**
     * Creates an EventManager for the provided map.
     * Initialises the Computer Science buildings position and boundaries,
     * and adds any the TimeTableEvent to the list of GameEvents.
     *
     * @param map the map used within the game.
     */
    public EventManager(Map map) {
        events = new ArrayList<GameEvent>();

        this.csBuildingWidth = 30f; // adjust to change building size
        this.csBuildingHeight = 30f * 64f / 96f; // width divided by the size of the sprite to stay proportionate
        this.csBuildingX = 8f; // adjust to change building location
        this.csBuildingY = 6f;

        TimeTableEvent hiddenEvent = new TimeTableEvent(map.getWidth(), map.getHeight());
        addEvent(hiddenEvent);
    }

    /**
     * Adds a new event to the EventManager list.
     *
     * @param event the {@link GameEvent} to add.
     */
    public void addEvent(GameEvent event) {
        events.add(event);
    }

    /**
     * Updates all managed events based on the players position and timetable state.
     *
     * @param playerPos the current position of the player.
     * @param timeTable the players timetable logic, used for TimeTableEvent logic.
     */
    public void update(Vector2 playerPos, TimeTable timeTable) {
        for (GameEvent event : events) {
            event.update(playerPos, timeTable);
        }
    }

    /**
     * Sets the dialogue for every event.
     *
     * @param dialogue the {@link EventDialogue} used for the event.
     */
    public void setEventDialogue(EventDialogue dialogue) {
        for (GameEvent event : events) {
            event.setEventDialogue(dialogue);
        }
    }

    /**
     * Checks  if the player is within the Computer Science building zone.
     *
     * @param playerPos the current position of the player.
     * @return True if the player is within the Computer Science building, false otherwise.
     */
    public boolean checkCSBuilding(Vector2 playerPos) {
        return playerInCS(playerPos);
    }

    /**
     * Helper for assessing if the player is within the Computer Science buildings boundaries.
     *
     * @param playerPos the position of the player currently.
     * @return true if the player is in the area of the Computer Science building, false otherwise.
     */
    private boolean playerInCS(Vector2 playerPos) {
        float px = playerPos.x;
        float py = playerPos.y;

        return px >= csBuildingX && px <= csBuildingX + csBuildingWidth &&
            py >= csBuildingY && py <= csBuildingY + csBuildingHeight;
    }

    // getters for external classes

    /**
     * @return the X coordinate of the Computer Science building's bottom left corner.
     */
    public float getCsBuildingX() {
        return csBuildingX;
    }

    /**
     * @return the Y coordinate of the Computer Science building's bottom left corner.
     */
    public float getCsBuildingY() {
        return csBuildingY;
    }

    /**
     * @return the height of the Computer Sciences building's area.
     */
    public float getCsBuildingHeight() {
        return csBuildingHeight;
    }

    /**
     * @return the width of the Computer Sciences building's area.
     */
    public float getCsBuildingWidth() {
        return csBuildingWidth;
    }
}
// COMMENTED OUT TEMPORARILY
/*
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
*/
