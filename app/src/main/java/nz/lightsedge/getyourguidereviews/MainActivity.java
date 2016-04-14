/*
 * Copyright (C) 2016 Kryspin Arnold
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
package nz.lightsedge.getyourguidereviews;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import nz.lightsedge.getyourguidereviews.model.ReviewDataModel;
import nz.lightsedge.getyourguidereviews.model.ReviewModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<ReviewModel> mReviews;
    private ReviewAdapter mReviewAdapter;

    @Inject
    ReviewService mReviewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ReviewDataModel reviewData = new ReviewDataModel();
        mReviews = reviewData.getData();

        // Create the recycler view and adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mReviewAdapter = new ReviewAdapter(this, mReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mReviewAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MainApp.getComponent(this).inject(this);

        Log.i(TAG, "TEST");

        mReviewService.getReviewData(new Callback<ReviewDataModel>() {
            @Override
            public void success(ReviewDataModel reviewDataModel, Response response) {

                Log.i(TAG, response.toString());
                Log.i(TAG, reviewDataModel.toString());

                showData(reviewDataModel);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getUrl());
            }
        });
    }

    private void showData(ReviewDataModel reviewData) {

        if (reviewData != null && reviewData.getData() != null) {
            Log.i(TAG, reviewData.getData().get(0).getMessage());

            mReviews.clear();
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            mReviews.addAll(reviewData.getData());
            Log.i(TAG, "Reviews Size: " + mReviews.size());
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
