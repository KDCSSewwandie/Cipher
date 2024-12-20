package com.home.cipher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    public static TabLayout tabLayout;
    private ViewPager viewPager;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setCommon();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(common.server);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Ciph"));
        tabLayout.addTab(tabLayout.newTab().setText("Meet"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = findViewById(R.id.viewpager);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOnPageChangeListener(this);

        loadImage();

    }

    private void loadImage() {
        if (common.snapshot.child("user/" + common.rDBEmail + "/img").exists()) {
            CircleImageView img = findViewById(R.id.user_image);

            Handler handler = new Handler(Looper.getMainLooper());

            try {
                img.setImageBitmap(common.ImageBitmap.get(common.ImageID.indexOf(common.rDBEmail)));
            }catch (Exception e){
                Runnable delayRunnable = this::loadImage;
                handler.postDelayed(delayRunnable, 5000);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", null)
                .show();
    }

    public void setCommon() {
        SharedVariable sharedVariable = new SharedVariable(this);
        common.rDBEmail = sharedVariable.getDBEmail();
        common.server = sharedVariable.getServer();
    }

    public void option(View view) {
        common.edit = false;
        PopupAddMenu popupAddMenu = new PopupAddMenu(getLayoutInflater(), getWindow());

        // Set click listener for the OK button
        popupAddMenu.addTask.setOnClickListener(v ->
        {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, AddTask.class));
        });

        // Set click listener for the OK button
        popupAddMenu.addMeet.setOnClickListener(v ->
        {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, AddMeet.class));
        });

        // Set click listener for the OK button
        popupAddMenu.addGroup.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, AddGroup.class));
        });

        // Set click listener for the OK button
        popupAddMenu.leaveGroup.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, LeaveGroup.class));
        });

        // Set click listener for the OK button
        popupAddMenu.createGroup.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, CreateGroup.class));
        });

        // Set click listener for the OK button
        popupAddMenu.changeName.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, Setting.class));
        });

        popupAddMenu.howToUse.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, HowToUse.class));
        });

        popupAddMenu.about.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, About.class));
        });

        popupAddMenu.feedback.setOnClickListener(v -> {
            popupAddMenu.popupWindow.dismiss();
            startActivity(new Intent(this, Feedback.class));
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @SuppressLint("ResourceType")
    @Override
    public void onPageScrollStateChanged(int state) {
        int currentPage = viewPager.getCurrentItem();
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        if (currentPage != selectedTabPosition) {
            TabLayout.Tab desiredTab = tabLayout.getTabAt(currentPage); // Example: changing to the third tab
            if (desiredTab != null) {
                desiredTab.select();
            }
        }
    }

    public void refresh(View view) {
        startActivity(new Intent(this, Home.class));
        finish();
    }
}