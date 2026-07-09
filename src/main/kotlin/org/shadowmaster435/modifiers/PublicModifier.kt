package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.abstracts.AccessModifier
import org.shadowmaster435.impl.enums.AccessType

object PublicModifier : AccessModifier() {
    override val accessType = AccessType.PUBLIC
    override val chainableWith = listOf(StaticModifier::class.java, AbstractModifier::class.java, FinalModifier::class.java, MetaModifier::class.java, OpenModifier::class.java, ExternalModifier::class.java)
}