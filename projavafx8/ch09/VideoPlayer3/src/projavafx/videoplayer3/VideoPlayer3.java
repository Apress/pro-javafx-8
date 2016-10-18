package projavafx.videoplayer3;

/*
 * Copyright (c) 2011, Pro JavaFX Authors All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of JFXtras nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
 * VideoPlayer3.java - An example from the Pro JavaFX Platform book.
 */
import java.io.File;
import java.net.URL;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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
 * @author dean
 */
public class VideoPlayer3 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final Label message = new Label("I \u2764 Robots");
        message.setVisible(false);

        String workingDir = System.getProperty("user.dir");
        final File f = new File(workingDir, "../media/omgrobots.flv");

        final Media m = new Media(f.toURI().toString());
        m.getMarkers().put("Split", Duration.millis(3000));
        m.getMarkers().put("Join", Duration.millis(9000));

        final MediaPlayer mp = new MediaPlayer(m);

        final MediaView mv1 = new MediaView(mp);
        mv1.setViewport(new Rectangle2D(0, 0, 960 / 2, 540));
        StackPane.setAlignment(mv1, Pos.CENTER_LEFT);

        final MediaView mv2 = new MediaView(mp);
        mv2.setViewport(new Rectangle2D(960 / 2, 0, 960 / 2, 540));
        StackPane.setAlignment(mv2, Pos.CENTER_RIGHT);

        StackPane root = new StackPane();
        root.getChildren().addAll(message, mv1, mv2);
        root.setOnMouseClicked((event) -> {
            mp.seek(Duration.ZERO);
            message.setVisible(false);
        });

        final Scene scene = new Scene(root, 960, 540);
        final URL stylesheet = getClass().getResource("media.css");
        scene.getStylesheets().add(stylesheet.toString());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Player 3");
        primaryStage.show();

        mp.setOnMarker((event) -> {
            Platform.runLater(() -> {
                if (event.getMarker().getKey().equals("Split")) {
                    message.setVisible(true);
                    buildSplitTransition(mv1, mv2).play();
                } else {
                    buildJoinTransition(mv1, mv2).play();
                }
            });
        });
        mp.play();
    }

    private ParallelTransition buildJoinTransition(Node one, Node two) {
        TranslateTransition translate1 = new TranslateTransition(Duration.millis(1000), one);
        translate1.setByX(200);
        TranslateTransition translate2 = new TranslateTransition(Duration.millis(1000), two);
        translate2.setByX(-200);
        ParallelTransition answer = new ParallelTransition(translate1, translate2);
        return answer;
    }

    private ParallelTransition buildSplitTransition(Node one, Node two) {
        TranslateTransition translate1 = new TranslateTransition(Duration.millis(1000), one);
        translate1.setByX(-200);
        TranslateTransition translate2 = new TranslateTransition(Duration.millis(1000), two);
        translate2.setByX(200);
        ParallelTransition answer = new ParallelTransition(translate1, translate2);
        return answer;

    }
}
