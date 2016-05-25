package charlesli.com.personalvocabbuilder.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by charles on 2016-05-24.
 */
public class JSONParser {

    public String parseJSONString(String jsonString) {
        try {
            JSONObject object = (JSONObject) new JSONTokener(jsonString).nextValue();
            String data = object.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
