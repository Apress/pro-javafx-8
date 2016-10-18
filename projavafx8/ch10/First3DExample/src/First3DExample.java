import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class First3DExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("First 3D Example");
        stage.setScene(makeScene());
        stage.show();
    }

    private Scene makeScene() {
        Sphere sphere = new Sphere(100);
        Group root = new Group(sphere);
        root.setTranslateX(320);
        root.setTranslateY(240);
        Scene scene = new Scene(root, 640, 480);

        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
