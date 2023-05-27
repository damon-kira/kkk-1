package com.colombia.credit.app

import androidx.lifecycle.ViewModel
import com.colombia.credit.di.ViewModelKey
import com.colombia.credit.module.appupdate.AppUpdateViewModel
import com.colombia.credit.module.banklist.BankCardViewModel
import com.colombia.credit.module.config.ConfigViewModel
import com.colombia.credit.module.custom.CustomViewModel
import com.colombia.credit.module.firstconfirm.AutoConfirmViewModel
import com.colombia.credit.module.firstconfirm.FirstConfirmViewModel
import com.colombia.credit.module.history.HistoryViewModel
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.login.LoginViewModel
import com.colombia.credit.module.process.bank.BankInfoViewModel
import com.colombia.credit.module.process.contact.ContactViewModel
import com.colombia.credit.module.process.face.FaceViewModel
import com.colombia.credit.module.process.kyc.KycViewModel
import com.colombia.credit.module.process.personalinfo.PersonalViewModel
import com.colombia.credit.module.process.work.WorkInfoViewModel
import com.colombia.credit.module.repay.RepayCheckViewModel
import com.colombia.credit.module.repay.RepayTabViewModel
import com.colombia.credit.module.repaydetail.RepayDetailViewModel
import com.colombia.credit.module.repeat.confirm.RepeatConfirmViewModel
import com.colombia.credit.module.setting.SettingViewModel
import com.colombia.credit.module.upload.UploadViewModel
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
    @ViewModelKey(WorkInfoViewModel::class)
    abstract fun bindWorkInfoViewModel(viewModel: WorkInfoViewModel): ViewModel

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
}