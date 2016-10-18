package com.projavafx;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.GroupBuilder;
import javafx.scene.SceneBuilder;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class VanishingCirclesBuilder extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Vanishing Circles");
    List<Circle> circles = new ArrayList<Circle>();
    for (int i = 0; i < 50; i++) {
      Circle circle = CircleBuilder.create()
        .radius(150)
        .centerX(Math.random() * 800)
        .centerY(Math.random() * 600)
        .stroke(Color.WHITE)
        .fill(new Color(Math.random(), Math.random(), Math.random(), .2))
        .effect(new BoxBlur(10, 10, 3))
        .onMouseClicked(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent t) {
            TimelineBuilder.create().keyFrames(
              new KeyFrame(Duration.seconds(3),
                new KeyValue(((Circle) t.getSource()).radiusProperty(), 0))
            ).build().play();
          }
        })
        .build();
      circle.strokeWidthProperty().bind(Bindings.when(circle.hoverProperty())
        .then(4)
        .otherwise(0));
      circles.add(circle);
    }
    primaryStage.setScene(SceneBuilder.create()
      .width(800)
      .height(600)
      .fill(Color.BLACK)
      .root(GroupBuilder.create()
        .children(
          circles
        ).build()
      ).build());
    primaryStage.show();
    
    List<KeyFrame> keyFrames = new ArrayList<KeyFrame>();
    for (Circle circle : circles) {
      keyFrames.add(new KeyFrame(Duration.seconds(40),
        new KeyValue(circle.centerXProperty(), Math.random() * 800),
        new KeyValue(circle.centerYProperty(), Math.random() * 600)));
    }
    TimelineBuilder.create()
      .keyFrames(keyFrames)
      .build().play();
  }
}
