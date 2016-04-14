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

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nz.lightsedge.getyourguidereviews.model.ReviewModel;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewModel> mReviews;
    private static final String TAG = "ReviewAdapter";

    public ReviewAdapter(List<ReviewModel> reviews) {
        this.mReviews = reviews;
    }

    @Override
    public int getItemCount() {
        return this.mReviews.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleText;
        public TextView mMessageText;
        public TextView mFootNoteText;

        public ViewHolder(View view) {
            super(view);

            mTitleText = (TextView) view.findViewById(R.id.text_title);
            mMessageText = (TextView) view.findViewById(R.id.text_message);
            mFootNoteText = (TextView) view.findViewById(R.id.text_footnote);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ReviewModel review = mReviews.get(position);
        ReviewView reviewView = new ReviewView(review);
        holder.mTitleText.setText(reviewView.getTitle());
        holder.mMessageText.setText(reviewView.getMessage());
        holder.mFootNoteText.setText(reviewView.getFootnote());
    }
}
