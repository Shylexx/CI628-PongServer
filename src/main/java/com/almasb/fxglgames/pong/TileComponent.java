package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TileComponent extends Component {
  public TileType type;

  protected ViewComponent view;
  protected PhysicsComponent physics;
  protected BoundingBoxComponent bbox;
  protected Rectangle rect;

  public TileComponent() {
    type = TileType.EMPTY;
  }

  public TileComponent(TileType initType) {
    type = initType;
  }

  @Override
  public void onAdded() {
//    FXGLForKtKt.getGameTimer().runAtInterval(() -> {
//      ((Rectangle)view.getChildren().get(0)).setFill(Color.color(Math.random(), Math.random(), Math.random()));
//    }, Duration.millis(300));
    switch (type) {
      case EMPTY: {
        view.setVisible(false);
        // DEBUG: MAKE EMPTY TILES GREEN
        //((Rectangle) view.getChildren().get(0)).setFill(Color.GREEN);

        physics.setOnPhysicsInitialized(() -> physics.getBody().setActive(false));
        break;
      }
      case WALL: {
      }
    }
  }

  public void MakeWall() {
    if (type == TileType.WALL) return;

    type = TileType.WALL;
    view.setVisible(true);
    physics.getBody().setActive(true);
    ((Rectangle) view.getChildren().get(0)).setFill(Color.BLUE);
    getEntity().getComponent(CollidableComponent.class).setValue(true);
  }

  public void ChangeWallToRed() {
    ((Rectangle) view.getChildren().get(0)).setFill(Color.RED);
  }

  public void BreakTile() {
    if (type == TileType.EMPTY) return;

    type = TileType.EMPTY;
    view.setVisible(false);
    //bbox.clearHitBoxes();
    physics.getBody().setActive(false);
    getEntity().getComponent(CollidableComponent.class).setValue(false);
  }

}
