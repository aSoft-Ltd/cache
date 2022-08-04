import cache.CacheFile
import cache.CacheFileConfig
import cache.internal.AbstractCacheTest
import cache.save
import expect.expect
import koncurrent.later.flatten
import koncurrent.later.test
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem

import kotlin.test.Test

class CacheJvmFileSystemTest : AbstractCacheTest(CacheFile(config)) {

    companion object {
        private val config = CacheFileConfig(
            fs = FileSystem.SYSTEM,
            path = "/tmp/foundation/cache".toPath(),
            namespace = "test"
        )
    }

    @Test
    fun should_be_using_a_cache_file_object() {
        expect(cache.toString()).toBe("CacheFile(namespace=test)")
    }

    @Test
    fun should_save_a_bunch_of_things_in_the_tmp_dir() = cache.save("author", mapOf("name" to "anderson")).flatten {
        cache.save("hobbies", listOf("Programming", "Gaming", "Tech"))
    }.test()
}