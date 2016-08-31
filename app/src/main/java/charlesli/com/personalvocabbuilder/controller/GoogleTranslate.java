package charlesli.com.personalvocabbuilder.controller;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by charles on 2016-05-24.
 */
public class GoogleTranslate extends AsyncTask<String, Void, String>{

    private ProgressBar progressBar;
    private String APIKey;

    private Listener listener;

    public GoogleTranslate(ProgressBar progressBar, String key) {
        super();
        this.progressBar = progressBar;
        APIKey = key;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        if (listener!=null){ listener.onTaskResult(s); }
        progressBar.setVisibility(View.GONE);

    }

    @Override
    protected String doInBackground(String... params) {
        String vocab = params[0];
        String source = params[1];
        String target = params[2];

        String sourceQuery = "";
        String targetQuery = "&target=" + target;

        if (!source.equals("Detect Language")) {
            sourceQuery = "&source=" + source;
        }

        try {
            String encodedQuery = URLEncoder.encode(vocab, "UTF-8");
            URL url = new URL("https://www.googleapis.com/language/translate/v2?key=" +
                    APIKey +
                    "&q=" +
                    encodedQuery +
                    sourceQuery +
                    targetQuery);
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

    public interface Listener{
        void onTaskResult(String string);
    }

}
