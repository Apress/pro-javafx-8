package projavafx.videoplayer2;

/*
 * Copyright (c) 2011, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *  VideoPlayer2.java - An example from the Pro JavaFX Platform book.
 */


import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author dean
 */
public class VideoPlayer2 extends Application {

  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    final Label markerText = new Label();
    StackPane.setAlignment(markerText, Pos.TOP_CENTER);
    
    String workingDir = System.getProperty("user.dir");
    final File f = new File(workingDir, "../media/omgrobots.flv");
    
    final Media m = new Media(f.toURI().toString());
    
    final ObservableMap<String, Duration> markers = m.getMarkers();
    markers.put("Robot Finds Wall", Duration.millis(3100));
    markers.put("Then Finds the Green Line", Duration.millis(5600));
    markers.put("Robot Grabs Sled", Duration.millis(8000));
    markers.put("And Heads for Home", Duration.millis(11500));
    
    final MediaPlayer mp = new MediaPlayer(m);
    mp.setOnMarker((event) -> {
        Platform.runLater(() -> {
            markerText.setText(event.getMarker().getKey());
        });
    });
    
    final MediaView mv = new MediaView(mp);
    
    final StackPane root = new StackPane();
    root.getChildren().addAll(mv, markerText);
    root.setOnMouseClicked((event) -> {
        mp.seek(Duration.ZERO);
        markerText.setText("");
    });
    
    final Scene scene = new Scene(root, 960, 540);
    final URL stylesheet = getClass().getResource("media.css");
    scene.getStylesheets().add(stylesheet.toString());
    
    primaryStage.setScene(scene);
    primaryStage.setTitle("Video Player 2");
    primaryStage.show();
    
    mp.play();
  }
}
