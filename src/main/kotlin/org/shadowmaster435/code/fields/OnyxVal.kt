package org.shadowmaster435.code.fields

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxModifiers

class OnyxConst(name: String, provider: DataProvider, modifiers: OnyxModifiers): OnyxVal(name, provider, modifiers) {
    override fun toString(): String {
        return "$modifiers const $name = $provider"
    }
}
open class OnyxVal(name: String, provider: DataProvider, modifiers: OnyxModifiers) : OnyxField(name, modifiers, provider) {
    override var held = provider.held
        set(v) {
            val caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass
            if (caller.isAssignableFrom(this::class.java))
                throw RuntimeException("Cannot set a value or const outside of pre-comp")
            else
                if (!modifiers.isMeta) field = v
                else throw RuntimeException("Cannot set a meta value or meta const")
        }

    override fun toString(): String {
        return "$modifiers val $name: ${provider.type.name} = $provider"
    }

    override fun instantiate(vararg params: DataProvider): OnyxMember {
        return OnyxVal(name, provider.instantiate(*params) as DataProvider, modifiers)
    }
}