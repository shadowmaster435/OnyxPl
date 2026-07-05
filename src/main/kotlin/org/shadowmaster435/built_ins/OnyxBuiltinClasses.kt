package org.shadowmaster435.built_ins

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.classes.anyType
import org.shadowmaster435.code.OnyxCodeBlock
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.statement.OnyxReturnStatement
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxPolymorphicType
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.misc.OnyxExpression
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxTuples
import org.shadowmaster435.modifiers.AbstractModifier
import org.shadowmaster435.modifiers.OpenModifier
import org.shadowmaster435.modifiers.PublicModifier
import org.shadowmaster435.operators.other.OnyxAs
import java.util.Objects


val typeType = OnyxType("Type", OnyxPackages.onyxLang, size = 8)
val numberType = OnyxType("Number", OnyxPackages.onyxPrimitives, size =  -1)
val intType = OnyxType("Int", OnyxPackages.onyxPrimitives, size = 4)
val longType = OnyxType("Long", OnyxPackages.onyxPrimitives, size = 8)
val booleanType = OnyxType("Boolean", OnyxPackages.onyxPrimitives, size = 1)
val byteType = OnyxType("Byte", OnyxPackages.onyxPrimitives, size = 1)
val shortType = OnyxType("Short", OnyxPackages.onyxPrimitives, size = 2)
val floatType = OnyxType("Float", OnyxPackages.onyxPrimitives, size = 3)
val doubleType = OnyxType("Double", OnyxPackages.onyxPrimitives, size = 8)
val stringType = OnyxType("String", OnyxPackages.onyxPrimitives, size = 8)
val nullType = OnyxType("Nothing", OnyxPackages.onyxPrimitives, size = -1)
val voidType = OnyxType("Unit", OnyxPackages.onyxPrimitives, size = -1)
val classType = OnyxPolymorphicType("Class", OnyxPackages.onyxClassUtil, listOf(anyType), -1, mapOf("T" to anyType))
val pointerType = OnyxPolymorphicType("Pointer", OnyxPackages.onyxPrimitives, listOf(anyType), -1, mapOf("T" to anyType))
val arrayType = OnyxPolymorphicType("Array", OnyxPackages.onyxPrimitives, listOf(anyType), -1, mapOf("T" to anyType))
val collectionType = OnyxPolymorphicType("Collection", OnyxPackages.onyxCollections, listOf(anyType), -1, mapOf("T" to anyType))

private val numberCodeBlock = OnyxCodeBlock(listOf(
    OnyxFunction("toInt", OnyxIntClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
    OnyxFunction("toLong", OnyxLongClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
    OnyxFunction("toFloat", OnyxFloatClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
    OnyxFunction("toDouble", OnyxDoubleClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
    OnyxFunction("toByte", OnyxByteClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
    OnyxFunction("toShort", OnyxShortClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, AbstractModifier), null),
))

private val anyTypeCodeBlock = OnyxCodeBlock(listOf(
    OnyxFunction("hashCode", OnyxIntClass.type, OnyxTuples.empty, OnyxModifiers(PublicModifier, OpenModifier), null),
))


open class OnyxNumberClass(name: String = "Number", supertypes: List<OnyxType>, modifiers: OnyxModifiers = OnyxModifiers(PublicModifier, AbstractModifier), type: OnyxType) : OnyxClass(
    modifiers,
    name,
    OnyxPackages.onyxPrimitives,
    listOf(),
    numberCodeBlock,
    supertypes, type
) {
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember? {
        throw RuntimeException("Should never be called")
    }
}

open class OnyxPointerClass(name: String = "Pointer", modifiers: OnyxModifiers = OnyxModifiers(PublicModifier,
    OpenModifier
)) : OnyxClass(modifiers, name, OnyxPackages.onyxPrimitives, supertypes = listOf(anyType), type = pointerType)


object OnyxTypeClass : OnyxClass(OnyxModifiers(PublicModifier), "Type", OnyxPackages.onyxLang)


object OnyxIntClass : OnyxNumberClass("Int", listOf(numberType), OnyxModifiers(PublicModifier), intType)
object OnyxLongClass : OnyxNumberClass("Long", listOf(numberType), OnyxModifiers(PublicModifier), longType)
object OnyxBooleanClass : OnyxNumberClass("Boolean", listOf(numberType), OnyxModifiers(PublicModifier), booleanType)
object OnyxFloatClass : OnyxNumberClass("Float", listOf(numberType), OnyxModifiers(PublicModifier), floatType)
object OnyxDoubleClass : OnyxNumberClass("Double", listOf(numberType), OnyxModifiers(PublicModifier), doubleType)
object OnyxByteClass : OnyxNumberClass("Byte", listOf(numberType), OnyxModifiers(PublicModifier), byteType)
object OnyxShortClass : OnyxNumberClass("Short", listOf(numberType), OnyxModifiers(PublicModifier), shortType)
object OnyxStringClass : OnyxNumberClass("String", listOf(numberType), OnyxModifiers(PublicModifier), stringType)
