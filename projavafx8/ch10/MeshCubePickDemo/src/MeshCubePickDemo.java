import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class MeshCubePickDemo extends Application {

    double anchorX, anchorY;

    private static final float EDGE_LENGTH = 200;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private final DoubleProperty angleX = new SimpleDoubleProperty(25);
    private final DoubleProperty angleY = new SimpleDoubleProperty(40);

    PerspectiveCamera camera = new PerspectiveCamera(false);

    private Slider zSlider;
    private final DoubleProperty zPos = new SimpleDoubleProperty(-200);

    private CheckBox meshCheckBox;

    private final BooleanProperty showWireframe = new SimpleBooleanProperty(false);

    private Label facePickedLabel = new Label();

    static TriangleMesh createMesh(float w, float h, float d) {

        if (w * h * d == 0) {
            return null;
        }

        float hw = w / 2f;
        float hh = h / 2f;
        float hd = d / 2f;

        float x0 = 0f;
        float x1 = 1f / 4f;
        float x2 = 2f / 4f;
        float x3 = 3f / 4f;
        float x4 = 1f;
        float y0 = 0f;
        float y1 = 1f / 3f;
        float y2 = 2f / 3f;
        float y3 = 1f;

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(
            hw, hh, hd,   //point A
            hw, hh, -hd,  //point B
            hw, -hh, hd,  //point C
            hw, -hh, -hd, //point D
            -hw, hh, hd,  //point E
            -hw, hh, -hd, //point F
            -hw, -hh, hd, //point G
            -hw, -hh, -hd //point H
        );
        mesh.getTexCoords().addAll(
            x1, y0,
            x2, y0,
            x0, y1,
            x1, y1,
            x2, y1,
            x3, y1,
            x4, y1,
            x0, y2,
            x1, y2,
            x2, y2,
            x3, y2,
            x4, y2,
            x1, y3,
            x2, y3
        );
        mesh.getFaces().addAll(
            0, 10, 2, 5, 1, 9,   //triangle A-C-B
            2, 5, 3, 4, 1, 9,    //triangle C-D-B
            4, 7, 5, 8, 6, 2,    //triangle E-F-G
            6, 2, 5, 8, 7, 3,    //triangle G-F-H
            0, 13, 1, 9, 4, 12,  //triangle A-B-E
            4, 12, 1, 9, 5, 8,   //triangle E-B-F
            2, 1, 6, 0, 3, 4,    //triangle C-G-D
            3, 4, 6, 0, 7, 3,    //triangle D-G-H
            0, 10, 4, 11, 2, 5,  //triangle A-E-C
            2, 5, 4, 11, 6, 6,   //triangle C-E-G
            1, 9, 3, 4, 5, 8,    //triangle B-D-F
            5, 8, 3, 4, 7, 3     //triangle F-D-H
        );
        mesh.getFaceSmoothingGroups().addAll(
            0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5
        );
        return mesh;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("MeshCubePickDemo");

        Image diffuseMap = new Image(MeshCubePickDemo.class
            .getResource("cbGn_pof-bm.png").toExternalForm());

        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(diffuseMap);

        MeshView cube =
            new MeshView(createMesh(EDGE_LENGTH, EDGE_LENGTH, EDGE_LENGTH));
        cube.setMaterial(earthMaterial);

        final Group parent = new Group(cube);
        parent.setTranslateX(320);
        parent.setTranslateY(240);
        parent.setTranslateZ(0);

        Rotate xRotate;
        Rotate yRotate;
        parent.getTransforms().setAll(
            xRotate = new Rotate(0, Rotate.X_AXIS),
            yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        final Group root = new Group(parent);

        final Scene scene = new Scene(root, 640, 480, true);
        scene.setFill(Color.WHITE);

        scene.setOnMousePressed((MouseEvent event) -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();

            PickResult pr = event.getPickResult();

            facePickedLabel.setText("Face picked: "
                + pr.getIntersectedFace());
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(200);
        pointLight.setTranslateZ(-2000);

        root.getChildren().add(pointLight);

        scene.setCamera(camera);
        stage.setScene(scene);

        // SubScene code
        Label zLabel = new Label("Z");
        zSlider = new Slider(-200, 3000, -200);
        meshCheckBox = new CheckBox("Wireframe");
        HBox subSceneGroup = new HBox(70, new HBox(10, zLabel, zSlider),
            meshCheckBox, facePickedLabel);
        subSceneGroup.setPadding(new Insets(10, 10, 10, 10));
        SubScene subScene = new SubScene(subSceneGroup, 640, 50);

        root.getChildren().add(subScene);

        // Setup binding
        zSlider.valueProperty().bindBidirectional(zPos);
        parent.translateZProperty().bind(zPos);

        meshCheckBox.selectedProperty().bindBidirectional(showWireframe);
        cube.drawModeProperty().bind(new When(showWireframe)
            .then(DrawMode.LINE).otherwise(DrawMode.FILL));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
