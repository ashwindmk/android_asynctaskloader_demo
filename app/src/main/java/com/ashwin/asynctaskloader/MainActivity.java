package com.ashwin.asynctaskloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<String> {
    public static final String URL = "https://reqres.in/api/users?page=2";

    public static final int LOADER_ID = 22;
    public static final String URL_KEY = "url";

    private LoaderManager loaderManager;

    private Button loadButton;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderManager = LoaderManager.getInstance(this);

        loadButton = findViewById(R.id.load_button);
        contentTextView = findViewById(R.id.content_textview);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(URL_KEY, URL);

                Loader<String> loader = loaderManager.getLoader(LOADER_ID);
                if (loader == null) {
                    loaderManager.initLoader(LOADER_ID, args, MainActivity.this);
                } else {
                    loaderManager.restartLoader(LOADER_ID, args, MainActivity.this);
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        // thread: main
        Log.w(TAG, "onCreateLoader | thread: " + Thread.currentThread().getName() + " | id: " + id + " | args: " + args);

        // Problem: This will leak the Activity
        // Solution: Create a separate class extending AsyncTaskLoader<String>
        return new AsyncTaskLoader<String>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                // thread: main
                Log.w(TAG, "AsyncTaskLoader | onStartLoading | thread: " + Thread.currentThread().getName());
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                // thread: ModernAsyncTask #1
                Log.w(TAG, "AsyncTaskLoader | loadInBackground | thread: " + Thread.currentThread().getName());

                String url = args.getString(URL_KEY);
                if (url == null && "".equals(url)) {
                    return null;
                }
                String content = null;
                try {
                    content = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return content;
            }

            @Override
            public void deliverCancellation() {
                Log.w(TAG, "AsyncTaskLoader | deliverCancellation | thread: " + Thread.currentThread().getName());
                super.deliverCancellation();
            }

            @Override
            public void deliverResult(@Nullable String data) {
                // thread: main
                Log.w(TAG, "AsyncTaskLoader | deliverResult | thread: " + Thread.currentThread().getName());
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        // thread: main
        Log.w(TAG, "onLoadFinished | thread: " + Thread.currentThread().getName() + " | data: " + data);
        contentTextView.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // thread: main
        Log.w(TAG, "onLoaderReset | thread: " + Thread.currentThread().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Loader<String> loader = loaderManager.getLoader(LOADER_ID);
        boolean r = false;
        if (loader != null) {
            r = loader.cancelLoad();
        }
        Log.w(TAG, "loader.cancelLoad: " + r);*/

        /*if (loaderManager.hasRunningLoaders()) {
            loaderManager.destroyLoader(LOADER_ID);
            r = true;
        }
        Log.w(TAG, "loaderManager.destroyLoader: " + r);*/
    }
}