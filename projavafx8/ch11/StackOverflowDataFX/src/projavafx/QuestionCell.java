package projavafx;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.ListCell;

/**
 *
 * @author johan
 */
public class QuestionCell extends ListCell<Question> {
    
    static final SimpleDateFormat sdf = new SimpleDateFormat ("dd-MM-YY");
    @Override protected void updateItem(Question question, boolean empty){
        super.updateItem(question, empty);
        if (empty) {
            setText("");
        } else {
            StringBuilder sb= new StringBuilder();
            sb.append("[").append(sdf.format(new Date(question.getTimestamp()))).append("]")
                    .append(" ").append(question.getOwner()+": "+question.getQuestion());
            setText(sb.toString());
        }
    }

}
