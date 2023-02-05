package com.example.drhotel;
import com.example.drhotel.adapter.RoomCardAdapter;
import com.example.drhotel.model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private static final String TAG = "MainActivity";
    private RoomCardAdapter customAdapter;
    private List<Room> itemsList = new ArrayList<>();
    public FragmentHome() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        GridView gridView = view.findViewById(R.id.grid_view);
        customAdapter = new RoomCardAdapter(getActivity(), R.layout.grid_layout, itemsList);
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Room item = itemsList.get(i);
                Log.d("TT",item.getNameRoom());
                if(item.getStatus().equals("0")) {
                    Intent intent = new Intent(getActivity(), TimeActivity.class);
                    intent.putExtra("nameRoomString", "Farm " + item.getNameRoom());
                    intent.putExtra("nameRoom",item.getNameRoom());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Farm đã được đặt",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        getData();
        return view;
    }

    private void getData() {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://drhotle-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("room");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Room room = dataSnapshot.getValue(Room.class);
                    itemsList.add(room);
                }
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
