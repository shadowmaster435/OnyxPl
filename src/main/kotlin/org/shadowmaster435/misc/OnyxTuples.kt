package org.shadowmaster435.misc

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxTuples(val tuples: List<OnyxTuple>): OnyxMember {

    override var initialized = false
    val requiredTuples = run {
        var count = 0
        tuples.forEach { if (!it.optional) count++ }
        count
    }


    fun validate(input: List<DataProvider>){
        if (input.size < requiredTuples) throw RuntimeException("Invalid input size for params")
        repeat(input.size) {
            val value = input[it]
            val tuple = input[it]
            if (!tuple.type.castableTo(value.type)) {
                throw RuntimeException("Invalid value type '${value.type}' for tuple of type ${tuple.type}")
            }
        }
    }

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            initialized = true
            tuples.forEach { it.initialize(namedScopeMembers) }
        }
    }

    override fun instantiate(vararg params: DataProvider): OnyxMember {
        return OnyxTuples(buildList {
            tuples.forEach {
                add(it.instantiate(*params) as OnyxTuple)
            }
        })
    }

    companion object {
        val empty = OnyxTuples(listOf())
    }

}