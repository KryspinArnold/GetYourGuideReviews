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

import nz.lightsedge.getyourguidereviews.module.MainAppModule;

public class MainApp extends Application {

    private MainAppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerMainAppComponent.builder()
                .mainAppModule(new MainAppModule(this))
                .build();
        mComponent.inject(this);
    }

    public static MainAppComponent getComponent(Context context) {
        return ((MainApp) context.getApplicationContext()).mComponent;
    }
}
