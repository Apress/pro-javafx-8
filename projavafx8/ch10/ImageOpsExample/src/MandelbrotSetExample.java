import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MandelbrotSetExample extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mandelbrot iSet Example");
        primaryStage.setScene(makeScene());
        primaryStage.show();
    }

    private Scene makeScene() {
        WritableImage mandelbrotSet = new WritableImage(840, 480);
        populateMandelbrotSet(mandelbrotSet);
        Canvas canvas = new Canvas(840, 480);
        final GraphicsContext graphicsContext =
            canvas.getGraphicsContext2D();
        graphicsContext.drawImage(mandelbrotSet, 0, 0);
        Group root = new Group(canvas);
        Scene scene = new Scene(root);
        return scene;
    }

    private void populateMandelbrotSet(WritableImage mandelbrotSet) {
        final PixelWriter pixelWriter = mandelbrotSet.getPixelWriter();
        for (int i = 0; i < 840; i++) {
            for (int j = 0; j < 480; j++) {
                double x = (i - 600) / 240.0d;
                double y = (240 - j) / 240.0d;
                Color color = calculateColor(x, y);
                pixelWriter.setColor(i, j, color);
            }
        }
    }

    private Color calculateColor(double x0, double y0) {
        double x = 0.0;
        double y = 0.0;
        int iteration = 0;
        int max_iteration = 1000;
        while (x * x + y * y < 2 * 2 && iteration < max_iteration) {
            double xtemp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = xtemp;
            iteration = iteration + 1;
        }
        double b = (iteration % 10) / 10.0d;
        double g = ((iteration / 10) % 10) / 10.0d;
        double r = ((iteration / 100) % 10) / 10.0d;
        return Color.color(r, g, b);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
