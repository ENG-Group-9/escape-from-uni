package group9.eng;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import group9.eng.events.GameEvent;
import group9.eng.events.TimeTableEvent;

/**
 * Manages all the ga
 */
public class EventManager   {
    private List<GameEvent> events;

    private float csBuildingWidth;
    private float csBuildingHeight;
    private float csBuildingX;
    private float csBuildingY;


    public EventManager(Map map) {
        events = new ArrayList<GameEvent>();

        this.csBuildingWidth = 30f;
        this.csBuildingHeight = 30f * 64f / 96f;
        this.csBuildingX = 8f;
        this.csBuildingY = 6f;

        TimeTableEvent hiddenEvent = new TimeTableEvent(map.getWidth(), map.getHeight());
        addEvent(hiddenEvent);
    }

    public void addEvent(GameEvent event)   {
        events.add(event);
    }

    public void update(Vector2 playerPos, TimeTable timeTable) {
        for (GameEvent event : events) {
            event.update(playerPos, timeTable);
        }
    }

    public void setEventDialogue(EventDialogue dialogue)  {
        for (GameEvent event : events)  {
            event.setEventDialogue(dialogue);
        }
    }

    public boolean checkCSBuilding(Vector2 playerPos)   {
        return playerInCS(playerPos);
    }

    private boolean playerInCS(Vector2 playerPos)   {
        float px = playerPos.x;
        float py = playerPos.y;

        return px >= csBuildingX && px <= csBuildingX + csBuildingWidth &&
               py >= csBuildingY && py <= csBuildingY + csBuildingHeight;
    }

    // getters for external classes
    public float getCsBuildingX() { return csBuildingX; }
    public float getCsBuildingY() { return csBuildingY; }
    public float getCsBuildingHeight() { return csBuildingHeight; }
    public float getCsBuildingWidth() { return csBuildingWidth; }
}