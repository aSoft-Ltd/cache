package cache

import cache.exceptions.CacheLoadException
import cache.exceptions.CacheMissException
import kotlinx.serialization.KSerializer
import koncurrent.Later
import koncurrent.later.asLater
import koncurrent.later.flatten
import koncurrent.later.then

class CacheAsyncStorage(val config: CacheAsyncStorageConfig = CacheAsyncStorageConfig()) : Cache {

    private val namespace get() = config.namespace

    private val storage get() = config.storage

    private val executor get() = config.executor

    private val codec get() = config.codec

    override fun <T> save(
        key: String, obj: T,
        serializer: KSerializer<T>
    ): Later<out T> = storage.setItem("$namespace:$key", codec.encodeToString(serializer, obj)).asLater().then(executor) { obj }

    override fun <T> load(key: String, serializer: KSerializer<T>) = Later(executor) { resolve, reject ->
        storage.getItem("$namespace:$key").asLater().then {
            if (it != null) try {
                resolve(codec.decodeFromString(serializer, it))
            } catch (err: Throwable) {
                reject(CacheLoadException(key, cause = err))
            } else reject(CacheMissException(key))
        }
    }

    override fun keys() = storage.getAllKeys().asLater().then { it.toSet() }

    override fun size() = storage.getAllKeys().asLater().then { it.size }

    override fun clear() = storage.clear().asLater()

    override fun remove(key: String): Later<out Unit?> {
        val fullKey = "$namespace:$key"
        return storage.getItem(fullKey).asLater().flatten { res ->
            if (res == null) {
                Later.resolve<Unit?>(null)
            } else {
                storage.removeItem(fullKey).asLater().then { it }
            }
        }
    }

    override fun toString(): String = "CacheAsyncStorage(namespace=$namespace)"
}