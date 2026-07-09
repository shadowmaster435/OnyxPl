package org.shadowmaster435.misc

import org.shadowmaster435.built_ins.typeType
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.util.GenericHolder
import kotlin.math.max

class OnyxTuples(val tuples: List<OnyxTuple>): OnyxMember {
    constructor(vararg tuples: OnyxTuple) : this(tuples.toList())
    override var initialized = false
    val requiredTuples = run {
        var count = 0
        tuples.forEach { if (!it.optional) count++ }
        count
    }
    val size = run {
        var i = 0
        var usesAbstractTypes = false
        for (tuple in tuples) {
            if (tuple.type.size == -1) usesAbstractTypes = true
            else i += tuple.type.size
        }
        i * if (usesAbstractTypes) -1 else 1
    }

    private fun normal(v: OnyxType, t: OnyxType) = t.castableTo(v)
    private fun strict(v: OnyxType, t: OnyxType) = t == v
    private fun error(v: OnyxType, t: OnyxType) {
        throw RuntimeException("Invalid value type '$v' for tuple of type $t")
    }



    private fun iter(input: Iterator<DataProvider>, size: Int, strict: Boolean) {
        if (size < requiredTuples) throw RuntimeException("Invalid input size for params")
        var i = 0
        input.forEach { value ->
            val tuple = tuples[i]
            val v = value.type
            val t = tuple.type
            if (!(if (strict) strict(v, t) else normal(v, t))) error(v, t)
            i++
        }
    }

    fun validateStrict(input: Array<out DataProvider>) = iter(input.iterator(), input.size, true)
    fun validateStrict(input: List<DataProvider>) = iter(input.iterator(), input.size, true)
    fun validate(input: Array<out DataProvider>) = iter(input.iterator(), input.size, false)
    fun validate(input: List<DataProvider>) = iter(input.iterator(), input.size, false)

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            initialized = true
            tuples.forEach { it.initialize(namedScopeMembers) }
        }
    }

    override fun instantiate(
        thisInstance: DataProvider?,
        vararg params: DataProvider
    ) = this

    override fun toString(): String {
        var str = ""
        tuples.forEachIndexed { index, tuple ->
            str += if (index == tuples.size - 1) "$tuple" else "$tuple, "
        }
        return str
    }

    companion object {
        val empty = OnyxTuples(listOf())
    }

}