package org.shadowmaster435.util

import org.shadowmaster435.tokenizer.Token

val List<Token>.prettyString: String get() {
    var str = ""
    forEach { token ->
        str += token.tokenString

    }
    return str
}
