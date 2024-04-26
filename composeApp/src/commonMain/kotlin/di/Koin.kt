package di

import com.unwur.mabiaho.database.createDatabase
import com.unwur.mabiaho.database.dao.IUserEmojiDao
import com.unwur.mabiaho.database.dao.UserEmojiDao
import data.ILocalSetting
import data.ISqlDelightDataSource
import data.LocalSettingStorageImpl
import data.SqlDelightDataSourceImpl
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repo.ISqlStorageRepository
import repo.KeyValueStorageRepository
import repo.KeyValueStorageRepositoryImpl
import repo.SqlStorageRepositoryImpl
import service.GenerativeAiService
import ui.screen.emojis.EmojiListScreenViewModel
import ui.viewmodel.HistoryScreenViewModel
import ui.viewmodel.MainViewModel
import ui.viewmodel.MoodStateScreenViewModel

fun letsKoinStart() {
    stopKoin()
    startKoin {
        modules(platformModule() + appModule() + databaseModule())
    }
}

fun appModule() = module {

    singleOf(::LocalSettingStorageImpl) bind ILocalSetting::class
    single<KeyValueStorageRepository> {
        KeyValueStorageRepositoryImpl(localSetting = get())
    }
    single {
        EmojiListScreenViewModel(
            kvsRepo = get(),
            sqlStorageRepository = get()
        )
    }
    single {
        MainViewModel(kvsRepo = get())
    }

    single {
        GenerativeAiService.instance
    }

    single {
        HistoryScreenViewModel(sqlStorageRepository = get(), aiService = get())
    }

    single {
        MoodStateScreenViewModel(kvsRepo = get(), sqlStorageRepository = get())
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