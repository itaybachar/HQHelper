import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleAPI extends Thread{

    private String cx = "015527553999090345192:z-h9z3nnck0";
    private String key = "AIzaSyBqVU8wqd9c6RztpXKlanYV8KC2WTaXNqA";
    public String A,B,C, query;
    private AppController controller;
    public boolean done = false;
    public int countA = 0;
    public int countB = 0;
    public int countC = 0;
    public int total  = 0;

    public GoogleAPI(String A, String B, String C, String query, AppController controller) {
        this.A = A.replaceAll(" ", "");
        this.B = B.replaceAll(" ", "");
        this.C = C.replaceAll(" ", "");
        this.query = query;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            done = false;
            search();
            if(total>0) {
                controller.percent[0] = countA * 100 / total;
                controller.percent[1] = countB * 100 / total;
                controller.percent[2] = countC * 100 / total;
            }
            done = true;

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private synchronized void search() throws Exception{

        Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),null)
                .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(key)).build();

        Customsearch.Cse.List list = cs.cse().list(query).setCx(cx);
        list.setNum((long)9);

        Search result = list.execute();
        if(result.getItems() !=null){
            for(Result r : result.getItems()) {
              URL url = new URL(r.getLink());
              HttpURLConnection con = (HttpURLConnection) url.openConnection();

              if (con.getResponseCode() == 200) {

                  InputStream inputStream = con.getInputStream();

                  BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                  String line;


                  while ((line = br.readLine()) != null) {
                      line = line.replaceAll(" ","");
                      if (line.toLowerCase().contains(A)) {
                          countA++;
                          total++;

                      }
                      if (line.toLowerCase().contains(B)) {
                          countB++;
                          total++;

                      }
                      if (line.toLowerCase().contains(C)) {
                          countC++;
                          total++;

                      }
                  }
              }
          }
        }
    }
}
