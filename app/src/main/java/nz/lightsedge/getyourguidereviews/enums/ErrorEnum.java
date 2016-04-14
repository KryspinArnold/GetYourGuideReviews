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
package nz.lightsedge.getyourguidereviews.enums;

import nz.lightsedge.getyourguidereviews.R;

public enum ErrorEnum {
    ReviewTitleBlank(R.string.error_review_no_title),
    ReviewMessageBlank(R.string.error_review_no_message);

    private int mStringId;

    ErrorEnum(int stringId) {
        mStringId = stringId;
    }

    public int getStringId() {
        return mStringId;
    }
}
