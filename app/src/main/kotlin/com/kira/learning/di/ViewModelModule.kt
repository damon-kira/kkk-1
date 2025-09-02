package com.kira.learning.di

import androidx.lifecycle.ViewModel
import com.kira.learning.xml.modules.supereditor.viewmodel.SuperEditorViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(SuperEditorViewModel::class)
    abstract fun bindSuperEditorViewModel(viewModel: SuperEditorViewModel): ViewModel

}