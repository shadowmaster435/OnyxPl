package org.shadowmaster435.code

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxImport(classes: List<OnyxClass>) : OnyxMember {
    override var initialized = true
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {

    }

    override fun instantiate(
        thisInstance: DataProvider?,
        vararg params: DataProvider
    ) = this
}