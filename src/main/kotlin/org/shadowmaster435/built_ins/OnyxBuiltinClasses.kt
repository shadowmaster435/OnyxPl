package org.shadowmaster435.built_ins

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.code.OnyxCodeBlock
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.modifiers.AbstractModifier
import org.shadowmaster435.modifiers.PublicModifier


val numberType = OnyxType("Number", OnyxPackages.onyxPrimitives)
val nullType = OnyxType("Null", OnyxPackages.onyxPrimitives)

private val numberCodeBlock = OnyxCodeBlock(listOf(
 //   OnyxFunction("toInt", OnyxInt.type, OnyxTuples.empty, OnyxModifiers(AbstractModifier))
))

open class OnyxNumber(name: String = "Number", supertypes: List<OnyxType>, modifiers: OnyxModifiers = OnyxModifiers(PublicModifier, AbstractModifier)) : OnyxClass(
    modifiers,
    name,
    OnyxPackages.onyxPrimitives,
    listOf(),
    null,
    supertypes
) {
    override fun instantiate(vararg params: DataProvider): OnyxMember? {
        throw RuntimeException("Should never be called")
    }
}

object OnyxIntClass : OnyxNumber("Int", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxLongClass : OnyxNumber("Long", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxBooleanClass : OnyxNumber("Boolean", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxFloatClass : OnyxNumber("Float", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxDoubleClass : OnyxNumber("Double", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxByteClass : OnyxNumber("Byte", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxShortClass : OnyxNumber("Short", listOf(numberType), OnyxModifiers(PublicModifier))
object OnyxStringClass : OnyxNumber("String", listOf(numberType), OnyxModifiers(PublicModifier))
