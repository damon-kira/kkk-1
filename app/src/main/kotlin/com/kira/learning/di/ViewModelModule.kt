package com.kira.learning.di

import androidx.lifecycle.ViewModel
import com.kira.learning.xml.modules.ai.AIChatViewModel
import com.kira.learning.xml.modules.answer.vm.AnswerViewModel
import com.kira.learning.xml.modules.appupdate.AppUpdateViewModel
import com.kira.learning.xml.modules.chat.ui.ChatViewModel
import com.kira.learning.xml.modules.config.ConfigViewModel
import com.kira.learning.xml.modules.custom.CustomViewModel
import com.kira.learning.xml.modules.firstconfirm.vm.AutoConfirmViewModel
import com.kira.learning.xml.modules.firstconfirm.vm.FirstConfirmViewModel
import com.kira.learning.xml.modules.history.HistoryViewModel
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.kira.learning.xml.modules.login.vm.LoginViewModel
import com.kira.learning.xml.modules.quiz.vm.QuizViewModel
import com.kira.learning.xml.modules.setting.SettingViewModel
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
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeLoanViewModel::class)
    abstract fun bindHomeLoanViewModel(model: HomeLoanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(viewModel: SettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FirstConfirmViewModel::class)
    abstract fun bindFirstConfirmViewModel(viewModel: FirstConfirmViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppUpdateViewModel::class)
    abstract fun bindAppUpdateViewModel(viewModel: AppUpdateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomViewModel::class)
    abstract fun bindCustomViewModel(viewModel: CustomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AutoConfirmViewModel::class)
    abstract fun bindAutoConfirmViewModel(viewModel: AutoConfirmViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfigViewModel::class)
    abstract fun bindConfigViewModel(viewModel: ConfigViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AIChatViewModel::class)
    abstract fun bindAIChatViewModel(viewModel: AIChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnswerViewModel::class)
    abstract fun bindAnswerViewModel(viewModel: AnswerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindQuizViewModel(viewModel: QuizViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SuperEditorViewModel::class)
    abstract fun bindSuperEditorViewModel(viewModel: SuperEditorViewModel): ViewModel

}