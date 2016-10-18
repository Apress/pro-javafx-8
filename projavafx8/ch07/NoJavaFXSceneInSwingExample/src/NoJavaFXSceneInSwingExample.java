import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoJavaFXSceneInSwingExample {
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingMain(args);
            }
        });
    }

    private static void swingMain(String[] args) {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        controller.mainLoop();
    }

    private static class Model {
        public Color fillColor = Color.LIGHT_GRAY;
        public Color strokeColor = Color.DARK_GRAY;
    }

    private static class View {
        public JFrame frame;
        public JComponent canvas;
        public JButton changeFillButton;
        public JButton changeStrokeButton;

        private View(final Model model) {
            frame = new JFrame("No JavaFX in Swing Example");
            canvas = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(model.strokeColor);
                    g.fillRect(0, 0, 200, 200);
                    g.setColor(model.fillColor);
                    g.fillRect(10, 10, 180, 180);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(200, 200);
                }
            };
            FlowLayout canvasPanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
            JPanel canvasPanel = new JPanel(canvasPanelLayout);
            canvasPanel.add(canvas);

            changeFillButton = new JButton("Change Fill");
            changeStrokeButton = new JButton("Change Stroke");
            FlowLayout buttonPanelLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
            JPanel buttonPanel = new JPanel(buttonPanelLayout);
            buttonPanel.add(changeFillButton);
            buttonPanel.add(changeStrokeButton);

            frame.add(canvasPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.pack();
        }
    }

    private static class Controller {
        private View view;

        private Controller(final Model model, final View view) {
            this.view = view;
            this.view.changeFillButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (model.fillColor.equals(Color.LIGHT_GRAY)) {
                        model.fillColor = Color.GRAY;
                    } else {
                        model.fillColor = Color.LIGHT_GRAY;
                    }
                    view.canvas.repaint();
                }
            });
            this.view.changeStrokeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (model.strokeColor.equals(Color.DARK_GRAY)) {
                        model.strokeColor = Color.BLACK;
                    } else {
                        model.strokeColor = Color.DARK_GRAY;
                    }
                    view.canvas.repaint();
                }
            });
        }

        public void mainLoop() {
            view.frame.setVisible(true);
        }
    }
}
