package com.up_coders.astan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.up_coders.astan.ShowFragment.OnListFragmentInteractionListener;
import com.up_coders.astan.dummy.DummyContent.DummyItem;
import com.up_coders.astan.model.Martyr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShowRecyclerViewAdapter extends RecyclerView.Adapter<MyShowRecyclerViewAdapter.ViewHolder> {

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

        pb = (ProgressBar) view.findViewById(R.id.list_progressbar);
        pb.setVisibility(View.GONE);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mItem = martyrList.get(position);
        //show id in the 3rd column
//        holder.mIdView.setText(Integer.toString(martyrList.get(poslition).getId()));
        //Use Span to color word شهید
        //TODO: madhi: check by breakpoint
        final SpannableStringBuilder sb = new SpannableStringBuilder(
                "شهید" + " " + martyrList.get(position).getFirst_name() + " "
                        + martyrList.get(position).getLast_name());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 0, 0));
        sb.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.mContentView.setText(sb);

        //Image
        Bitmap bitmap = imageCache.get(holder.mItem.getId());
        //Check if avatar already downloaded
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File dir = new File(MainActivity.basePath, "/Avatars/");
            dir.mkdirs();
            bitmap = BitmapFactory.decodeFile(dir +"/" + String.valueOf(holder.mItem.getId()));
            martyrList.get(position).setAvatar(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            holder.mImageView.setImageBitmap(holder.mItem.getAvatar());
        } else {
            MartyrAndView container = new MartyrAndView();
            container.martyr = holder.mItem;
            container.view = holder.mView;
            //Download and save Image
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
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected MartyrAndView doInBackground(MartyrAndView... params) {
            MartyrAndView container = params[0];
            Martyr martyr = container.martyr;
            // get avatar
            try {
                String imgeUrl = MainActivity.baseURl + "martyr/" + martyr.getId() + "/avatar/download";
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
            //Image load
            ImageView image = (ImageView) martyrAndView.view.findViewById(R.id.portrait);
            image.setImageBitmap(martyrAndView.bitmap);
//            martyrAndView.martyr.setBitmap(martyrAndView.bitmap);
            imageCache.put(martyrAndView.martyr.getId(), martyrAndView.bitmap);

            //save avatar
            String path = Environment.getExternalStorageDirectory().toString();
            File dir = new File(MainActivity.basePath, "/Avatars");
            dir.mkdirs();
            File avatar = new File(dir, String.valueOf(martyrAndView.martyr.getId()));
            try {
                FileOutputStream out = new FileOutputStream(avatar);
                martyrAndView.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            pb.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //show id in the 3rd column
//        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        //        public DummyItem mItem;
        public Martyr mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            //show id in the 3rd column
//            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.portrait);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
