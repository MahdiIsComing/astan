package ir.upcoders.sheydaee.netrequests;

import android.os.AsyncTask;

import org.json.JSONArray;

import ir.upcoders.sheydaee.HttpManager;
import ir.upcoders.sheydaee.parser.GeneralParser;

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
