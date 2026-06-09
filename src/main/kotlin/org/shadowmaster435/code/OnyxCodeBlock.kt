package org.shadowmaster435.code

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxCodeBlock(val members: List<OnyxMember>, val needsReturn: Boolean = true, val ret: DataProvider? = null): OnyxMember {
    val last = members.lastOrNull()
    override var initialized: Boolean = false

    fun execute() {
        members.forEach {
            when(it) {
                is OnyxCodeBlock -> it.execute()
                is DataProvider -> it.held
            }
        }
    }

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            members.forEach {
                it.initialize(namedScopeMembers)
            }
            initialized = true
        }
    }


    override fun instantiate(vararg params: DataProvider) = this

}