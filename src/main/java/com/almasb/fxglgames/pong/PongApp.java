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

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.net.*;
import com.almasb.fxgl.ui.UI;
import com.almasb.fxglgames.pong.net.UDPServer;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * A simple clone of Pong.
 * Sounds from https://freesound.org/people/NoiseCollector/sounds/4391/ under CC BY 3.0.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class PongApp extends GameApplication implements MessageHandler<String> {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Pong");
        settings.setVersion("1.0");
        settings.setFontUI("pong.ttf");
        settings.setApplicationMode(ApplicationMode.DEBUG);
        settings.setDeveloperMenuEnabled(true);
    }

    private Entity player1;
    private Entity player2;
    private PlayerComponent player1comp;
    private PlayerComponent player2comp;
    private Entity terrain;
    private Entity mapManager;
    private TilemapComponent mapComponent;

    private Server<String> server;
    private UDPServer udpServer;

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Up1") {
            @Override
            protected void onActionBegin() {
                player1comp.jump();
            }

            @Override
            protected void onActionEnd() {
                player1comp.stopJump();
            }
        }, KeyCode.W);


//        getInput().addAction(new UserAction("Down1") {
//            @Override
//            protected void onAction() {
//                player1comp.down();
//            }
//
//            @Override
//            protected void onActionEnd() {
//                player1comp.stop();
//            }
//        }, KeyCode.S);

      getInput().addAction(new UserAction("Left1") {
        @Override
        protected void onAction() {
          player1comp.left();
        }

        @Override
        protected void onActionEnd() {
          player1comp.stop();
        }
      }, KeyCode.A);

      getInput().addAction(new UserAction("Right1") {
        @Override
        protected void onAction() {
          player1comp.right();
        }

        @Override
        protected void onActionEnd() {
          player1comp.stop();
        }
      }, KeyCode.D);

      getInput().addAction(new UserAction("BreakBelowBlock") {
          @Override
          protected void onActionBegin() {
              //FXGL.debug(mapComponent.MapLocAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)));
              mapComponent.TileAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)).BreakTile();
              //FXGL.debug(mapComponent.TileAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)).getEntity().toString());

          }
      }, KeyCode.X);

      getInput().addAction(new UserAction("MakeBelowBlock") {
        @Override
        protected void onActionBegin() {
          //FXGL.debug(mapComponent.MapLocAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)));
          mapComponent.TileAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)).MakeWall();
          //FXGL.debug(mapComponent.TileAtPoint(new Point2D(player1.getX() + 5, player1.getY() + 70)).getEntity().toString());

        }
      }, KeyCode.Z);

        getInput().addAction(new UserAction("Up2") {
            @Override
            protected void onAction() {
                player2comp.jump();
            }

//            @Override
//            protected void onActionEnd() {
//                player2comp.stopJump();
//            }
        }, KeyCode.I);

//        getInput().addAction(new UserAction("Down2") {
//            @Override
//            protected void onAction() {
//                player2comp.down();
//            }
//
//            @Override
//            protected void onActionEnd() {
//                player2comp.stop();
//            }
//        }, KeyCode.K);

      getInput().addAction(new UserAction("Left2") {
        @Override
        protected void onAction() {
          player2comp.left();
        }

        @Override
        protected void onActionEnd() {
          player2comp.stop();
        }
      }, KeyCode.J);

      getInput().addAction(new UserAction("Right2") {
        @Override
        protected void onAction() {
          player2comp.right();
        }

        @Override
        protected void onActionEnd() {
          player2comp.stop();
        }
      }, KeyCode.L);

      getInput().addAction(new UserAction("ColorChange") {
        // WHEN I PRESS B, CHANGE TILE (7, 3) TO RED TEST
        @Override
        protected void onAction() { mapComponent.tiles[3][7].getComponent(TileComponent.class).ChangeWallToRed();}

      }, KeyCode.B);




    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("player1score", 0);
        vars.put("player2score", 0);
    }

    @Override
    protected void initGame() {

        Writers.INSTANCE.addTCPWriter(String.class, outputStream -> new MessageWriterS(outputStream));
        Readers.INSTANCE.addTCPReader(String.class, in -> new MessageReaderS(in));

        server = getNetService().newTCPServer(55555, new ServerConfig<>(String.class));

        udpServer = new UDPServer();
        udpServer.start();

        server.setOnConnected(connection -> {
            connection.addMessageHandlerFX(this);
            // ASSIGN NEW PLAYERS THEIR ID
            var message = "ON_CONNECT," + server.getConnections().size();

            connection.send(message);
        });


        getGameWorld().addEntityFactory(new PongFactory());
        getGameScene().setBackgroundColor(Color.rgb(0, 0, 5));

        //initScreenBounds();
        initGameObjects();

        var t = new Thread(server.startTask()::run);
        t.setDaemon(true);
        t.start();
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 720);

    }

    @Override
    protected void initUI() {
        MainUIController controller = new MainUIController();
        UI ui = getAssetLoader().loadUI("main.fxml", controller);

        controller.getLabelScorePlayer().textProperty().bind(getip("player1score").asString());
        controller.getLabelScoreEnemy().textProperty().bind(getip("player2score").asString());

        getGameScene().addUI(ui);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (!server.getConnections().isEmpty()) {
            var message = "GAME_DATA," + player1.getX() + "," + player1.getY() + "," + player2.getX() + "," + player2.getY();

            server.broadcast(message);
        }



    }

    private void initScreenBounds() {
        Entity walls = entityBuilder()
                .type(EntityType.WALL)
                .collidable()
                .buildScreenBounds(150);

        getGameWorld().addEntity(walls);
    }

    private void initGameObjects() {
        player1 = spawn("player", new SpawnData(getAppWidth() / 4, getAppHeight() / 2 - 30).put("isPlayer", true));
        player2 = spawn("player", new SpawnData(3 * getAppWidth() / 4 - 20, getAppHeight() / 2 - 30).put("isPlayer", false));

        player1comp = player1.getComponent(PlayerComponent.class);
        player2comp = player2.getComponent(PlayerComponent.class);

        mapManager = spawn("mapManager", new SpawnData(0, 0).put("width", 40).put("height", 50));
        mapComponent = mapManager.getComponent(TilemapComponent.class);
        mapComponent.spawnMap();

    }

    private void playHitAnimation(Entity bat) {
        animationBuilder()
                .autoReverse(true)
                .duration(Duration.seconds(0.5))
                .interpolator(Interpolators.BOUNCE.EASE_OUT())
                .rotate(bat)
                .from(FXGLMath.random(-25, 25))
                .to(0)
                .buildAndPlay();
    }

    @Override
    public void onReceive(Connection<String> connection, String message) {
        var tokens = message.split(",");

        Arrays.stream(tokens).skip(1).forEach(key -> {
          // W_DOWN
//            if (key.endsWith("_DOWN")) {
//              if(key.startsWith("1")) {
//                getInput().mockKeyPress(KeyCode.valueOf(key.substring(0, 1)));
//              } else if (key.startsWith("2")) {
//                getInput().mockKeyPress(KeyCode.valueOf(key.substring(0, 1)));
//              }
//            } else if (key.endsWith("_UP")) {
//              getInput().mockKeyRelease(KeyCode.valueOf(key.substring(0, 1)));
//            }

            switch (key.charAt(0)) {
              case '1': {
                // Player1
                if(key.endsWith("_DOWN")) {
                  switch (key.charAt(1)) {
                    case 'W': {
                      player1comp.jump();
                      break;
                    }
//                    case 'S': {
//                      player1comp.down();
//                      break;
//                    }
                    case 'A': {
                      player1comp.left();
                      break;
                    }
                    case 'D': {
                      player1comp.right();
                      break;
                    }
                  }
                } else if (key.endsWith("_UP")) {
                  switch (key.charAt(1)) {
                    case 'W': {
                      player1comp.stopJump();
                      break;
                    }
//                    case 'S': {
//                      player1comp.stop();
//                      break;
//                    }
                    case 'A': {
                      player1comp.stop();
                      break;
                    }
                    case 'D': {
                      player1comp.stop();
                      break;
                    }
                  }
                }
                break;
              }
              case '2': {
                // Player2
                if(key.endsWith("_DOWN")) {
                  // On button press
                  switch (key.charAt(1)) {
                    case 'W': {
                      player2comp.jump();
                      break;
                    }
//                    case 'S': {
//                      player2comp.down();
//                      break;
//                    }
                    case 'A': {
                      player2comp.left();
                      break;
                    }
                    case 'D': {
                      player2comp.right();
                      break;
                    }
                  }
                } else if (key.endsWith("_UP")) {
                  // On button release
                  switch (key.charAt(1)) {
                    case 'W': {
                      player2comp.stopJump();
                      break;
                    }
//                    case 'S': {
//                      player2comp.stop();
//                      break;
//                    }
                    case 'A': {
                      player2comp.stop();
                      break;
                    }
                    case 'D': {
                      player2comp.stop();
                      break;
                    }
                  }
                }
                break;
              }
            }
        });
    }

    static class MessageWriterS implements TCPMessageWriter<String> {

        private OutputStream os;
        private PrintWriter out;

        MessageWriterS(OutputStream os) {
            this.os = os;
            out = new PrintWriter(os, true);
        }

        @Override
        public void write(String s) throws Exception {
            out.print(s.toCharArray());
            out.flush();
        }
    }

    static class MessageReaderS implements TCPMessageReader<String> {

        private BlockingQueue<String> messages = new ArrayBlockingQueue<>(50);

        private InputStreamReader in;

        MessageReaderS(InputStream is) {
            in =  new InputStreamReader(is);

            var t = new Thread(() -> {
                try {

                    char[] buf = new char[36];

                    int len;

                    while ((len = in.read(buf)) > 0) {
                        var message = new String(Arrays.copyOf(buf, len));

                        System.out.println("Recv message: " + message);

                        messages.put(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            t.setDaemon(true);
            t.start();
        }

        @Override
        public String read() throws Exception {
            return messages.take();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
