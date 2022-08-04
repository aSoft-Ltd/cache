package cache

import cache.exceptions.CacheLoadException
import cache.exceptions.CacheSaveException
import koncurrent.Later
import koncurrent.later.catch
import koncurrent.later.then
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Save object [T] on to the [Cache] with a [key]
 *
 * @see [save]
 *
 * @return a [Later] that
 * - on success: resolves the saved object as it was cached
 * - on failure: rejects with a [CacheSaveException]
 */
inline fun <reified T> Cache.save(key: String, obj: T): Later<out T> = try {
    save(key, obj, serializer())
} catch (e: Throwable) {
    Later.reject(CacheSaveException(key, cause = e))
}

/**
 * Save object [T] on to the [Cache] with a [key] and an optional [serializer]
 *
 * @see [Cache.save]
 *
 * @return [Later] that
 * - on success: resolves the saved object as it was cached
 * - on failure: resolves with a null
 */
inline fun <reified T> Cache.saveOrNull(
    key: String, obj: T, serializer: KSerializer<T>? = null
): Later<out T?> = try {
    save(key, obj, serializer ?: serializer())
} catch (e: Throwable) {
    Later.reject(CacheSaveException(key, cause = e))
}.then {
    it as? T
}.catch { null }

/**
 * Load object [T] from the [Cache], that was saved with a [key] with an optional serializer [serializer]
 *
 * @see [load]
 *
 * @return [Later] that
 * - on success: resolves the saved object as it was cached
 * - on failure: resolves with a null
 */
inline fun <reified T> Cache.load(key: String): Later<out T> = try {
    load(key, serializer<T>())
} catch (e: Throwable) {
    Later.reject(CacheLoadException(key, cause = e))
}

/**
 * Load object [T] from the [Cache] with a [key] and an optional [serializer]
 *
 * @see [Cache.load]
 *
 * @return a [Later] that
 * - on success: resolves the saved object as it was cached
 * - on failure: resolves with a null
 */
inline fun <reified T> Cache.loadOrNull(
    key: String, serializer: KSerializer<T>? = null
): Later<out T?> = try {
    load(key, serializer ?: serializer())
} catch (e: Throwable) {
    Later.reject(CacheLoadException(key, cause = e))
}.catch { null }