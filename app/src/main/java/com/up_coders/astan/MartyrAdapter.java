package com.up_coders.astan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.up_coders.astan.model.Martyr;


import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by mahdi on 10/24/16.
 */
public class MartyrAdapter extends ArrayAdapter<Martyr> {

    private Context context;
    private List<Martyr> martyrList;

    private LruCache<Integer, Bitmap> imageCache;


    public MartyrAdapter(Context context, int resource, List<Martyr> objects) {
        super(context, resource, objects);
        this.context = context;
        this.martyrList = objects;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mylist, parent, false);


        //Display martyr name in the TextView widget
        Martyr martyr = martyrList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.itemname);
        tv.setText(martyr.getFirst_name() + " " + martyr.getLast_name());

        //Image
        Bitmap bitmap = imageCache.get(martyr.getId());

        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.itemicon);
            image.setImageBitmap(martyr.getAvatar());
        } else {
            MartyrAndView container = new MartyrAndView();
            container.martyr = martyr;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);

        }


        return view;
    }

    class MartyrAndView {
        public Martyr martyr;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<MartyrAndView, Void, MartyrAndView> {

        @Override
        protected MartyrAndView doInBackground(MartyrAndView... params) {
            MartyrAndView container = params[0];
            Martyr martyr = container.martyr;


            try {
                String imgeUrl = MainActivity.baseURl + martyr.getId() + "/avatar" + "/download";
                InputStream in = (InputStream) new URL(imgeUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                martyr.setAvatar(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MartyrAndView martyrAndView) {
            //Image
            ImageView image = (ImageView) martyrAndView.view.findViewById(R.id.itemicon);
            image.setImageBitmap(martyrAndView.bitmap);
//            martyrAndView.martyr.setBitmap(martyrAndView.bitmap);
            imageCache.put(martyrAndView.martyr.getId(), martyrAndView.bitmap);

        }

    }

}
