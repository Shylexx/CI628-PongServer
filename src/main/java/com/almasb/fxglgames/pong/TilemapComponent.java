package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

public class TilemapComponent extends Component {
  public Entity[][] tiles;
  public int mapHeight;
  public int mapWidth;

  public TilemapComponent(int width, int height) {
    mapHeight = height;
    mapWidth = width;
  }

  public void spawnMap() {
    tiles = new Entity[mapHeight][mapWidth];
    // Spawn a map
    for(int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        tiles[y][x] = FXGL.spawn("tile", new SpawnData(x * 10, y * 10).put("type", TileType.WALL));
      }
    }
  }







}
