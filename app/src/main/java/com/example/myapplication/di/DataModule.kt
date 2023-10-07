package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.network.ApiFactory
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.data.repository.NewsFeedRepositoryImpl
import com.example.myapplication.domain.repository.NewsFeedRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object {
        @ApplicationScope
        @Provides
        fun providesApiFactory(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun providesVKPreferencesKeyValueStorage(context: Context): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}