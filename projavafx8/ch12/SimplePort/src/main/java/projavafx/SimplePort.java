package projavafx;

import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimplePort extends Application {
    
    final String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    @Override
    public void start(Stage primaryStage) {
        final Random random = new Random();
        Button btn = new Button();
        btn.setText("Guess a day");
        final Label answer = new Label("no input received");
        answer.setTranslateY(50);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                int day = random.nextInt(7);
                answer.setText(days[day]);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().addAll(btn, answer);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("JavaFX Porting");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
