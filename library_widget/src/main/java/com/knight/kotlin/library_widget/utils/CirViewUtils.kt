package com.knight.kotlin.library_widget.utils

import android.annotation.SuppressLint
import android.app.Activity
import com.knight.kotlin.library_widget.loadcircleview.ProgressHud

/**
 * Author:Knight
 * Time:2022/3/28 16:22
 * Description:CirViewUtils
 */
class CirViewUtils {

      companion object {
          @SuppressLint("StaticFieldLeak")
          private var mProgressHud: ProgressHud?=null
          fun showHud(activity: Activity, message:String) {
              mProgressHud = ProgressHud(activity,message)
              mProgressHud?.show()

          }

          fun dismissHud() {
              if (mProgressHud != null) {
                  mProgressHud?.dismiss()
                  mProgressHud == null
              }
          }
      }
}