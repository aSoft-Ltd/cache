import cache.CacheBrowser
import cache.internal.AbstractCacheTest
import expect.expect

import kotlin.test.Test

class CacheBrowserTest : AbstractCacheTest(CacheBrowser()) {

    @Test
    fun should_be_using_a_mock_cache() {
        expect(cache.toString()).toBe("CacheBrowser(namespace=app)")
    }
}