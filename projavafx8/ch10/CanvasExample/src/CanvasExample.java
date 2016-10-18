import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class CanvasExample extends Application {
    private Point2D p0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Canvas Example");
        stage.setScene(makeScene());
        stage.show();
    }

    private Scene makeScene() {
        Canvas canvas = new Canvas(640, 480);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setFont(Font.font(14));
        graphicsContext.strokeText("Click and drag mouse to draw ovals", 20, 20);

        canvas.setOnMousePressed(event -> {
            graphicsContext.clearRect(0, 0, 640, 480);
            p0 = new Point2D(event.getX(), event.getY());
        });

        canvas.setOnMouseDragged(event -> {
            Point2D p1 = new Point2D(event.getX(), event.getY());
            graphicsContext.clearRect(0, 0, 640, 480);
            double x = min(p0.getX(), p1.getX());
            double y = min(p0.getY(), p1.getY());
            double width = abs(p1.getX() - p0.getX());
            double height = abs(p1.getY() - p0.getY());
            graphicsContext.fillOval(x, y, width, height);
            graphicsContext.strokeText("Oval(" + x + ", " + y + ", " +
                width + ", " + height + ")", x, y - 10);
        });
        Group group = new Group(canvas);
        return new Scene(group, 640, 480);
    }
}