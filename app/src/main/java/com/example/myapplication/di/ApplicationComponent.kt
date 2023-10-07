package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.presintation.main.LoginActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [DataModule::class,
        ViewModelModule::class]
)
interface ApplicationComponent {

    fun inject(loginActivity: LoginActivity)

    fun getCommentsScreenComponentFactory() : CommentsScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}