package org.shadowmaster435.impl

import org.shadowmaster435.util.BindableFunction

interface Preprocessor {
    val processor: BindableFunction<String, *>
}