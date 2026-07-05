package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.abstracts.AccessModifier
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.ModifierScope

object MetaModifier : Modifier {

    override val modifierScopes = listOf(ModifierScope.CLASS_DEF, ModifierScope.FIELD_DEF, ModifierScope.FUNCTION_DEF)

    override val chainableWith = listOf(
        StaticModifier::class.java,
        AbstractModifier::class.java,
        ProtectedModifier::class.java,
        PublicModifier::class.java,
        PackageModifier::class.java,
        GlobalModifier::class.java,
        OpenModifier::class.java
    )
}