package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = "DetailActivityFragment";
    private String weatherData;

    public static DetailActivityFragment newInstance(String weatherData) {
        DetailActivityFragment myFragment = new DetailActivityFragment();

        Bundle args = new Bundle();
        args.putString("weatherData", weatherData);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherData = getArguments().getString("weatherData", "Error - no weather data found");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView textViewDetail = (TextView) rootView.findViewById(R.id.textview_detail);
        textViewDetail.setText(weatherData);
        return rootView;
    }
}
