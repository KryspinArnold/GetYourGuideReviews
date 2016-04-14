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
package nz.lightsedge.getyourguidereviews.mock;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Mocks the Retrofit client by creating dummy responses
 */
public class MockClient implements Client {

    private static final String TAG = "MockClient";
    private static final int HTTP_OK_STATUS = 200;

    private static final String RESPONSE_JSON = "{\"status\":true,"
            + "\"total_reviews\":197,"
            + "\"data\":[{"
            + "\"review_id\":311244,"
            + "\"rating\":\"5.0\","
            + "\"title\":\"Amazing!\","
            + "\"message\":\"Great place to visit. Guide was super informative and very nice\","
            + "\"author\":\"a GetYourGuide Customer \u2013 Berlin, Germany\","
            + "\"foreignLanguage\":false,"
            + "\"date\":\"April 4, 2016\","
            + "\"date_unformatted\":{},"
            + "\"languageCode\":\"en\","
            + "\"traveler_type\":null}]}";

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Log.d(TAG, "Uri: " + uri.toString());

        byte[] body;

        // Mock the create review, by returning the same posted data
        if (uri.getPath().equals("/review/create/")) {

            Log.d(TAG, "Create Review");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            request.getBody().writeTo(out);
            body = out.toByteArray();
        }
        else {
            // Mock the get reviews, by returning dummy json data
            Log.d(TAG, "Get Reviews");
            body = RESPONSE_JSON.getBytes();
        }

        return new Response(request.getUrl(), HTTP_OK_STATUS, "nothing", Collections.EMPTY_LIST,
                new TypedByteArray("application/json", body));
    }
}


