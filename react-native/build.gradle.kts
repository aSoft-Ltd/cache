plugins {
    kotlin("js")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    js(IR) {
        browserLib()
        // do not add node js coz async storage doesn't run in node
    }

    sourceSets {
        val main by getting {
            dependencies {
                api(projects.cacheApi)
                api(kotlinx.serialization.json)
                api(npm("@react-native-async-storage/async-storage", npm.versions.asyncStorage.get()))
            }
        }

        val test by getting {
            dependencies {
                implementation(projects.cacheTest)
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.foundation.get(), description = "An implementation of the cache-api to help caching simple objects on react-native-targets"
)