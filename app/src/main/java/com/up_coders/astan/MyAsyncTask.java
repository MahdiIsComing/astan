package com.up_coders.astan;

import android.os.AsyncTask;

/**
 * Created by mahdi on 12/10/16.
 * A general class to download files for Martyr Rest
 */
public class MyAsyncTask extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        String content = HttpManager.getData(params[0]);

//        martyrList = MartyrJSONParser.parseFeed(content);
//            Log.d("json", content);

        return content;
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onProgressUpdate(String... values) {
//            updateDisplay();
    }
}
