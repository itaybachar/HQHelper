import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GoogleAPI{

    private String cx = "015527553999090345192:z-h9z3nnck0";
    private String key = "AIzaSyBqVU8wqd9c6RztpXKlanYV8KC2WTaXNqA";
    public static String A,B,C, query;
    private AppController controller;

    public static double countA = 0;
    public static double countB = 0;
    public static double countC = 0;
    public static double total  = 0;

    public GoogleAPI(String A, String B, String C, String query, AppController controller) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.query = query;
        this.controller = controller;
    }


    private Search getSearchResults() throws GeneralSecurityException, IOException {
        Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),null)
                .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(key)).build();

        Customsearch.Cse.List list = cs.cse().list(query).setCx(cx);
        list.setNum((long)9);

        return list.execute();
    }

    public void doAPI() throws GeneralSecurityException, IOException {
        //Get Search Results
        List<Result> resultList = getSearchResults().getItems();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i = 0; i< resultList.size(); i++){
            executorService.submit(new SearchProcessor(resultList.get(i),i,controller));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class SearchProcessor implements Runnable {

    private Result result;
    private int Id;
    private AppController controller;
    private Object lock1 = new Object(),lock2 = new Object(),lock3 = new Object();

    public SearchProcessor(Result result, int Id, AppController controller) {
        this.result = result;
        this.Id = Id;
        this.controller = controller;
    }

    @Override
    public void run() {
        System.out.println("Starting: " + Id);
        try {
            URL url = new URL(result.getLink());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            if (con.getResponseCode() == 200) {

                InputStream inputStream = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                String line;


                while ((line = br.readLine()) != null) {
                    line = line.replaceAll(" ", "");
                        if (line.toLowerCase().contains(GoogleAPI.A)) {
                            synchronized (lock1) {

                            GoogleAPI.countA++;
                            GoogleAPI.total++;
                            System.out.println("A: " + GoogleAPI.countA + " B: " + GoogleAPI.countB + " C: " + GoogleAPI.countC + " Total: " + GoogleAPI.total);
                        }
                    }
                        if (line.toLowerCase().contains(GoogleAPI.B)) {
                            synchronized (lock2) {

                            GoogleAPI.countB++;
                            GoogleAPI.total++;
                            System.out.println("A: " + GoogleAPI.countA + " B: " + GoogleAPI.countB + " C: " + GoogleAPI.countC + " Total: " + GoogleAPI.total);
                        }
                    }
                    synchronized (lock3) {
                        if (line.toLowerCase().contains(GoogleAPI.C)) {
                            GoogleAPI.countC++;
                            GoogleAPI.total++;
                            System.out.println("A: " + GoogleAPI.countA + " B: " + GoogleAPI.countB + " C: " + GoogleAPI.countC + " Total: " + GoogleAPI.total);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }



        System.out.println("Completed: " + Id);
    }
}
