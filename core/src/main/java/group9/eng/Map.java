package group9.eng;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Map {
    private World physicsWorld;
    private Viewport viewport;
    private TiledMap mapData;
    private OrthogonalTiledMapRenderer mapRenderer;

    private int tileSize;
    private int width;
    private int height;

    public Map(World physicsWorld, Viewport viewport) {
        this.physicsWorld = physicsWorld;
        this.viewport = viewport;

        mapData = new TmxMapLoader().load("Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(mapData, 1f);

        MapProperties mapProperties = mapData.getProperties();
        tileSize = mapProperties.get("tilewidth", Integer.class);
        width = mapProperties.get("width", Integer.class) * tileSize;
        height = mapProperties.get("height", Integer.class) * tileSize;

        create_collision_shapes();
    }

    /**
     * Creates Box2D collision shapes using map data from a Tiled map.
     */
    private void create_collision_shapes() {
        // At the moment only looks at layer 1 (2nd layer) of the map.
        // This could be changed by adding a for loop and iterating over all layers if needed.
        TiledMapTileLayer layer = (TiledMapTileLayer) mapData.getLayers().get(1);

        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                // If the current map cell is empty, don't bother to run any of the other code.
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null) continue;
                MapObjects cellObjects = cell.getTile().getObjects();
                if (cellObjects.getCount() < 1) continue;
                
                // Here we create a Box2D static body (doesn't move, but has collision).
                // On its own it doesn't have a shape, this needs to be added later.
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(x * tileSize, y * tileSize);
                Body body = physicsWorld.createBody(bodyDef);

                // Looping through each collision shape that has been defined for the current tile
                // in Tiled. At the moment only polygons are supported.
                for (MapObject mapObject: cellObjects) {
                    if (mapObject instanceof PolygonMapObject) {
                        PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                        Polygon polygon = polygonMapObject.getPolygon();
                        
                        /*
                         * In order to deal with tiles that have been flipped horizontally or
                         * vertically, we need to do some extra work with the collision polygon's
                         * vertices. The vertices are stored in memory as an 1D array of floats,
                         * with the x and y coordinates stored one after another. To make them
                         * easier to work with we move them into a list of Vector2s.
                         * 
                         * The first vertex in the list is always (0, 0), and all of the other
                         * vertices are relative to this first vertex. The actual position of the
                         * first vertex on the tile is stored separately. To make it easier to deal
                         * with the vertices they are all adjusted to their actual positions
                         * instead of their position relative to the first vertex.
                         */
                        float[] floatVertices = polygon.getVertices();
                        ArrayList<Vector2> vertices = new ArrayList<Vector2>();
                        for (int i = 0; i < floatVertices.length; i += 2) {
                            Vector2 v = new Vector2(floatVertices[i], floatVertices[i+1]);
                            v.add(
                                polygon.getX(),
                                polygon.getY()
                            );
                            vertices.add(v);
                        }
                        polygon.setPosition(0, 0);
                        
                        // Writing the Vector2 vertices back into the list of floats to update the
                        // original polygon.
                        for (int i = 0; i < vertices.size(); i++) {
                            Vector2 v = vertices.get(i);
                            floatVertices[i*2] = v.x;
                            floatVertices[i*2+1] = v.y;
                        }
                        
                        // If the tile has been flipped, we must flip the polygon around the tile's
                        // centre to match the tile.
                        polygon.setOrigin(tileSize * 0.5f, tileSize * 0.5f);
                        polygon.setScale(
                            cell.getFlipHorizontally() ? -1 : 1,
                            cell.getFlipVertically()   ? -1 : 1
                        );
                        
                        // Finally the collision shape is added to the static body.
                        PolygonShape polygonShape = new PolygonShape();
                        polygonShape.set(polygon.getTransformedVertices());
                        body.createFixture(polygonShape, 0.0f);
                        polygonShape.dispose();
                    }
                }
            }
        }
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) viewport.getCamera());
        mapRenderer.render();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
