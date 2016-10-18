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
 *  AudioPlayer2.java - Playing audio files in JavaFX 2.0.
 *
 */
package projavafx.audioplayer2;

import java.net.URL;
import javafx.application.Application;
import javafx.collections.MapChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author dean
 */
public class AudioPlayer2 extends Application {
  private Media media;
  private MediaPlayer mediaPlayer;
  
  private Label artist;
  private Label album;
  private Label title;
  private Label year;
  private ImageView albumCover;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    createControls();
    createMedia();
    
    final Scene scene = new Scene(createGridPane(), 800, 400);
    final URL stylesheet = getClass().getResource("media.css");
    scene.getStylesheets().add(stylesheet.toString());

    primaryStage.setScene(scene);
    primaryStage.setTitle("Audio Player 2");
    primaryStage.show();
  }

  private GridPane createGridPane() {
    final GridPane gp = new GridPane();
    gp.setPadding(new Insets(10));
    gp.setHgap(20);
    gp.add(albumCover, 0, 0, 1, GridPane.REMAINING);
    gp.add(title, 1, 0);
    gp.add(artist, 1, 1);
    gp.add(album, 1, 2);
    gp.add(year, 1, 3);
    
    final ColumnConstraints c0 = new ColumnConstraints();
    final ColumnConstraints c1 = new ColumnConstraints();
    c1.setHgrow(Priority.ALWAYS);
    gp.getColumnConstraints().addAll(c0, c1);
    
    final RowConstraints r0 = new RowConstraints();
    r0.setValignment(VPos.TOP);
    gp.getRowConstraints().addAll(r0, r0, r0, r0);
    
    return gp;
  }

  private void createControls() {
    artist = new Label();
    artist.setId("artist");
    album = new Label();
    album.setId("album");
    title = new Label();
    title.setId("title");
    year = new Label();
    year.setId("year");
    
    final Reflection reflection = new Reflection();
    reflection.setFraction(0.2);

    final URL url = getClass().getResource("resources/defaultAlbum.png");
    final Image image = new Image(url.toString());
    
    albumCover = new ImageView(image);
    albumCover.setFitWidth(240);
    albumCover.setPreserveRatio(true);
    albumCover.setSmooth(true);
    albumCover.setEffect(reflection);
  }

  private void createMedia() {
    try {
      media = new Media("http://traffic.libsyn.com/dickwall/JavaPosse373.mp3");
      media.getMetadata().addListener((Change<? extends String, ? extends Object> ch) -> {
          if (ch.wasAdded()) {
              handleMetadata(ch.getKey(), ch.getValueAdded());
          }
      });

      mediaPlayer = new MediaPlayer(media);
      mediaPlayer.setOnError(() -> {
          final String errorMessage = media.getError().getMessage();
          // Handle errors during playback
          System.out.println("MediaPlayer Error: " + errorMessage);
      });

      mediaPlayer.play();

    } catch (RuntimeException re) {
      // Handle construction errors
      System.out.println("Caught Exception: " + re.getMessage());
    }
  }

  private void handleMetadata(String key, Object value) {
      switch (key) {
          case "album":
              album.setText(value.toString());
              break;
          case "artist":
              artist.setText(value.toString());
              break;
          case "title":
              title.setText(value.toString());
              break;
          case "year":
              year.setText(value.toString());
              break;
          case "image":
              albumCover.setImage((Image)value);
              break;
      }
  }
}
