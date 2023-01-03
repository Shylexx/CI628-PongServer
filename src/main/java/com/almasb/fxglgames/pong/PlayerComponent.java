package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerComponent extends Component {

  private static final double MOVE_SPEED = 200;

  protected PhysicsComponent physics;

  @Override
  public void onAdded() {
    super.onAdded();
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
