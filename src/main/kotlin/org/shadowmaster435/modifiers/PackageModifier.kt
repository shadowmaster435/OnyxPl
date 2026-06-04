package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.ModifierScope

object PackageModifier : Modifier {
    override val modifierScopes = listOf(ModifierScope.FUNCTION_DEF, ModifierScope.FIELD_DEF, ModifierScope.CONSTRUCTOR_PARAM)
    override val chainableWith = listOf(AbstractModifier::class.java, OverrideModifier::class.java)
}