package id.co.app.retrofiturlhandler.cache

/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

interface Cache <K,V> {
    fun getMaxSize(): Int

    fun get(key: K): V?

    fun put(key: K, value: V): V?

    fun remove(key: K): V?

    fun containsKey(key: K): Boolean

    fun keySet(): Set<K>

    fun clear()
}