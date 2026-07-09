package org.shadowmaster435.code

import org.shadowmaster435.code.fields.OnyxField
import org.shadowmaster435.code.statement.OnyxReturnStatement
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.util.GenericHolder

class OnyxCodeBlock(val members: List<OnyxMember>, val needsReturn: Boolean = true, val ret: DataProvider? = null): OnyxMember {
    val last = members.lastOrNull()
    override var initialized: Boolean = false
    val size: Int get() {
        var i = 0
        var usesAbstractTypes = false
        members.forEach {
            if (it is OnyxField) {
                if (it.type.size == -1) usesAbstractTypes = true
                else i += it.type.size
            }
        }
        return i * if (usesAbstractTypes) -1 else 1
    }

    fun execute(thisInstance: DataProvider? = null): DataProvider {
        var ret: DataProvider = GenericHolder(Unit)
        members.forEach {
            when(it) {
                is OnyxCodeBlock -> ret = it.execute(thisInstance)
                is OnyxReturnStatement -> {
                    it.held?.let { passed ->
                        ret = GenericHolder(passed)
                    }
                    return@forEach
                }
                is DataProvider -> ret = GenericHolder(it.held)
            }
        }
        return ret
    }

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            members.forEach {
                it.initialize(namedScopeMembers)
            }
            initialized = true
        }
    }

    override fun instantiate(
        thisInstance: DataProvider?,
        vararg params: DataProvider
    ) = this

    override fun toString(): String {
        var str = ""
        members.forEach {
            str += "\t$it"
        }
        if (ret != null) {
            str += "\n\t\t$ret"
        }
        return "$str\n"
    }

}