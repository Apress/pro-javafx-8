import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerAndTaskExample extends Application {
    private Model model;
    private View view;

    public static void main(String[] args) {
        launch(args);
    }

    public WorkerAndTaskExample() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) throws Exception {
        view = new View(model);
        hookupEvents();
        stage.setTitle("Worker and Task Example");
        stage.setScene(view.scene);
        stage.show();
    }

    private void hookupEvents() {
        view.startButton.setOnAction(actionEvent -> {
            new Thread((Runnable) model.worker).start();
        });
        view.cancelButton.setOnAction(actionEvent -> {
            model.worker.cancel();
        });
        view.exceptionButton.setOnAction(actionEvent -> {
            model.shouldThrow.getAndSet(true);
        });
    }

    private static class Model {
        public Worker<String> worker;
        public AtomicBoolean shouldThrow = new AtomicBoolean(false);

        private Model() {
            worker = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    updateTitle("Example Task");
                    updateMessage("Starting...");
                    final int total = 250;
                    updateProgress(0, total);
                    for (int i = 1; i <= total; i++) {
                        if (isCancelled()) {
                            updateValue("Canceled at " + System.currentTimeMillis());
                            return null; // ignored
                        }
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            if (isCancelled()) {
                                updateValue("Canceled at " + System.currentTimeMillis());
                                return null; // ignored
                            }
                        }
                        if (shouldThrow.get()) {
                            throw new RuntimeException("Exception thrown at " + System.currentTimeMillis());
                        }
                        updateTitle("Example Task (" + i + ")");
                        updateMessage("Processed " + i + " of " + total + " items.");
                        updateProgress(i, total);
                    }
                    return "Completed at " + System.currentTimeMillis();
                }

                @Override
                protected void scheduled() {
                    System.out.println("The task is scheduled.");
                }

                @Override
                protected void running() {
                    System.out.println("The task is running.");
                }
            };
            ((Task<String>) worker).setOnSucceeded(event -> {
                System.out.println("The task succeeded.");
            });
            ((Task<String>) worker).setOnCancelled(event -> {
                System.out.println("The task is canceled.");
            });
            ((Task<String>) worker).setOnFailed(event -> {
                System.out.println("The task failed.");
            });
        }
    }

    private static class View {
        public ProgressBar progressBar;

        public Label title;
        public Label message;
        public Label running;
        public Label state;
        public Label totalWork;
        public Label workDone;
        public Label progress;
        public Label value;
        public Label exception;

        public Button startButton;
        public Button cancelButton;
        public Button exceptionButton;

        public Scene scene;

        private View(final Model model) {
            progressBar = new ProgressBar();
            progressBar.setMinWidth(250);

            title = new Label();
            message = new Label();
            running = new Label();
            state = new Label();
            totalWork = new Label();
            workDone = new Label();
            progress = new Label();
            value = new Label();
            exception = new Label();

            startButton = new Button("Start");
            cancelButton = new Button("Cancel");
            exceptionButton = new Button("Exception");

            final ReadOnlyObjectProperty<Worker.State> stateProperty =
                model.worker.stateProperty();

            progressBar.progressProperty().bind(model.worker.progressProperty());

            title.textProperty().bind(
                model.worker.titleProperty());
            message.textProperty().bind(
                model.worker.messageProperty());
            running.textProperty().bind(
                Bindings.format("%s", model.worker.runningProperty()));
            state.textProperty().bind(
                Bindings.format("%s", stateProperty));
            totalWork.textProperty().bind(
                model.worker.totalWorkProperty().asString());
            workDone.textProperty().bind(
                model.worker.workDoneProperty().asString());
            progress.textProperty().bind(
                Bindings.format("%5.2f%%", model.worker.progressProperty().multiply(100)));
            value.textProperty().bind(
                model.worker.valueProperty());
            exception.textProperty().bind(Bindings.createStringBinding(() -> {
                final Throwable exception = model.worker.getException();
                if (exception == null) return "";
                return exception.getMessage();
            }, model.worker.exceptionProperty()));

            startButton.disableProperty().bind(
                stateProperty.isNotEqualTo(Worker.State.READY));
            cancelButton.disableProperty().bind(
                stateProperty.isNotEqualTo(Worker.State.RUNNING));
            exceptionButton.disableProperty().bind(
                stateProperty.isNotEqualTo(Worker.State.RUNNING));

            HBox topPane = new HBox(10, progressBar);
            topPane.setAlignment(Pos.CENTER);
            topPane.setPadding(new Insets(10, 10, 10, 10));

            ColumnConstraints constraints1 = new ColumnConstraints();
            constraints1.setHalignment(HPos.CENTER);
            constraints1.setMinWidth(65);

            ColumnConstraints constraints2 = new ColumnConstraints();
            constraints2.setHalignment(HPos.LEFT);
            constraints2.setMinWidth(200);

            GridPane centerPane = new GridPane();
            centerPane.setHgap(10);
            centerPane.setVgap(10);
            centerPane.setPadding(new Insets(10, 10, 10, 10));
            centerPane.getColumnConstraints()
                .addAll(constraints1, constraints2);

            centerPane.add(new Label("Title:"), 0, 0);
            centerPane.add(new Label("Message:"), 0, 1);
            centerPane.add(new Label("Running:"), 0, 2);
            centerPane.add(new Label("State:"), 0, 3);
            centerPane.add(new Label("Total Work:"), 0, 4);
            centerPane.add(new Label("Work Done:"), 0, 5);
            centerPane.add(new Label("Progress:"), 0, 6);
            centerPane.add(new Label("Value:"), 0, 7);
            centerPane.add(new Label("Exception:"), 0, 8);

            centerPane.add(title, 1, 0);
            centerPane.add(message, 1, 1);
            centerPane.add(running, 1, 2);
            centerPane.add(state, 1, 3);
            centerPane.add(totalWork, 1, 4);
            centerPane.add(workDone, 1, 5);
            centerPane.add(progress, 1, 6);
            centerPane.add(value, 1, 7);
            centerPane.add(exception, 1, 8);

            HBox buttonPane = new HBox(10,
                startButton, cancelButton, exceptionButton);
            buttonPane.setPadding(new Insets(10, 10, 10, 10));
            buttonPane.setAlignment(Pos.CENTER);

            BorderPane root = new BorderPane(centerPane,
                topPane, null, buttonPane, null);
            scene = new Scene(root);
        }
    }
}
