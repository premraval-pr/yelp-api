package com.example.yelpapi;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class YelpAdapter extends ArrayAdapter<Place> implements Filterable {

    private List<Place> mListData;

    public YelpAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mListData = new ArrayList<>();
    }

    public void addData(Place list){
        mListData.add(list);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Nullable
    @Override
    public Place getItem(int position) {
        return mListData.get(position);
    }

    public void clearData() {
        mListData.clear();
    }

    public List<Place> getmListData(){
        return mListData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Place getItemById(final String id){
        return mListData.stream()
                .filter(new Predicate<Place>() {
                    @Override
                    public boolean test(Place place) {
                        return place.getId().equals(id);
                    }
                })
                .findAny()
                .orElse(null);
    }
}
