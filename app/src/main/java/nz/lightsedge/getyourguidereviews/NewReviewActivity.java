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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import javax.inject.Inject;

import nz.lightsedge.getyourguidereviews.enums.ErrorEnum;
import nz.lightsedge.getyourguidereviews.enums.IntentExtra;

public class NewReviewActivity extends AppCompatActivity {

    @Inject
    ErrorHandler mErrorHandler;

    private EditText mReviewTitle;
    private EditText mReviewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mReviewTitle = (EditText) findViewById(R.id.edit_review_title);
        mReviewMessage = (EditText) findViewById(R.id.edit_review_message);

        MainApp.getComponent(this).inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            saveNewReview();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Send review back to the main activity
     */
    private void saveNewReview() {

        String title = mReviewTitle.getText().toString();
        String message = mReviewMessage.getText().toString();

        if (title.isEmpty()) {
            mErrorHandler.showError(ErrorEnum.ReviewTitleBlank);
        }
        else if (message.isEmpty()) {
            mErrorHandler.showError(ErrorEnum.ReviewMessageBlank);
        }
        else {
            Intent intent = new Intent();
            intent.putExtra(IntentExtra.ReviewTitle.toString(), title);
            intent.putExtra(IntentExtra.ReviewMessage.toString(), message);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
