package com.gyg.tempelhoftour.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gyg.tempelhoftour.BuildConfig;

/**
 * Main app class. This setups applications modules and start routines
 *
 * Created by thalespessoa on 3/29/18.
 */
public class ApplicationController extends Application {

    private ApplicationComponent applicationComponent;

	@Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
