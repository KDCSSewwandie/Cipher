package com.home.cipher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentDone extends Fragment implements SharedVariable.DataSnapshotListener {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    //My attribute
    private LinearLayout parentLayout;
    private static LayoutInflater inflater;
    private final ArrayList<String> dataKey = new ArrayList<>();

    public FragmentDone() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);
        common.startLoading(context, "Loading History");
        parentLayout = view.findViewById(R.id.done_view);
        FragmentDone.inflater = inflater;

        if(!common.tips){
            TextView tipsBox = view.findViewById(R.id.tips);
            tipsBox.setVisibility(View.GONE);
        }

        loadData();

        return view;
    }

    private void loadData() {
        ArrayList<String> dataName = new ArrayList<>();
        ArrayList<String> dataStatus = new ArrayList<>();

        DataSnapshot dataSnapshot = common.snapshot.child("task");
        dataKey.clear();
        clearViewChild();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            // Get the value of each child in the node
            for (DataSnapshot groupID :snapshot.child("groups").getChildren()){
                if(String.valueOf(groupID.child(common.rDBEmail).getValue()).equals("done") || String.valueOf(groupID.child(common.rDBEmail).getValue()).equals("late")){
                    dataName.add(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    dataKey.add(snapshot.getKey());
                    dataStatus.add(String.valueOf(groupID.child(common.rDBEmail).getValue()));
                    break;//stop groupID
                }
            }
        }
        for (int i = 0; i < dataKey.size(); i++) {
            try {
                addData(dataKey.get(i), dataName.get(i), i, dataStatus.get(i));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        common.stopLoading();
    }

    private void addData(String key, String name, int id, String dateState) throws ParseException {
        // Inflate the custom layout XML file for the clickable element
        View clickableElement = inflater.inflate(R.layout.one_task, parentLayout, false);

        // Find the image view and text view in the clickable element
        TextView nameTextView = clickableElement.findViewById(R.id.task_name);
        TextView dayTextView = clickableElement.findViewById(R.id.task_date);
        TextView timeTextView = clickableElement.findViewById(R.id.task_time);
        String[] dayTime = common.convertToDayTime(key);
        LinearLayout clickMe = clickableElement.findViewById(R.id.click_me);
        // Set the image resource ID and person's name for the clickable element
        nameTextView.setText(name);
        nameTextView.setId(id);
        dayTextView.setText(dayTime[0]);
        timeTextView.setText(dayTime[1]);

        switch (dateState) {
            case "done":
                clickableElement.setBackgroundColor(Color.argb(60, 0, 255, 0));
                break;
            case "late":
                clickableElement.setBackgroundColor(Color.argb(60, 255, 0, 0));
                break;
            default:
                clickableElement.setBackgroundColor(Color.argb(60, 255, 255, 255));
                break;
        }

        // Set an onClickListener for the clickable element
        clickMe.setOnClickListener(v -> {
            // Handle the click event here
            int dataID = nameTextView.getId();
            common.dataID = dataKey.get(dataID);
            common.fragmentNumber = 3;
            context.startActivity(new Intent(context, CiphData.class));
        });

        // Add the clickable element to the parent layout
        parentLayout.addView(clickableElement);
    }

    private void clearViewChild() {
        // Remove all views from the parent layout
        parentLayout.removeAllViews();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentDone.context = context;
    }

    @Override
    public void onDataSnapshotChanged(String dataSnapshot) {
        startActivity(new Intent(context, Home.class));
    }
}