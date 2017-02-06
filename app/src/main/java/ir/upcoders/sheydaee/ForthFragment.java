package ir.upcoders.sheydaee;

/**
 * Created by mahdi on 10/25/16.
 */

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.up_coders.astan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import ir.upcoders.sheydaee.db.DbHandler;
import ir.upcoders.sheydaee.model.Martyr;
import ir.upcoders.sheydaee.parser.GeneralParser;

public class ForthFragment extends Fragment {

    ProgressBar pB;
    WebView webView;

    DbHandler db;
    Martyr martyr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forth_tab_layout, null);
        pB = (ProgressBar) view.findViewById(R.id.progressBar_forthTab);
        webView = (WebView) view.findViewById(R.id.webview_forth);

        pB.getIndeterminateDrawable().setColorFilter(getResources().
                        getColor(R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN);

                /*TODO: mahdi: check if this ID has info in db or not
        and if there is no information about this ID, Add to db */

        MainActivity activity = (MainActivity) getActivity();
        DbHandler db = new DbHandler(activity);
        Integer MartyrID = activity.getMartyrID() - 1;
        martyr = activity.getMartyr().get(MartyrID);

        //check db and requisite for download or display
        if (db.getMartyr(martyr.getId()) == null || db.getMartyr(martyr.getId())[1] == null) {
            requestData(activity.baseURl + "martyrcontent/find?_px_fk=martyr&_px_fv=" + martyr.getId());
        } else {
            martyr.setMartyr_bio_id(Integer.parseInt(db.getMartyr(martyr.getId())[1]));
            martyr.setMartyr_memo_id(Integer.parseInt(db.getMartyr(martyr.getId())[2]));
            martyr.setMartyr_will_id(Integer.parseInt(db.getMartyr(martyr.getId())[3]));
            if (this.checkFileById(martyr.getMartyr_will_id())) {
                updateDisplay(readFile(martyr.getMartyr_will_id().toString()));
            } else {
                downloadData("http://martyr.sellonclouds.com/api/cms/" + martyr.getMartyr_will_id() + "/download");
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onStart() {
        super.onStart();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MainActivity activity = (MainActivity) getActivity();
            Integer MartyrID = activity.martyrID - 1;
            martyr = activity.getMartyr().get(MartyrID);
            this.db = new DbHandler(activity);

            //TODO:mahdi: review here
//            if (db.getMartyr(martyr.getId()) != null || db.getMartyr(martyr.getId())[1] != null) {
//                updateDisplay(readFile(martyr.getMartyr_bio_id().toString()));
//            }
        } else {
        }
    }


    private void downloadData(String uri) {
        MyTaskFragment task = new MyTaskFragment();
        task.execute(uri);
    }

    private void requestData(String uri) {
        NetRequest netRequest = new NetRequest();
        netRequest.execute(uri);

    }

    private class MyTaskFragment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pB.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);

            int count;
            try {

                // Output stream to write file
                File dir = new File(MainActivity.basePath);
                dir.mkdirs();

                OutputStream output = new FileOutputStream(new File(dir, martyr.getMartyr_will_id().toString()));

                writeToFile(content, output);

                byte data[] = new byte[1024];

                long total = 0;

                // flushing output
                output.flush();

                // closing streams
                output.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            pB.setVisibility(View.GONE);
            updateDisplay(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            updateDisplay();
        }
    }

    private void writeToFile(String data, OutputStream output) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    protected String readFile(String name) {
        File dir = new File(MainActivity.basePath);
        File file = new File(dir, name);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            Log.d("Error", e.getMessage());
        }
        return text.toString();
    }

    protected void updateDisplay(String result) {
        pB.setVisibility(View.GONE);
        WebSettings ws = webView.getSettings();
        ws.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);

        String intro = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<style> div{line-height: 160%;text-align: justify;text-indent: 30px; padding: 5px;}</style></head>" +
                "<body dir=\"rtl\"><div id=\"maindiv\">" + result + " </div>" +
                "</body></html>";
        webView.loadData(intro, "text/html; charset=utf-8", "utf-8");

    }

    private class NetRequest extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);

            JSONArray result = GeneralParser.parseFeed(content);

            try {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject obj = result.getJSONObject(i);

                    Log.d("title:", obj.getString("title"));
                    if (obj.getString("title").equals("bio"))
                        martyr.setMartyr_bio_id(obj.getInt("content"));
                    if (obj.getString("title").equals("memo"))
                        martyr.setMartyr_memo_id(obj.getInt("content"));
                    if (obj.getString("title").equals("will"))
                        martyr.setMartyr_will_id(obj.getInt("content"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //Update database
            db = new DbHandler((MainActivity) getActivity());
            db.updateMartyr(martyr);
            //Check if file exist otherwise download the specified file
            if (!checkFileById(martyr.getMartyr_will_id()))
                downloadData("http://martyr.sellonclouds.com/api/cms/" + martyr.getMartyr_will_id() + "/download");
            updateDisplay(readFile(martyr.getMartyr_will_id().toString()));
            return;

        }

        @Override
        protected void onProgressUpdate(String... values) {
//            updateDisplay();
        }
    }

    private boolean checkFileById(int id) {
        File file = new File(MainActivity.basePath + "/" + String.valueOf(id));

        return file.exists();
    }
}
