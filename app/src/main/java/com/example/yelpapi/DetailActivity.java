package com.example.yelpapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity{

    Place receivedPLace;
    YelpAPI yelpAPI;
    TextView tv_name,tv_phone,tv_url,tv_price,tv_cat,tv_hours;
    ImageView im_business;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_name = findViewById(R.id.bs_name);
        tv_phone = findViewById(R.id.bs_phone);
        tv_phone = findViewById(R.id.bs_phone);
        tv_url = findViewById(R.id.bs_web);
        tv_price = findViewById(R.id.bs_price);
        tv_cat = findViewById(R.id.bs_cat);
        tv_hours = findViewById(R.id.bs_hours);
        im_business = findViewById(R.id.image_business);

        tv_url.setMovementMethod(new ScrollingMovementMethod());

        yelpAPI = new YelpAPI(this);
        receivedPLace = getIntent().getParcelableExtra("BUSINESS");
        tv_name.setText(receivedPLace.getName());
        tv_phone.setText(receivedPLace.getPhone());
        tv_url.setText(receivedPLace.getWeb_url());
        if(!receivedPLace.getPrice().isEmpty()) tv_price.setText(receivedPLace.getPrice());
        tv_cat.setText(receivedPLace.getCat().toString());
        if(!receivedPLace.getImage_url().isEmpty()) Picasso.get().load(receivedPLace.getImage_url()).into(im_business);

        Log.d("Object Print", "onCreate: " + receivedPLace.objectPrint());
        if(!receivedPLace.getPlaceHours().isEmpty()){
            StringBuilder output = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            for (Place.Hours hours:receivedPLace.getPlaceHours()){
                output.append(hours.getDay())
                        .append(": ")
                        .append(dateFormat.format(hours.getStart_time()))
                        .append("-")
                        .append(dateFormat.format(hours.getEnd_time()))
                        .append("\n");
            }
            tv_hours.setText(output);
        }
    }

}