package projavafx.jerseystackoverflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
//import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.GZipEncoder;

public class QuestionRetrievalService extends Service<ObservableList<Question>> {

    private String loc;
    private String path;
    private String search;

    public QuestionRetrievalService(String loc, String path, String search) {
        this.loc = loc;
        this.path = path;
        this.search = search;
    }

    @Override
    protected Task<ObservableList<Question>> createTask() {
        return new Task<ObservableList<Question>>() {
            @Override
            protected ObservableList<Question> call() throws Exception {
                ReaderInterceptor ri = new ReaderInterceptor() {
                    @Override
                    public Object aroundReadFrom(ReaderInterceptorContext context)
                            throws IOException, WebApplicationException {
                        final InputStream originalInputStream = context.getInputStream();
                        context.setInputStream(new GZIPInputStream(originalInputStream));
                        return context.proceed();
                    }
                };
                Client client = ClientBuilder.newClient();
                client.register(ri);
 //               client.register(JacksonFeature.class);
                WebTarget target = client.target(loc).path(path).queryParam("tagged", search).queryParam("site", "stackoverflow");
                //Response r = target.request(MediaType.APPLICATION_JSON).get();
               JsonStructure obj = target.request(MediaType.APPLICATION_JSON).get(JsonStructure.class);
                //StackResponse response = target.request(MediaType.APPLICATION_JSON).get(StackResponse.class);
System.out.println("obj? "+obj);
return null;
            //    return FXCollections.observableArrayList(response.getItems());
            }
        };
    }

}
