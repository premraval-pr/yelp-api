package com.example.yelpapi;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YelpAPI {
    public static final String YELP_API_URL = "https://api.yelp.com/v3";
    public static final String AUTO_COMPLETE_TEXT_URL = "/autocomplete";
    public static final String BUSINESS_URL = "/businesses";
    public static final String API_KEY = "QKgK2brMUWR4nNz-qU3TohIdTw6-8jtbJGF6wBGqG04FjUuldXlK27vAJafPaLMXIHtA33fsW5x8rM2ERJ14XG1ZaCKvhiigZMBz3MFfMI7Sdy6qXpMR0lc_qbL2XnYx";
    public static final String[] DAYS = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
    Context context;
    public YelpAdapter yelpAdapter;
    MapsInterface mapsInterface;
    public YelpAPI(Context context, YelpAdapter yelpAdapter,MapsInterface mapsInterface){
        this.context = context;
        this.yelpAdapter = yelpAdapter;
        this.mapsInterface = mapsInterface;
    }

    public YelpAPI(Context context){
        this.context = context;
    }

    public void getSuggestedPlaces(String text_to_search, double lat, double lon){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = YELP_API_URL +
                AUTO_COMPLETE_TEXT_URL +
                "?text=" + text_to_search +
                "&latitude=" + lat +
                "&longitude=" + lon;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        yelpAdapter.clearData();
                        try {
                            Log.d("mssg", "onResponse: " + response.toString());
                            JSONArray businessFound = response.getJSONArray("businesses");
                            for(int i =0;i<businessFound.length();i++){
                                getBusinessInformation(businessFound.getJSONObject(i).getString("id"),i);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mssg", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","Bearer " + API_KEY);

                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }


    public void getBusinessInformation(String id, final int position){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = YELP_API_URL +
                BUSINESS_URL + "/" + id;
        final Place business = new Place();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    List<String> catList = new ArrayList<>();
                    List<Place.Hours> hoursList = new ArrayList<>();
                    String price="";
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("business", "onResponse: " + response.toString());
                            JSONArray categories = response.getJSONArray("categories");

                            if(response.has("price")){
                                price = response.getString("price");
                            }

                            for (int i = 0; i < categories.length(); i++) {
                                catList.add(categories.getJSONObject(i).getString("title"));
                            }
                            if (response.has("hours")){
                                JSONArray hours = response.getJSONArray("hours").getJSONObject(0).getJSONArray("open");
                                DateFormat timeFormat = new SimpleDateFormat("hhmm");
                                for (int i = 0; i < hours.length(); i++) {
                                    JSONObject hour = hours.getJSONObject(i);
                                    hoursList.add(new Place.Hours(DAYS[hour.getInt("day")],
                                            timeFormat.parse(hour.getString("start")),
                                            timeFormat.parse(hour.getString("end"))));
                                }
                            }

                            business.setLatitude(response.getJSONObject("coordinates").getDouble("latitude"));
                            business.setLongitude(response.getJSONObject("coordinates").getDouble("longitude"));
                            business.setId(response.getString("id"));
                            business.setName(response.getString("name"));
                            business.setPhone(response.getString("display_phone"));
                            business.setImage_url(response.getString("image_url"));
                            business.setWeb_url(response.getString("url"));
                            business.setPrice(price);
                            Log.d("PRICE FOUND", "onResponse: " + price + response.getString("name"));
                            business.setCat(catList);
                            business.setPlaceHours(hoursList);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        yelpAdapter.addData(business);
                        yelpAdapter.notifyDataSetChanged();
                        mapsInterface.updateMap();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("business", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","Bearer " + API_KEY);

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
