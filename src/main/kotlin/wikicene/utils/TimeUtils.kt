package wikicene.utils

fun timeThis(description: String, block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println("$description took ${System.currentTimeMillis() - start} ms")
}

fun <T> timeIt(block: () -> T): Pair<T, Int> {
    val start = System.currentTimeMillis()
    val ret = block()
    val time = (System.currentTimeMillis() - start).toInt()
    return Pair(ret, time)
}
