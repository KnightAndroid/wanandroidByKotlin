package com.knight.kotlin.library_network.log

/**
 * Author:Knight
 * Time:2021/12/22 10:34
 * Description:LoggerPrinter
 */
object LoggerPrinter {

    private val MIN_STACK_OFFSET = 3

    /**
     * It is used for json pretty print
     */
    val JSON_INDENT = 3

    /**
     * Drawing toolbox
     */
    private const val TOP_LEFT_CORNER    = '╔'
    private const val BOTTOM_LEFT_CORNER = '╚'
    private const val MIDDLE_CORNER      = '╟'
    private const val DOUBLE_DIVIDER     = "═════════════════════════════════════════════════"
    private const val SINGLE_DIVIDER     = "─────────────────────────────────────────────────"

    val TOP_BORDER               = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val BOTTOM_BORDER            = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val MIDDLE_BORDER            = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER

    val BR                       = System.getProperty("line.separator")!!     // 换行符
    const val HORIZONTAL_DOUBLE_LINE     = " "
    const val BLANK                      = " "
    const val COMMA                      = ","

    fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LoggerPrinter::class.java.name && name != L::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }
}