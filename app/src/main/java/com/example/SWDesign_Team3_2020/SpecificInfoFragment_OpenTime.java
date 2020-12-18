package com.example.SWDesign_Team3_2020;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecificInfoFragment_OpenTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificInfoFragment_OpenTime extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GlobalVar m_gvar = null;

    android.widget.TextView tvmonto1,tvmonto2,tvtueto1,tvtueto2,tvwedto1,tvwedto2,tvthuto1,tvthuto2,tvfrito1,tvfrito2,tvsatto1,tvsatto2,tvsunto1,tvsunto2;
    android.widget.TextView tvmonfrom1,tvmonfrom2,tvtuefrom1,tvtuefrom2,tvwedfrom1,tvwedfrom2,tvthufrom1,tvthufrom2,tvfrifrom1,tvfrifrom2,tvsatfrom1,tvsatfrom2,tvsunfrom1,tvsunfrom2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpecificInfoFragment_OpenTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecificInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificInfoFragment_OpenTime newInstance(String param1, String param2) {
        SpecificInfoFragment_OpenTime fragment = new SpecificInfoFragment_OpenTime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //globalVal
        m_gvar = (GlobalVar) getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_specific_info_opentime, container, false);

        tvmonto1 = (TextView) v.findViewById(R.id.TextView_Mon_StartHour);
        tvmonto2 = (TextView) v.findViewById(R.id.TextView_Mon_StartMin);
        tvtueto1 = (TextView) v.findViewById(R.id.TextView_Tue_StartHour);
        tvtueto2 = (TextView) v.findViewById(R.id.TextView_Tue_StartMin);
        tvwedto1 = (TextView) v.findViewById(R.id.TextView_Wed_StartHour);
        tvwedto2 = (TextView) v.findViewById(R.id.TextView_Wed_StartMin);
        tvthuto1 = (TextView) v.findViewById(R.id.TextView_Thu_StartHour);
        tvthuto2 = (TextView) v.findViewById(R.id.TextView_Thu_StartMin);
        tvfrito1 = (TextView) v.findViewById(R.id.TextView_Fri_StartHour);
        tvfrito2 = (TextView) v.findViewById(R.id.TextView_Fri_StartMin);
        tvsatto1 = (TextView) v.findViewById(R.id.TextView_Sat_StartHour);
        tvsatto2 = (TextView) v.findViewById(R.id.TextView_Sat_StartMin);
        tvsunto1 = (TextView) v.findViewById(R.id.TextView_Sun_StartHour);
        tvsunto2 = (TextView) v.findViewById(R.id.TextView_Sun_StartMin);

        tvmonfrom1 = (TextView) v.findViewById(R.id.TextView_Mon_EndHour);
        tvmonfrom2 = (TextView) v.findViewById(R.id.TextView_Mon_EndMin);
        tvtuefrom1 = (TextView) v.findViewById(R.id.TextView_Tue_EndHour);
        tvtuefrom2 = (TextView) v.findViewById(R.id.TextView_Tue_EndMin);
        tvwedfrom1 = (TextView) v.findViewById(R.id.TextView_Wed_EndHour);
        tvwedfrom2 = (TextView) v.findViewById(R.id.TextView_Wed_EndMin);
        tvthufrom1 = (TextView) v.findViewById(R.id.TextView_Thu_EndHour);
        tvthufrom2 = (TextView) v.findViewById(R.id.TextView_Thu_EndMin);
        tvfrifrom1 = (TextView) v.findViewById(R.id.TextView_Fri_EndHour);
        tvfrifrom2 = (TextView) v.findViewById(R.id.TextView_Fri_EndMin);
        tvsatfrom1 = (TextView) v.findViewById(R.id.TextView_Sat_EndHour);
        tvsatfrom2 = (TextView) v.findViewById(R.id.TextView_Sat_EndMin);
        tvsunfrom1 = (TextView) v.findViewById(R.id.TextView_Sun_EndHour);
        tvsunfrom2 = (TextView) v.findViewById(R.id.TextView_Sun_EndMin);

        showopenTime();
        // Inflate the layout for this fragment
        return v;

    }

    public void showopenTime(){
        String ot_temp="";
        int endflag=0;
        String ot=m_gvar.getselectedOpenTime();
        Log.i("well",ot);
        do {
            int idx1 = ot.indexOf(";");
            if (idx1 == -1 || endflag==1)
                break;
            if((idx1+1)<=ot.length())
                ot_temp = ot.substring(idx1+1);
            else
                endflag=1;
            Log.i("yeah",ot.substring(3, 5));
            switch(ot.substring(0, 3)){
                case "sun":

                    tvsunto1.setText(ot.substring(3, 5));
                    tvsunto2.setText(ot.substring(5, 7));
                    tvsunfrom1.setText(ot.substring(7, 9));
                    tvsunfrom2.setText(ot.substring(9, 11));
                    break;
                case "mon":
                    tvmonto1.setText(ot.substring(3, 5));
                    tvmonto2.setText(ot.substring(5, 7));
                    tvmonfrom1.setText(ot.substring(7, 9));
                    tvmonfrom2.setText(ot.substring(9, 11));
                    break;
                case "tue":
                    tvtueto1.setText(ot.substring(3, 5));
                    tvtueto2.setText(ot.substring(5, 7));
                    tvtuefrom1.setText(ot.substring(7, 9));
                    tvtuefrom2.setText(ot.substring(9, 11));
                    break;
                case "wed":
                    tvwedto1.setText(ot.substring(3, 5));
                    tvwedto2.setText(ot.substring(5, 7));
                    tvwedfrom1.setText(ot.substring(7, 9));
                    tvwedfrom2.setText(ot.substring(9, 11));
                    break;
                case "thu":
                    tvthuto1.setText(ot.substring(3, 5));
                    tvthuto2.setText(ot.substring(5, 7));
                    tvthufrom1.setText(ot.substring(7, 9));
                    tvthufrom2.setText(ot.substring(9, 11));
                    break;
                case "fri":
                    tvfrito1.setText(ot.substring(3, 5));
                    tvfrito2.setText(ot.substring(5, 7));
                    tvfrifrom1.setText(ot.substring(7, 9));
                    tvfrifrom2.setText(ot.substring(9, 11));
                    break;
                case "sat":
                    tvsatto1.setText(ot.substring(3, 5));
                    tvsatto2.setText(ot.substring(5, 7));
                    tvsatfrom1.setText(ot.substring(7, 9));
                    tvsatfrom2.setText(ot.substring(9, 11));
                    break;
            }
            ot=ot_temp;
        }while(true);
    }
}