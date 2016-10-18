
public class Tweet {

    private String author;
    private String title;
    private String timeStamp;

    public Tweet() {
    }

    public Tweet(String a, String t, String s) {
        this.author = a;
        this.title = t;
        this.timeStamp = s;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
