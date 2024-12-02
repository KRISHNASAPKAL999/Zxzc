package com.example.blood_donation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Viewpageradapter1 extends PagerAdapter {
    Context context;
    int[] images =
            {
                    R.drawable.doctor,
                    R.drawable.app_logo,
                    R.drawable.doctor,
                    R.drawable.app_logo,
                    R.drawable.doctor
            };
    int text[]= {
            R.string.slogan1,
            R.string.slogan2,
            R.string.slogan3,
            R.string.slogon4,
            R.string.slogan5
    };
    public Viewpageradapter1(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
       return  view == (LinearLayout) object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout for each page
        View view = inflater.inflate(R.layout.slider_layout1, container, false);

        // Find the ImageView and TextView by ID
        ImageView add1 = view.findViewById(R.id.slidingadd);
        TextView slogan = view.findViewById(R.id.sliddingtext);

        // Set the image and text
        add1.setImageResource(images[position]);
        slogan.setText(text[position]);

        // Add the view to the container
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
