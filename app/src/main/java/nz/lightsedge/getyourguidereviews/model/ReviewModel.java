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
package nz.lightsedge.getyourguidereviews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    public ReviewModel(String title, String message, String author, String date) {
        this.mTitle = title;
        this.mMessage = message;
        this.mAuthor = author;
        this.mDate = date;
    }

    @SerializedName("review_id")
    @Expose
    private Integer mReviewId;

    @SerializedName("rating")
    @Expose
    private Float mRating;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("message")
    @Expose
    private String mMessage;

    @SerializedName("author")
    @Expose
    private String mAuthor;

    @SerializedName("foreignLanguage")
    @Expose
    private Boolean mForeignLanguage;

    @SerializedName("date")
    @Expose
    private String mDate;

    @SerializedName("languageCode")
    @Expose
    private String mLanguageCode;

    public Integer getReviewId() {
        return mReviewId;
    }

    public Float getRating() {
        return mRating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public Boolean getForeignLanguage() {
        return mForeignLanguage;
    }

    public String getDate() {
        return mDate;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }
}
