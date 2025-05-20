package com.kira.learning.app

import androidx.lifecycle.ViewModel
import com.kira.learning.di.ViewModelKey
import com.kira.learning.module.ai.AIChatViewModel
import com.kira.learning.module.answer.vm.AnswerViewModel
import com.kira.learning.module.appupdate.AppUpdateViewModel
import com.kira.learning.module.banklist.BankCardViewModel
import com.kira.learning.module.chat.ui.ChatViewModel
import com.kira.learning.module.config.ConfigViewModel
import com.kira.learning.module.custom.CustomViewModel
import com.kira.learning.module.firstconfirm.AutoConfirmViewModel
import com.kira.learning.module.firstconfirm.FirstConfirmViewModel
import com.kira.learning.module.history.HistoryViewModel
import com.kira.learning.module.home.HomeLoanViewModel
import com.kira.learning.module.login.LoginViewModel
import com.kira.learning.module.process.bank.BankInfoViewModel
import com.kira.learning.module.process.contact.ContactViewModel
import com.kira.learning.module.process.face.FaceViewModel
import com.kira.learning.module.process.kyc.KycViewModel
import com.kira.learning.module.process.personalinfo.PersonalViewModel
import com.kira.learning.module.quiz.vm.QuizViewModel
import com.kira.learning.module.repay.RepayCheckViewModel
import com.kira.learning.module.repay.RepayTabViewModel
import com.kira.learning.module.repaydetail.RepayDetailViewModel
import com.kira.learning.module.repeat.confirm.RepeatConfirmViewModel
import com.kira.learning.module.setting.SettingViewModel
import com.kira.learning.module.upload.UploadViewModel
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
    @ViewModelKey(RepayTabViewModel::class)
    abstract fun bindRepayTabViewModel(model: RepayTabViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankCardViewModel::class)
    abstract fun bindBankCardViewModel(model: BankCardViewModel): ViewModel

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
    @ViewModelKey(UploadViewModel::class)
    abstract fun bindUploadViewModel(viewModel: UploadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KycViewModel::class)
    abstract fun bindKycViewModel(viewModel: KycViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FaceViewModel::class)
    abstract fun bindFaceViewModel(viewModel: FaceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankInfoViewModel::class)
    abstract fun bindBankInfoViewModel(viewModel: BankInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    abstract fun bindContactViewModel(viewModel: ContactViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalViewModel::class)
    abstract fun bindPersonalViewModel(viewModel: PersonalViewModel): ViewModel

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
    @ViewModelKey(RepeatConfirmViewModel::class)
    abstract fun bindRepeatConfirmViewModel(viewModel: RepeatConfirmViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepayDetailViewModel::class)
    abstract fun bindRepayDetailViewModel(viewModel: RepayDetailViewModel): ViewModel

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
    @ViewModelKey(RepayCheckViewModel::class)
    abstract fun bindRepayCheckViewModel(viewModel: RepayCheckViewModel): ViewModel

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
}