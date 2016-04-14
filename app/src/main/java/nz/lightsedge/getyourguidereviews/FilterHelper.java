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

import java.util.ArrayList;
import java.util.List;

import nz.lightsedge.getyourguidereviews.enums.FilterLanguageEnum;
import nz.lightsedge.getyourguidereviews.enums.FilterRatingEnum;
import nz.lightsedge.getyourguidereviews.model.ReviewModel;

public class FilterHelper {

    private FilterRatingEnum mRatingFilter = FilterRatingEnum.Any;
    private FilterLanguageEnum mLanguageFilter = FilterLanguageEnum.Any;
    private List<ReviewModel> mReviews;

    public FilterHelper(List<ReviewModel> reviews) {
        this.mReviews = reviews;
    }

    /**
     * Gets the filtered reviews
     * @return filtered reviews
     */
    public List<ReviewModel> getFilteredReviews() {

        List<ReviewModel> filteredReviews = new ArrayList<>();
        for (ReviewModel review : mReviews) {

            if (inFilter(review)) {
                filteredReviews.add(review);
            }
        }

        return filteredReviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.mReviews = reviews;
    }

    public void addReview(ReviewModel review) {
        this.mReviews.add(0, review);
    }

    public void setRatingFilter(FilterRatingEnum ratingFilter) {
        this.mRatingFilter = ratingFilter;
    }

    public void setLanguageFilter(FilterLanguageEnum languageFilter) {
        this.mLanguageFilter = languageFilter;
    }

    /**
     * Calculates if a review should be included in the filter
     * @param review
     * @return is included
     */
    private boolean inFilter(ReviewModel review) {

        boolean isLanguage = false;
        boolean isRating = false;

        // Don't include null ratings/languages
        if (review.getRating() == null || review.getLanguageCode() == null) {
            return false;
        }

        // Filter Languages
        if (mLanguageFilter == FilterLanguageEnum.Any) {
            isLanguage = true;
        }
        else if (mLanguageFilter.toString().equalsIgnoreCase(review.getLanguageCode())) {
            isLanguage = true;
        }

        // Filter Ratings
        if (mRatingFilter == FilterRatingEnum.Any) {
            isRating = true;
        }
        else if (mRatingFilter == FilterRatingEnum.High
                && review.getRating() >= 4) {
            isRating = true;
        }
        else if (mRatingFilter == FilterRatingEnum.Medium
                && review.getRating() > 2
                && review.getRating() < 4) {
            isRating = true;
        }
        else if (mRatingFilter == FilterRatingEnum.Low
                && review.getRating() <= 2) {
            isRating = true;
        }

        return isLanguage && isRating;
    }
}
