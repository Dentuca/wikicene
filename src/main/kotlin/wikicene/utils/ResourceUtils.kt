package wikicene.utils

import java.io.File
import java.net.URI

fun getResourceURI(path: String): URI? = ClassLoader.getSystemResource(path)?.toURI()

fun readResource(path: String): String? = File(getResourceURI(path)).readText()
