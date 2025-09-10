//package com.kira.learning.di
//
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//
//@Module
//@InstallIn(SingletonComponent::class)
//class NetWorkModule {

//    @Provides
//    @Singleton
//    fun provideDispatcherProvider(): DispatcherProvider {
//        return DispatcherProviderImpl()
//    }

//    @Provides
//    @Singleton
//    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
//        return StringProviderImpl(context)
//    }

//}