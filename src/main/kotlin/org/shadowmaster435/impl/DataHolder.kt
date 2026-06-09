package org.shadowmaster435.impl

interface DataHolder<T> : DataProvider {
    fun <A> set(v: A)
}