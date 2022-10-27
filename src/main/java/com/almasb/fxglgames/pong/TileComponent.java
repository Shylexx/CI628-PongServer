package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TileComponent extends Component {
  public TileType type;

  protected ViewComponent view;
  protected Rectangle rect;

  public TileComponent() {
    type = TileType.EMPTY;
  }

  public TileComponent(TileType initType) {
    type = initType;
  }

  @Override
  public void onAdded() {
    FXGLForKtKt.getGameTimer().runAtInterval(() -> {
      ((Rectangle)view.getChildren().get(0)).setFill(Color.color(Math.random(), Math.random(), Math.random()));
    }, Duration.millis(300));
  }

  @Override
  public void onUpdate(double tpf) {

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
