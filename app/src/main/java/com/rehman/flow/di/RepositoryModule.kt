package com.rehman.flow.di

import com.rehman.flow.network.ApiService
import com.rehman.flow.network.RemoteDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun getRepository(apiService: ApiService): RemoteDataRepository =
        RemoteDataRepository(apiService)

}