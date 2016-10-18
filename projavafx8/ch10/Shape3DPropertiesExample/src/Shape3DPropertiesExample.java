import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Shape3DPropertiesExample extends Application {
    private Model model;
    private View view;

    public Shape3DPropertiesExample() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) throws Exception {
        view = new View(model);
        hookupEvents();
        stage.setTitle("Shape3D Properties Example");
        stage.setScene(view.scene);
        stage.show();
    }

    private void hookupEvents() {
        view.colorPicker.setOnAction(event -> {
            ColorPicker colorPicker = (ColorPicker) event.getSource();
            model.setMaterial(new PhongMaterial(colorPicker.getValue()));
        });

        view.drawModeComboBox.setOnAction(event -> {
            ComboBox<DrawMode> drawModeComboBox = (ComboBox<DrawMode>) event.getSource();
            model.setDrawMode(drawModeComboBox.getValue());
        });

        view.cullFaceComboBox.setOnAction(event -> {
            ComboBox<CullFace> cullFaceComboBox = (ComboBox<CullFace>) event.getSource();
            model.setCullFace(cullFaceComboBox.getValue());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Model {
        private ObjectProperty<Material> material = new SimpleObjectProperty<>(
                this, "material", new PhongMaterial(Color.BLUE));
        private ObjectProperty<DrawMode> drawMode = new SimpleObjectProperty<>(
                this, "drawMode", DrawMode.FILL);
        private ObjectProperty<CullFace> cullFace = new SimpleObjectProperty<>(
                this, "cullFace", CullFace.BACK);

        public final Material getMaterial() {
            return material.get();
        }

        public final void setMaterial(Material material) {
            this.material.set(material);
        }

        public final ObjectProperty<Material> materialProperty() {
            return material;
        }

        public final DrawMode getDrawMode() {
            return drawMode.getValue();
        }

        public final void setDrawMode(DrawMode drawMode) {
            this.drawMode.set(drawMode);
        }

        public final ObjectProperty<DrawMode> drawModeProperty() {
            return drawMode;
        }

        public final CullFace getCullFace() {
            return cullFace.get();
        }

        public final void setCullFace(CullFace cullFace) {
            this.cullFace.set(cullFace);
        }

        public final ObjectProperty<CullFace> cullFaceProperty() {
            return cullFace;
        }
    }

    private static class View {
        public Scene scene;
        public Sphere sphere;
        public ColorPicker colorPicker;
        public ComboBox<DrawMode> drawModeComboBox;
        public ComboBox<CullFace> cullFaceComboBox;

        public View(Model model) {
            sphere = new Sphere(100);
            sphere.materialProperty().bind(model.materialProperty());
            sphere.drawModeProperty().bind(model.drawModeProperty());
            sphere.cullFaceProperty().bind(model.cullFaceProperty());

            colorPicker = new ColorPicker(Color.BLUE);

            drawModeComboBox = new ComboBox<>();
            drawModeComboBox.setItems(FXCollections.observableArrayList(
                    DrawMode.FILL, DrawMode.LINE));
            drawModeComboBox.setValue(DrawMode.FILL);

            cullFaceComboBox = new ComboBox<>();
            cullFaceComboBox.setItems(FXCollections.observableArrayList(
                    CullFace.BACK, CullFace.FRONT, CullFace.NONE));
            cullFaceComboBox.setValue(CullFace.BACK);

            HBox hbox = new HBox(10, new Label("Color:"), colorPicker,
                    new Label("DrawMode:"), drawModeComboBox,
                    new Label("CullFace:"), cullFaceComboBox);
            hbox.setPadding(new Insets(10, 10, 10, 10));
            hbox.setAlignment(Pos.CENTER);
            BorderPane root = new BorderPane(sphere, null, null, hbox, null);
            scene = new Scene(root, 640, 480);
        }
    }
}
