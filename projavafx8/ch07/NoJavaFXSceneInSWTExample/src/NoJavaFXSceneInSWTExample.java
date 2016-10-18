import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class NoJavaFXSceneInSWTExample {
    public static void main(final String[] args) {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        controller.mainLoop();
    }

    private static class Model {
        public static final RGB LIGHT_GRAY = new RGB(0xd3, 0xd3, 0xd3);
        public static final RGB GRAY = new RGB(0x80, 0x80, 0x80);
        public static final RGB DARK_GRAY = new RGB(0xa9, 0xa9, 0xa9);
        public static final RGB BLACK = new RGB(0x0, 0x0, 0x0);
        public RGB fillColor = LIGHT_GRAY;
        public RGB strokeColor = DARK_GRAY;
    }

    private static class View {
        public Display display;
        public Shell frame;
        public Canvas canvas;
        public Button changeFillButton;
        public Button changeStrokeButton;
        public Label mouseLocation;
        public boolean mouseInCanvas;

        private View(final Model model) {
            this.display = new Display();
            frame = new Shell(display);
            frame.setText("No JavaFX in SWT Example");
            RowLayout frameLayout = new RowLayout(SWT.VERTICAL);
            frameLayout.spacing = 10;
            frameLayout.center = true;
            frame.setLayout(frameLayout);

            Composite canvasPanel = new Composite(frame, SWT.NONE);
            RowLayout canvasPanelLayout = new RowLayout(SWT.VERTICAL);
            canvasPanelLayout.spacing = 10;
            canvasPanel.setLayout(canvasPanelLayout);

            canvas = new Canvas(canvasPanel, SWT.NONE);
            canvas.setLayoutData(new RowData(200, 200));
            canvas.addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent paintEvent) {
                    final GC gc = paintEvent.gc;
                    final Color strokeColor = new Color(display, model.strokeColor);
                    gc.setBackground(strokeColor);
                    gc.fillRectangle(0, 0, 200, 200);
                    final Color fillColor = new Color(display, model.fillColor);
                    gc.setBackground(fillColor);
                    gc.fillRectangle(10, 10, 180, 180);
                    strokeColor.dispose();
                    fillColor.dispose();
                }
            });

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
                    if (model.fillColor.equals(model.LIGHT_GRAY)) {
                        model.fillColor = model.GRAY;
                    } else {
                        model.fillColor = model.LIGHT_GRAY;
                    }
                    view.canvas.redraw();
                }
            });
            view.changeStrokeButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (model.strokeColor.equals(model.DARK_GRAY)) {
                        model.strokeColor = model.BLACK;
                    } else {
                        model.strokeColor = model.DARK_GRAY;
                    }
                    view.canvas.redraw();
                }
            });
            view.canvas.addMouseMoveListener(new MouseMoveListener() {
                @Override
                public void mouseMove(MouseEvent mouseEvent) {
                    if (view.mouseInCanvas) {
                        view.mouseLocation.setText("(" + mouseEvent.x + ", " + mouseEvent.y + ")");
                    }
                }
            });
            this.view.canvas.addMouseTrackListener(new MouseTrackAdapter() {
                @Override
                public void mouseEnter(MouseEvent e) {
                    view.mouseInCanvas = true;
                }

                @Override
                public void mouseExit(MouseEvent e) {
                    view.mouseInCanvas = false;
                    view.mouseLocation.setText("");
                }
            });

        }

        public void mainLoop() {
            view.frame.open();
            while (!view.frame.isDisposed()) {
                if (!view.display.readAndDispatch()) {
                    view.display.sleep();
                }
            }
            view.display.dispose();
        }
    }
}
