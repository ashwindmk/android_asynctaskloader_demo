package com.ashwin.asynctaskloader;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = "asynctask-loader";
    protected String activity = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate: " + activity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(TAG, "onRestart: " + activity);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.w(TAG, "onRestoreInstanceState: " + activity + " | bundle: " + savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart: " + activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: " + activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: " + activity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: " + activity);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.w(TAG, "onSaveInstanceState: " + activity);
        outState.putInt("count", 1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: " + activity);
    }
}
