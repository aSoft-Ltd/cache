@file:JsModule("@react-native-async-storage/async-storage")
@file:JsNonModule

package cache.npm

import koncurrent.Promise

/*
Types can be found here: https://github.com/react-native-async-storage/async-storage/blob/master/types/index.d.ts
 */
external interface ReactNativeAsyncStorage {
    fun getAllKeys(): Promise<Array<String>>
    fun setItem(key: String, value: String): Promise<Unit>
    fun getItem(key: String): Promise<String?>
    fun removeItem(key: String): Promise<Unit>
    fun clear(): Promise<Unit>
}

@JsName("default")
external val AsyncStorage: ReactNativeAsyncStorage