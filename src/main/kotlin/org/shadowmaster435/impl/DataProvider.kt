package org.shadowmaster435.impl

import org.shadowmaster435.impl.abstracts.OnyxOperator

interface DataProvider<T> {
    var held : T
    val typeClass : Class<*>

}
