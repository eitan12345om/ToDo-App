package com.simlerentertainment.todoapp;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * @author Eitan created on 3/30/2017.
 */

public class MyAdapter<T> extends ArrayAdapter<T> {

    // Constructors
    public MyAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null; // Change
    }
}
