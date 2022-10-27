package com.almasb.fxglgames.pong;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileComponent extends Component {
  public TileType type;

  protected ViewComponent view;

  public TileComponent() {
    type = TileType.EMPTY;
  }

  public TileComponent(TileType initType) {
    type = initType;
  }

  public void MakeWall() {
    type = TileType.WALL;
    view.setVisible(true);
    ((Rectangle) view.getChildren().get(0)).setFill(Color.BLUE);
  }

  public void ChangeWallToRed() {
    ((Rectangle) view.getChildren().get(0)).setFill(Color.RED);
  }

  public void BreakTile() {
    type = TileType.EMPTY;
    view.setVisible(false);
  }
}
