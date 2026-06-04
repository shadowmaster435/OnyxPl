package org.shadowmaster435.impl.abstracts

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.ModifierScope

abstract class AccessModifier : Modifier {
    override val modifierScopes = listOf(ModifierScope.ACCESS)
    abstract val accessType: AccessType
}