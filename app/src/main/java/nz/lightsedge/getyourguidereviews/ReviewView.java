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

import nz.lightsedge.getyourguidereviews.model.ReviewModel;

public class ReviewView {

    private String mTitle;
    private String mMessage;
    private String mFootnote;

    /**
     * Formats the review data for display on the Adapter
     *
     * @param review
     */
    public ReviewView(ReviewModel review) {

        mTitle = review.getTitle();
        mMessage = review.getMessage();
        mFootnote = createFootnote(review);
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public String getFootnote() {
        return this.mFootnote;
    }

    /**
     * Creates the footnote string from the review data
     *
     * @param review
     * @return Footnote string
     */
    private String createFootnote(ReviewModel review) {

        String footnote = "";
        String author = review.getAuthor() == null ? "" : review.getAuthor();
        String date = review.getDate() == null ? "" : review.getDate();

        footnote += author;
        // Add a mid-dot if both author and date exist
        if (!author.isEmpty() && !date.isEmpty()) {
            footnote += " \u00B7 ";
        }
        footnote += date;

        return footnote;
    }
}
