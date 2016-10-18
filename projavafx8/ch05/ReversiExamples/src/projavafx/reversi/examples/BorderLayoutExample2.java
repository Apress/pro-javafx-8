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
package projavafx.reversi.examples;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SceneBuilder;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.InnerShadowBuilder;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import projavafx.reversi.model.Owner;
import projavafx.reversi.model.ReversiModel;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class BorderLayoutExample2 extends Application {
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setScene(SceneBuilder.create()
      .width(600)
      .height(400)
      .root(BorderPaneBuilder.create()
         .top(createTitle())
         .center(createBackground())
         .bottom(createScoreBoxes())
         .build())
      .build());
    primaryStage.show();
  }

  private Node createTitle() {
    StackPane left = new StackPane();
    left.setStyle("-fx-background-color: black");
    Text text = new Text("JavaFX");
    text.setFont(Font.font(null, FontWeight.BOLD, 18));
    text.setFill(Color.WHITE);
    StackPane.setAlignment(text, Pos.CENTER_RIGHT);
    left.getChildren().add(text);
    Text right = new Text("Reversi");
    right.setFont(Font.font(null, FontWeight.BOLD, 18));
    TilePane tiles = new TilePane();
    tiles.setSnapToPixel(false);
    TilePane.setAlignment(right, Pos.CENTER_LEFT);
    tiles.getChildren().addAll(left, right);
    tiles.setPrefTileHeight(40);
    tiles.prefTileWidthProperty().bind(Bindings.selectDouble(tiles.parentProperty(), "width").divide(2));
    return tiles;
  }

  private Node createBackground() {
    return RegionBuilder.create()
      .style("-fx-background-color: radial-gradient(radius 100%, white, gray)")
      .build();
  }
  
  private Node createScoreBoxes() {
    TilePane tiles = TilePaneBuilder.create()
      .snapToPixel(false)
      .prefColumns(2)
      .children(
        createScore(Owner.BLACK),
        createScore(Owner.WHITE))
      .build();
    tiles.prefTileWidthProperty().bind(Bindings.selectDouble(tiles.parentProperty(), "width").divide(2));
    return tiles;
  }
  
  private Node createScore(Owner owner) {
    Region background;
    Ellipse piece;
    Text score;
    Text remaining;
    ReversiModel model = ReversiModel.getInstance();
    StackPane stack = StackPaneBuilder.create()
      .children(
        background = RegionBuilder.create()
          .style("-fx-background-color: " + owner.opposite().getColorStyle())
          .build(),
        FlowPaneBuilder.create()
          .hgap(20)
          .vgap(10)
          .alignment(Pos.CENTER)
          .children(
            score = TextBuilder.create()
              .font(Font.font(null, FontWeight.BOLD, 100))
              .fill(owner.getColor())
              .build(),
            VBoxBuilder.create()
              .alignment(Pos.CENTER)
              .spacing(10)
              .children(
                piece = EllipseBuilder.create()
                  .effect(DropShadowBuilder.create().color(Color.DODGERBLUE).spread(0.2).build())
                  .radiusX(32)
                  .radiusY(20)
                  .fill(owner.getColor())
                  .build(),
                remaining = TextBuilder.create()
                  .font(Font.font(null, FontWeight.BOLD, 12))
                  .fill(owner.getColor())
                  .build()
              )
              .build()
          )
          .build()
        )
      .build();
    InnerShadow innerShadow = InnerShadowBuilder.create()
      .color(Color.DODGERBLUE)
      .choke(0.5)
      .build();
    background.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
      .then(innerShadow)
      .otherwise((InnerShadow) null));
    DropShadow dropShadow = DropShadowBuilder.create()
      .color(Color.DODGERBLUE)
      .spread(0.2)
      .build();
    piece.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
      .then(dropShadow)
      .otherwise((DropShadow) null));
    score.textProperty().bind(model.getScore(owner).asString());
    remaining.textProperty().bind(model.getTurnsRemaining(owner).asString().concat(" turns remaining"));
    return stack;
  }
}
