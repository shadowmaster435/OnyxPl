package org.shadowmaster435.classes

import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxRef
import org.shadowmaster435.tokenizer.Token

class OnyxOperatorClass(override val name: String?,
                        override val finishedParsing: () -> Boolean,
                        override val supplyToken: (token: Token, index: Int, isRef: Boolean, ref: OnyxRef<*>) -> Unit
) : OnyxParserClass(), OnyxMember