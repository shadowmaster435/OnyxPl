package org.shadowmaster435.misc

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType

class OnyxTuple(optionalVal: DataProvider? = null, val type: OnyxType, val name: String): OnyxMember {
    override var initialized = false
    val optionalValue = optionalVal
    val optional = optionalValue != null
    constructor(type: OnyxType, name: String) : this(null, type, name)

    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            optionalValue?.initialize(namedScopeMembers)
            initialized = true
        }
    }

    override fun toString() = "$name: $type"
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember {
        return OnyxTuple(params.getOrNull(0) ?: optionalValue, type, name)
    }
}