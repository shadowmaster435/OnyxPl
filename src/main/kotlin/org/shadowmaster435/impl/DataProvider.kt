package org.shadowmaster435.impl

interface DataProvider: OnyxMember {
    var held : Any?
    val type: OnyxType
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {}
}
