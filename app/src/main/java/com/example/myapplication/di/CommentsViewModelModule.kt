package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.presintation.comment.CommentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface CommentsViewModelModule {

    @IntoMap
    @ViewModuleKey(CommentViewModel::class)
    @Binds
    fun bindsCommentViewModel(newsCommentViewModel: CommentViewModel): ViewModel

}