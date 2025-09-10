package com.kira.learning.di

import com.kira.learning.modules.answer.AnswerRepository
import com.kira.learning.modules.answer.MockAnswerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(ActivityComponent::class, FragmentComponent::class)
//abstract class ViewModelModule {


//    @Binds
//    @IntoMap
//    @ViewModelKey(SuperEditorViewModel::class)
//    abstract fun bindSuperEditorViewModel(viewModel: SuperEditorViewModel): ViewModel

//}

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindAnswerRepository(impl: MockAnswerRepository): AnswerRepository
}