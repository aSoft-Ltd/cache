package cache

import koncurrent.Executor
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

interface CacheFileConfig : CacheConfig {
    val fs: FileSystem
    val dir: Path
    val codec: StringFormat
    val extension: String

    companion object {

        @JvmField
        val DEFAULT_EXTENSION = "cache"

        @JvmField
        val DEFAULT_CODEC = Json { }

        @JvmName("create")
        @JvmStatic
        @JvmOverloads
        operator fun invoke(
            fs: FileSystem,
            path: Path,
            namespace: String = CacheConfig.DEFAULT_NAMESPACE,
            executor: Executor = CacheConfig.DEFAULT_EXECUTOR,
            codec: StringFormat = DEFAULT_CODEC,
            extension: String = DEFAULT_EXTENSION
        ): CacheFileConfig = object : CacheFileConfig {
            override val fs: FileSystem = fs
            override val dir: Path = path
            override val codec: StringFormat = codec
            override val namespace: String = namespace
            override val executor: Executor = executor
            override val extension: String = extension
        }
    }
}