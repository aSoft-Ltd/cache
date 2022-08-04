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
                if (System.getenv("INCLUDE_BUILD") == "true") {
                    api(asoft.koncurrent.primitives.mock)
                } else {
                    api(project(":koncurrent-primitives-mock"))
                }
                api(projects.cacheApi)
                api(projects.cacheFile)
                api(asoft.kotlinx.collections.atomic)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(projects.cacheTest)
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.root.get(),
    description = "An implementation of the cache-api to help caching simple objects in memory"
)