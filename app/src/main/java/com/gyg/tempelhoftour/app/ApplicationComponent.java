package com.gyg.tempelhoftour.app;

import com.gyg.tempelhoftour.reviews.ReviewsViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component to handle dependency injection
 *
 * Created by thalespessoa on 3/29/18.
 */
@Singleton
@Component(modules = {DataModule.class})
public interface ApplicationComponent {
    void inject(ReviewsViewModel viewModel);

    interface Injectable {
        void inject(ApplicationComponent applicationComponent);
    }
}