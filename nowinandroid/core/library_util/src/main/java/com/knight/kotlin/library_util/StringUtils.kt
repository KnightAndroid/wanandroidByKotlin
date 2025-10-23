package com.knight.kotlin.library_util

import android.R
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Author:Knight
 * Time:2021/12/29 14:37
 * Description:StringUtils
 */
object StringUtils {
    /**
     * is null or its length is 0 or it is made by space
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
    </pre> *
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    fun isBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }

    /**
     * is null or its length is 0
     *
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
    </pre> *
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     */
    fun isEquals(actual: String?, expected: String?): Boolean {
        return ObjectUtils.isEquals(
            actual,
            expected
        )
    }

    /**
     * null string to empty string
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
    </pre> *
     *
     * @param str
     * @return
     */
    fun nullStrToEmpty(str: String?): String? {
        return str ?: ""
    }

    /**
     * capitalize first letter
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
    </pre> *
     *
     * @param str
     * @return
     */
    fun capitalizeFirstLetter(str: String): String? {
        if (isEmpty(str)) {
            return str
        }
        val c = str[0]
        return if (!Character.isLetter(c) || Character.isUpperCase(c)) str else StringBuilder(str.length)
            .append(Character.toUpperCase(c)).append(str.substring(1)).toString()
    }

    /**
     * encoded in utf-8
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
    </pre> *
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    fun utf8Encode(str: String): String? {
        return if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException("UnsupportedEncodingException occurred. ", e)
            }
        } else str
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    fun utf8Encode(str: String, defultReturn: String?): String? {
        return if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                defultReturn
            }
        } else str
    }

    /**
     * get innerHtml from href
     *
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
    </pre> *
     *
     * @param href
     * @return
     *  * if href is null, return ""
     *  * if not match regx, return source
     *  * return the last string that match regx
     *
     */
    fun getHrefInnerHtml(href: String?): String? {
        if (isEmpty(href)) {
            return ""
        }
        val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
        val hrefPattern: Pattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE)
        val hrefMatcher: Matcher = hrefPattern.matcher(href)
        return if (hrefMatcher.matches()) {
            hrefMatcher.group(1)
        } else href
    }

    /**
     * process special char in html
     *
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
    </pre> *
     *
     * @param source
     * @return
     */
    fun htmlEscapeCharsToString(source: String): String? {
        return if (isEmpty(source)) source else source.replace("&lt;".toRegex(), "<")
            .replace("&gt;".toRegex(), ">")
            .replace("&amp;".toRegex(), "&").replace("&quot;".toRegex(), "\"")
    }

    /**
     * transform half width char to full width char
     *
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
    </pre> *
     *
     * @param s
     * @return
     */
    fun fullWidthToHalfWidth(s: String): String? {
        if (isEmpty(s)) {
            return s
        }
        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i] == (12288 as Char)) {
                source[i] = ' '
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 as Char && source[i] <= 65374 as Char) {
                source[i] = (source[i] - 65248) as Char
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    /**
     * transform full width char to half width char
     *
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
    </pre> *
     *
     * @param s
     * @return
     */
    fun halfWidthToFullWidth(s: String): String? {
        if (isEmpty(s)) {
            return s
        }
        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i] == ' ') {
                source[i] = 12288.toChar()
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 as Char && source[i] <= 126 as Char) {
                source[i] = (source[i] + 65248) as Char
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    fun unicodeToString(str: String): String? {
        var str = str
        val pattern: Pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))")
        val matcher: Matcher = pattern.matcher(str)
        var ch: Char
        while (matcher.find()) {
            ch = matcher.group(2).toInt(16) as Char
            str = str.replace(matcher.group(1), ch.toString() + "")
        }
        return str
    }

    fun insertToString(item: String, q: String): String? {
        val itemChar = item.toCharArray()
        val builder = StringBuilder()
        for (i in itemChar.indices) {
            builder.append(itemChar[i].toString() + q)
        }
        return builder.toString()
    }

    /**
     * 大于13位的把中间那几位去掉
     *
     * @param address
     * @return
     */
    fun splitAddress(address: String): String? {
        return if (address.length > 13) {
            val sb = StringBuilder()
            sb.append(address, 0, 6)
            sb.append("...")
            //拼上最后四位
            sb.append(address, address.length - 5, address.length)
            sb.toString()
        } else {
            address
        }
    }

    /**
     *
     * 匹配字符串
     * @param context
     * @param str
     * @param keywords
     * @return
     */
    fun getStyle(
        context: Context,
        str: String?,
        keywords: String?
    ): SpannableStringBuilder { // 将字符串中所有与关键字相同的部分标红
        var str = str
        var keywords = keywords
        str = str ?: ""
        keywords = keywords ?: ""
        val style = SpannableStringBuilder(str)
        var sonStr: String
        if (str.isNotEmpty() && keywords.isNotEmpty()) {
            val upperStr = str.uppercase(Locale.CHINESE)
            val upperKeywords = keywords.uppercase(Locale.CHINESE)
            for (i in str.indices) {
                // 只检查从 i 开始的子串是否以关键字开头
                if (upperStr.startsWith(upperKeywords, i)) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(context, R.color.holo_red_light)),
                        i, i + keywords.length,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
            }
        }
        return style
    }


    /**
     * 转16进制字符串
     * @param data 数据
     * @return 16进制字符串
     *
     */
    fun bytesToHex(data:ByteArray):String {
        val sb = StringBuilder()
        var stmp:String?
        for (element in data) {
            stmp = Integer.toHexString(element.toInt() and 0xFF)
            if (stmp.length == 1) sb.append("0")
            sb.append(stmp)
        }
        return sb.toString().uppercase(Locale.CHINA)
    }

    /**
     * 取SHA1
     * @param data 数据
     * @return 对应的hash值
     */
    fun getHashByString(data:String) :ByteArray {
        try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(data.toByteArray(charset("UTF-8")))
            return messageDigest.digest()
        } catch (e: Exception) {
            return "".toByteArray()
        }
    }
}