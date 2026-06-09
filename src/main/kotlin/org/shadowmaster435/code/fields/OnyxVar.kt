package org.shadowmaster435.code.fields

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxModifiers
import java.lang.reflect.Member

class OnyxVar(name: String, provider: DataProvider, modifiers: OnyxModifiers) : OnyxField(name, modifiers, provider) {
    override var held
        get() = provider.held
        set(value) {provider.held = value}

    override fun toString(): String {
        return "$modifiers var $name = $provider"
    }

    override fun instantiate(vararg params: DataProvider): OnyxMember {
        return OnyxVar(name, provider.instantiate(*params) as DataProvider, modifiers)
    }

}