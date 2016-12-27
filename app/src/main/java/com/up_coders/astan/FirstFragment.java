package com.up_coders.astan;

/**
 * Created by mahdi on 10/25/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.up_coders.astan.calendar.JalaliCalendar;
import com.up_coders.astan.model.Martyr;


public class FirstFragment extends Fragment {

    Martyr martyr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_layout_first, null);
        MainActivity activity = (MainActivity) getActivity();
        Integer MartyrID = activity.getMartyrID() - 1;

        martyr = activity.getMartyr().get(MartyrID);

        ImageView avatar = (ImageView) view.findViewById(R.id.first_tab_avatar);
        ImageView backImage = (ImageView) view.findViewById(R.id.backPick);

        avatar.setImageBitmap(martyr.getAvatar());
        backImage.setImageBitmap(martyr.getAvatar());

        TextView martyrName = (TextView) view.findViewById(R.id.first_tab_name);
        TextView martyrBirth = (TextView) view.findViewById(R.id.first_tab_birth);
        TextView martyrMartyrdom = (TextView) view.findViewById(R.id.first_tab_martyrdom);

        //TODO: mahdi: convert all textview with number contents to PersianTextView
//        PersianTextView ptv = (PersianTextView) view.findViewById(R.id.first_tab_ptv);
//        ptv.setText(convertToJalali(martyr.getBirth_date()));

        martyrName.setText("شهید" + " " + martyr.getFirst_name() + " " + martyr.getLast_name());
        martyrBirth.setText(convertToJalali(martyr.getBirth_date()) + "،  " + martyr.getBirth_place());


        martyrMartyrdom.setText(convertToJalali(martyr.getMartyrdom_date()) + "،  " + martyr.getMartyrdom_place());

        if (martyr.getMission() != null){
            TextView martyrMission = (TextView) view.findViewById(R.id.first_tab_mission);
            martyrMission.setText(martyr.getMission());
        }
        return view;
    }

    public String convertToJalali(String datetime){
        datetime = datetime.split(" ")[0];
        int year = Integer.parseInt(datetime.split("-")[0]);
        int month = Integer.parseInt(datetime.split("-")[1]);
        int day = Integer.parseInt(datetime.split("-")[2]);

        JalaliCalendar.YearMonthDate yearMonthDate = new JalaliCalendar.YearMonthDate(year, month, day);

        yearMonthDate = JalaliCalendar.gregorianToJalali(yearMonthDate);
        return yearMonthDate.toString();
    }

    public void getResult(String result) {
        String RRR = result;
    }
}
