package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.abstracts.AccessModifier
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.ModifierScope

object FinalModifier : Modifier {
    override val modifierScopes: List<ModifierScope> = listOf(ModifierScope.FIELD_DEF, ModifierScope.FUNCTION_DEF, ModifierScope.CONSTRUCTOR_PARAM)
    override val chainableWith = listOf(OverrideModifier::class.java, PublicModifier::class.java, ProtectedModifier::class.java,
        PrivateModifier::class.java, StaticModifier::class.java)
}