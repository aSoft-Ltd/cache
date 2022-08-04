import cache.internal.AbstractCacheTest
import expect.expect
import cache.CacheMock
import cache.CacheMockConfig
import koncurrent.Executors
import koncurrent.MockExecutor

import kotlin.test.Test

class CacheMockSynchronousExecutorTest : AbstractCacheTest(CacheMock(config)) {

    companion object {
        private val config = CacheMockConfig(
            executor = MockExecutor()
        )
    }

    @Test
    fun should_be_using_a_mock_cache() {
        expect(cache.toString()).toBe("CacheMock(namespace=app)")
    }
}