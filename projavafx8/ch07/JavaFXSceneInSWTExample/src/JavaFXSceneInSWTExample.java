import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class JavaFXSceneInSWTExample {
    public static void main(final String[] args) {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        controller.mainLoop();
    }

    private static class Model {
        private ObjectProperty<Color> fill = new SimpleObjectProperty<>(Color.LIGHTGRAY);
        private ObjectProperty<Color> stroke = new SimpleObjectProperty<>(Color.DARKGRAY);

        public Color getFill() {
            return fill.get();
        }

        public void setFill(Color value) {
            this.fill.set(value);
        }

        public Color getStroke() {
            return stroke.get();
        }

        public void setStroke(Color value) {
            this.stroke.set(value);
        }

        public ObjectProperty<Color> fillProperty() {
            return fill;
        }

        public ObjectProperty<Color> strokeProperty() {
            return stroke;
        }
    }

    private static class View {
        public Display display;
        public Shell frame;
        public FXCanvas canvas;
        public Button changeFillButton;
        public Button changeStrokeButton;
        public Label mouseLocation;
        public boolean mouseInCanvas;
        public Rectangle rectangle;

        private View(final Model model) {
            this.display = new Display();
            frame = new Shell(display);
            frame.setText("JavaFX in SWT Example");
            RowLayout frameLayout = new RowLayout(SWT.VERTICAL);
            frameLayout.spacing = 10;
            frameLayout.center = true;
            frame.setLayout(frameLayout);

            Composite canvasPanel = new Composite(frame, SWT.NONE);
            RowLayout canvasPanelLayout = new RowLayout(SWT.VERTICAL);
            canvasPanelLayout.spacing = 10;
            canvasPanel.setLayout(canvasPanelLayout);
            canvas = new FXCanvas(canvasPanel, SWT.NONE);
            rectangle = new Rectangle(200, 200);
            rectangle.setStrokeWidth(10);
            VBox vBox = new VBox(rectangle);
            Scene scene = new Scene(vBox, 210, 210);
            canvas.setScene(scene);
            rectangle.fillProperty().bind(model.fillProperty());
            rectangle.strokeProperty().bind(model.strokeProperty());

            Composite buttonPanel = new Composite(frame, SWT.NONE);
            RowLayout buttonPanelLayout = new RowLayout(SWT.HORIZONTAL);
            buttonPanelLayout.spacing = 10;
            buttonPanelLayout.center = true;
            buttonPanel.setLayout(buttonPanelLayout);

            changeFillButton = new Button(buttonPanel, SWT.NONE);
            changeFillButton.setText("Change Fill");
            changeStrokeButton = new Button(buttonPanel, SWT.NONE);
            changeStrokeButton.setText("Change Stroke");
            mouseLocation = new Label(buttonPanel, SWT.NONE);
            mouseLocation.setLayoutData(new RowData(50, 15));

            frame.pack();
        }
    }

    private static class Controller {
        private View view;

        private Controller(final Model model, final View view) {
            this.view = view;
            view.changeFillButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    final Paint fillPaint = model.getFill();
                    if (fillPaint.equals(Color.LIGHTGRAY)) {
                        model.setFill(Color.GRAY);
                    } else {
                        model.setFill(Color.LIGHTGRAY);
                    }
                }
            });
            view.changeStrokeButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    final Paint strokePaint = model.getStroke();
                    if (strokePaint.equals(Color.DARKGRAY)) {
                        model.setStroke(Color.BLACK);
                    } else {
                        model.setStroke(Color.DARKGRAY);
                    }
                }
            });
            view.rectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    view.mouseInCanvas = true;
                }
            });
            view.rectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    view.mouseInCanvas = false;
                    view.mouseLocation.setText("");
                }
            });
            view.rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    if (view.mouseInCanvas) {
                        view.mouseLocation.setText("(" + (int) mouseEvent.getSceneX() + ", " + (int) mouseEvent.getSceneY() + ")");
                    }
                }
            });
        }

        public void mainLoop() {
            view.frame.open();
            while (!view.frame.isDisposed()) {
                if (!view.display.readAndDispatch()) view.display.sleep();
            }
            view.display.dispose();
        }
    }
}
