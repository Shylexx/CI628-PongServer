package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerComponent extends Component {

  private static final double MOVE_SPEED = 420;

  protected PhysicsComponent physics;

  public void up() {
    if (entity.getY() >= MOVE_SPEED / 60)
      physics.setVelocityY(-MOVE_SPEED);
    else
      stop();
  }

  public void jump() {
    if (physics.isOnGround()) {
      physics.setVelocityY(-420);
    }
  }

  public void stopJump() {
    if (!physics.isOnGround()) {
      physics.setVelocityY(0);
    }
  }

  public void down() {
    if (entity.getBottomY() <= FXGL.getAppHeight() - (MOVE_SPEED / 60))
      physics.setVelocityY(MOVE_SPEED);
    else
      stop();
  }

  public void left() {
    if (entity.getX() >= MOVE_SPEED / 60)
      physics.setVelocityX(-MOVE_SPEED);
    else
      stop();
  }

  public void right() {
    if (entity.getRightX() <= FXGL.getAppWidth() - (MOVE_SPEED / 60))
      physics.setVelocityX(MOVE_SPEED);
    else {
      stop();
    }
  }

  public void stop() {
    physics.setLinearVelocity(0, 0);
  }

}
