package di

import org.koin.dsl.module
import repo.KeyValueStorageRepository
import repo.KeyValueStorageRepositoryImpl
import repo.LocalSetting
import repo.LocalSettingStorageImpl
import ui.viewmodel.EmojiListScreenViewModel
import ui.viewmodel.MainViewModel

fun appModule() = module {
    single<LocalSetting> {
        LocalSettingStorageImpl()
    }
    single<KeyValueStorageRepository> {
        KeyValueStorageRepositoryImpl(localSetting = get())
    }
    single {
        EmojiListScreenViewModel(savedStateHolder = it.get(), keyValueStorageRepository = get())
    }
    single {
        MainViewModel(keyValueStorageRepository = get())
    }
}