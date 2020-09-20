package wikicene.utils

fun String.kebabToScreamingSnake() = replace("-", "_").toUpperCase()
