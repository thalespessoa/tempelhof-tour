package com.gyg.tempelhoftour.app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApplicationController application;

    public ViewModelFactory(ApplicationController application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        T t = super.create(modelClass);
        if (t instanceof ApplicationComponent.Injectable) {
            ((ApplicationComponent.Injectable) t).inject(application.getAppComponent());
        }
        return t;
    }
}
