package org.shadowmaster435.built_ins

import org.shadowmaster435.misc.OnyxPackage

object OnyxPackages {
    val onyxRoot = OnyxPackage("onyx", null)
    val onyxTypes: OnyxPackage = onyxRoot.add("lang").add("types")
    val onyxPrimitives: OnyxPackage = onyxRoot.add("lang").add("primitives")
}