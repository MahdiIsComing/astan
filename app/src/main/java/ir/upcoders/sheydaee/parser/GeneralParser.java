package ir.upcoders.sheydaee.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mahdi on 12/13/16.
 */
public class GeneralParser {
    public static JSONArray parseFeed(String content) {

        try {
            JSONObject main_obj = new JSONObject(content);
            JSONArray ar = main_obj.getJSONArray("items");


            return ar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
