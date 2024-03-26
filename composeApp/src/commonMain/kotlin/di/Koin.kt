package di

import com.unwur.mabiaho.database.createDatabase
import com.unwur.mabiaho.database.dao.IUserEmojiDao
import com.unwur.mabiaho.database.dao.UserEmojiDao
import data.ILocalSetting
import data.ISqlDelightDataSource
import data.LocalSettingStorageImpl
import data.SqlDelightDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repo.ISqlStorageRepository
import repo.KeyValueStorageRepository
import repo.KeyValueStorageRepositoryImpl
import repo.SqlStorageRepositoryImpl
import ui.viewmodel.EmojiListScreenViewModel
import ui.viewmodel.HistoryScreenViewModel
import ui.viewmodel.MainViewModel

fun appModule() = module {

    singleOf(::LocalSettingStorageImpl) bind ILocalSetting::class
    single<KeyValueStorageRepository> {
        KeyValueStorageRepositoryImpl(localSetting = get())
    }
    single {
        EmojiListScreenViewModel(
            savedStateHolder = it.get(),
            keyValueStorageRepository = get(),
            sqlStorageRepository = get()
        )
    }
    single {
        MainViewModel(keyValueStorageRepository = get())
    }

    single {
        HistoryScreenViewModel(sqlStorageRepository = get())
    }
}

// TODO Need to research more about SQLDelight + Koin.
//      expectation this module initialization only for `DataSource`
fun databaseModule() = module {

    single {
        createDatabase()
    }

    single<IUserEmojiDao>{
        UserEmojiDao(database = get())
    }

    single<ISqlDelightDataSource>{
        SqlDelightDataSourceImpl(userEmojiDao = get())
    }

    single<ISqlStorageRepository>{
        SqlStorageRepositoryImpl(sqlDelightDataSource = get())
    }
}