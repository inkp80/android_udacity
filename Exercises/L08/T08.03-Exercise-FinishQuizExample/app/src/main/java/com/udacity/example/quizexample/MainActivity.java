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

package com.udacity.example.quizexample;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.example.droidtermsprovider.DroidTermsExampleContract;

/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */
//개선사항 - Loader, 참조되는 데이터의 변화가 일어날 경우 즉각 반영, 처음으로 돌아가면 Toast 출력
public class MainActivity extends AppCompatActivity {

    Context Main = this;
    // The data from the DroidTermsExample content provider
    private Cursor mData;

    // The current state of the app
    private int mCurrentState;

    private Button mButton;

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private final int STATE_HIDDEN = 0;

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private final int STATE_SHOWN = 1;

    private TextView mDefView;
    private  TextView mWordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the views
        mWordView = (TextView) findViewById(R.id.text_view_word);
        mDefView = (TextView) findViewById(R.id.text_view_definition);
        mButton = (Button) findViewById(R.id.button_next);

        //Run the database operation to get the cursor off of the main thread
        new WordFetchTask().execute();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mData.close();
    }

    /**
     * This is called from the layout when the button is clicked and switches between the
     * two app states.
     * @param view The view that was clicked
     */
    public void onButtonClick(View view) {

        // Either show the definition of the current word, or if the definition is currently
        // showing, move to the next word.
        switch (mCurrentState) {
            case STATE_HIDDEN:
                showDefinition();
                break;
            case STATE_SHOWN:
                nextWord();
                break;
        }
    }

    public void nextWord() {
        if(mData == null){
            //DO NOTHING just return
            return;
        }
        //데이터가 없으면 여기까지는 온다.
        // Change button text
        mButton.setText(getString(R.string.show_definition));

        //데이터 없으면 여기서 문제 생김
        Log.d("Debug*****","Breaking Point");
        mWordView.setText(mData.getString(mData.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD)));
        mDefView.setVisibility(View.INVISIBLE);
        mDefView.setText(mData.getString(mData.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION)));

        if(!mData.moveToNext()){
            mData.moveToFirst();
            Toast.makeText(Main, "End of Test, Move to first", Toast.LENGTH_SHORT); //여긴 또 왜 안되니..
        }//why Toast doesn't work?


        // Note that you shouldn't try to do this if the cursor hasn't been set yet.
        // If you reach the end of the list of words, you should start at the beginning again.
        mCurrentState = STATE_HIDDEN;

    }

    public void showDefinition() {

        // Change button text
        mButton.setText(getString(R.string.next_word));
        mDefView.setVisibility(View.VISIBLE);
        mCurrentState = STATE_SHOWN;

    }

    // Use an async task to do the data fetch off of the main thread.
    public class WordFetchTask extends AsyncTask<Void, Void, Cursor> {

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            // Make the query to get the data

            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            String[] projection = new String[] {DroidTermsExampleContract.COLUMN_WORD, DroidTermsExampleContract.COLUMN_DEFINITION};
            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(DroidTermsExampleContract.CONTENT_URI,
                    projection, null, null, null);
            //커서가 긁어온 결과에 아무 것도 없으면 처리를 어떻게 해야하나
            if(cursor.getCount() == 0 ){
                Log.d("debug****", "No values");
                return null;
            }
            return cursor;
        }


        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor == null){
                Log.d("debug*****", "no data onpostEXEC");
                Toast.makeText(Main, "NO DATA", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(Main, "LOADED", Toast.LENGTH_LONG).show();
            // Set the data for MainActivity
            mData = cursor;
            mData.moveToFirst();

            nextWord();
        }
    }

}
