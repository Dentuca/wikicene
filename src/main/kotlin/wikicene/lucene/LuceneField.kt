package wikicene.lucene

import org.apache.lucene.document.Field
import org.apache.lucene.document.StoredField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.Term

enum class LuceneField(val instance: Field) {
    TITLE(TextField("title", "", Field.Store.YES)),
    SUMMARY(StoredField("summary", "")),
    DESCRIPTION(StoredField("description", "")),
    TIMESTAMP(StoredField("timestamp", "")),
    THUMBNAIL(StoredField("thumbnail", "")),
    PAGE(StoredField("page", ""));

    val fieldName: String = instance.name()

    fun buildTerm(s: String) = Term(fieldName, s)
}
