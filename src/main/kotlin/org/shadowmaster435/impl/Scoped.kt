package org.shadowmaster435.impl

import org.shadowmaster435.impl.enums.ScopeType

interface Scoped {
    val validScopes: List<ScopeType>
}