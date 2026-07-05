package org.shadowmaster435.code

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.ModifierScope
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxTuples

open class OnyxFunction(val name: String, val type: OnyxType, val tuples: OnyxTuples, val modifiers: OnyxModifiers, val block: OnyxCodeBlock?, val classFuncType: OnyxType? = null) : OnyxMember {
    override var initialized: Boolean = false

    fun invoke(inputs: List<DataProvider>, prevalidated: Boolean = false): DataProvider {
        if (block == null) {
            if (modifiers.isAbstract) throw RuntimeException("Called an abstract function this shouldn't happen")
            else throw RuntimeException("Called a function with no code block this shouldn't happen")
        }
        if (!prevalidated) tuples.validate(inputs)
        val last = if (block.needsReturn) {
            block.ret ?: throw RuntimeException("Not all code paths return a value")
        } else {
            block.last ?: throw RuntimeException("This shouldn't happen")
        }

        return last.instantiate(if (classFuncType != null) inputs.first() else null, *inputs.toTypedArray()) as DataProvider
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + tuples.hashCode()
        return result
    }

    fun accessPermits(type: ModifierScope) {
        modifiers.mods.forEach {
            it.modifierScopes
        }
    }

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        initialized = true
    }

    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OnyxFunction

        if (name != other.name) return false
        if (type != other.type) return false
        if (tuples != other.tuples) return false
        if (modifiers != other.modifiers) return false
        if (block != other.block) return false

        return true
    }

    override fun toString() = "$modifiers fun $name($tuples): $type ${if (block != null) "$block" else ""}\n"

}