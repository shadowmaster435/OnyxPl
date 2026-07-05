package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.ModifierScope

object OpenModifier : Modifier {
    override val modifierScopes = listOf(ModifierScope.CLASS, ModifierScope.FUNCTION, ModifierScope.FIELD)
    override val chainableWith = listOf(PublicModifier::class.java, PrivateModifier::class.java, ProtectedModifier::class.java,
        PackageModifier::class.java)
}