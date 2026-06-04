package org.shadowmaster435.impl

import org.shadowmaster435.impl.enums.ModifierScope

interface Modifier {
    val modifierScopes: List<ModifierScope>
    val chainableWith: List<Class<out Modifier>>
    fun chainsWith(modifier: Modifier): Boolean {
        return chainableWith.contains(modifier::class.java)
    }
}