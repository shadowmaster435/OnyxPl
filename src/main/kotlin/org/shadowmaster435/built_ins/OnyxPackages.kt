package org.shadowmaster435.built_ins

import org.shadowmaster435.misc.OnyxPackage

object OnyxPackages {
    internal fun init() {} //static init
    val globalRoot = OnyxPackage("", null)
    val onyxRoot = globalRoot/"onyx"
    val onyxLang: OnyxPackage = onyxRoot/"lang"
    val onyxPrimitives: OnyxPackage = onyxRoot/"lang"/"primitives"
    val onyxUtil: OnyxPackage = onyxRoot/"lang"/"util"
    val onyxClassUtil: OnyxPackage = onyxRoot/"lang"/"util"/"class"
    val onyxCollections: OnyxPackage = onyxRoot/"lang"/"collection"
}