package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.presintation.main.LoginViewModel
import com.example.myapplication.presintation.news.NewsFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModuleKey(NewsFeedViewModel::class)
    @Binds
    fun bindsNewsFeedViewModel(newsFeedViewModel: NewsFeedViewModel): ViewModel

    @IntoMap
    @ViewModuleKey(LoginViewModel::class)
    @Binds
    fun bindsLoginViewModel(newsLoginViewModel: LoginViewModel): ViewModel
}