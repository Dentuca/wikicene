package wikicene.lucene.query

enum class QueryType {
    TERM,
    PREFIX,
    WILDCARD,
    FUZZY,
    PARSED,
    // PHRASE,
    // BOOLEAN,
    // MATCH_ALL,
    // CUSTOM
}
