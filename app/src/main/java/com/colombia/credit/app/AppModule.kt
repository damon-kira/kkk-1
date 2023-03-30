package com.colombia.credit.app

import com.colombia.credit.LoanApplication
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

/**
 * Created by weisl on 2019/10/12.
 */
@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
class AppModule {

//    @Provides
//    fun bindFloatingViewModel(floatingRepository: FloatingRepository): FloatingViewModel {
//        return FloatingViewModel(floatingRepository)
//    }
//
//    @Provides
//    fun bindFloatingRepository(apiService: ApiService): FloatingRepository {
//        return FloatingRepository(apiService)
//    }
}

internal fun getAppContext() = LoanApplication.getAppContext()