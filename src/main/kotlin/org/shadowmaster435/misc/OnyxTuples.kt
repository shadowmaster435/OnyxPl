package org.shadowmaster435.misc

import org.shadowmaster435.impl.DataProvider

class OnyxTuples(list: List<OnyxTuple<*>>) {
    val requiredTuples = run {
        var count = 0
        list.forEach { if (!it.optional) count++ }
        count
    }

    fun validate(input: List<DataProvider<*>>){
        if (input.size < requiredTuples) throw RuntimeException("Invalid input size for params")
        repeat(input.size) {
            val value = input[it]
            val tuple = input[it]
            if (!tuple.typeClass.isInstance(value.held)) {
                throw RuntimeException("Invalid value type '${value.typeClass}' for tuple of type ${tuple.typeClass}")
            }
        }
    }
}