package org.shadowmaster435.misc

import kotlin.reflect.KProperty

class OnyxFloat(v: Float) : OnyxConstable<Float>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxDouble(v: Double) : OnyxConstable<Double>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxInt(v: Int) : OnyxConstable<Int>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxLong(v: Long) : OnyxConstable<Long>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxShort(v: Short) : OnyxConstable<Short>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxByte(v: Byte) : OnyxConstable<Byte>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxBoolean(v: Boolean) : OnyxConstable<Boolean>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
class OnyxString(v: String) : OnyxConstable<String>(v) {operator fun getValue(ref: Any?, prop: KProperty<*>) = v}
