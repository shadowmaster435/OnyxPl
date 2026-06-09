package org.shadowmaster435.misc

import org.shadowmaster435.classes.OnyxClass

class OnyxPackage(val packageName: String, val parent: OnyxPackage?) {
    private val children = hashMapOf<String, OnyxPackage>()
    private val classes = hashMapOf<String, OnyxClass>()
    val qualifiedName = run {
        var cur = ""
        while (parent != null) cur += "${parent.packageName}."
        "$cur$packageName"
    }

    fun add(packageName: String): OnyxPackage {
        var wasPresent = true
        val new = children[packageName] ?: run {
            wasPresent = false
            OnyxPackage(packageName, this)
        }
        if (!wasPresent) children[packageName] = new
        return new
    }
    fun byQualifiedNameFromCurrent(qualifiedName: String) = byQualifiedName(this, qualifiedName)
    companion object {

        fun byQualifiedName(pkg: OnyxPackage, qualifiedName: String): OnyxPackage {
            val arr = qualifiedName.split(".")
            var pkg = pkg
            for (str in arr) {
                pkg = pkg.children[str]!!
            }
            return pkg
        }
    }

}