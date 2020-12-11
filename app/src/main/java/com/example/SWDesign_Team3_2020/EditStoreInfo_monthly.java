
package com.example.SWDesign_Team3_2020;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.Dialog;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import java.util.Calendar;
import android.widget.TextView;


public class EditStoreInfo_monthly extends DialogFragment implements OnTimeSetListener {

    int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle margs = getArguments();
        String mValue = margs.getString("id");
        id = Integer.parseInt(mValue);

        //현재 시간을 타임 피커의 초기값으로 사용
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this,
                hour, minute, DateFormat.is24HourFormat(getContext()));

        return tpd;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv = (TextView) getActivity().findViewById(id);
        String aMpM = "AM";
        if(hourOfDay > 11){
            aMpM = "PM";
        }
        String currentHour;
        String currentMin;
        if(hourOfDay < 10){
            currentHour = '0'+String.valueOf(hourOfDay);
        }
        else{
            currentHour = String.valueOf(hourOfDay);
        }
        if(minute < 10){
            currentMin = '0'+ String.valueOf(minute);
        }
        else{
            currentMin = String.valueOf(minute);
        }
        tv.setText(currentHour + currentMin);
    }
}