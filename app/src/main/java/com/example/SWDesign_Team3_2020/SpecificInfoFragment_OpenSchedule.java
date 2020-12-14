package com.example.SWDesign_Team3_2020;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecificInfoFragment_OpenSchedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificInfoFragment_OpenSchedule extends Fragment implements DayViewDecorator {
    public ArrayList<CalendarDay> holidays = new ArrayList<>();

    private MaterialCalendarView calendarView;
    View v;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Object DayViewDecorator;

    public SpecificInfoFragment_OpenSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecificInfoFragment_OpenSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificInfoFragment_OpenSchedule newInstance(String param1, String param2) {
        SpecificInfoFragment_OpenSchedule fragment = new SpecificInfoFragment_OpenSchedule();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_specific_info__open_schedule, container, false);
        MaterialCalendarView materialCalendarView = v.findViewById(R.id.Calendarview_Calendar);


        int afterYear = CalendarDay.today().getYear();
        int afterMonth = CalendarDay.today().getMonth() + 1;
        if (afterMonth == 13) {
            afterMonth = 1;
            afterYear += 1;
        }
        int afterDay = CalendarDay.today().getDay();
        if (afterMonth == 2 && afterDay > 28) {
            if (afterYear % 4 == 0)
                afterDay = 29;
            else {
                afterDay = 28;
            }
        } else if ((afterMonth == 4 || afterMonth == 6 || afterMonth == 9 || afterMonth == 11) && afterDay == 31)
            afterDay = 30;

        materialCalendarView.state().edit()
                .setMinimumDate(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()))
                .setMaximumDate(CalendarDay.from(afterYear, afterMonth, afterDay))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setSelectionMode(com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_NONE);

        CalendarDay tmp = CalendarDay.from(2020,12,20);
        CalendarDay tmpP = CalendarDay.from(2020,12,23);
        holidays.add(tmpP);

        holidays.add(tmp);
       // materialCalendarView.addDecorator((DayViewDecorator) DayViewDecorator);
    materialCalendarView.setSelectedDate(tmp);
        materialCalendarView.setSelectedDate(tmpP);
        return v;
    }


    @Override
    public boolean shouldDecorate(com.prolificinteractive.materialcalendarview.CalendarDay day) {
        return false;
    }

    @Override
    public void decorate(com.prolificinteractive.materialcalendarview.DayViewFacade view) {

    }
}