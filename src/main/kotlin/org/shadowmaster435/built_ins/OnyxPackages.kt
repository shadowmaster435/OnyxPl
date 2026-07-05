package org.shadowmaster435.built_ins

import org.shadowmaster435.misc.OnyxPackage

object OnyxPackages {
    val onyxRoot = OnyxPackage("onyx", null)
    val onyxLang: OnyxPackage = onyxRoot/"lang"
    val onyxPrimitives: OnyxPackage = onyxRoot/"lang"/"primitives"
    val onyxUtil: OnyxPackage = onyxRoot/"lang"/"util"
    val onyxClassUtil: OnyxPackage = onyxRoot/"lang"/"util"/"class"
    val onyxCollections: OnyxPackage = onyxRoot/"lang"/"collection"
}