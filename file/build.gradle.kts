plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    jvm { library() }
    js(IR) { library() }
//    val nativeTargets = nativeTargets(true)
    val nativeTargets = linuxTargets(true)

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.cacheApi)
                api(kotlinx.serialization.json)
                api(squareup.okio.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(projects.cacheTest)
                implementation(squareup.okio.fake)
                implementation(kotlinx.datetime) // was added because squareup.okio.fake kept bringing in an old dependency of kotlinx-datetime which failed the build
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.foundation.get(),
    description = "An implementation of the cache-api to help caching simple objects in memory"
)