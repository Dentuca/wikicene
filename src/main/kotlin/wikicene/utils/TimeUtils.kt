package wikicene.utils

fun timeThis(description: String, block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println("$description took ${System.currentTimeMillis() - start} ms")
}
