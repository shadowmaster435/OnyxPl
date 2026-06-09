package org.shadowmaster435.memory

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxClassInstance(val clazz: OnyxClass, members: List<OnyxMember>): OnyxMember {
    override var initialized: Boolean = true
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {

    }

    override fun instantiate(vararg params: DataProvider): OnyxMember? {
        throw RuntimeException("Instantiate should never be called on a class instance")
    }

    val members = buildList {
        members.forEach {
            add(it.instantiate())
        }
    }


}