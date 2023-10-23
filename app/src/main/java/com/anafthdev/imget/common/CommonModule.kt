package com.anafthdev.imget.common

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    fun provideFileManager(
        @ApplicationContext context: Context
    ): FileManager = FileManager(context)

    @Provides
    @Singleton
    fun provideImageScheduler(
        @ApplicationContext context: Context
    ): ImageScheduler = ImageScheduler(context)

    @Provides
    @Singleton
    fun provideWidgetStateManager(
        @ApplicationContext context: Context
    ): WidgetStateManager = WidgetStateManager(context)

}