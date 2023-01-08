package com.almasb.fxglgames.pong;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class BulletComponent extends Component {

    public int owner;
    BulletComponent(int ownerID) {
        owner = ownerID;
    }

    private PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {
        checkOffscreen();
    }

    // this is a hack:
    // we use a physics engine, so it is possible to push the ball through a wall to outside of the screen
    private void checkOffscreen() {
        if (getEntity().getBoundingBoxComponent().isOutside(getGameScene().getViewport().getVisibleArea())) {
            getEntity().removeFromWorld();
        }
    }
}
