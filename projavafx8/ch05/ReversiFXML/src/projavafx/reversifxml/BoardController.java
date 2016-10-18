package projavafx.reversifxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import projavafx.reversi.model.Owner;
import projavafx.reversi.model.ReversiModel;

/**
 *
 * @author johan
 */
public class BoardController  {
    
    private final ReversiModel model = ReversiModel.getInstance();
    @FXML private TilePane titlePane;
    @FXML private TilePane scorePane;
    @FXML private StackPane centerPane;
    @FXML private Text scoreBlack;
    @FXML private Text scoreWhite;
    @FXML private Text remainingBlack;
    @FXML private Text remainingWhite;
    @FXML private Region blackRegion;
    @FXML private Region whiteRegion;
    @FXML private Ellipse blackEllipse;
    @FXML private Ellipse whiteEllipse;
    
   
    public void initialize() {
        titlePane.prefTileWidthProperty().bind(Bindings.selectDouble(titlePane.parentProperty(), "width").divide(2));
        scorePane.prefTileWidthProperty().bind(Bindings.selectDouble(scorePane.parentProperty(), "width").divide(2));
        centerPane.getChildren().add(tiles());
        scoreBlack.textProperty().bind(model.getScore(Owner.BLACK).asString());
        scoreWhite.textProperty().bind(model.getScore(Owner.WHITE).asString());
        remainingBlack.textProperty().bind(model.getTurnsRemaining(Owner.BLACK).asString().concat(" turns remaining"));
        remainingWhite.textProperty().bind(model.getTurnsRemaining(Owner.WHITE).asString().concat(" turns remaining"));
        
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.DODGERBLUE);
        innerShadow.setChoke(0.5);
        whiteRegion.effectProperty().bind(Bindings.when(model.turn.isEqualTo(Owner.WHITE))
                .then(innerShadow)
                .otherwise((InnerShadow) null));
        blackRegion.effectProperty().bind(Bindings.when(model.turn.isEqualTo(Owner.BLACK))
                .then(innerShadow)
                .otherwise((InnerShadow) null));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DODGERBLUE);
        dropShadow.setSpread(0.2);
        blackEllipse.setEffect(dropShadow);
        blackEllipse.effectProperty().bind(Bindings.when(model.turn.isEqualTo(Owner.BLACK))
                .then(dropShadow)
                .otherwise((DropShadow) null));
        whiteEllipse.effectProperty().bind(Bindings.when(model.turn.isEqualTo(Owner.WHITE))
                .then(dropShadow)
                .otherwise((DropShadow) null));
        
        
        
    }    
    
    @FXML
    public void restart() {
        model.restart();
    }
    
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
    
}
