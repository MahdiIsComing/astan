package com.up_coders.astan.parser;

/**
 * Created by mahdi on 10/23/16.
 */

import com.up_coders.astan.model.Martyr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                martyr.setName(obj.getString("name"));
                martyr.setFile_path(obj.getString("file_path"));
                martyr.setFile_name(obj.getString("file_name"));
                martyr.setFile_size(obj.getString("file_size"));
                martyr.setTitle(obj.getString("title"));
                martyr.setCreation_dtime(obj.getString("creation_dtime"));
                martyr.setModif_dtime(obj.getString("modif_dtime"));
                martyr.setTag(obj.getString("tag"));
                martyr.setMime_type(obj.getString("mime_type"));
                martyr.setTenant(obj.getString("tenant"));
                martyr.setSubmitter(obj.getString("submitter"));
//                martyr.setDownloads(obj.getString("downloads"));

                if (martyr.getName().contains("portrait")) {
                    MartyrList.add(martyr);
                }
            }

            return MartyrList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}