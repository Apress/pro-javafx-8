import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
//import org.scenicview.ScenicView;

// 
public class ChartApp6Css extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);
        scatterChart.setData(getChartData());
     boolean old =    scatterChart.alternativeRowFillVisibleProperty().get();
        System.out.println("old = "+old);
     scatterChart.alternativeRowFillVisibleProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                System.out.println("ALT WAS "+t+" and now "+t1);
            }
        });
        scatterChart.setAlternativeRowFillVisible(true);
        scatterChart.setAlternativeColumnFillVisible(true);
        scatterChart.setTitle("speculations");
        primaryStage.setTitle("ScatteredChart example");
        List cssMetaData = scatterChart.getCssMetaData();
        for (Object css : cssMetaData) {
            System.out.println("-> "+css);
        }
        StackPane root = new StackPane();
        root.getChildren().add(scatterChart);
        Scene scene = new Scene (root, 400, 250);
        scene.getStylesheets().add("/chartapp6.css");
    //    ScenicView.show(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("altvis? "+scatterChart.isAlternativeRowFillVisible());
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() {
        double javaValue = 17.56;
        double cValue = 17.06;
        double cppValue = 8.25;
        ObservableList<XYChart.Series<String, Double>> answer = FXCollections.observableArrayList();
        Series<String, Double> java = new Series<String, Double>();
        Series<String, Double> c = new Series<String, Double>();
        Series<String, Double> cpp = new Series<String, Double>();
        java.setName("java");
        c.setName("C");
        cpp.setName("C++");
        
        for (int i = 2011; i < 2021; i++) {
            java.getData().add(new XYChart.Data(Integer.toString(i), javaValue));
            javaValue = javaValue + Math.random() - .5;
            c.getData().add(new XYChart.Data(Integer.toString(i), cValue));
            cValue = cValue + Math.random() - .5;
            cpp.getData().add(new XYChart.Data(Integer.toString(i), cppValue));
            cppValue = cppValue + Math.random() - .5;
        }
        answer.addAll(java, c, cpp);
        return answer;
    }
}