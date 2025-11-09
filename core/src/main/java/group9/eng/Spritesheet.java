package group9.eng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Allows for an image file to easily be split up for use in animations.
 */
public class Spritesheet {
    private final Texture texture;
    private final TextureRegion[][] frames;
    private final int width;
    private final int tileWidth;
    private final int tileHeight;

    /**
     * Creates a new spritesheet from an image file.
     * @param filename the name of the image file to use.
     * @param tileWidth the width of each frame.
     * @param tileHeight the height of each frame.
     */
    public Spritesheet(String filename, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        texture = new Texture(Gdx.files.internal(filename));
        frames = TextureRegion.split(texture, this.tileWidth, this.tileHeight);
        width = frames[0].length;
    }

    /**
     * Returns a frame at the given index, if the frames of the spritesheet were numbered from
     * left to right then top to bottom (0 is top left, highest index is bottom right).
     * @param index the index of the frame to get.
     * @return the frame (a TextureRegion) at the given index.
     */
    public TextureRegion get(int index) {
        return get(index % width, index / width);
    }

    /**
     * Returns a frame at the given coordinates on the spritesheet.
     * @param x the x position of the frame to get.
     * @param y the y position of the frame to get.
     * @return the frame (a TextureRegion) at the given coordinates.
     */
    public TextureRegion get(int x, int y) {
        return frames[y][x];
    }
    
    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
