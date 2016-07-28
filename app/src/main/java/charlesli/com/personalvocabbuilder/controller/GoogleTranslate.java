package charlesli.com.personalvocabbuilder.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Created by charles on 2016-05-24.
 */
public class GoogleTranslate extends AsyncTask<String, Void, String>{

    private ProgressBar mProgressBar;

    private Listener listener;

    public interface Listener{
        void onTaskResult(String string);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }


    public GoogleTranslate(ProgressBar progressBar) {
        super();
        mProgressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        if (listener!=null){ listener.onTaskResult(s); }
        mProgressBar.setVisibility(View.GONE);

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
            String APIKey = "AIzaSyDGijVCq6fPpmoP9ZLJwr9GZPtOuvVxrSU";
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

}
