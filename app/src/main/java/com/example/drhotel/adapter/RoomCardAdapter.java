package com.example.drhotel.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.drhotel.LoginActivity;
import com.example.drhotel.MainActivity2;
import com.example.drhotel.TimeActivity;
import com.example.drhotel.model.Room;
import com.example.drhotel.R;
import com.google.android.material.snackbar.Snackbar;
import com.example.drhotel.FragmentHome;

import java.util.List;

public class RoomCardAdapter extends ArrayAdapter<Room> {
    List<Room> items_list;
    int custom_layout_id;
    public RoomCardAdapter(@NonNull Activity context, int resource, @NonNull List<Room> objects) {
        super(context, resource, objects);
        items_list = objects;
        custom_layout_id = resource;
    }
    @Override public int getCount() {
        return items_list.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_layout, null);
        }
//        ImageView imageView = v.findViewById(R.id.imageView);
        TextView btnRoom = v.findViewById(R.id.roomView);
        // get the item using the position param
        Room item = items_list.get(position);
        btnRoom.setBackgroundResource(item.getStatus().equals("0")?R.color.colorAccent:R.color.error);

        btnRoom.setText(item.getNameRoom());
        return v;
    }


}
