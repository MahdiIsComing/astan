package com.up_coders.astan;

/**
 * Created by mahdi on 10/25/16.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.up_coders.astan.model.Martyr;
import com.up_coders.astan.parser.MartyrJSONParser;

import java.util.List;


public class SecondFragment extends Fragment {


    ProgressBar pb;
//    List<MyTaskFragment> tasks;

    List<Martyr> martyrList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.primary_layout,null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        Integer MartyrID = activity.martyrID - 1;

        requestData(MainActivity.baseURl + MartyrID.toString() + "/download");
    }



    private void requestData(String uri) {
        MyTaskFragment task = new MyTaskFragment();
        task.execute(uri);
    }

    private class MyTaskFragment extends AsyncTask<String, String, List<Martyr>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay();

//            if (tasks.size() == 0) {
////                pb.setVisibility(View.VISIBLE);
//            }
//            tasks.add(this);
        }

        @Override
        protected List<Martyr> doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);

//            martyrList = MartyrJSONParser.parseFeed(content);
//            Log.d("json", content);

            return martyrList;
        }

        @Override
        protected void onPostExecute(List<Martyr> result) {
//            tasks.remove(this);
//            if (tasks.size() == 0) {
////                pb.setVisibility(View.INVISIBLE);
//            }

            if (result == null) {
//                Toast.makeText(MainActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            martyrList = result;
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            updateDisplay();
        }
    }

    protected void updateDisplay(){


    }
}
