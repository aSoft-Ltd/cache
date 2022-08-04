package cache

import cache.exceptions.CacheLoadException
import cache.exceptions.CacheSaveException
import kotlinx.serialization.KSerializer
import koncurrent.Later

/**
 * An interface to be able to [Cache] different objects
 */
interface Cache {
    /**
     * Should return the set of all available keys in the [Cache]
     */
    fun keys(): Later<out Set<String>>

    /**
     * Should return the size of the [Cache] which should ideally equal the number of [keys]
     */
    fun size(): Later<out Int>

    /**
     * Clears the entire [Cache]
     */
    fun clear(): Later<out Unit>

    /**
     * Removes a [key] from the [Cache]
     * @return the removed object or null if nothing was removed
     */
    fun remove(key: String): Later<out Unit?>

    /**
     * Save object [T] on to the [Cache] with a [key] and its serializer [serializer]
     *
     * @return a [Later] that
     * - on success: resolves the saved object as it was cached
     * - on failure: rejects with a [CacheSaveException]
     */
    fun <T> save(key: String, obj: T, serializer: KSerializer<T>): Later<out T>

    /**
     * Load object [T] from the [Cache], that was saved with a [key] and its serializer [serializer]
     *
     * @return a [Later] that
     * - on success: resolves to the cached object
     * - on failure: rejects with a [CacheLoadException]
     */
    fun <T> load(key: String, serializer: KSerializer<T>): Later<out T>
}