package org.shadowmaster435.code.fields

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxModifiers

abstract class OnyxField(val name: String, val modifiers: OnyxModifiers, provider: DataProvider): DataProvider, OnyxMember {
    override var initialized: Boolean = false
    protected var provider: DataProvider = provider; private set
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        namedScopeMembers["field:$name"] = this
        provider.initialize(namedScopeMembers)
        initialized = true
    }

    override val type = provider.type
    internal fun metaSet(v: Any) {
        held = v
    }



}