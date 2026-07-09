package org.shadowmaster435.built_ins

import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxBooleanClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxByteClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxDoubleClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxFloatClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxIntClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxLongClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxNumberClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxPointerClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxShortClass
import org.shadowmaster435.built_ins.OnyxBuiltinClasses.OnyxStringClass
import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.classes.anyType
import org.shadowmaster435.code.OnyxCodeBlock
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.statement.OnyxReturnStatement
import org.shadowmaster435.code.statement.OnyxThisStatement
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxPolymorphicType
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.misc.OnyxExpression
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxTuple
import org.shadowmaster435.misc.OnyxTuples
import org.shadowmaster435.modifiers.AbstractModifier
import org.shadowmaster435.modifiers.ExternalModifier
import org.shadowmaster435.modifiers.FinalModifier
import org.shadowmaster435.modifiers.OpenModifier
import org.shadowmaster435.modifiers.OverrideModifier
import org.shadowmaster435.modifiers.PublicModifier
import org.shadowmaster435.operators.other.OnyxAs


private typealias pkg = OnyxPackages
private typealias block = OnyxCodeBlock
private typealias fn = OnyxFunction
private typealias types = List<OnyxType>
private typealias i = OnyxIntClass
private typealias l = OnyxLongClass
private typealias f = OnyxFloatClass
private typealias d = OnyxDoubleClass
private typealias s = OnyxShortClass
private typealias str = OnyxStringClass
private typealias by = OnyxByteClass
private typealias b = OnyxBooleanClass
private typealias mods = OnyxModifiers
private typealias num = OnyxNumberClass
private typealias pub = PublicModifier
private typealias abst = AbstractModifier
private typealias ext = ExternalModifier
private typealias opn = OpenModifier
private typealias tups = OnyxTuples
private typealias tup = OnyxTuple
private typealias ptr = OnyxPointerClass
private typealias over = OverrideModifier
private typealias fin = FinalModifier
private typealias expr = OnyxExpression
private typealias ncast = OnyxAs.NumberCast
private typealias ths = OnyxThisStatement
private typealias rt = OnyxReturnStatement
private typealias nil = OnyxPrimitives.OnyxNull

val numberType = OnyxType("Number", pkg.onyxPrimitives, size =  -1)
val intType = OnyxType("Int", pkg.onyxPrimitives, size = 4)
val longType = OnyxType("Long", pkg.onyxPrimitives, size = 8)
val booleanType = OnyxType("Boolean", pkg.onyxPrimitives, size = 1)
val byteType = OnyxType("Byte", pkg.onyxPrimitives, size = 1)
val shortType = OnyxType("Short", pkg.onyxPrimitives, size = 2)
val floatType = OnyxType("Float", pkg.onyxPrimitives, size = 3)
val doubleType = OnyxType("Double", pkg.onyxPrimitives, size = 8)
val stringType = OnyxType("String", pkg.onyxPrimitives, size = 8)
val nullType = OnyxType("Nothing", pkg.onyxPrimitives, size = -1)
val voidType = OnyxType("Unit", pkg.onyxPrimitives, size = -1)
val typeType = OnyxPolymorphicType("Type", pkg.onyxLang, listOf(anyType), -1, mapOf("T" to anyType))

val classType = OnyxPolymorphicType("Class", pkg.onyxClassUtil, listOf(anyType), -1, mapOf("T" to anyType))
val pointerType = OnyxPolymorphicType("Pointer", pkg.onyxPrimitives, listOf(anyType), -1, mapOf("T" to anyType))
val arrayType = OnyxPolymorphicType("Array", pkg.onyxPrimitives, listOf(anyType), -1, mapOf("T" to anyType))
val collectionType = OnyxPolymorphicType("Collection", pkg.onyxCollections, listOf(anyType), -1, mapOf("T" to anyType))
private val numberClass = OnyxNumberClass()
private infix fun OnyxReturnStatement.Companion.with(provider: DataProvider): rt {
    return OnyxReturnStatement(provider)
}
object OnyxBuiltinClasses {
    object OnyxIntClass : num("Int", listOf(numberType), mods(pub, fin), intType)
    object OnyxLongClass : num("Long", listOf(numberType), mods(pub, fin), longType)
    object OnyxBooleanClass : num("Boolean", listOf(numberType), mods(pub, fin), booleanType)
    object OnyxFloatClass : num("Float", listOf(numberType), mods(pub, fin), floatType)
    object OnyxDoubleClass : num("Double", listOf(numberType), mods(pub, fin), doubleType)
    object OnyxByteClass : num("Byte", listOf(numberType), mods(pub, fin), byteType)
    object OnyxShortClass : num("Short", listOf(numberType), mods(pub, fin), shortType)
    object OnyxStringClass : num("String", listOf(numberType), mods(pub), stringType)
    object OnyxTypeClass : OnyxClass(mods(pub, fin), "Type", pkg.onyxLang)

    internal fun staticInit() {
        i.staticInit()
        l.staticInit()
        b.staticInit()
        f.staticInit()
        d.staticInit()
        by.staticInit()
        s.staticInit()
        str.staticInit()
        i.codeBlock = createNumberCodeBlock(OnyxIntClass)
        l.codeBlock = createNumberCodeBlock(OnyxLongClass)
        f.codeBlock = createNumberCodeBlock(OnyxFloatClass)
        d.codeBlock = createNumberCodeBlock(OnyxDoubleClass)
        by.codeBlock = createNumberCodeBlock(OnyxByteClass)
        s.codeBlock = createNumberCodeBlock(OnyxShortClass)
    }


    open class OnyxNumberClass(name: String = "Number", supertypes: types = listOf(anyType), modifiers: mods = mods(pub, abst), type: OnyxType = numberType, block: block? = numberCodeblock) : OnyxClass(
        modifiers,
        name,
        pkg.onyxPrimitives,
        listOf(),
        block,
        supertypes, type
    ) {
        override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember? {
            throw RuntimeException("Should never be called")
        }
    }

    open class OnyxPointerClass(name: String = "Pointer", modifiers: mods = mods(pub,
        OpenModifier
    )) : OnyxClass(modifiers, name, pkg.onyxPrimitives, supertypes = listOf(anyType), type = pointerType)

}
private val numberCodeblock = run {
    block(listOf(
        fn("toInt", intType, mods(pub, abst), null),
        fn("toLong", longType, mods(pub, abst), null),
        fn("toFloat", floatType, mods(pub, abst), null),
        fn("toDouble", doubleType, mods(pub, abst), null),
        fn("toByte", byteType, mods(pub, abst), null),
        fn("toShort", shortType, mods(pub, abst), null),
    ))
}

private fun createNumberCodeBlock(clazz: OnyxClass): block {
    val i = toIntRet(clazz)
    val l = toLongRet(clazz)
    val f = toFloatRet(clazz)
    val d = toDoubleRet(clazz)
    val b = toByteRet(clazz)
    val s = toShortRet(clazz)
    return block(listOf(
        fn("toInt", intType, mods(pub, over), block(listOf(), true, i)),
        fn("toLong", longType, mods(pub, over), block(listOf(), true, l)),
        fn("toFloat", floatType, mods(pub, over), block(listOf(), true, f)),
        fn("toDouble", doubleType, mods(pub, over), block(listOf(), true, d)),
        fn("toByte", byteType, mods(pub, over), block(listOf(), true, b)),
        fn("toShort", shortType, mods(pub, over), block(listOf(), true, s)),
    ))
}
private fun toFloatRet(c: OnyxClass) = rt with expr(floatType, ncast(c.type, floatType), ths(c))
private fun toDoubleRet(c: OnyxClass) = rt with expr(doubleType, ncast(c.type, doubleType), ths(c))
private fun toIntRet(c: OnyxClass) = rt with expr(intType, ncast(c.type, intType), ths(c))
private fun toLongRet(c: OnyxClass) = rt with expr(longType, ncast(c.type, longType), ths(c))
private fun toShortRet(c: OnyxClass) = rt with expr(shortType, ncast(c.type, shortType), ths(c))
private fun toByteRet(c: OnyxClass) = rt with expr(byteType, ncast(c.type, byteType), ths(c))
private val anyTypeCodeblock = block(listOf(
    fn("hashCode", intType, mods(pub, opn, ext), null),
    fn("toString", stringType, mods(pub, opn, ext), null),
))
