package org.shadowmaster435.misc

import org.shadowmaster435.impl.DataProvider

class OnyxTuple<T>(val clazz: Class<T>, val optionalValue: DataProvider<T>? = null) {
    val optional = optionalValue != null
}