package ir.upcoders.sheydaee;

/**
 * Created by mahdi on 10/25/16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.up_coders.astan.R;

public class CustomListFragment extends Fragment {

    final String[] itemname = {};

    Integer[] itemicon = {};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view =  inflater.inflate(R.layout.list_fragment,null);

        /**
         *Set an Apater for the View Pager
         */
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView list = (ListView) getActivity().findViewById(R.id.myList);
        CustomList listAdapter = new CustomList(getActivity(), itemname, itemicon);


        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }



}