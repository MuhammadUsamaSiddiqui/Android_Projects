package com.example.muhammadusama.weather;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muhammad Usama on 5/10/2018.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private int mColorResourceId;

    public WeatherAdapter(Activity context, ArrayList<Weather> words, int colorResourceId) {

        super(context, 0, words);
        mColorResourceId = colorResourceId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Weather currentWeather = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.english);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        defaultTextView.setText(currentWeather.getmDefaultTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        miwokTextView.setText(currentWeather.getmMiwokTranslation());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if(currentWeather.hasImage()) {
            imageView.setImageResource(currentWeather.getmImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        }
        else {

            imageView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(),mColorResourceId);
        textContainer .setBackgroundColor(color);
        return listItemView;
    }

}
