package com.lolo.io.onelist.core.data.di

import android.content.Context
import androidx.room.Room
import com.lolo.io.onelist.core.database.OneListDatabase
import com.lolo.io.onelist.core.database.dao.ItemListDao
import com.lolo.io.onelist.core.domain.use_cases.CreateList
import com.lolo.io.onelist.core.domain.use_cases.EditList
import com.lolo.io.onelist.core.domain.use_cases.GetAllLists
import com.lolo.io.onelist.core.domain.use_cases.HandleFirstLaunch
import com.lolo.io.onelist.core.domain.use_cases.ImportList
import com.lolo.io.onelist.core.domain.use_cases.MoveList
import com.lolo.io.onelist.core.domain.use_cases.OneListUseCases
import com.lolo.io.onelist.core.domain.use_cases.RemoveList
import com.lolo.io.onelist.core.domain.use_cases.SetBackupUri
import com.lolo.io.onelist.core.domain.use_cases.ShowWhatsNew
import com.lolo.io.onelist.core.domain.use_cases.SyncAllLists
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModuleNew {

    @Provides
    @Singleton
    fun provideOneListDatabase(
        @ApplicationContext appContext: Context
    ): OneListDatabase = Room.databaseBuilder(
        appContext,
        OneListDatabase::class.java,
        "onelist.db"
    ).build()

    @Provides
    @Singleton
    fun provideItemListDao(db: OneListDatabase): ItemListDao = db.itemListDao

    @Provides
    @Singleton
    fun provideOneListUseCases(
        createList: CreateList,
        getAllLists: GetAllLists,
        removeList: RemoveList,
        handleFirstLaunch: HandleFirstLaunch,
        editList: EditList,
        importList: ImportList,
        moveList: MoveList,
        setBackupUri: SetBackupUri,
        syncAllLists: SyncAllLists,
        showWhatsNew: ShowWhatsNew,
    ): OneListUseCases = OneListUseCases(
        createList,
        importList,
        moveList,
        editList,
        getAllLists,
        setBackupUri,
        removeList,
        handleFirstLaunch,
        syncAllLists,
        showWhatsNew,
    )

//    @Provides
//    @Singleton
//    fun provideSharedPreferencesHelper(
//        app: Application
//    ): SharedPreferencesHelper = SharedPreferencesHelper(app)
}