package projavafx;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.ListCell;

public class QuestionCell extends ListCell<Question> {
    
    static final SimpleDateFormat sdf = new SimpleDateFormat ("dd-MM-YY");
    @Override
    protected void updateItem(Question question, boolean b){
        if (question!= null) {
            StringBuilder sb= new StringBuilder();
            sb.append("[").append(sdf.format(new Date(question.getTimestamp()))).append("]")
                    .append(" ").append(question.getOwner())
                    .append(": ").append(question.getQuestion());
            setText(sb.toString());
        }
    }
}
