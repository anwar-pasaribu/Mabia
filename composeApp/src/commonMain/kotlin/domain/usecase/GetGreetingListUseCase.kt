package domain.usecase

import kotlinx.collections.immutable.persistentListOf

class GetGreetingListUseCase {
    operator fun invoke() = persistentListOf(
        "halo!",
        "gimana hari ini?",
        "all good?",
        "seru ga?",
        "hi!",
        "enjoy ga hari ini?",
        "aman?"
    )
}