package org.shadowmaster435.precomp

import org.shadowmaster435.impl.AccessContextual
import org.shadowmaster435.impl.Preprocessor
import org.shadowmaster435.impl.Scoped
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.ScopeType
import org.shadowmaster435.precomp.enums.DefineScope
import org.shadowmaster435.precomp.enums.DefineType
import org.shadowmaster435.util.BindableFunction

class Define(type: DefineType, scope: DefineScope, override val accessType: AccessType) : Preprocessor, Scoped, AccessContextual {
    override val processor = BindableFunction { str: String ->
        ""
    }

    override val static: Boolean = scope == DefineScope.FILE || scope == DefineScope.CLASS_BODY

    override val validScopes = listOf(
        ScopeType.GLOBAL,
        ScopeType.FILE,
        ScopeType.CLASS_BODY,
        ScopeType.FUNCTION_BODY,
    )
}