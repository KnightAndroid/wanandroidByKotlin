package com.knight.library.cryption

class NativeLib {

    /**
     * A native method that is implemented by the 'cryption' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'cryption' library on application startup.
        init {
            System.loadLibrary("cryption")
        }
    }
}