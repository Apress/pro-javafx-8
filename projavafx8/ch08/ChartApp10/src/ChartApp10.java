import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

// listing 8-11, fig 8-16
public class ChartApp10 extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) {
      NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(30);
        xAxis.setAutoRanging(false);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(20110);
        xAxis.setUpperBound(20201);
        xAxis.setTickUnit(10);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number n) {
                return String.valueOf(n.intValue() / 10);
            }

            @Override
            public Number fromString(String s) {
                return Integer.valueOf(s) * 10;
            }
        });
        BubbleChart bubbleChart = new BubbleChart(xAxis, yAxis);
        bubbleChart.setData(getChartData());
        bubbleChart.setTitle("Speculations");
        primaryStage.setTitle("BubbleChart example");

        StackPane root = new StackPane();
        root.getChildren().add(bubbleChart);
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }

    private ObservableList<XYChart.Series<Integer, Double>> getChartData() {
        double javaValue = 17.56;
        double cValue = 17.06;
        double cppValue = 8.25;
        ObservableList<XYChart.Series<Integer, Double>> answer = FXCollections.observableArrayList();
        Series<Integer, Double> java = new Series<>();
        Series<Integer, Double> c = new Series<>();
        Series<Integer, Double> cpp = new Series<>();
        java.setName("java");
        c.setName("C");
        cpp.setName("C++");
        for (int i = 20110; i < 20210; i = i + 10) {
            double diff = Math.random();
            java.getData().add(new XYChart.Data(i, javaValue));
            javaValue = Math.max(javaValue + 4 * diff - 2, 0);
            diff = Math.random();
            c.getData().add(new XYChart.Data(i, cValue));
            cValue = Math.max(cValue + 4 * diff - 2, 0);
            diff = Math.random();
            cpp.getData().add(new XYChart.Data(i, cppValue));
            cppValue = Math.max(cppValue + 4 * diff - 2, 0);
        }
        answer.addAll(java, c, cpp);
        return answer;
    }
}
    
    
    
    
    
    