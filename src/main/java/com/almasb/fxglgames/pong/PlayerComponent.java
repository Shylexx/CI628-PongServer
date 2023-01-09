package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import java.util.concurrent.ThreadLocalRandom;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class PlayerComponent extends Component {

  public int playerId;
  public Point2D spawnPoints[] = {
          new Point2D(getAppWidth() / 4, getAppHeight() / 2 - 30),
          new Point2D(3 * getAppWidth() / 4 - 20, getAppHeight() / 2 - 30),
          new Point2D(getAppWidth() / 4, getAppHeight() / 4),
          new Point2D((getAppWidth() / 4) * 3, (getAppHeight() / 4) * 3),
  };

  PlayerComponent(int id) {
    playerId = id;
  }

  private static final double MOVE_SPEED = 50;
  private boolean dead = false;

  protected PhysicsComponent physics;

  @Override
  public void onAdded() {
    super.onAdded();
  }

  @Override
  public void onUpdate(double tpf) {
    if(dead) {
      int randomIndex = ThreadLocalRandom.current().nextInt(0, 3 + 1);
      physics.overwritePosition(spawnPoints[randomIndex]);
      dead = false;
    }
  }

  public void onHit() {
    dead = true;
  }

  public void fireLeft() {
    FXGL.spawn("bullet", new SpawnData(entity.getX() - 24, entity.getY()).put("ownerID", playerId).put("dir", BulletDir.LEFT));
  }

  public void fireRight() {
    FXGL.spawn("bullet", new SpawnData(entity.getX() + 24, entity.getY()).put("ownerID", playerId).put("dir", BulletDir.RIGHT));
  }
  public void fireUp() {
    FXGL.spawn("bullet", new SpawnData(entity.getX(), entity.getY() - 24).put("ownerID", playerId).put("dir", BulletDir.UP));
  }
  public void fireDown() {
    FXGL.spawn("bullet", new SpawnData(entity.getX(), entity.getY() + 24).put("ownerID", playerId).put("dir", BulletDir.DOWN));
  }
  public void up() {
    if (entity.getY() >= MOVE_SPEED / 60)
      physics.setVelocityY(-MOVE_SPEED);
    else
      stopY();
  }

  public void down() {
    if (entity.getY() >= MOVE_SPEED / 60)
      physics.setVelocityY(MOVE_SPEED);
    else
      stopY();
  }

  public void left() {
      physics.setVelocityX(-MOVE_SPEED);
  }

  public void right() {
      physics.setVelocityX(MOVE_SPEED);

  }

  public void stopX() {
    physics.setVelocityX(0);
  }
  public void stopY() { physics.setVelocityY(0);}

}
