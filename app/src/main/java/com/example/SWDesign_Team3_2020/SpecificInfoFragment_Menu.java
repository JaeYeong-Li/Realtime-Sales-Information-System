package com.example.SWDesign_Team3_2020;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecificInfoFragment_Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificInfoFragment_Menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GlobalVar m_gvar = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView menuView;

    public SpecificInfoFragment_Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecificInfoFragment_Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificInfoFragment_Menu newInstance(String param1, String param2) {
        SpecificInfoFragment_Menu fragment = new SpecificInfoFragment_Menu();
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
        View v = inflater.inflate(R.layout.fragment_specific_info__menu, container, false);
        menuView = (TextView) v.findViewById(R.id.TextView_Menu);
        menuView.setText(m_gvar.getselectedMenu());
        // Inflate the layout for this fragment
        return v;
    }
}