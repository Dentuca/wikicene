package wikicene.lucene.analysis

enum class SupportedAnalyzerType(val id: String) {
    STANDARD("standard"),
    KEYWORD("keyword"),
    CUSTOM("custom")
}
