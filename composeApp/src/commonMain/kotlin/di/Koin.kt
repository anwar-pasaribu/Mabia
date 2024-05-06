package di

import com.unwur.mabiaho.database.createDatabase
import com.unwur.mabiaho.database.dao.IUserEmojiDao
import com.unwur.mabiaho.database.dao.UserEmojiDao
import data.ILocalSetting
import data.ISqlDelightDataSource
import data.LocalSettingStorageImpl
import data.SqlDelightDataSourceImpl
import domain.usecase.GetEmojisByDateUseCase
import domain.usecase.GetGreetingListUseCase
import domain.usecase.GetMoodRateCalculationUseCase
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
import ui.screen.history.HistoryScreenViewModel
import ui.screen.moodRate.MoodStateScreenViewModel
import ui.screen.onboarding.MoodRateViewModel
import ui.screen.reward.CongratulationViewModel
import ui.screen.splash.SplashViewModel

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
            sqlStorageRepository = get(),
            getGreetingListUseCase = get()
        )
    }

    single {
        GenerativeAiService.instance
    }

    single {
        SplashViewModel(kvsRepo = get())
    }

    single {
        CongratulationViewModel(kvsRepo = get())
    }

    single {
        MoodRateViewModel(kvsRepo = get())
    }

    single {
        HistoryScreenViewModel(
            sqlStorageRepository = get(),
            aiService = get(),
            getEmojisByDateUseCase = get(),
            getMoodRateCalculationUseCase = get()
        )
    }

    single {
        MoodStateScreenViewModel(
            kvsRepo = get(),
            sqlStorageRepository = get(),
            getEmojisByDateUseCase = get(),
            getMoodRateCalculationUseCase = get(),
        )
    }

    single {
        GetEmojisByDateUseCase(sqlStorageRepository = get())
    }

    single {
        GetMoodRateCalculationUseCase()
    }

    single {
        GetGreetingListUseCase()
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