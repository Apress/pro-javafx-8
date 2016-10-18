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
 * AudioPlayer4.java - An example from the Pro JavaFX Platform book.
 */
package projavafx.audioplayer4;

import com.sun.javafx.runtime.VersionInfo;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author dean
 */
public class AudioPlayer4 extends Application {
  private final SongModel songModel;

  private Parent page1;
  private Parent page2;
  private StackPane rootNode;

  private MetadataView metaDataView;
  private EqualizerView equalizerView;
  private PlayerControlsView playerControlsView;
  
  public static void main(String[] args) {
    launch(args);
  }
  
  public AudioPlayer4() {
    songModel = new SongModel();
  }

  @Override
  public void start(Stage primaryStage) {
    System.out.println("JavaFX version: "+VersionInfo.getRuntimeVersion());
    
    songModel.setURL("http://traffic.libsyn.com/dickwall/JavaPosse373.mp3");
    
    page1 = createPageOne();
    page2 = createPageTwo();
    
    rootNode = new StackPane();
    rootNode.getChildren().add(page1);
    
    final Scene scene = new Scene(rootNode, 800, 400);
    initSceneDragAndDrop(scene);
    
    final URL stylesheet = getClass().getResource("media.css");
    scene.getStylesheets().add(stylesheet.toString());

    primaryStage.setScene(scene);
    primaryStage.setTitle("Audio Player 4");
    primaryStage.show();
    
    songModel.getMediaPlayer().play();
  }

  private Parent createPageOne() {
    metaDataView = new MetadataView(songModel);
    playerControlsView = new PlayerControlsView(songModel);
    playerControlsView.setNextHandler(act -> {
        rootNode.getChildren().setAll(page2);
    });
    
    final BorderPane bp = new BorderPane();
    bp.setCenter(metaDataView.getViewNode());
    bp.setBottom(playerControlsView.getViewNode());
    
    return bp;
  }
  
  private Parent createPageTwo() {
    equalizerView = new EqualizerView(songModel);
    
    equalizerView.setNextHandler(act -> {
        System.out.println("Got back button click");
        rootNode.getChildren().setAll(page1);
    });
    
    return (Parent)equalizerView.getViewNode();
  }

  private void initSceneDragAndDrop(Scene scene) {
    scene.setOnDragOver(event -> {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() || db.hasUrl()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    });

    scene.setOnDragDropped(event -> {
        Dragboard db = event.getDragboard();
        String url = null;
        
        if (db.hasFiles()) {
            url = db.getFiles().get(0).toURI().toString();
        } else if (db.hasUrl()) {
            url = db.getUrl();
        }
        
        if (url != null) {
            songModel.setURL(url);
            songModel.getMediaPlayer().play();
        }
        
        event.setDropCompleted(url != null);
        event.consume();
    });
  }
}
