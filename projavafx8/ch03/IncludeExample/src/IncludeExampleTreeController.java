import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;

public class IncludeExampleTreeController {
    @FXML
    private TreeTableView<Product> treeTableView;

    @FXML
    private TreeTableColumn<Product, String> category;

    @FXML
    private TreeTableColumn<Product, String> name;

    @FXML
    private VBox details;

    @FXML
    private IncludeExampleDetailController detailsController;

    @FXML
    public void initialize() {
        Product[] products = new Product[101];
        for (int i = 0; i <= 100; i++) {
            products[i] = new Product();
            products[i].setCategory("Category" + (i / 10));
            products[i].setName("Name" + i);
            products[i].setDescription("Description" + i);
        }
        TreeItem<Product> root = new TreeItem<>(products[100]);
        root.setExpanded(true);
        for (int i = 0; i < 10; i++) {
            TreeItem<Product> firstLevel =
                new TreeItem<>(products[i * 10]);
            firstLevel.setExpanded(true);
            for (int j = 1; j < 10; j++) {
                TreeItem<Product> secondLevel =
                    new TreeItem<>(products[i * 10 + j]);
                secondLevel.setExpanded(true);
                firstLevel.getChildren().add(secondLevel);
            }
            root.getChildren().add(firstLevel);
        }

        category.setCellValueFactory(param ->
            new ReadOnlyStringWrapper(param.getValue().getValue().getCategory()));
        name.setCellValueFactory(param ->
            new ReadOnlyStringWrapper(param.getValue().getValue().getName()));

        treeTableView.setRoot(root);

        treeTableView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                Product product = null;
                if (newValue != null) {
                    product = newValue.getValue();
                }
                detailsController.setProduct(product);
            });
    }
}