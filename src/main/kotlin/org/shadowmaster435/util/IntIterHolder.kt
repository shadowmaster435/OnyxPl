package org.shadowmaster435.util

import kotlin.reflect.KProperty

class IntIterHolder {
    var i = 0; private set

    fun inc(by: Int = 1) {i += by}

    operator fun getValue(a: Nothing?, prop: KProperty<*>) = i

}
