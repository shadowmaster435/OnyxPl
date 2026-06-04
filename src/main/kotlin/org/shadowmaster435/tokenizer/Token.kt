package org.shadowmaster435.tokenizer

class Token(val type: TokenType, val tokenString: String, val scopeLevel: Int, val flags: Int, val sourceStringIndex: Int) {
    val flag0 = (flags and 0b1) != 0
    val flag1 = (flags and 0b10) != 0
    val flag2 = (flags and 0b100) != 0


    fun isInScope(level: Int) = scopeLevel <= level
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (scopeLevel != other.scopeLevel) return false
        if (type != other.type) return false
        if (tokenString != other.tokenString) return false

        return true
    }

    fun hasSubType(type: TokenType.TokenSubtype): Boolean {
        return this.type.subtypes.contains(type)
    }

    override fun hashCode(): Int {
        var result = scopeLevel
        result = 31 * result + type.hashCode()
        result = 31 * result + tokenString.hashCode()
        return result
    }

    override fun toString(): String {
        return "type:$type, value:'$tokenString', scopeLevel:$scopeLevel)"
    }

}