package com.gyg.tempelhoftour.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.gyg.tempelhoftour.data.DataRepository;
import com.gyg.tempelhoftour.data.db.LocalDatabase;
import com.gyg.tempelhoftour.data.remote.NetworkApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module responsible for create data providers
 *
 * Created by thalespessoa on 3/29/18.
 */
@Module
public class DataModule {

    Application mApplication;

    public DataModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public NetworkApi getNetworkApi() {
        return new NetworkApi();
    }

    @Provides
    @Singleton
    public LocalDatabase getLocalDatabase() {
        return Room.databaseBuilder(mApplication, LocalDatabase.class, AppConfig.DB_NAME).build();
    }

    @Provides
    @Singleton
    public DataRepository getDataRepository(NetworkApi networkApi, LocalDatabase localDatabase) {
        return new DataRepository(networkApi, localDatabase, mApplication);
    }
}
