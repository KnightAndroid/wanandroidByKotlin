package com.knight.kotlin.library_network.log

/**
 * Author:Knight
 * Time:2021/12/22 10:45
 * Description:LogLevel
 */
enum class LogLevel {

    ERROR {
        override val value: Int
            get() = 0
    },
    WARN {
        override val value: Int
            get() = 1
    },
    INFO {
        override val value: Int
            get() = 2
    },
    DEBUG {
        override val value: Int
            get() = 3
    };

    abstract val value: Int
}