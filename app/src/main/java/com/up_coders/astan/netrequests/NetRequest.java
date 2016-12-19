package com.up_coders.astan.netrequests;

import android.os.AsyncTask;

import com.up_coders.astan.HttpManager;
import com.up_coders.astan.parser.GeneralParser;

import org.json.JSONArray;

/**
 * Created by mahdi on 12/13/16.
 */
public class NetRequest extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String content = HttpManager.getData(params[0]);

        JSONArray result = GeneralParser.parseFeed(content);
//            Log.d("json", content);

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onProgressUpdate(String... values) {
//            updateDisplay();
    }
}
