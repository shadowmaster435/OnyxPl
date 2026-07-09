package org.shadowmaster435.misc

import org.shadowmaster435.built_ins.OnyxBuiltinClasses
import org.shadowmaster435.built_ins.OnyxPackages
import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.fields.OnyxField
import org.shadowmaster435.impl.OnyxMember

class OnyxPackage(val packageName: String, val parent: OnyxPackage?) {
    private val children = hashMapOf<String, OnyxPackage>()
    private val classes = hashMapOf<String, OnyxClass>()
    private val fields = hashMapOf<String, OnyxField>()
    private val functions = hashMapOf<String, OnyxFunction>()
    val qualifiedName = run {
        var cur = ""
        var parent = parent
        while (parent != null) {
            if (parent !== OnyxPackages.globalRoot) cur = "${parent.packageName}.$cur"
            parent = parent.parent
        }
        "$cur$packageName"
    }

    fun addClass(clazz: OnyxClass) {
        classes[clazz.type.name] = clazz
    }
    fun addField(field: OnyxField) {
        fields[field.name] = field
    }
    fun addFunction(function: OnyxFunction) {
        functions[function.name] = function
    }

    fun findClassBySimpleName(name: String): OnyxClass? {
        var clazz: OnyxClass? = null
        forEachClass {
            if (it.type.name == name) {
                clazz = it
                return@forEachClass
            }
        }
        if (clazz == null) {
            children.forEach {
                val subClass = it.value.findClassBySimpleName(name)
                if (subClass != null) return subClass
            }
        }
        return clazz
    }

    fun forEachFunction(consumer: (OnyxFunction) -> Unit) {
        functions.forEach { (_, function) ->
            consumer(function)
        }
    }
    fun forEachClass(consumer: (OnyxClass) -> Unit) {
        classes.forEach { (_, function) ->
            consumer(function)
        }
    }
    fun forEachField(consumer: (OnyxField) -> Unit) {
        fields.forEach { (_, function) ->
            consumer(function)
        }
    }

    fun forEachMember(consumer: (OnyxMember) -> Unit) {
        forEachClass(consumer)
        forEachField(consumer)
        forEachFunction(consumer)
    }

    override fun toString(): String {
        return qualifiedName
    }

    fun packageTreeString(indentLevel: Int = 0): String {
        var str = StringBuilder().repeat(" ", indentLevel).toString() + packageName + "\n"
        for (entry in children) {
            str += entry.value.packageTreeString(indentLevel + 1)
        }
        return str
    }
    fun treeString(indentLevel: Int = 0): String {
        OnyxBuiltinClasses.staticInit()
        var str = StringBuilder().repeat(" ", indentLevel).toString() + packageName + "\n"
        for (clazz in classes) {
            str += clazz.value.type.name + "\n"
        }
        for (entry in children) {
            str += entry.value.packageTreeString(indentLevel + 1)
        }
        return str
    }



    override fun hashCode() = qualifiedName.hashCode()

    override fun equals(other: Any?) =
        if (other !is OnyxPackage) false else qualifiedName == other.qualifiedName

    operator fun div(string: String) = add(string)

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

        private fun getImportPkg(qualifiedName: String) = byQualifiedName(qualifiedName.substring(0..<(qualifiedName.lastIndexOf('.'))))
        private fun getImportQname(qualifiedName: String) = qualifiedName.substring((qualifiedName.lastIndexOf('.') + 1))

        fun getFunction(qualifiedName: String, pkg: OnyxPackage? = null): OnyxFunction? {
            val pkg = pkg ?: getImportPkg(qualifiedName) ?: return null
            val funcName = getImportQname(qualifiedName)
            return pkg.functions[funcName]
        }
        fun getClass(qualifiedName: String, pkg: OnyxPackage? = null): OnyxClass? {
            val pkg = pkg ?: getImportPkg(qualifiedName) ?: return null
            val className = getImportQname(qualifiedName)
            return pkg.classes[className]
        }
        fun getField(qualifiedName: String, pkg: OnyxPackage? = null): OnyxField? {
            val pkg = pkg ?: getImportPkg(qualifiedName) ?: return null
            val fieldName = getImportQname(qualifiedName)
            return pkg.fields[fieldName]
        }

        fun getMember(qualifiedName: String, pkg: OnyxPackage? = null): OnyxMember? =
            getClass(qualifiedName, pkg) ?: getField(qualifiedName, pkg) ?: getFunction(qualifiedName, pkg)


        fun byQualifiedName(qualifiedName: String) = byQualifiedName(OnyxPackages.globalRoot, qualifiedName)

        fun byQualifiedName(pkg: OnyxPackage, qualifiedName: String): OnyxPackage? {
            val arr = qualifiedName.split(".")
            var pkg = pkg
            for (str in arr) {
                pkg = pkg.children[str] ?: return null
            }
            return pkg
        }
    }

}