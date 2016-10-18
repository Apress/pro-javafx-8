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
import static javafx.application.Application.launch;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projavafx.reversi.model.Owner;
import projavafx.reversi.model.ReversiModel;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class BorderLayoutExample extends Application {

    TilePane scoreTiles;
    TilePane titleTiles;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTitle());
        borderPane.setCenter(createBackground());
        borderPane.setBottom(createScoreBoxes());
        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
       // scoreTiles.prefTileWidthProperty().bind(Bindings.selectDouble(scoreTiles.parentProperty(), "width").divide(2));
       // titleTiles.prefTileWidthProperty().bind(Bindings.selectDouble(titleTiles.parentProperty(), "width").divide(2));

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
        titleTiles = new TilePane();
        titleTiles.setSnapToPixel(false);
        TilePane.setAlignment(right, Pos.CENTER_LEFT);
        titleTiles.getChildren().addAll(left, right);
        titleTiles.setPrefTileHeight(40);
        titleTiles.prefTileWidthProperty().bind(Bindings.selectDouble(titleTiles.parentProperty(), "width").divide(2));
        return titleTiles;
    }

    private Node createBackground() {
        Region answer = new Region();
        RadialGradient rg = new RadialGradient(225, 0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.WHITE),
                new Stop(1.0, Color.GRAY)
        );
        answer.setBackground(new Background(new BackgroundFill(rg, null, null)));
        //       answer.setStyle("-fx-background-color: radial-gradient(radius 100%, white, gray)");
        return answer;
    }

    private Node createScoreBoxes() {
        scoreTiles = new TilePane(createScore(Owner.BLACK), createScore(Owner.WHITE));
        scoreTiles.setSnapToPixel(false);
        scoreTiles.setPrefColumns(2);
        scoreTiles.prefTileWidthProperty().bind(Bindings.selectDouble(scoreTiles.parentProperty(), "width").divide(2));
        
        return scoreTiles;
    }

    private Node createScore(Owner owner) {
        Region background;
        Ellipse piece = new Ellipse(32, 20);
        piece.setFill(owner.getColor());
        DropShadow pieceEffect = new DropShadow();
        pieceEffect.setColor(Color.DODGERBLUE);
        pieceEffect.setSpread(.2);
        piece.setEffect(pieceEffect);

        Text score = new Text();
        score.setFont(Font.font(null, FontWeight.BOLD, 100));
        score.setFill(owner.getColor());
        Text remaining = new Text();
        remaining.setFont(Font.font(null, FontWeight.BOLD, 12));
        remaining.setFill(owner.getColor());
        VBox remainingBox = new VBox(10, piece, remaining);
        remainingBox.setAlignment(Pos.CENTER);
        FlowPane flowPane = new FlowPane(20, 10, score, remainingBox);
        flowPane.setAlignment(Pos.CENTER);
        background = new Region();
        background.setStyle("-fx-background-color: " + owner.opposite().getColorStyle());
        ReversiModel model = ReversiModel.getInstance();
        StackPane stack = new StackPane(background, flowPane);
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.DODGERBLUE);
        innerShadow.setChoke(.5);
        background.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
                .then(innerShadow)
                .otherwise((InnerShadow) null));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DODGERBLUE);
        dropShadow.setSpread(.2);

        piece.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
                .then(dropShadow)
                .otherwise((DropShadow) null));
        score.textProperty().bind(model.getScore(owner).asString());
        remaining.textProperty().bind(model.getTurnsRemaining(owner).asString().concat(" turns remaining"));
        return stack;
    }
}
