package org.shadowmaster435.code.statement

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType

class OnyxThisStatement(clazz: OnyxClass): DataProvider {

    override var held: Any? = null
    override val type: OnyxType = clazz.type
    override var initialized: Boolean = true
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {

    }

    override fun toString() = "this"
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember {
        return thisInstance!!
    }
}