/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2017 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.fxglgames.pong;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.tiled.Tile;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class PongFactory implements EntityFactory {

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.3f).restitution(1.0f));
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(5 * 60, -5 * 60));

        var endGame = getip("player1score").isEqualTo(10).or(getip("player2score").isEqualTo(10));

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.startColorProperty().bind(
                Bindings.when(endGame)
                        .then(Color.LIGHTYELLOW)
                        .otherwise(Color.LIGHTYELLOW)
        );

        emitter.endColorProperty().bind(
                Bindings.when(endGame)
                        .then(Color.RED)
                        .otherwise(Color.LIGHTBLUE)
        );

        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setSize(5, 10);
        emitter.setEmissionRate(1);

        return entityBuilder(data)
                .type(EntityType.BALL)
                .bbox(new HitBox(BoundingShape.circle(5)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new ParticleComponent(emitter))
                .with(new BallComponent())
                .build();
    }

/*    @Spawns("bat")
    public Entity newBat(SpawnData data) {
        boolean isPlayer = data.get("isPlayer");

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(isPlayer ? EntityType.PLAYER_BAT : EntityType.ENEMY_BAT)
                .viewWithBBox(new Rectangle(20, 60, Color.LIGHTGRAY))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new BatComponent())
                .build();
    }*/

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
      boolean isPlayer = data.get("isPlayer");
      int id = data.get("playerID");

      PhysicsComponent physics = new PhysicsComponent();
      physics.setBodyType(BodyType.DYNAMIC);
      //physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(2, 58), BoundingShape.box(16, 2)));

      // this avoids player sticking to walls
      physics.setFixtureDef(new FixtureDef().friction(0.0f));

      int playerID = data.get("playerID");
      Color playerColor;
      if (playerID == 1) {
          playerColor = Color.GREEN;
      } else {
          playerColor = Color.RED;
      }

      return entityBuilder(data)
              .type(EntityType.PLAYER)
              .view(new Rectangle(16, 16, playerColor))
              .bbox(new HitBox("bodybox", new Point2D(0, 0), BoundingShape.circle(10)))
              .with(physics)
              .with(new CollidableComponent(true))
              .with(new PlayerComponent(id))
              .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        int owner = data.get("ownerID");

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        physics.setFixtureDef(new FixtureDef().density(0.3f).restitution(1.0f));

        BulletDir dir = data.get("dir");
        switch (dir) {
            case UP:
                physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, -5 * 10));
                break;
            case DOWN:
                physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 5 * 10));
                break;
            case LEFT:
                physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(-5 * 10, 0));
                break;
            case RIGHT:
                physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(5 * 10, 0));
                break;
        }



        return entityBuilder(data)
                .type(EntityType.BULLET)
                .bbox(new HitBox(BoundingShape.circle(5)))
                .view(new Rectangle(8, 8, Color.YELLOW))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new BulletComponent(owner))
                .build();
    }

    @Spawns("mapManager")
    public Entity newMapManager(SpawnData data) {
      return entityBuilder(data)
              .type(EntityType.MAP)
              .with(new TilemapComponent())
              .build();
    }

    @Spawns("tile")
    public Entity newTile(SpawnData data) {
        TileType type = data.get("type");
        int xcoord = data.get("x");
        int ycoord = data.get("y");

        PhysicsComponent physics = new PhysicsComponent();
        // Bodies start as static, can become dynamic when no surrounding tiles
        physics.setBodyType(BodyType.STATIC);

        boolean collidable;
        if (type == TileType.WALL) {
            collidable = true;
        } else {
            collidable = false;
        }

        return entityBuilder(data)
                .type(EntityType.TILE)
                .view(new Rectangle(16, 16, Color.BLUE))
                .bbox(new HitBox("tilebox", new Point2D(0, 0), BoundingShape.box(16, 16)))
                .with(physics)
                .with(new TileComponent(type, xcoord, ycoord))
                .with(new CollidableComponent(collidable))
                .build();

    }
}
