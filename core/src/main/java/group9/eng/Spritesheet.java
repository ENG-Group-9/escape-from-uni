package group9.eng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spritesheet {
    private Texture texture;
    private TextureRegion[][] frames;
    private int width;
    private int tileWidth;
    private int tileHeight;

    public Spritesheet(String filename, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        texture = new Texture(Gdx.files.internal(filename));
        frames = TextureRegion.split(texture, this.tileWidth, this.tileHeight);
        width = frames[0].length;
    }

    public TextureRegion get(int index) {
        return get(index % width, index / width);
    }

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
