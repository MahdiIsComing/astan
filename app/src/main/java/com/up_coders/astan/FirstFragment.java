package com.up_coders.astan;

/**
 * Created by mahdi on 10/25/16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.up_coders.astan.model.Martyr;

import java.util.List;


public class FirstFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_layout,null);

        MainActivity activity = (MainActivity) getActivity();
        Integer MartyrID = activity.getMartyrID() -1 ;

        Martyr martyr = activity.getMartyr().get(MartyrID);


        WebView webview = (WebView) view.findViewById(R.id.webview_intro);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webview.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>", "text/html", "UTF-8", null);

        String intro = "<html><body>" + "<img src=\"http://martyr.sellonclouds.com/api/martyrs/martyr/" + martyr.getId() +  "/avatar/download\""   + " />" +
                "</body></html>";
        webview.loadData(intro, "text/html", null);



        return view;

    }
}
