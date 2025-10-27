package group9.eng.events;

import com.badlogic.gdx.math.Vector2;

import group9.eng.TimeTable;

/**
 * A hidden event that occurs once throughout the game, causes the player to lose their timetable and forced to get a new one.
 * 
 * Chooses a random square zone on the map, when the player enters the zone with their timetable, it is lost and a piece of
 * dialogue instructs them to get a new one from the Computer Science building.
 * When triggered once, it cannot be triggered again.
 */
public class TimeTableEvent extends GameEvent {
    private final float zoneX;
    private final float zoneY;
    private final float zoneWidth;
    private final float zoneHeight;

    /**
     * Designates a random zone on the map where the event will be triggered.
     * 
     * @param mapWidth the width of the map.
     * @param mapHeight the height of the map.
     */
    public TimeTableEvent(float mapWidth, float mapHeight) {
        // adjust to make event area bigger/smaller
        this.zoneWidth = 40f;
        this.zoneHeight = 40f;

        this.zoneX = (float)(Math.random() * (mapWidth - zoneWidth));
        this.zoneY = (float)(Math.random() * (mapHeight - zoneHeight));
    }

    /**
     * Checks if the event should be triggered based on the players positon (if they're in the zone) and timetable state.
     * 
     * @param playerPos the players current positon.
     * @param timeTable the players timetable state.
     */
    @Override
    public void update(Vector2 playerPos, TimeTable timeTable) {
        if (isPlayerInZone(playerPos) && !eventTriggered && timeTable.hasTimeTable()) {
            timeTable.lostTimeTable();

        if (dialogue != null) {
            dialogue.show("Oh no! You've lost your timetable, the wind blew it away!\nVisit the Computer Science reception to get a new one.");
            }
            eventTriggered = true;
        }
    }

    /** 
     * Checks if the player is in the event zone.
     * 
     * @param playerPos the players current position.
     * @return true if the PlayerPos lies within the event zone, false otherwise.
     */
    private boolean isPlayerInZone(Vector2 playerPos) {
        return playerPos.x >= zoneX && playerPos.x <= zoneX + zoneWidth &&
               playerPos.y >= zoneY && playerPos.y <= zoneY + zoneHeight;
    }
}
