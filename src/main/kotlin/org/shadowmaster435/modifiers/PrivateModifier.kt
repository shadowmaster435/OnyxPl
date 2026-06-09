package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.abstracts.AccessModifier
import org.shadowmaster435.impl.enums.AccessType

object PrivateModifier : AccessModifier() {
    override val accessType = AccessType.PRIVATE
    override val chainableWith = listOf(StaticModifier::class.java, AbstractModifier::class.java, FinalModifier::class.java, MetaModifier::class.java)
}