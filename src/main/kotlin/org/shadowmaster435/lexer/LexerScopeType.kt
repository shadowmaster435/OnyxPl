package org.shadowmaster435.lexer

enum class LexerScopeType(grouperType: Int = 0, val supportsBodyTypeNesting: Boolean = false, val supportsFunctionCalls: Boolean = false, val polymorphic: Boolean = false) {
    GLOBAL(0, true),
    FILE(0, true),
    IMPORT(0, false),
    PACKAGE(0, false),

    EXPRESSION(1, false, true),
    
    GETTER_BODY(1, true, true),
    SETTER_PARAM,
    SETTER_BODY(1, true, true),

    TYPEALIAS,
    ASSIGNMENT(0, false, true),

    DATATYPE_POLYMORPHIC_INPUT(1, polymorphic = true),
    
    STATEMENT_BODY(1, true, true),
    IF_EXPRESSION(1, false, true),
    WHILE_EXPRESSION(1, false, true),
    WHEN_BRANCH_COND,
    WHEN_BRANCH_EXPRESSION(0, false, true),
    WHEN_BRANCH_BODY(1, true),

    INTERFACE_HEADER,
    INTERFACE_BODY,

    OBJECT_HEADER,
    ANONYMOUS_OBJECT_HEADER,


    ENUM_HEADER,
    ENUM_VALUES,
    ENUM_BODY,


    CLASS_BODY(1, true),
    CLASS_HEADER,
    CLASS_POLYMORPHIC_TYPEDEF(1, polymorphic = true),
    CLASS_EXTENSION_POLYMORPHIC_INPUTS(polymorphic = true),

    CONSTRUCTOR_BODY(1, true, true),
    CONSTRUCTOR_PARAMS(1, true, true),
    CONSTRUCTOR_DEFAULT_PARAM(0, false, true),
    CONSTRUCTOR_EXTENSION,
    CONSTRUCTOR_EXTENSION_PARAMS(1, false, true),
    
    FUNCTION_BODY(1, true, true),
    FUNCTION_HEADER,
    FUNCTION_PARAMS(1, false, true),
    FUNCTION_DEFAULT_PARAM(0, false, true),
    FUNCTION_CALL_INPUTS(1, false, true),
    FUNCTION_CALL_POLYMORPHIC_INPUTS(1, polymorphic = true),
    FUNCTION_POLYMORPHIC_TYPEDEF(1, polymorphic = true),

    STRUCT_BODY(1, true),
    STRUCT_PARAMS(1, false, true),
    STRUCT_DEFAULT_PARAM(supportsFunctionCalls = true),
    STRUCT_POLYMORPHIC_TYPEDEF(1, polymorphic = true),
    STRUCT_HEADER,

    STRING,
    STRING_TEMPLATE,
    STRING_TEMPLATE_LAMBDA(1, true, true),
    
    LAMBDA_HEADER,
    LAMBDA_BODY(1, true, true),

    MODIFIERS,

    DEFINE,
    UNDEFINE;
    val isOpener = grouperType == 1
    val isCloser = grouperType == 2

    

}