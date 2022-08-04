package cache

import koncurrent.Executor
import koncurrent.Executors
import kotlin.jvm.JvmStatic

interface CacheConfig {
    val namespace: String
    val executor: Executor

    companion object {
        @JvmStatic
        val DEFAULT_NAMESPACE = "app"

        @JvmStatic
        val DEFAULT_EXECUTOR = Executors.default()
    }
}