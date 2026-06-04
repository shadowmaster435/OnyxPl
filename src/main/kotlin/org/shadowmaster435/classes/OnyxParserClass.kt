package org.shadowmaster435.classes

import org.shadowmaster435.misc.OnyxRef
import org.shadowmaster435.tokenizer.Token

abstract class OnyxParserClass {
    abstract val finishedParsing : () -> Boolean
    abstract val supplyToken : (token: Token, index: Int, isRef: Boolean, ref: OnyxRef<*>) -> Unit

}