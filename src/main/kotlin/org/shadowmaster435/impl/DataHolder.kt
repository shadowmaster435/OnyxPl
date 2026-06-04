package org.shadowmaster435.impl

interface DataHolder<T> : DataProvider<T> {
    fun <A> set(v: A)
}