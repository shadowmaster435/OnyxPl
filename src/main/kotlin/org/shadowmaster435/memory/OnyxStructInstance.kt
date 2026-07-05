package org.shadowmaster435.memory

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.classes.OnyxStruct
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType

class OnyxStructInstance(struct: OnyxStruct, val data: Array<out DataProvider>): OnyxMember, DataProvider {
    override var initialized: Boolean = true
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {}
    override var held: Any? = this
    override val type: OnyxType = struct.type
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember? {
        throw RuntimeException("Instantiate should never be called on a class instance")
    }
}