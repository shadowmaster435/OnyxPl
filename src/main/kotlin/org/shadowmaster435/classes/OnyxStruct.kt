package org.shadowmaster435.classes

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.memory.OnyxStructInstance
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxPackage
import org.shadowmaster435.misc.OnyxTuples

class OnyxStruct(
    val modifiers: OnyxModifiers,
    name: String, pkg: OnyxPackage, val tuples: OnyxTuples,
    val type: OnyxType = OnyxType(name, pkg, listOf(anyType), size = tuples.size)
): OnyxMember {
    override var initialized: Boolean = true


    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {}

    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember {
        tuples.validateStrict(params)
        return OnyxStructInstance(this, params)
    }

}