package cache.exceptions

sealed class CacheException(
    open val key: String,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
