package com.projectmirror.di

import android.content.Context
import androidx.room.Room
import com.projectmirror.data.local.MirrorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MirrorDatabase =
        Room.databaseBuilder(
            context,
            MirrorDatabase::class.java,
            "mirror.db",
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSaveSlotDao(db: MirrorDatabase) = db.saveSlotDao()

    @Provides
    fun provideChoiceLogDao(db: MirrorDatabase) = db.choiceLogDao()

    @Provides
    fun provideForeshadowDao(db: MirrorDatabase) = db.foreshadowDao()
}
