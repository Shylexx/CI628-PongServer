package com.almasb.fxglgames.pong;

import com.almasb.fxgl.pathfinding.astar.AStarGrid;

public class Tilemap {
  private AStarGrid map;

  public Tilemap(int width, int height) {
    this.map = new AStarGrid(width, height);
  }


}
