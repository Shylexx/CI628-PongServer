package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class TilemapComponent extends Component {
  public Entity[][] tiles;
  public int mapHeight;
  public int mapWidth;

  public static final double MAP_X_OFFSET = 0;
  public static final double MAP_Y_OFFSET = 0;
  public static final double TILE_SIZE = 20;

  public TilemapComponent(int width, int height) {
    mapHeight = height;
    mapWidth = width;
  }

  public void spawnMap() {
    tiles = new Entity[mapHeight][mapWidth];
    // Spawn a map
    for(int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        if(y < (mapHeight / 3)) {
          tiles[y][x] = FXGL.spawn("tile", new SpawnData(((x * 20) + MAP_X_OFFSET), ((y * 20) + MAP_Y_OFFSET)).put("type", TileType.EMPTY));
        } else {
          tiles[y][x] = FXGL.spawn("tile", new SpawnData(((x * 20) + MAP_X_OFFSET), ((y * 20) + MAP_Y_OFFSET)).put("type", TileType.WALL));
        }
      }
    }
  }

  public TileComponent TileAtPoint(Point2D point) {
    return tiles[(int) ((point.getY() - MAP_Y_OFFSET) / TILE_SIZE)][(int) ((point.getX() - MAP_X_OFFSET) / TILE_SIZE)].getComponent(TileComponent.class);
  }

  public String MapLocAtPoint(Point2D point) {
    return "X: " + ((int)((point.getX() - MAP_X_OFFSET) / TILE_SIZE)) + " Y: " + ((int)((point.getY() - MAP_Y_OFFSET) / TILE_SIZE));
  }







}
