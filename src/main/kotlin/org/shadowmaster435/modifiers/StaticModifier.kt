package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.ModifierScope

object StaticModifier : Modifier {
    override val modifierScopes = listOf(ModifierScope.FIELD_DEF, ModifierScope.FUNCTION_DEF)
    override val chainableWith = listOf(PublicModifier::class.java, FinalModifier::class.java, MetaModifier::class.java)
}