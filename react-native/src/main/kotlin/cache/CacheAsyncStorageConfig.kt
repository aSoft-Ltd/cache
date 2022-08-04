package cache

import cache.npm.AsyncStorage
import cache.npm.ReactNativeAsyncStorage
import koncurrent.Executor
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json

interface CacheAsyncStorageConfig : CacheConfig {
    val storage: ReactNativeAsyncStorage
    val codec: StringFormat

    companion object {
        val DEFAULT_CODEC = Json { }

        operator fun invoke(
            namespace: String = CacheConfig.DEFAULT_NAMESPACE,
            codec: StringFormat = DEFAULT_CODEC,
            executor: Executor = CacheConfig.DEFAULT_EXECUTOR
        ) = object : CacheAsyncStorageConfig {
            override val namespace = namespace
            override val storage = AsyncStorage
            override val codec = codec
            override val executor: Executor = executor
        }
    }
}