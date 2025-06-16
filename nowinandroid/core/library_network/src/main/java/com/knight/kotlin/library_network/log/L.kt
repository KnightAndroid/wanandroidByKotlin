package com.knight.kotlin.library_network.log


import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.converter.Converter
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.formatter.BorderFormatter


import com.knight.kotlin.library_network.log.handler.BaseHandler
import com.knight.kotlin.library_network.log.handler.BundleHandler
import com.knight.kotlin.library_network.log.handler.CollectionHandler
import com.knight.kotlin.library_network.log.handler.IntentHandler
import com.knight.kotlin.library_network.log.handler.MapHandler
import com.knight.kotlin.library_network.log.handler.ObjectHandler
import com.knight.kotlin.library_network.log.handler.ReferenceHandler
import com.knight.kotlin.library_network.log.handler.StringHandler
import com.knight.kotlin.library_network.log.handler.ThrowableHandler
import com.knight.kotlin.library_network.log.handler.UriHandler
import com.knight.kotlin.library_network.log.printer.LogcatPrinter
import com.knight.kotlin.library_network.log.printer.Printer


import com.knight.kotlin.library_network.log.utils.getFullStackTrace
import java.util.*


/**
 * Author:Knight
 * Time:2021/12/22 10:34
 * Description:L
 */
object L {

    private var TAG = "SAF_L"
    private var header: String? = ""
    private val handlers = LinkedList<BaseHandler>()
    private var firstHandler: BaseHandler
    private val printers = Collections.synchronizedSet(mutableSetOf<Printer>())
    private var displayThreadInfo:Boolean  = true
    private var displayClassInfo:Boolean   = true
    private var converter: Converter?=null

    init {
        printers.add(LogcatPrinter()) // 默认添加 LogcatPrinter

        handlers.apply {

            add(StringHandler())
            add(CollectionHandler())
            add(MapHandler())
            add(BundleHandler())
            add(IntentHandler())
            add(UriHandler())
            add(ThrowableHandler())
            add(ReferenceHandler())
            add(ObjectHandler())
        }

        val len = handlers.size

        for (i in 0 until len) {
            if (i > 0) {
                handlers[i - 1].setNextHandler(handlers[i])
            }
        }

        firstHandler = handlers[0]
    }

    @JvmStatic
    var logLevel = LogLevel.DEBUG // 日志的等级，可以进行配置，最好在 Application 中进行全局的配置

    /******************* L 的配置方法 Start *******************/

    @JvmStatic
    fun init(clazz: Class<*>): L {
        TAG = clazz.simpleName
        return this
    }

    /**
     * 支持用户自己传 tag，可扩展性更好
     * @param tag
     */
    @JvmStatic
    fun init(tag: String): L {
        TAG = tag
        return this
    }

    /**
     * header 是 L 自定义的内容，可以放 App 的信息版本号等，方便查找和调试
     * @param tag
     */
    @JvmStatic
    fun header(header: String?): L {
        this.header = header
        return this
    }

    /**
     * 自定义 Handler 来解析 Object
     */
    @JvmStatic
    fun addCustomerHandler(handler: BaseHandler): L {

        val size = handlers.size

        return addCustomerHandler(handler, size - 1) // 插入在ObjectHandler之前
    }

    /**
     * 自定义 Handler 来解析 Object，并指定 Handler 的位置
     */
    @JvmStatic
    fun addCustomerHandler(handler: BaseHandler, index: Int): L {

        handlers.add(index, handler)

        val len = handlers.size

        for (i in 0 until len) {
            if (i > 0) {
                handlers[i - 1].setNextHandler(handlers[i])
            }
        }

        return this
    }

    /**
     * 添加自定义的 Printer
     */
    @JvmStatic
    fun addPrinter(printer: Printer): L {

        this.printers.add(printer)
        return this
    }

    /**
     * 删除 Printer
     */
    @JvmStatic
    fun removePrinter(printer: Printer): L {

        this.printers.remove(printer)
        return this
    }

    /**
     * 是否打印线程信息
     */
    @JvmStatic
    fun displayThreadInfo(displayThreadInfo:Boolean): L {

        this.displayThreadInfo = displayThreadInfo
        return this
    }

    /**
     * 是否打印类的信息
     */
    @JvmStatic
    fun displayClassInfo(displayClassInfo:Boolean): L {

        this.displayClassInfo = displayClassInfo
        return this
    }

    /**
     * 用于解析 json 的 Converter
     */
    @JvmStatic
    fun converter(converter:Converter): L {

        this.converter = converter
        return this
    }

    /******************* L 的配置方法 End *******************/

    /******************* L 提供打印的方法 Start *******************/

    @JvmStatic
    fun e(throwable: Throwable?) = e(TAG, getFullStackTrace(throwable))

    @JvmStatic
    fun e(tag: String?, throwable: Throwable?) = e(tag, getFullStackTrace(throwable))

    @JvmStatic
    fun e(msg: String?) = e(TAG,msg)

    @JvmStatic
    fun e(tag: String?, msg: String?) = printLog(LogLevel.ERROR,tag,msg)

    @JvmStatic
    fun w(msg: String?) = w(TAG,msg)

    @JvmStatic
    fun w(tag: String?, msg: String?) = printLog(LogLevel.WARN,tag,msg)

    @JvmStatic
    fun i(msg: String?) = i(TAG,msg)

    @JvmStatic
    fun i(tag: String?, msg: String?) = printLog(LogLevel.INFO,tag,msg)

    @JvmStatic
    fun d(msg: String?) = d(TAG,msg)

    @JvmStatic
    fun d(tag: String?, msg: String?) = printLog(LogLevel.DEBUG,tag,msg)

    /**
     * 使用特定的 printer 进行打印日志
     */
    @JvmStatic
    fun print(logLevel: LogLevel, tag: String?, msg: String?,vararg printers: Printer)  = printLog(logLevel,tag,msg, printers.toMutableSet())

    private fun printLog(logLevel: LogLevel, tag: String?, msg: String?,set: MutableSet<Printer> = printers) {

        if (logLevel.value <= L.logLevel.value) {

            if (tag != null && tag.isNotEmpty() && msg != null && msg.isNotEmpty()) {

                set.map {

                    val s = getMethodNames(it.formatter)

                    if (msg.contains("\n")) {
                        it.printLog(logLevel, tag, String.format(s, msg.replace("\n", "\n${it.formatter.spliter()}")))
                    } else {
                        it.printLog(logLevel, tag, String.format(s, msg))
                    }
                }
            }
        }
    }

    /**
     * 将任何对象转换成json字符串进行打印
     */
    @JvmStatic
    @JvmOverloads
    fun json(obj: Any?, jsonConfig: JSONConfig = JSONConfig(logLevel, TAG, printers)) {

        if (obj == null) {
            e("object is null")
            return
        }

        firstHandler.handleObject(obj,jsonConfig)
    }

    /******************* L 提供打印的方法 End *******************/

    @JvmStatic
    @JvmOverloads
    fun getMethodNames(formatter: Formatter = BorderFormatter): String {

        val sElements = Thread.currentThread().stackTrace

        var stackOffset = LoggerPrinter.getStackOffset(sElements)

        stackOffset++

        return StringBuilder().apply {

            append("  ").append(formatter.top())
        }.apply {

            header?.takeIf { it.isNotEmpty() }?.let {
                // 添加 Header
                append("Header: $it").append(formatter.middle()).append(formatter.spliter())
            }

        }.apply {

            if (displayThreadInfo) {
                // 添加当前线程名
                append("Thread: ${Thread.currentThread().name}")
                    .append(formatter.middle())
                    .append(formatter.spliter())
            }

            if (displayClassInfo) {
                // 添加类名、方法名、行数
                append(sElements[stackOffset].className)
                    .append(".")
                    .append(sElements[stackOffset].methodName)
                    .append(" ")
                    .append("(")
                    .append(sElements[stackOffset].fileName)
                    .append(":")
                    .append(sElements[stackOffset].lineNumber)
                    .append(")")
                    .append(formatter.middle())
                    .append(formatter.spliter())
            }

            append("%s").append(formatter.bottom())

        }.toString()
    }

    @JvmStatic
    fun printers() = printers

    @JvmStatic
    fun getConverter() = converter
}