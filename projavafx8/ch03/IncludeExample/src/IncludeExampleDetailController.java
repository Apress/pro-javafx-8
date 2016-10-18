import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class IncludeExampleDetailController {
    @FXML
    private Label category;

    @FXML
    private Label name;

    @FXML
    private TextArea description;

    private Product product;
    private ChangeListener<String> listener;

    public void setProduct(Product product) {
        if (this.product != null) {
            unhookListener();
        }
        this.product = product;
        hookTo(product);
    }

    private void unhookListener() {
        description.textProperty().removeListener(listener);
    }

    private void hookTo(Product product) {
        if (product == null) {
            category.setText("");
            name.setText("");
            description.setText("");
            listener = null;
        } else {
            category.setText(product.getCategory());
            name.setText(product.getName());
            description.setText(product.getDescription());
            listener = (observable, oldValue, newValue) ->
                product.setDescription(newValue);
            description.textProperty().addListener(listener);
        }
    }
}
