package charlesli.com.personalvocabbuilder.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by charles on 2016-05-24.
 */
public class GoogleTranslate extends AsyncTask<String, Void, String>{

    private Exception exception;

    @Override
    protected String doInBackground(String... params) {
        String vocab = params[0];

        try {
            String APIKey = "AIzaSyDGijVCq6fPpmoP9ZLJwr9GZPtOuvVxrSU";
            String encodedQuery = URLEncoder.encode(vocab, "UTF-8");
            //String source = "es";
            String target = "en";
            URL url = new URL("https://www.googleapis.com/language/translate/v2?key=" +
                    APIKey +
                    "&q=" +
                    encodedQuery +
                    //"&source=" +
                    //source +
                    "&target=" +
                    target);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e) {
            return null;
        }
    }
}
