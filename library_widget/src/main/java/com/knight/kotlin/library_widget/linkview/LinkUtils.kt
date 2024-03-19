package com.knight.kotlin.library_widget.linkview

import android.util.Log




/**
 * Author:Knight
 * Time:2024/3/18 15:37
 * Description:LinkUtils
 */
class LinkUtils {

    companion object {


        private fun isValidRegex(regex: String?): Boolean {
            return !regex.isNullOrEmpty() && regex.length > 2
        }


        fun getRegexByAutoLinkMode(anAutoLinkMode: LinkMode?, customRegex: String?): String {
            return when (anAutoLinkMode) {
                LinkMode.MODE_HASHTAG -> RegexParser.HASHTAG_PATTERN
                LinkMode.MODE_MENTION -> RegexParser.MENTION_PATTERN
                LinkMode. MODE_URL -> RegexParser.URL_PATTERN
                LinkMode.MODE_PHONE -> RegexParser.PHONE_PATTERN
                LinkMode.MODE_EMAIL -> RegexParser.EMAIL_PATTERN
                LinkMode. MODE_CUSTOM -> if (!isValidRegex(customRegex)) {
                    Log.e(LinkTextView.TAG, "Your custom regex is null, returning URL_PATTERN")
                    RegexParser.URL_PATTERN
                } else {
                    customRegex ?: RegexParser.URL_PATTERN
                }

                else -> RegexParser.URL_PATTERN
            }
        }
    }
}