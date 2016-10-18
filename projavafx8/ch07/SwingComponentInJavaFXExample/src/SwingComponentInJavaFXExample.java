import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class SwingComponentInJavaFXExample extends Application {
    private Model model;
    private View view;

    public static void main(String[] args) {
        launch(args);
    }

    public SwingComponentInJavaFXExample() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) throws Exception {
        view = new View(model);
        hookupEvents();
        stage.setTitle("Swing in JavaFX Example");
        stage.setScene(view.scene);
        stage.show();
    }

    private void hookupEvents() {
        view.changeFillButton.setOnAction(actionEvent -> {
            EventQueue.invokeLater(() -> {
                final java.awt.Color fillColor = model.getFillColor();
                if (fillColor.equals(java.awt.Color.LIGHT_GRAY)) {
                    model.setFillColor(java.awt.Color.GRAY);
                } else {
                    model.setFillColor(java.awt.Color.LIGHT_GRAY);
                }
                view.canvas.repaint();
            });
        });

        view.changeStrokeButton.setOnAction(actionEvent -> {
            EventQueue.invokeLater(() -> {
                final java.awt.Color strokeColor = model.getStrokeColor();
                if (strokeColor.equals(java.awt.Color.GRAY)) {
                    model.setStrokeColor(java.awt.Color.BLACK);
                } else {
                    model.setStrokeColor(java.awt.Color.GRAY);
                }
                view.canvas.repaint();
            });
        });
    }

    private static class Model {
        private java.awt.Color fillColor;
        private java.awt.Color strokeColor;
        final private StringProperty mouseLocation = new SimpleStringProperty(this, "mouseLocation", "");

        private Model() {
            fillColor = java.awt.Color.LIGHT_GRAY;
            strokeColor = java.awt.Color.GRAY;
        }

        public java.awt.Color getFillColor() {
            return fillColor;
        }

        public void setFillColor(java.awt.Color fillColor) {
            this.fillColor = fillColor;
        }

        public java.awt.Color getStrokeColor() {
            return strokeColor;
        }

        public void setStrokeColor(java.awt.Color strokeColor) {
            this.strokeColor = strokeColor;
        }

        public final void setMouseLocation(String mouseLocation) {
            this.mouseLocation.set(mouseLocation);
        }

        public final StringProperty mouseLocationProperty() {
            return mouseLocation;
        }
    }

    private static class View {
        public JComponent canvas;
        public Button changeFillButton;
        public Button changeStrokeButton;
        public Label mouseLocation;
        public HBox buttonHBox;
        public Scene scene;

        private View(Model model) {
            SwingNode swingNode = new MySwingNode();

            EventQueue.invokeLater(() -> {
                canvas = new MyRectangle(model);
                canvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        Platform.runLater(() -> {
                            model.setMouseLocation("");
                        });
                    }
                });
                canvas.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Platform.runLater(() -> {
                            model.setMouseLocation("(" + e.getX() + ", " + e.getY() + ")");
                        });
                    }
                });
                swingNode.setContent(canvas);
            });

            changeFillButton = new Button("Change Fill");
            changeStrokeButton = new Button("Change Stroke");
            mouseLocation = new Label("(100, 100)");
            mouseLocation.setPrefSize(60, 15);
            mouseLocation.textProperty().bind(model.mouseLocationProperty());

            buttonHBox = new HBox(10, changeFillButton, changeStrokeButton, mouseLocation);
            buttonHBox.setPadding(new Insets(10, 0, 10, 0));
            buttonHBox.setAlignment(Pos.CENTER);

            VBox root = new VBox(10, swingNode, buttonHBox);
            root.setPadding(new Insets(10, 10, 10, 10));

            scene = new Scene(root);
        }
    }

    private static class MySwingNode extends SwingNode {
        @Override
        public double minWidth(double height) {
            return 250;
        }

        @Override
        public double minHeight(double width) {
            return 200;
        }
    }

    private static class MyRectangle extends JComponent {
        private final Model model;

        public MyRectangle(Model model) {
            this.model = model;
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(model.getStrokeColor());
            g.fillRect(0, 0, 200, 200);
            g.setColor(model.getFillColor());
            g.fillRect(10, 10, 180, 180);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(200, 200);
        }
    }
}
