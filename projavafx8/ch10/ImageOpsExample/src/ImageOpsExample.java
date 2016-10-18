import javafx.application.Application;
import javafx.geometry.BoundingBox;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageOpsExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Image Ops Example");
        stage.setScene(makeScene());
        stage.show();
    }

    private Scene makeScene() {
        final String imageUrl = "http://projavafx.com/images/earthrise.jpg";
        Image image = new Image(imageUrl);
        final PixelReader pixelReader = image.getPixelReader();
        ImageView imageView = new ImageView(image);
        TextArea textArea = new TextArea();
        textArea.setPrefSize(image.getWidth(), image.getHeight());
        textArea.setText("Examining: " + imageUrl + "\n" +
            "\tPixelFormat: " + pixelReader.getPixelFormat().getType() + "\n");
        imageView.setOnMouseClicked(event -> {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final Color color = pixelReader.getColor(x, y);
            final int argb = pixelReader.getArgb(x, y);
            String pixelDescription = String.format(
                "Pixel[%d, %d]:\n" +
                    "\t argb: %x\n" +
                    "\tcolor: %s (%5.4f, %5.4f, %5.4f, %5.4f)\n",
                x, y, argb, color, color.getOpacity(),
                color.getRed(), color.getGreen(), color.getBlue()
            );
            textArea.setText(textArea.getText() + pixelDescription);

        });
        HBox root = new HBox(10, imageView, textArea);
        Scene scene = new Scene(root);
        return scene;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
