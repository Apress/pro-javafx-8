package com.projavafx;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class VanishingCircles extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Vanishing Circles");
    Group root = new Group();
    Scene scene = new Scene(root, 800, 600, Color.BLACK);
    List<Circle> circles = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      final Circle circle = new Circle(150);
      circle.setCenterX(Math.random() * 800);
      circle.setCenterY(Math.random() * 600);
      circle.setFill(new Color(Math.random(), Math.random(), Math.random(), .2));
      circle.setEffect(new BoxBlur(10, 10, 3));
      circle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent t) -> {
          KeyValue collapse = new KeyValue(circle.radiusProperty(), 0);
          new Timeline(new KeyFrame(Duration.seconds(3), collapse)).play();
      });
      circle.setStroke(Color.WHITE);
      circle.strokeWidthProperty().bind(Bindings.when(circle.hoverProperty())
        .then(4)
        .otherwise(0));
      circles.add(circle);
    }
    root.getChildren().addAll(circles);
    primaryStage.setScene(scene);
    primaryStage.show();
    
    Timeline moveCircles = new Timeline();
    circles.stream().forEach((circle) -> {
          KeyValue moveX = new KeyValue(circle.centerXProperty(), Math.random() * 800);
          KeyValue moveY = new KeyValue(circle.centerYProperty(), Math.random() * 600);
          moveCircles.getKeyFrames().add(new KeyFrame(Duration.seconds(40), moveX, moveY));
      });
    moveCircles.play();
  }
}
