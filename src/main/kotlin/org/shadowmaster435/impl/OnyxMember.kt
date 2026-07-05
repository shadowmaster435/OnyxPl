package org.shadowmaster435.impl

import org.shadowmaster435.impl.enums.ScopeType

interface OnyxMember: Scoped {
    var initialized: Boolean
    override val validScopes: List<ScopeType>
        get() = listOf(ScopeType.FUNCTION_BODY, ScopeType.FILE, ScopeType.CLASS_BODY)
    fun initialize(namedScopeMembers: HashMap<String, OnyxMember>)
    fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember?
}