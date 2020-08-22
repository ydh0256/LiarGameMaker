package com.duckkite.android.liargamemaker.di

import com.duckkite.android.liargamemaker.data.source.local.GameRoomDatabase
import com.duckkite.android.liargamemaker.data.source.remote.*
import com.duckkite.android.liargamemaker.ui.game.generate.GameGenerateViewModel
import com.duckkite.android.liargamemaker.ui.game.room.GameRoomViewModel
import com.duckkite.android.liargamemaker.ui.main.ui.history.OfflineHistoryViewModel
import com.duckkite.android.liargamemaker.ui.main.ui.home.HomeViewModel
import com.duckkite.android.liargamemaker.ui.main.ui.profile.ProfileEditViewModel
import com.duckkite.android.liargamemaker.ui.main.ui.profile.ProfileViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    // Remote
    single { FirebaseFirestore.getInstance() }
    single { Firebase.storage }
    // Local
    single {
        GameRoomDatabase.getInstance(androidContext()).gameRoomDao()
    }

    single<UserDataSource> {
        UserRepository(get(), get())
    }
    single<GameListDataSource> {
        GameListRepository(get(), get())
    }
    single<GameRoomDataSource> {
        GameRoomRepository(get())
    }
}

val viewModelModule: Module = module {
    viewModel {
        ProfileViewModel(get())
    }
    viewModel {
        ProfileEditViewModel(get())
    }
    viewModel {
        GameGenerateViewModel(get())
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        OfflineHistoryViewModel(get())
    }
    viewModel {
        GameRoomViewModel(get(), get())
    }
}

val appModules = listOf(repositoryModule, viewModelModule)