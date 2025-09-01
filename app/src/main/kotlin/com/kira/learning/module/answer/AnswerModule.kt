package com.kira.learning.module.answer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnswerModule {
    @Binds
    @Singleton
    abstract fun bindAnswerRepository(impl: MockAnswerRepository): AnswerRepository
}

