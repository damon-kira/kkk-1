package com.kira.learning.di

import androidx.lifecycle.ViewModel
import com.kira.learning.module.answer.AnswerRepository
import com.kira.learning.module.answer.MockAnswerRepository
import com.kira.learning.xml.modules.supereditor.viewmodel.SuperEditorViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(SuperEditorViewModel::class)
    abstract fun bindSuperEditorViewModel(viewModel: SuperEditorViewModel): ViewModel

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnswerModule {
    @Binds
    @Singleton
    abstract fun bindAnswerRepository(impl: MockAnswerRepository): AnswerRepository
}