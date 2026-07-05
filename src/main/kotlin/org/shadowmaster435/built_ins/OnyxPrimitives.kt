package org.shadowmaster435.built_ins

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.misc.OnyxConstable
import kotlin.reflect.KProperty

class OnyxFloat(v: Float) : OnyxConstable(v, OnyxFloatClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxDouble(v: Double) : OnyxConstable(v, OnyxDoubleClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxInt(v: Int) : OnyxConstable(v, OnyxIntClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxLong(v: Long) : OnyxConstable(v, OnyxLongClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxShort(v: Short) : OnyxConstable(v, OnyxShortClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxByte(v: Byte) : OnyxConstable(v, OnyxByteClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxBoolean(v: Boolean) : OnyxConstable(v, OnyxBooleanClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxString(v: String) : OnyxConstable(v, OnyxStringClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}
class OnyxPointer(v: DataProvider, val refType: OnyxType) : OnyxConstable(v, OnyxBooleanClass.type) {
    operator fun getValue(ref: Any?, prop: KProperty<*>) = v
    var pointerData
        get() = (held as? DataProvider)?.held
        set(v) {
            val provider = (held as? DataProvider) ?: throw NullPointerException()
            if (!provider.type.castableTo(refType)) throw ClassCastException("${provider.type} cannot be cast to $refType")
            provider.held = v
        }
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}


object OnyxVoid : OnyxConstable(Unit, voidType), CodeObject {
    override val objType = CodeObjType.DATA
    override var held: Any?
        get() = Unit
        set(_) {}
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}


object OnyxNull : OnyxConstable(Unit, nullType), CodeObject {
    override val objType = CodeObjType.DATA
    override var held: Any?
        get() = null
        set(_) {}
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
}