
package projavafx.jerseystackoverflow;

import java.util.List;

/**
 *
 * @author johan
 */
public class StackResponse {
    private List<Question> items;

    /**
     * @return the item
     */
    public List<Question> getItems() {
        return items;
    }

    /**
     * @param item the item to set
     */
    public void setItems(List<Question> items) {
        this.items = items;
    }
}
