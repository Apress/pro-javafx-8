import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author johan
 */
// Listing 8-4
public class ChartApp2Css extends Application {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    PieChart pieChart = new PieChart();
    pieChart.setData(getChartData());
    pieChart.setTitle("Tiobe index");
    //pieChart.setLegendSide(Side.LEFT);
   
    primaryStage.setTitle("PieChart");    
    StackPane root = new StackPane();
    root.getChildren().add(pieChart);
    Scene scene = new Scene(root, 400, 250);
    scene.getStylesheets().add("/chartappstyle.css");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private ObservableList<Data> getChartData() {
    ObservableList<Data> answer = FXCollections.observableArrayList();
    answer.addAll(new PieChart.Data("java", 17.56),
            new PieChart.Data("C", 17.06),
            new PieChart.Data("C++", 8.25),
            new PieChart.Data("C#", 8.20),
            new PieChart.Data("ObjectiveC", 6.8),
            new PieChart.Data("PHP", 6.0),
            new PieChart.Data("(Visual)Basic", 4.76),
            new PieChart.Data("Other",31.37 )
            );
    return answer;
  }
  
  
}