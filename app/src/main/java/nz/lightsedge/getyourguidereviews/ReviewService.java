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

import nz.lightsedge.getyourguidereviews.model.ReviewDataModel;
import nz.lightsedge.getyourguidereviews.model.ReviewModel;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ReviewService {

    @GET("/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json?page=0&rating=0&sortBy=date_of_review&direction=DESC")
    void getReviewData(Callback<ReviewDataModel> response);

    @POST("/review/create/")
    void createReview(@Body ReviewModel reviewModel, Callback<ReviewModel> response);
}
