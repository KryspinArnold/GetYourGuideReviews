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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import nz.lightsedge.getyourguidereviews.enums.ErrorEnum;
import nz.lightsedge.getyourguidereviews.enums.FilterLanguageEnum;
import nz.lightsedge.getyourguidereviews.enums.IntentCode;
import nz.lightsedge.getyourguidereviews.enums.IntentExtra;
import nz.lightsedge.getyourguidereviews.enums.FilterRatingEnum;
import nz.lightsedge.getyourguidereviews.model.ReviewDataModel;
import nz.lightsedge.getyourguidereviews.model.ReviewModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ReviewDataModel mReviewData;
    private List<ReviewModel> mFilteredReviews;
    private ReviewAdapter mReviewAdapter;
    private ProgressBar mProgressBar;
    private Spinner mRatingSpinner;
    private Spinner mLanguageSpinner;
    private FilterRatingEnum[] mRatings;
    private FilterLanguageEnum[] mLanguages;
    private FilterHelper mFilterHelper;

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

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Progress Bar
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        // Setup Spinners
        setupSpinners();

        mReviewData = new ReviewDataModel();
        mFilterHelper = new FilterHelper(mReviewData.getData());
        mFilteredReviews = mFilterHelper.getFilteredReviews();

        // Create the recycler view and adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mReviewAdapter = new ReviewAdapter(mFilteredReviews);
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
                setData(reviewDataModel);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.d(TAG, error.getUrl());
                Log.d(TAG, error.getMessage());
                mProgressBar.setVisibility(View.GONE);
                mErrorHandler.showError(ErrorEnum.NetworkError);
                setData(mApp.getCachedReviewData());
            }
        });
    }

    /**
     * Sets the review data and refreshes the recycler
     * @param reviewData
     */
    private void setData(ReviewDataModel reviewData) {

        if (reviewData != null && reviewData.getData() != null) {

            mFilterHelper.setReviews(reviewData.getData());
            filterReviews();
        }
    }

    /**
     * Update the recycler with the filtered review data
     */
    private void filterReviews() {
        mFilteredReviews.clear();
        mFilteredReviews.addAll(mFilterHelper.getFilteredReviews());
        Log.d(TAG, "Reviews Size: " + mFilteredReviews.size());
        runOnUiThread(refreshAdapterRunnable);
    }

    /**
     * Starts the activity to create a new review
     */
    private void startNewReviewActivity() {

        Intent intent = new Intent(this, NewReviewActivity.class);
        this.startActivityForResult(intent, IntentCode.NewReviewActivity.ordinal());
    }

    /**
     * Gets the result from the New Review Activity
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

                mFilterHelper.addReview(reviewModel);
                filterReviews();
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

    /**
     * Setup the filter spinners
     */
    private void setupSpinners() {

        mRatings = FilterRatingEnum.values();
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);
        ArrayAdapter<FilterRatingEnum> ratingAdapter =
                new ArrayAdapter<FilterRatingEnum>(this, R.layout.spinner_item, mRatings);
        mRatingSpinner.setAdapter(ratingAdapter);
        mRatingSpinner.setOnItemSelectedListener(this);

        mLanguages = FilterLanguageEnum.values();
        mLanguageSpinner = (Spinner) findViewById(R.id.spinner_language);
        ArrayAdapter<FilterLanguageEnum> languageAdapter =
                new ArrayAdapter<FilterLanguageEnum>(this, R.layout.spinner_item, mLanguages);
        mLanguageSpinner.setAdapter(languageAdapter);
        mLanguageSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Filter click handlers
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinner_rating) {

            mFilterHelper.setRatingFilter((FilterRatingEnum) parent.getSelectedItem());
            filterReviews();
        }
        else if (parent.getId() == R.id.spinner_language) {

            mFilterHelper.setLanguageFilter((FilterLanguageEnum) parent.getSelectedItem());
            filterReviews();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}