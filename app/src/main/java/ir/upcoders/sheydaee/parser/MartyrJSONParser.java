package ir.upcoders.sheydaee.parser;

/**
 * Created by mahdi on 10/23/16.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.upcoders.sheydaee.model.Martyr;

public class MartyrJSONParser {

    public static List<Martyr> parseFeed(String content) {

        try {
            JSONObject main_obj = new JSONObject(content);
            JSONArray ar = main_obj.getJSONArray("items");

            List<Martyr> MartyrList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Martyr martyr = new Martyr();

                martyr.setId(obj.getInt("id"));
                martyr.setFirst_name(obj.getString("first_name"));
                martyr.setLast_name(obj.getString("last_name"));
                martyr.setBirth_date(obj.getString("birth_date"));
                martyr.setBirth_place(obj.getString("birth_place"));
                martyr.setMartyrdom_date(obj.getString("martyrdom_date"));
                martyr.setMartyrdom_place(obj.getString("martyrdom_place"));
//                martyr.setMission(obj.getString("mission"));

                MartyrList.add(martyr);
            }

            return MartyrList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}