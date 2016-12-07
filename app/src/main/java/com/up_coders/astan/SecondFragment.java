package com.up_coders.astan;

/**
 * Created by mahdi on 10/25/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.up_coders.astan.model.Martyr;
import com.up_coders.astan.parser.MartyrJSONParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;


public class SecondFragment extends Fragment {

    ProgressBar pB;
    WebView webView;
//    List<MyTaskFragment> tasks;

    List<Martyr> martyrList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.second_tab_layout, null);
        pB = (ProgressBar) view.findViewById(R.id.progressBar_secondTab);
        webView = (WebView) view.findViewById(R.id.webview_second);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        Integer MartyrID = activity.martyrID - 1;

//        requestData(MainActivity.baseURl + MartyrID.toString() + "/download");
        requestData("http://martyr.sellonclouds.com/api/cms/2" + "/download");
    }


    private void requestData(String uri) {
        MyTaskFragment task = new MyTaskFragment();
        task.execute(uri);
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
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/astan");
                dir.mkdirs();

                OutputStream output = new FileOutputStream(new File(dir, "2.txt"));

                writeToFile(content, output);

                byte data[] = new byte[1024];

                long total = 0;

//                while ((count = input.read(data)) != -1) {
//                    total += count;
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }

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
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    protected void updateDisplay(String result) {
        WebSettings ws = webView.getSettings();
        ws.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>", "text/html", "UTF-8", null);

        String intro = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>" +  result  + " />" +
                "</body></html>";
        webView.loadData(intro, "text/html; charset=utf-8", "utf-8");

    }
}
