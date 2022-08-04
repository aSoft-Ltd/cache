package cache

import cache.exceptions.CacheLoadException
import cache.exceptions.CacheSaveException
import koncurrent.Later
import koncurrent.later.then
import kotlinx.serialization.KSerializer

class CacheFile(val config: CacheFileConfig) : Cache {
    private val namespace get() = config.namespace
    private val fs get() = config.fs
    private val executor get() = config.executor
    private val ext get() = config.extension
    private val codec get() = config.codec

    private val root by lazy {
        if (!fs.exists(config.dir / namespace)) fs.createDirectories(config.dir / namespace)
        config.dir / namespace
    }

    override fun keys(): Later<out Set<String>> = Later(executor) { resolve, reject ->
        try {
            resolve(fs.list(root).map { it.name.replace(".$ext", "") }.toSet())
        } catch (err: Throwable) {
            reject(err)
        }
    }

    override fun size(): Later<out Int> = keys().then { it.size }

    override fun clear(): Later<out Unit> = Later(executor) { resolve, reject ->
        try {
            fs.deleteRecursively(root, mustExist = false)
            fs.createDirectories(root)
            resolve(Unit)
        } catch (err: Throwable) {
            reject(err)
        }
    }

    override fun remove(key: String): Later<out Unit?> = Later(executor) { resolve, _ ->
        val filename = root / "$key.$ext"
        if (fs.exists(filename)) try {
            fs.delete(filename, mustExist = false)
            resolve(Unit)
        } catch (err: Throwable) {
            resolve(null)
        } else resolve(null)
    }

    override fun <T> save(key: String, obj: T, serializer: KSerializer<T>) = Later(executor) { resolve, reject ->
        try {
            val filename = root / "$key.$ext"
            fs.write(filename) { writeUtf8(codec.encodeToString(serializer, obj)) }
            resolve(obj)
        } catch (err: Throwable) {
            reject(CacheSaveException(key, cause = err))
        }
    }

    override fun <T> load(key: String, serializer: KSerializer<T>): Later<out T> = Later(executor) { resolve, reject ->
        try {
            val filename = root / "$key.$ext"
            val content = fs.read(filename) { readUtf8() }
            resolve(codec.decodeFromString(serializer, content))
        } catch (err: Throwable) {
            reject(CacheLoadException(key, cause = err))
        }
    }

    override fun toString() = "CacheFile(namespace=$namespace)"
}