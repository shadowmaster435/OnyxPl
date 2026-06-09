package org.shadowmaster435.misc

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxTuple(optionalVal: DataProvider? = null): OnyxMember {
    override var initialized = false
    val optionalValue = optionalVal
    val optional = optionalValue != null
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            optionalValue?.initialize(namedScopeMembers)
            initialized = true
        }
    }

    override fun instantiate(vararg params: DataProvider): OnyxMember {
        return OnyxTuple(optionalValue)
    }
}