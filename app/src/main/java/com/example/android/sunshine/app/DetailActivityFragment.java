package com.example.android.sunshine.app;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import static com.example.android.sunshine.app.ForecastFragment.COL_WEATHER_DATE;
import static com.example.android.sunshine.app.ForecastFragment.COL_WEATHER_DESC;
import static com.example.android.sunshine.app.ForecastFragment.COL_WEATHER_MAX_TEMP;
import static com.example.android.sunshine.app.ForecastFragment.COL_WEATHER_MIN_TEMP;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "DetailActivityFragment";
    private static final int DETAIL_LOADER = 1;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView textViewDetail = (TextView) rootView.findViewById(R.id.textview_detail);
        if (weatherData != null) {
            textViewDetail.setText(weatherData);
        }
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String locationSetting = Utility.getPreferredLocation(getActivity());
        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                ForecastFragment.FORECAST_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dateString = Utility.formatDate(
                data.getLong(COL_WEATHER_DATE));

        String weatherDescription =
                data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(
                data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(
                data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        weatherData = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView)getView().findViewById(R.id.textview_detail);
        detailTextView.setText(weatherData);

        if (getActivity() instanceof DetailActivity) {
            ((DetailActivity) getActivity()).updateShareIntent(weatherData);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
