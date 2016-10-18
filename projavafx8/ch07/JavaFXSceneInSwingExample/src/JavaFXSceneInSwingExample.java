/*
 * Copyright (c) 2011, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import javax.swing.*;
import java.awt.*;

public class JavaFXSceneInSwingExample {
    public static void main(final String[] args) {
        EventQueue.invokeLater(() -> {
            swingMain(args);
        });
    }

    private static void swingMain(String[] args) {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        controller.mainLoop();
    }

    private static class Model {
        private ObjectProperty<Color> fill = new SimpleObjectProperty<>(Color.LIGHTGRAY);
        private ObjectProperty<Color> stroke = new SimpleObjectProperty<>(Color.DARKGRAY);

        public final Color getFill() {
            return fill.get();
        }

        public final void setFill(Color value) {
            this.fill.set(value);
        }

        public final Color getStroke() {
            return stroke.get();
        }

        public final void setStroke(Color value) {
            this.stroke.set(value);
        }

        public final ObjectProperty<Color> fillProperty() {
            return fill;
        }

        public final ObjectProperty<Color> strokeProperty() {
            return stroke;
        }
    }

    private static class View {
        public JFrame frame;
        public JFXPanel canvas;
        public JButton changeFillButton;
        public JButton changeStrokeButton;

        private View(final Model model) {
            frame = new JFrame("JavaFX in Swing Example");
            canvas = new JFXPanel();
            canvas.setPreferredSize(new Dimension(210, 210));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    final Rectangle rectangle = new Rectangle(200, 200);
                    rectangle.setStrokeWidth(10);
                    rectangle.fillProperty().bind(model.fillProperty());
                    rectangle.strokeProperty().bind(model.strokeProperty());
                    final VBox vBox = new VBox(rectangle);
                    final Scene scene = new Scene(vBox);
                    canvas.setScene(scene);
                }
            });
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
            this.view.changeFillButton.addActionListener(e -> {
                Platform.runLater(() -> {
                    final Paint fillPaint = model.getFill();
                    if (fillPaint.equals(Color.LIGHTGRAY)) {
                        model.setFill(Color.GRAY);
                    } else {
                        model.setFill(Color.LIGHTGRAY);
                    }
                });
            });
            this.view.changeStrokeButton.addActionListener(e -> {
                Platform.runLater(() -> {
                    final Paint strokePaint = model.getStroke();
                    if (strokePaint.equals(Color.DARKGRAY)) {
                        model.setStroke(Color.BLACK);
                    } else {
                        model.setStroke(Color.DARKGRAY);
                    }
                });
            });
        }

        public void mainLoop() {
            view.frame.setVisible(true);
        }
    }
}
