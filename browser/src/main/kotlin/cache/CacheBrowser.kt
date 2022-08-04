package cache

import cache.exceptions.CacheLoadException
import cache.exceptions.CacheMissException
import kotlinx.serialization.KSerializer
import koncurrent.Later
import koncurrent.later

class CacheBrowser(val config: CacheBrowserConfig = CacheBrowserConfig()) : Cache {
    private val storage get() = config.storage

    private val executor get() = config.executor

    private val json get() = config.codec

    private val namespace get() = config.namespace

    override fun size() = executor.later { storage.length }

    override fun keys() = executor.later {
        buildSet {
            for (i in 0 until storage.length) add(storage.key(i) as String)
        }
    }

    override fun <T> save(key: String, obj: T, serializer: KSerializer<T>) = executor.later {
        storage.setItem("${namespace}:${key}", json.encodeToString(serializer, obj))
        obj
    }

//    override fun <T> load(key: String, serializer: KSerializer<T>): Later<out T> = executor.later {
//        val js = storage.getItem("${namespace}:${key}") ?: throw CacheMissException(key)
//        json.decodeFromString(serializer, js)
//    }

    override fun <T> load(key: String, serializer: KSerializer<T>): Later<out T> = Later(executor) { res, rej ->
        val js = storage.getItem("${namespace}:${key}")
        if (js != null) try {
            res(json.decodeFromString(serializer, js))
        } catch (err: Throwable) {
            rej(CacheLoadException(key, cause = err))
        } else rej(CacheMissException(key))
    }

    override fun remove(key: String): Later<out Unit?> = executor.later {
        val item = storage.getItem("${namespace}:${key}")
        storage.removeItem("${namespace}:${key}")
        if (item != null) Unit else null
    }

    override fun clear(): Later<out Unit> = executor.later {
        storage.clear()
    }

    override fun toString(): String = "CacheBrowser(namespace=$namespace)"
}