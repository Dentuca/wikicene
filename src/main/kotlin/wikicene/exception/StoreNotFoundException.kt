package wikicene.exception

import java.lang.RuntimeException

class StoreNotFoundException(override val message: String?) : RuntimeException()
