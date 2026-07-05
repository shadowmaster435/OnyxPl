package org.shadowmaster435.code.statement

import org.shadowmaster435.built_ins.OnyxNull
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class OnyxReturnStatement(val retVal: DataProvider?) : OnyxStatement() {
    override var held: Any?
        get() = retVal?.held ?: OnyxNull
        set(_) {}
    override val type = retVal?.type ?: OnyxNull.type
    override var initialized = false
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            retVal?.initialize(namedScopeMembers)
            initialized = true
        }
    }
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) =
        OnyxReturnStatement((retVal?.instantiate(retVal, *params) ?: OnyxNull) as DataProvider)
}