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

    private void create_collision_shapes() {
        TiledMapTileLayer layer = (TiledMapTileLayer) mapData.getLayers().get(1);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null) continue;

                MapObjects cellObjects = cell.getTile().getObjects();
                if (cellObjects.getCount() != 1) continue;

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(x * tileSize, y * tileSize);
                Body body = physicsWorld.createBody(bodyDef);

                for (MapObject mapObject: cellObjects) {
                    if (mapObject instanceof PolygonMapObject) {
                        PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                        Polygon polygon = polygonMapObject.getPolygon();

                        polygon.setOrigin(tileSize * 0.5f, tileSize * 0.5f);

                        System.out.println();

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

                        System.out.println(vertices);

                        for (int i = 0; i < vertices.size(); i++) {
                            Vector2 v = vertices.get(i);
                            floatVertices[i*2] = v.x;
                            floatVertices[i*2+1] = v.y;
                        }

                        polygon.setScale(
                            cell.getFlipHorizontally() ? -1 : 1,
                            cell.getFlipVertically()   ? -1 : 1
                        );
                        
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
