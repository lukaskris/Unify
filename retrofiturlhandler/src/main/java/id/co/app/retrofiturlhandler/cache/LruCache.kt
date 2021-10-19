package id.co.app.retrofiturlhandler.cache

import kotlin.math.roundToInt


/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

open class LruCache<K, V>(size: Int) : Cache<K,V> {
    private val cache: LinkedHashMap<K, V> = LinkedHashMap(100, 0.75f, true)
    private var initialMaxSize = size
    private var maxSize = size
    private var currentSize = 0


    @Synchronized
    fun setSizeMultiplier(multiplier: Float) {
        require(multiplier >= 0) { "Multiplier must be >= 0" }
        maxSize = (initialMaxSize * multiplier).roundToInt()
        evict()
    }

    protected open fun getItemSize(item: V?): Int {
        return 1
    }

    protected open fun onItemEvicted(key: K, value: V) {
        // optional override
    }

    @Synchronized
    override fun getMaxSize(): Int {
        return maxSize
    }

    @Synchronized
    override fun get(key: K): V? {
        return cache[key]
    }

    @Synchronized
    override fun put(key: K, value: V): V? {
        val itemSize = getItemSize(value)

        if(itemSize >= maxSize){
            onItemEvicted(key, value)
            return null
        }

        val result = cache.put(key, value)
        if(value != null) currentSize += getItemSize(value)
        else currentSize -= getItemSize(result)
        evict()

        return result
    }

    @Synchronized
    override fun remove(key: K): V? {
        val value = cache.remove(key)
        if(value != null) currentSize -= getItemSize(value)
        return value
    }

    @Synchronized
    override fun containsKey(key: K): Boolean {
        return cache.containsKey(key)
    }

    @Synchronized
    override fun keySet(): Set<K> {
        return cache.keys
    }

    override fun clear() {
        trimToSize(0)
    }

    @Synchronized
    protected open fun trimToSize(size: Int) {
        var last: Map.Entry<K, V>
        while (currentSize > size) {
            last = cache.entries.iterator().next()
            val toRemove = last.value
            currentSize -= getItemSize(toRemove)
            val key = last.key
            cache.remove(key)
            onItemEvicted(key, toRemove)
        }
    }

    private fun evict() {
        trimToSize(maxSize)
    }
}