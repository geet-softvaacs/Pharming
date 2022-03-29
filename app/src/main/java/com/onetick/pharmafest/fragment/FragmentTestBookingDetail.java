package com.onetick.pharmafest.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onetick.pharmafest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTestBookingDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTestBookingDetail extends Fragment {

    String heading = "";
    String description = "";
    TextView textView;
    View view;

    public FragmentTestBookingDetail() {
        // Required empty public constructor
    }


    public static FragmentTestBookingDetail newInstance(String heading, String description) {
        FragmentTestBookingDetail fragment = new FragmentTestBookingDetail();
        Bundle args = new Bundle();
        args.putString("heading", heading);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString("description");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_fragmen_test_booking_detail, container, false);
        textView= view.findViewById(R.id.tv_overview_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(description!=null)
        {
            textView.setText(Html.fromHtml(description));
        }
    }
}