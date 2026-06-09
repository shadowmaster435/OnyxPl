package org.shadowmaster435.built_ins

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.misc.OnyxConstable
import kotlin.reflect.KProperty

class OnyxFloat(v: Float) : OnyxConstable(v, OnyxFloatClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxDouble(v: Double) : OnyxConstable(v, OnyxDoubleClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxInt(v: Int) : OnyxConstable(v, OnyxIntClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxLong(v: Long) : OnyxConstable(v, OnyxLongClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxShort(v: Short) : OnyxConstable(v, OnyxShortClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxByte(v: Byte) : OnyxConstable(v, OnyxByteClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxBoolean(v: Boolean) : OnyxConstable(v, OnyxBooleanClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxString(v: String) : OnyxConstable(v, OnyxStringClass.type) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
object OnyxNull : OnyxConstable(Unit, nullType), CodeObject {
    override val objType = CodeObjType.DATA
    override var held: Any?
        get() = null
        set(_) {}
}