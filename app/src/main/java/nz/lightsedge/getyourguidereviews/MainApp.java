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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import nz.lightsedge.getyourguidereviews.enums.PreferenceKey;
import nz.lightsedge.getyourguidereviews.model.ReviewDataModel;
import nz.lightsedge.getyourguidereviews.module.ErrorHandlerModule;
import nz.lightsedge.getyourguidereviews.module.MainAppModule;

public class MainApp extends Application {

    private static final String TAG = "MainApp";
    private MainAppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerMainAppComponent.builder()
                .mainAppModule(new MainAppModule(this))
                .errorHandlerModule(new ErrorHandlerModule(this))
                .build();
        mComponent.inject(this);
    }

    public static MainAppComponent getComponent(Context context) {
        return ((MainApp) context.getApplicationContext()).mComponent;
    }

    /**
     * Get the review data from the preferences
     * @return review data
     */
    public ReviewDataModel getCachedReviewData() {

        // Get default shared prefs
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = preferences.getString(PreferenceKey.ReviewData.toString(), null);

        ReviewDataModel reviewData = new ReviewDataModel();

        if (json != null) {

            try {
                Type listType = new TypeToken<ReviewDataModel>() {
                }.getType();
                reviewData = new Gson().fromJson(json, listType);
                Log.d(TAG, "Got Cached Data");

            } catch (Exception e) {
                // Ignore and return empty model list
            }
        }

        return reviewData;
    }

    /**
     * Add review data to the preferences
     * @param reviewData
     */
    public void setCachedReviewData(ReviewDataModel reviewData) {

        Log.d(TAG, "Caching Data");
        String json = new Gson().toJson(reviewData);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.ReviewData.toString(), json);
        editor.commit();
    }
}
