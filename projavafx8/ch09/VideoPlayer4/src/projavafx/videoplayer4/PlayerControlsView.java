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
 */
package projavafx.videoplayer4;

import java.io.File;
import java.net.URL;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * @author dean
 */
class PlayerControlsView extends AbstractView {
  private Image pauseImg;
  private Image playImg;
  private ImageView playPauseIcon;
  
  private StatusListener statusListener;
  private CurrentTimeListener currentTimeListener;
  
  private Node controlPanel;
  private Label statusLabel;
  private Label currentTimeLabel;
  private Label totalDurationLabel;
  private Slider volumeSlider;
  private Slider positionSlider;
  private Button eqButton;
  
  public PlayerControlsView(MediaModel mediaModel) {
    super(mediaModel);
    
    mediaModel.mediaPlayerProperty().addListener(new MediaPlayerListener());
    
    statusListener = new StatusListener();
    currentTimeListener = new CurrentTimeListener();
    addListenersAndBindings(mediaModel.getMediaPlayer());
  }

  @Override
  protected Node initView() {
    final Button openButton = createOpenButton();
    eqButton = createEQButton();
    controlPanel = createControlPanel();
    volumeSlider = createSlider("volumeSlider");
    statusLabel = createLabel("Buffering", "statusDisplay");
    positionSlider = createSlider("positionSlider");
    totalDurationLabel = createLabel("00:00", "mediaText");
    currentTimeLabel = createLabel("00:00", "mediaText");
    
    positionSlider.valueChangingProperty().addListener(new PositionListener());
    
    final ImageView volLow = new ImageView();
    volLow.setId("volumeLow");
    
    final ImageView volHigh = new ImageView();
    volHigh.setId("volumeHigh");
    
    final GridPane gp = new GridPane();
    gp.setHgap(1);
    gp.setVgap(1);
    gp.setPadding(new Insets(10));

    final ColumnConstraints buttonCol = new ColumnConstraints(100);
    final ColumnConstraints spacerCol = new ColumnConstraints(40, 80, 80);
    final ColumnConstraints middleCol = new ColumnConstraints();
    middleCol.setHgrow(Priority.ALWAYS);

    gp.getColumnConstraints().addAll(buttonCol, spacerCol, middleCol, 
                                     spacerCol, buttonCol);

    GridPane.setValignment(openButton, VPos.BOTTOM);
    GridPane.setHalignment(eqButton, HPos.RIGHT);
    GridPane.setValignment(eqButton, VPos.BOTTOM);
    GridPane.setHalignment(volHigh, HPos.RIGHT);
    GridPane.setValignment(volumeSlider, VPos.TOP);
    GridPane.setHalignment(statusLabel, HPos.RIGHT);
    GridPane.setValignment(statusLabel, VPos.TOP);
    GridPane.setHalignment(currentTimeLabel, HPos.RIGHT);
    
    gp.add(openButton, 0, 0, 1, 3);
    gp.add(volLow, 1, 0);
    gp.add(volHigh, 1, 0);
    gp.add(volumeSlider, 1, 1);
    gp.add(controlPanel, 2, 0, 1, 2);
    gp.add(statusLabel, 3, 1);
    gp.add(currentTimeLabel, 1, 2);
    gp.add(positionSlider, 2, 2);
    gp.add(totalDurationLabel, 3, 2);
    gp.add(eqButton, 4, 0, 1, 3);

    return gp;
  }

  @Override
  public void setNextHandler(EventHandler<ActionEvent> nextHandler) {
    eqButton.setOnAction(nextHandler);
  }

  private Button createOpenButton() {
    final Button button = new Button();
    button.setId("openButton");
    button.setOnAction(new OpenHandler());
    button.setPrefWidth(48);
    button.setPrefHeight(32);
    return button;
  }

  private Button createEQButton() {
    final Button button = new Button("EQ");
    button.setId("eqButton");
    button.setPrefWidth(48);
    button.setPrefHeight(32);
    return button;
  }
  
  private Node createControlPanel() {
    final HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);
    hbox.setFillHeight(false);
    
    final Button playPauseButton = createPlayPauseButton();

    final Button seekStartButton = new Button();
    seekStartButton.setId("seekStartButton");
    seekStartButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        seekAndUpdatePosition(Duration.ZERO);
      }
    });

    final Button seekEndButton = new Button();
    seekEndButton.setId("seekEndButton");
    seekEndButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
        final Duration totalDuration = mediaPlayer.getTotalDuration();
        final Duration oneSecond = Duration.seconds(1);
        seekAndUpdatePosition(totalDuration.subtract(oneSecond));
      }
    });

    hbox.getChildren().addAll(seekStartButton, playPauseButton, seekEndButton);
    return hbox;
  }

  private Button createPlayPauseButton() {
    URL url = getClass().getResource("resources/pause.png");
    pauseImg = new Image(url.toString());

    url = getClass().getResource("resources/play.png");
    playImg = new Image(url.toString());

    playPauseIcon = new ImageView(playImg);

    final Button playPauseButton = new Button(null, playPauseIcon);
    playPauseButton.setId("playPauseButton");
    playPauseButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
          mediaPlayer.pause();
        } else {
          mediaPlayer.play();
        }
      }
    });
    return playPauseButton;
  }

  private Slider createSlider(String id) {
    final Slider slider = new Slider(0.0, 1.0, 0.1);
    slider.setId(id);
    slider.setValue(0);
    return slider;
  }

  private Label createLabel(String text, String styleClass) {
    final Label label = new Label(text);
    label.getStyleClass().add(styleClass);
    return label;
  }

  private void addListenersAndBindings(final MediaPlayer mp) {
    mp.statusProperty().addListener(statusListener);
    mp.currentTimeProperty().addListener(currentTimeListener);
    mp.totalDurationProperty().addListener(new TotalDurationListener());
    
    mp.setOnEndOfMedia(new Runnable() {
      @Override
      public void run() {
        mediaModel.getMediaPlayer().stop();
      }
    });
    
    volumeSlider.valueProperty().bindBidirectional(mp.volumeProperty());
  }

  private void removeListenersAndBindings(MediaPlayer mp) {
    volumeSlider.valueProperty().unbind();
    mp.statusProperty().removeListener(statusListener);
    mp.currentTimeProperty().removeListener(currentTimeListener);
  }

  private void seekAndUpdatePosition(Duration duration) {
    final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();

    if (mediaPlayer.getStatus() == Status.STOPPED) {
      mediaPlayer.pause();
    }

    mediaPlayer.seek(duration);
    
    if (mediaPlayer.getStatus() != Status.PLAYING) {
      updatePositionSlider(duration);
    }
  }
  
  private String formatDuration(Duration duration) {
    double millis = duration.toMillis();
    int seconds = (int) (millis / 1000) % 60;
    int minutes = (int) (millis / (1000 * 60));
    return String.format("%02d:%02d", minutes, seconds);
  }
  
  private void updateStatus(Status newStatus) {
    if (newStatus == Status.UNKNOWN || newStatus == null) {
      controlPanel.setDisable(true);
      positionSlider.setDisable(true);
      statusLabel.setText("Buffering");
    } else {
      controlPanel.setDisable(false);
      positionSlider.setDisable(false);
      
      statusLabel.setText(newStatus.toString());

      if (newStatus == Status.PLAYING) {
        playPauseIcon.setImage(pauseImg);
      } else {
        playPauseIcon.setImage(playImg);
      }
    }
  }

  private void updatePositionSlider(Duration currentTime) {
    if (positionSlider.isValueChanging())
      return;
    
    final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
    final Duration total = mediaPlayer.getTotalDuration();

    if (total == null || currentTime == null) {
      positionSlider.setValue(0);
    } else {
      positionSlider.setValue(currentTime.toMillis() / total.toMillis());
    }
  }

  private class MediaPlayerListener implements ChangeListener<MediaPlayer> {
    @Override
    public void changed(ObservableValue<? extends MediaPlayer> observable,
                        MediaPlayer oldValue, MediaPlayer newValue) {
      if (oldValue != null) {
        removeListenersAndBindings(oldValue);
      }
      addListenersAndBindings(newValue);
    }
  }
  
  private class OpenHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      FileChooser fc = new FileChooser();
      fc.setTitle("Pick a Sound File");
      File song = fc.showOpenDialog(viewNode.getScene().getWindow());
      if (song != null) {
        mediaModel.setURL(song.toURI().toString());
        mediaModel.getMediaPlayer().play();
      }
    }
  }
  
  private class EQHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
    }
  }
  
  private class StatusListener implements InvalidationListener {
    @Override
    public void invalidated(Observable observable) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          updateStatus(mediaModel.getMediaPlayer().getStatus());
        }
      });
    }
  }

private class CurrentTimeListener implements InvalidationListener {
  @Override
  public void invalidated(Observable observable) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
        final Duration currentTime = mediaPlayer.getCurrentTime();
        currentTimeLabel.setText(formatDuration(currentTime));
        updatePositionSlider(currentTime);
      }
    });
  }
}

  private class TotalDurationListener implements InvalidationListener {
    @Override
    public void invalidated(Observable observable) {
      final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
      final Duration totalDuration = mediaPlayer.getTotalDuration();
      totalDurationLabel.setText(formatDuration(totalDuration));
    }
  }

  private class PositionListener implements ChangeListener<Boolean> {
    @Override
    public void changed(ObservableValue<? extends Boolean> observable, 
                        Boolean oldValue, Boolean newValue) {
      if (oldValue && !newValue) {
        double pos = positionSlider.getValue();
        final MediaPlayer mediaPlayer = mediaModel.getMediaPlayer();
        final Duration seekTo = mediaPlayer.getTotalDuration().multiply(pos);
        seekAndUpdatePosition(seekTo);
      }
    }
  }
}
