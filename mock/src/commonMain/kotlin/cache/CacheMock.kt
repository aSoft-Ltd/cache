package cache

import cache.exceptions.CacheMissException
import kotlinx.serialization.KSerializer
import koncurrent.Later
import koncurrent.later

class CacheMock(val config: CacheMockConfig = CacheMockConfig()) : Cache {
    private val cache = config.initialCache

    private val executor get() = config.executor

    private val namespace get() = config.namespace

    override fun keys(): Later<out Set<String>> = executor.later { cache.keys }

    override fun size(): Later<out Int> = executor.later { cache.size }

    override fun <T> save(key: String, obj: T, serializer: KSerializer<T>) = Later(executor) { resolve, _ ->
        cache["$namespace:$key"] = obj
        resolve(obj)
    }

    override fun <T> load(key: String, serializer: KSerializer<T>) = Later(executor) { resolve, reject ->
        val obj = cache["$namespace:$key"]
        if (obj != null) resolve(obj as T) else reject(CacheMissException(key))
    }

    override fun remove(key: String) = executor.later {
        val removed = cache.remove("$namespace:$key")
        if (removed != null) Unit else null
    }

    override fun clear() = executor.later { cache.clear() }

    override fun toString(): String = "CacheMock(namespace=$namespace)"
}