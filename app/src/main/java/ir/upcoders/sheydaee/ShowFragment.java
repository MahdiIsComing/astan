package ir.upcoders.sheydaee;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.up_coders.astan.R;

import java.util.ArrayList;
import java.util.List;

import ir.upcoders.sheydaee.db.DbHandler;
import ir.upcoders.sheydaee.model.Martyr;
import ir.upcoders.sheydaee.parser.MartyrJSONParser;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ShowFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private List<Martyr> martyrList;

    DbHandler db;

    List<AsTasks> tasks;

    ProgressBar pr;
    Activity ac = getActivity();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowFragment() {
    }

    onMartyrSelectedListener mCallback;

    public interface onMartyrSelectedListener {
        public void onMartyrSelected(int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onMartyrSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ShowFragment newInstance(int columnCount) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        //TODO: mahdi: decide about how much rely to put on local and how much on remote
        //check if sth is stored in DB
        db = new DbHandler(getActivity());

        if (db.getMartyrsCount() > 0) {
            this.martyrList = db.getAllMartyrs();
            if (MainActivity.isOnline(this.getContext())) {
                tasks = new ArrayList<>();
                requestData(MainActivity.baseURl + "martyr/find");
            }
        } else {
            if (MainActivity.isOnline(this.getContext())) {
                //request data from server
                tasks = new ArrayList<>();
                requestData(MainActivity.baseURl + "martyr/find");
            } else {
                Activity mainActivity = getActivity();
                Toast.makeText(mainActivity, "Network connection is required.", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show_list, container, false);

        //pr = (ProgressBar) view.findViewById(R.id.ShowFragmentPb);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new MyShowRecyclerViewAdapter(DummyContent.ITEMS, mListener));
            recyclerView.setAdapter(new MyShowRecyclerViewAdapter(martyrList, mListener));

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {


                            //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            mCallback.onMartyrSelected(martyrList.get(position).getId());

                        }
                    })
            );
        }
        updateDisplay();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onListFragmentInteraction(DummyItem item);

        void onListFragmentInteraction(Martyr item);
    }

    private void requestData(String uri) {
        AsTasks task = new AsTasks();
        task.execute(uri);
    }

    private void updateDisplay() {
        recyclerView.setAdapter(new MyShowRecyclerViewAdapter(martyrList, mListener));
        MainActivity activity = (MainActivity) getActivity();
        activity.setMartyrList(martyrList);
    }

    private class AsTasks extends AsyncTask<String, String, List<Martyr>> {
        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                //progressbar
//                pr.setVisibility(View.VISIBLE);

            }
            tasks.add(this);
        }

        @Override
        protected List<Martyr> doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            martyrList = MartyrJSONParser.parseFeed(content);
            return martyrList;
        }

        @Override
        protected void onPostExecute(List<Martyr> result) {
            tasks.remove(this);

            if (tasks.size() == 0) {
                //pro
                //pr.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                //Toast.makeText(, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            //add to database
            for (Martyr martyr : martyrList) {
                db.addMartyr(martyr);

            }
            martyrList = result;
            updateDisplay();

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

    }

}


