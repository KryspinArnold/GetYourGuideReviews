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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import nz.lightsedge.getyourguidereviews.enums.ErrorEnum;
import nz.lightsedge.getyourguidereviews.enums.IntentCode;
import nz.lightsedge.getyourguidereviews.enums.IntentExtra;
import nz.lightsedge.getyourguidereviews.model.ReviewDataModel;
import nz.lightsedge.getyourguidereviews.model.ReviewModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ReviewDataModel mReviewData;
    private List<ReviewModel> mReviews;
    private ReviewAdapter mReviewAdapter;
    private ProgressBar mProgressBar;

    @Inject
    @Named("realService")
    ReviewService mReviewService;

    @Inject
    @Named("mockService")
    ReviewService mMockService;

    @Inject
    MainApp mApp;

    @Inject
    ErrorHandler mErrorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mReviewData = new ReviewDataModel();
        mReviews = mReviewData.getData();

        // Create the recycler view and adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mReviewAdapter = new ReviewAdapter(mReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mReviewAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewReviewActivity();
            }
        });

        MainApp.getComponent(this).inject(this);

        mReviewService.getReviewData(new Callback<ReviewDataModel>() {
            @Override
            public void success(ReviewDataModel reviewDataModel, Response response) {

                Log.d(TAG, "Data Downloaded Successfully");
                mProgressBar.setVisibility(View.GONE);
                showData(reviewDataModel);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.d(TAG, error.getUrl());
                Log.d(TAG, error.getMessage());
                mProgressBar.setVisibility(View.GONE);
                mErrorHandler.showError(ErrorEnum.NetworkError);
                showData(mApp.getCachedReviewData());
            }
        });
    }

    /**
     * Update the recycler with the new review data
     * @param reviewData
     */
    private void showData(ReviewDataModel reviewData) {

        if (reviewData != null && reviewData.getData() != null) {

            mReviews.clear();
            mReviews.addAll(reviewData.getData());
            Log.d(TAG, "Reviews Size: " + mReviews.size());
            runOnUiThread(refreshAdapterRunnable);
        }
    }

    /**
     * Starts the activity to create a new review
     */
    private void startNewReviewActivity() {

        Intent intent = new Intent(this, NewReviewActivity.class);
        this.startActivityForResult(intent, IntentCode.NewReviewActivity.ordinal());
    }

    /**
     * Gets the result from the new review
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IntentCode.NewReviewActivity.ordinal()) {

                String title = data.getStringExtra(IntentExtra.ReviewTitle.toString());
                String message = data.getStringExtra(IntentExtra.ReviewMessage.toString());
                Log.d(TAG, "Review Message: " + message);

                ReviewModel review = new ReviewModel(title, message, "GetYourGuide User", "April 2016");
                saveReview(review);
            }
        }
    }

    /**
     * Mocking uploading the Review to the server
     */
    private void saveReview(ReviewModel review) {

        mMockService.createReview(review, new Callback<ReviewModel>() {
            @Override
            public void success(ReviewModel reviewModel, Response response) {

                mReviews.add(0, reviewModel);
                runOnUiThread(refreshAdapterRunnable);
            }

            @Override
            public void failure(RetrofitError error) {

                mErrorHandler.showError(ErrorEnum.ReviewSaveFailed);
            }
        });
    }

    /**
     * Use a runnable to make sure the adapter is refreshed on the ui thread
     */
    private Runnable refreshAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mReviewAdapter.notifyDataSetChanged();
        }
    };

    /**
     * Save the current data in the shared preferences onPause
     */
    @Override
    public void onPause() {
        super.onPause();

        mApp.setCachedReviewData(mReviewData);
    }
}
