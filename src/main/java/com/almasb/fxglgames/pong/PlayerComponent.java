package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerComponent extends Component {

  private static final double MOVE_SPEED = 200;

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

  public void left() {
      physics.setVelocityX(-MOVE_SPEED);
  }

  public void right() {
      physics.setVelocityX(MOVE_SPEED);

  }

  public void stop() {
    physics.setVelocityX(0);
  }

}
