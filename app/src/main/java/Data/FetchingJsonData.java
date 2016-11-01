package Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Korisnik on 19.10.2016..
 */
public class FetchingJsonData {

    private String address;
    URL url;

    public FetchingJsonData() {
    }

    public FetchingJsonData(String address) {
        this.address = address;
    }

    public String getJsonString(){
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null){
                responseStrBuilder.append(input);
            }
            bufferedReader.close();
            String fjd = responseStrBuilder.toString();

            return fjd;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
