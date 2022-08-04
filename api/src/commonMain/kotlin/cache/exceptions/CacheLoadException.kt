package cache.exceptions

open class CacheLoadException(
    key: String,
    message: String = "Failed to load object with key=$key from the cache",
    cause: Throwable? = null
) : CacheException(key, message, cause)