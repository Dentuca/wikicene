package wikicene.utils

internal fun <T> List<T>.allCombinations(): List<List<T>> = when {
    isEmpty() -> listOf(emptyList())
    else -> {
        val allOtherCombinations = drop(1).allCombinations()
        allOtherCombinations.map { listOf(get(0)) + it } + allOtherCombinations
    }
}
