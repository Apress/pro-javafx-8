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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;

/**
 * @author dean
 */
public class EqualizerView extends AbstractView {
  private static final double START_FREQ = 250.0;
  private static final int BAND_COUNT = 7;
  
  private SpectrumBar[] spectrumBars;
  private SpectrumListener spectrumListener;
  private Button backButton;

  public EqualizerView(MediaModel mediaModel) {
    super(mediaModel);
    
    mediaModel.mediaPlayerProperty().addListener(new MediaPlayerListener());
    createEQInterface();
    
    getViewNode().sceneProperty().addListener(new ChangeListener<Scene>() {
      @Override
      public void changed(ObservableValue<? extends Scene> observable, 
                          Scene oldValue, Scene newValue) {
        final MediaPlayer mp = EqualizerView.this.mediaModel.getMediaPlayer();
        if (newValue != null) {
          mp.setAudioSpectrumListener(spectrumListener);
        } else {
          mp.setAudioSpectrumListener(null);
        }
      }
    });
  }

  @Override
  public void setNextHandler(EventHandler<ActionEvent> nextHandler) {
    backButton.setOnAction(nextHandler);
  }
  
  @Override
  protected Node initView() {
    final GridPane gp = new GridPane();
    gp.setPadding(new Insets(10));
    gp.setHgap(20);

    RowConstraints middle = new RowConstraints();
    RowConstraints outside = new RowConstraints();
    outside.setVgrow(Priority.ALWAYS);
    
    gp.getRowConstraints().addAll(outside, middle, outside);

    createBackButton(gp);
    
    return gp;
  }

  private void createEQInterface() {
    final GridPane gp = (GridPane) getViewNode();
    final MediaPlayer mp = mediaModel.getMediaPlayer();

    createEQBands(gp, mp);
    createSpectrumBars(gp, mp);
    spectrumListener = new SpectrumListener(START_FREQ, mp, spectrumBars);

    GridPane.setValignment(backButton, VPos.BOTTOM);
    GridPane.setHalignment(backButton, HPos.CENTER);
    GridPane.setMargin(backButton, new Insets(20, 0, 0, 0));
    gp.add(backButton, 0, 3);
  }

  private void createBackButton(GridPane gp) {
    backButton = new Button("Back");
    backButton.setId("backButton");
    backButton.setPrefWidth(50);
    backButton.setPrefHeight(32);
  }

  private void createEQBands(GridPane gp, MediaPlayer mp) {
    final ObservableList<EqualizerBand> bands =
            mp.getAudioEqualizer().getBands();

    bands.clear();
    
    double min = EqualizerBand.MIN_GAIN;
    double max = EqualizerBand.MAX_GAIN;
    double mid = (max - min) / 2;
    double freq = START_FREQ;

    // Create the equalizer bands with the gains preset to
    // a nice cosine wave pattern.
    for (int j = 0; j < BAND_COUNT; j++) {
      // Use j and BAND_COUNT to calculate a value between 0 and 2*pi
      double theta = (double)j / (double)(BAND_COUNT-1) * (2*Math.PI);
      
      // The cos function calculates a scale value between 0 and 0.4
      double scale = 0.4 * (1 + Math.cos(theta));
      
      // Set the gain to be a value between the midpoint and 0.9*max.
      double gain = min + mid + (mid * scale);
      
      bands.add(new EqualizerBand(freq, freq/2, gain));
      freq *= 2;
    }
    
    for (int i = 0; i < bands.size(); ++i) {
      EqualizerBand eb = bands.get(i);
      Slider s = createEQSlider(eb, min, max);

      final Label l = new Label(formatFrequency(eb.getCenterFrequency()));
      l.getStyleClass().addAll("mediaText", "eqLabel");

      GridPane.setHalignment(l, HPos.CENTER);
      GridPane.setHalignment(s, HPos.CENTER);
      GridPane.setHgrow(s, Priority.ALWAYS);

      gp.add(l, i, 1);
      gp.add(s, i, 2);
    }
  }

  private Slider createEQSlider(EqualizerBand eb, double min, double max) {
    final Slider s = new Slider(min, max, eb.getGain());
    s.getStyleClass().add("eqSlider");
    s.setOrientation(Orientation.VERTICAL);
    s.valueProperty().bindBidirectional(eb.gainProperty());
    s.setPrefWidth(44);
    return s;
  }

private void createSpectrumBars(GridPane gp, MediaPlayer mp) {
  spectrumBars = new SpectrumBar[BAND_COUNT];

  for (int i = 0; i < spectrumBars.length; i++) {
    spectrumBars[i] = new SpectrumBar(100, 20);
    spectrumBars[i].setMaxWidth(44);
    GridPane.setHalignment(spectrumBars[i], HPos.CENTER);
    gp.add(spectrumBars[i], i, 0);
  }
}

  private String formatFrequency(double centerFrequency) {
    if (centerFrequency < 1000) {
      return String.format("%.0f Hz", centerFrequency);
    } else {
      return String.format("%.1f kHz", centerFrequency / 1000);
    }
  }

  private class MediaPlayerListener implements ChangeListener<MediaPlayer> {
    @Override
    public void changed(ObservableValue<? extends MediaPlayer> observable,
                        MediaPlayer oldValue, MediaPlayer newValue) {
      if (oldValue != null) {
        oldValue.setAudioSpectrumListener(null);
        clearGridPane();
      }

      createEQInterface();
    }

    private void clearGridPane() {
      for (Node node : ((GridPane)getViewNode()).getChildren()) {
        GridPane.clearConstraints(node);
      }
      ((GridPane)getViewNode()).getChildren().clear();
    }
  }
}
