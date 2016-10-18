import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PerspectiveCameraExample extends Application {
    private View view;

    @Override
    public void start(Stage stage) throws Exception {
        view = new View();
        stage.setTitle("PerspectiveCamera Example");
        stage.setScene(view.scene);
        stage.show();
        view.animate();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class View {
        public Scene scene;

        public Box box;
        public PerspectiveCamera camera;

        private final Rotate rotateX;
        private final Rotate rotateY;
        private final Rotate rotateZ;
        private final Translate translateZ;

        private View() {
            box = new Box(10, 10, 10);
            camera = new PerspectiveCamera(true);

            rotateX = new Rotate(-20, Rotate.X_AXIS);
            rotateY = new Rotate(-20, Rotate.Y_AXIS);
            rotateZ = new Rotate(-20, Rotate.Z_AXIS);
            translateZ = new Translate(0, 0, -100);

            camera.getTransforms().addAll(rotateX, rotateY, rotateZ, translateZ);

            Group group = new Group(box, camera);
            scene = new Scene(group, 640, 480);
            scene.setCamera(camera);
        }

        public void animate() {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(translateZ.zProperty(), -20),
                            new KeyValue(rotateX.angleProperty(), 90),
                            new KeyValue(rotateY.angleProperty(), 90),
                            new KeyValue(rotateZ.angleProperty(), 90)),
                    new KeyFrame(Duration.seconds(5),
                            new KeyValue(translateZ.zProperty(), -80),
                            new KeyValue(rotateX.angleProperty(), -90),
                            new KeyValue(rotateY.angleProperty(), -90),
                            new KeyValue(rotateZ.angleProperty(), -90))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }
}
