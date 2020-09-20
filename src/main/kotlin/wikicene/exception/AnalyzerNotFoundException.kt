package wikicene.exception

import wikicene.lucene.analysis.AnalyzerId

class AnalyzerNotFoundException(analyzerId: AnalyzerId) : WikiceneException(
    "Analyzer not found with id: '$analyzerId'"
)
