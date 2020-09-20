package wikicene.exception

import wikicene.lucene.analysis.AnalyzerId

class StoreNotFoundException(analyzerId: AnalyzerId) : WikiceneException(
    "Store not found for analyzer '$analyzerId'"
)
