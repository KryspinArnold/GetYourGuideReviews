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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import nz.lightsedge.getyourguidereviews.model.ReviewModel;

/**
 * Tests if the footnote in the ReviewView class is the correct format
 */
public class ReviewViewTest {

    private ReviewModel mNormalReview;
    private ReviewModel mNullReview;
    private ReviewModel mAuthorReview;
    private ReviewModel mDateReview;

    @Before
    public void setUp() throws Exception {

        mNormalReview = new ReviewModel("title", "message", "author", "date");
        mNullReview = new ReviewModel(null, null, null, null);
        mAuthorReview = new ReviewModel(null, null, "author", null);
        mDateReview = new ReviewModel(null, null, null, "date");
    }

    @Test
    public void testNormalReviewFootnote() throws Exception {

        ReviewView reviewView = new ReviewView(mNormalReview);
        String footnote = "author \u00B7 date";
        assertEquals("Normal review footnote is incorrect.", reviewView.getFootnote(), footnote);
    }

    @Test
    public void testNullReviewFootnote() throws Exception {

        ReviewView reviewView = new ReviewView(mNullReview);
        String footnote = "";
        assertEquals("Null review footnote is incorrect.", reviewView.getFootnote(), footnote);
    }

    @Test
    public void testAuthorReviewFootnote() throws Exception {

        ReviewView reviewView = new ReviewView(mAuthorReview);
        String footnote = "author";
        assertEquals("Author only review footnote is incorrect.", reviewView.getFootnote(), footnote);
    }

    @Test
    public void testDateReviewFootnote() throws Exception {

        ReviewView reviewView = new ReviewView(mDateReview);
        String footnote = "date";
        assertEquals("Date only review footnote is incorrect.", reviewView.getFootnote(), footnote);
    }
}
