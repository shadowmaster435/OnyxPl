package org.shadowmaster435.util

import org.shadowmaster435.impl.DataProvider

class GenericHolder<T>(override var held: T) : DataProvider<T> {
    override val typeClass: Class<*> = if (held == null) Nothing::class.java else held!!::class.java
    override fun toString() = "$held"
}