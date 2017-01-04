/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.asynctaskloader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.asynctaskloader.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

// TODO (1) implement LoaderManager.LoaderCallbacks<String> on MainActivity (DONE)

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    // TODO (28) Remove the key for storing the search results JSON (DONE)
    // 필요없는 이유에 대해서

    // TODO (2) Create a constant int to uniquely identify your loader. Call it GITHUB_SEARCH_LOADER (DONE)
    private static final int GITHUB_SEARCH_LOADER = 22; //숫자는 개인의 취향!
    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
            mUrlDisplayTextView.setText(queryUrl);
            // TODO (26) Remove the code that retrieves the JSON (DONE)
            // TODO (25) Remove the code that displays the JSON (DONE)
        }

        // TODO (24) Initialize the loader with GITHUB_SEARCH_LOADER as the ID, null for the bundle, and this for the context(DONE)
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally request that an AsyncTaskLoader performs the GET request.
     */
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();

        // TODO (17) If no search was entered, indicate that there isn't anything to search for and return (DONE)
        if(TextUtils.isEmpty(githubQuery)){
            mUrlDisplayTextView.setText("NO QUERY");
            return;
        }

        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        // TODO (18) Remove the call to execute the AsyncTask (DONE)
        //new GithubQueryTask().execute(githubSearchUrl);

        // TODO (19) Create a bundle called queryBundle (DONE)
        Bundle queryBundle = new Bundle();
        // TODO (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value(DONE)
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString());
        // TODO (21) Call getSupportLoaderManager and store it in a LoaderManager variable (DONE)
        LoaderManager loaderManager = getSupportLoaderManager();
        // TODO (22) Get our Loader by calling getLoader and passing the ID we specified (DONE)
        Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        // TODO (23) If the Loader was null, initialize it. Else, restart it. (DONE)
        if(githubSearchLoader == null){
            loaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }

        //* if we should restart the loader (if the loader already existed) or
        //* if we need to initialize the loader (if the loader did NOT already exist).

    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Main Page")
//                // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
     //   AppIndex.AppIndexApi.end(client, getIndexApiAction());
     //   client.disconnect();
    }

    // TODO (3) Override onCreateLoader(DONE)
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {  //final => for inner class
        // Within onCreateLoader
        // TODO (4) Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter (DONE)
        return new AsyncTaskLoader<String>(this) {

            // TODO (5) Override onStartLoading (DONE)
            @Override
            public void onStartLoading(){
                // Within onStartLoading
                // TODO (6) If args is null, return. (DONE)
                // TODO (7) Show the loading indicator (DONE)
                // TODO (8) Force a load (DONE)
                // END - onStartLoading

                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if(args == null){ //파라메터가 없으면 그냥 return;
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
                /**
                 * Force an asynchronous load. Unlike {@link #startLoading()} this will ignore a previously
                 * loaded data set and load a new one.  This simply calls through to the
                 * implementation's {@link #onForceLoad()}.  You generally should only call this
                 * when the loader is started -- that is, {@link #isStarted()} returns true.
                 *
                 * <p>Must be called from the process's main thread.
                 */
            }

            // TODO (9) Override loadInBackground (DONE)
            @Override
            public String loadInBackground(){

                // Within loadInBackground
                // TODO (10) Get the String for our URL from the bundle passed to onCreateLoader (DONE)
                // TODO (11) If the URL is null or empty, return null (DONE)
                // TODO (12) Copy the try / catch block from the AsyncTask's doInBackground method (DONE)
                // END - loadInBackground

                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);

                if(searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)){
                    return null;
                }

                try {
                    URL githubURL = new URL(searchQueryUrlString);
                    String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubURL);
                    return githubSearchResults;
                } catch(IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
    // TODO (13) Override onLoadFinished (DONE)
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        // Within onLoadFinished
        // TODO (14) Hide the loading indicator (DONE)
        // TODO (15) Use the same logic used in onPostExecute to show the data or the error message (DONE)
        // END - onLoadFinished
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data == null) {
            showErrorMessage();
        } else {
            mSearchResultsTextView.setText(data);
            showJsonDataView();
        }
    }

    // TODO (16) Override onLoaderReset as it is part of the interface we implement, but don't do anything in this method (DONE)
    @Override
    public void onLoaderReset(Loader<String> loader) {
        //DO NOTHING
    }


    // TODO (29) Delete the AsyncTask class (DONE)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);

        // TODO (27) Remove the code that persists the JSON (DONE)
    }
}