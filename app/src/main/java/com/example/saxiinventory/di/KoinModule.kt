package com.example.saxiinventory.di

import android.app.Application
import com.example.saxiinventory.data.remote.networkModule
import com.example.saxiinventory.data.remote.product.ProductRemoteDataManager
import com.example.saxiinventory.data.remote.product.ProductRemoteDataManagerImpl
import com.example.saxiinventory.data.repository.ProductRepository
import com.example.saxiinventory.presentation.viewmodel.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class KoinModule {
    companion object {
        @JvmStatic
        fun initKoin(app: Application) {
            startKoin() {
                androidLogger(Level.ERROR)
                androidContext(app)
                modules(
                    listOf(
                        networkModule,
                        repositoryModule,
                        viewModelModule
                    )
                )
            }
        }
    }
}


val repositoryModule = module {
    single<ProductRemoteDataManager> { ProductRemoteDataManagerImpl(get()) }
    single { ProductRepository(get()) }
}

val viewModelModule = module {
    viewModel { ProductViewModel(get()) }
}
