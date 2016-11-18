package com.up_coders.astan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.up_coders.astan.ShowFragment.OnListFragmentInteractionListener;
import com.up_coders.astan.dummy.DummyContent.DummyItem;
import com.up_coders.astan.model.Martyr;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShowRecyclerViewAdapter extends RecyclerView.Adapter<MyShowRecyclerViewAdapter.ViewHolder>{

//    private final List<DummyItem> mValues;

    private List<Martyr> martyrList;

    ProgressBar pb;

    private final OnListFragmentInteractionListener mListener;

    private LruCache<Integer, Bitmap> imageCache;

//    public MyShowRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public MyShowRecyclerViewAdapter(List<Martyr> martyrs, OnListFragmentInteractionListener listener) {
        martyrList = martyrs;
        mListener = listener;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_show, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mItem = martyrList.get(position);
        holder.mIdView.setText(Integer.toString(martyrList.get(position).getId()));
        holder.mContentView.setText(martyrList.get(position).getTitle());

        //Image
        Bitmap bitmap = imageCache.get(holder.mItem.getId());

        if (bitmap != null){
            holder.mImageView.setImageBitmap(holder.mItem.getBitmap());
        }else{
            MartyrAndView container = new MartyrAndView();
            container.martyr = holder.mItem;
            container.view = holder.mView;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
//        return mValues.size();
        if (martyrList == null)
            return 0;
        return martyrList.size();
    }

    class MartyrAndView {
        public Martyr martyr;
        public View view;
        public Bitmap bitmap;
    }

    public class ImageLoader extends AsyncTask<MartyrAndView, Void, MartyrAndView> {
        @Override
        protected MartyrAndView doInBackground(MartyrAndView... params){
            MartyrAndView container = params[0];
            Martyr martyr = container.martyr;

            if (martyr.getName().contains("portrait")) {
                try {
                    String imgeUrl = MainActivity.baseURl + martyr.getId() + "/download";
                    InputStream in = (InputStream) new URL(imgeUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    martyr.setBitmap(bitmap);
                    in.close();
                    container.bitmap = bitmap;
                    return container;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MartyrAndView martyrAndView){
            //Image load
            ImageView image = (ImageView) martyrAndView.view.findViewById(R.id.portrait);
            image.setImageBitmap(martyrAndView.bitmap);
//            martyrAndView.martyr.setBitmap(martyrAndView.bitmap);
            imageCache.put(martyrAndView.martyr.getId(), martyrAndView.bitmap);


        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
//        public DummyItem mItem;
        public Martyr mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.portrait);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
