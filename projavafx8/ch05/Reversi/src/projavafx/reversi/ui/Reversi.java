package projavafx.reversi.ui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
public class Reversi extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TilePane title = createTitle();
        TilePane scoreBoxes = createScoreBoxes();

        BorderPane game = new BorderPane();
        game.setTop(title);
        game.setCenter(new StackPane(createBackground(), tiles()));
        game.setBottom(scoreBoxes);
        Node restart = restart();
        AnchorPane root = new AnchorPane(game, restart);
Scene scene = new Scene(root, 600,400);
        primaryStage.setScene(scene);
        AnchorPane.setTopAnchor(game, 0d);
        AnchorPane.setBottomAnchor(game, 0d);
        AnchorPane.setLeftAnchor(game, 0d);
        AnchorPane.setRightAnchor(game, 0d);
        AnchorPane.setRightAnchor(restart, 10d);
        AnchorPane.setTopAnchor(restart, 10d);

        title.prefTileWidthProperty().bind(Bindings.selectDouble(title.parentProperty(), "width").divide(2));
        scoreBoxes.prefTileWidthProperty().bind(Bindings.selectDouble(scoreBoxes.parentProperty(), "width").divide(2));
        primaryStage.show();
    }

    private Node restart() {
        Button button = new Button("Restart");
        button.setOnAction(e -> model.restart());
        return button;
    }

    private TilePane createTitle() {
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
        return tiles;
    }

    private Node createBackground() {
        Region region = new Region();
        region.setStyle("-fx-background-color: radial-gradient(radius 100%, white, gray)");
        return region;
    }

    private final ReversiModel model = ReversiModel.getInstance();

    private Node tiles() {
        GridPane board = new GridPane();
        for (int i = 0; i < ReversiModel.BOARD_SIZE; i++) {
            for (int j = 0; j < ReversiModel.BOARD_SIZE; j++) {
                ReversiSquare square = new ReversiSquare(i, j);
                ReversiPiece piece = new ReversiPiece();
                piece.ownerProperty().bind(model.board[i][j]);
                board.add(new StackPane(square, piece), i, j);
            }
        }
        return board;
    }

    private TilePane createScoreBoxes() {
        TilePane tiles = new TilePane(createScore(Owner.BLACK), createScore(Owner.WHITE));
        tiles.setSnapToPixel(false);
        tiles.setPrefColumns(2);
        return tiles;
    }

    private Node createScore(Owner owner) {
        Region background = new Region();
        background.setStyle("-fx-background-color: " + owner.opposite().getColorStyle());
        Ellipse piece = new Ellipse(32, 20);
        piece.setFill(owner.getColor());
        Text score = new Text();
        score.setFont(Font.font(null, FontWeight.BOLD, 100));
        score.setFill(owner.getColor());
        Text remaining = new Text();
        remaining.setFont(Font.font(null, FontWeight.BOLD, 12));
        remaining.setFill(owner.getColor());

        VBox vbox = new VBox(10.0, piece, remaining);
        vbox.setAlignment(Pos.CENTER);
        FlowPane flowPane = new FlowPane(score, vbox);
        flowPane.setHgap(20);
        flowPane.setVgap(10);
        flowPane.setAlignment(Pos.CENTER);
        StackPane stack = new StackPane(background, flowPane);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.DODGERBLUE);
        innerShadow.setChoke(0.5);
        background.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
                .then(innerShadow)
                .otherwise((InnerShadow) null));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DODGERBLUE);
        dropShadow.setSpread(0.2);
        piece.setEffect(dropShadow);
        piece.effectProperty().bind(Bindings.when(model.turn.isEqualTo(owner))
                .then(dropShadow)
                .otherwise((DropShadow) null));
        score.textProperty().bind(model.getScore(owner).asString());
        remaining.textProperty().bind(model.getTurnsRemaining(owner).asString().concat(" turns remaining"));
        return stack;
    }
}
