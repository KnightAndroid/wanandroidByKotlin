package com.knight.kotlin.library_util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.core.library_common.ktx.getApplicationContext
import java.lang.reflect.InvocationTargetException
import java.util.UUID

/**
 * Author:Knight
 * Time:2023/5/11 11:11
 * Description:PhoneUtils
 */
object PhoneUtils {


    /**
     *
     * 获取DeviceId
     */
    @SuppressLint("MissingPermission")
    fun getDeviceId():String {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             return ""
         }
         val tm = getTelephontManager()
         val deviceId = tm.deviceId
         if (!deviceId.isNullOrEmpty()) return deviceId
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val imei = tm.imei
             if (!imei.isNullOrEmpty()) return imei
             val meid = tm.meid
             return if (meid.isNullOrEmpty()) "" else meid
         }
         return ""
     }

    /**
     * 获取DeviceId
     * @param context
     *
     *
     */
    fun getDeviceUUID(context: Context) :String {
        val sbDeviceId = StringBuilder()
        val imei = getIMEI()
        val androidId = getAndroidId(context)
        val serial = getSerial()
        val id = getDeviceUUID().replace("-","")
        if (imei.isNotEmpty()) {
            sbDeviceId.append(imei)
            sbDeviceId.append("|")
        }
        if (androidId.isNotEmpty()) {
            sbDeviceId.append(androidId)
            sbDeviceId.append("|")
        }

        if (serial.isNotEmpty()) {
            sbDeviceId.append(serial)
            sbDeviceId.append("|")
        }

        if (id.isNotEmpty()) {
            sbDeviceId.append(id)
        }

        //一系列的字符串 ----11 硬件标识有关 手机
        //生成SHA1，统一DeviceId长度
        if (sbDeviceId.isNotEmpty()) {
            try {
                val hash: ByteArray = StringUtils.getHashByString(sbDeviceId.toString())
                val sha1 = StringUtils.bytesToHex(hash)
                if (sha1.isNotEmpty()) {
                    //返回最终的DeviceId
                    return sha1
                }
            } catch (ex:Exception) {
                ex.printStackTrace()
            }

        }
        return ""
    }

    /**
     * 获取IMEI
     *
     */
    private fun getIMEI():String {
         return getImeiOrMeid(true)
    }

    /**
     *
     * 获取手机型号
     */
    fun getPhoneModel():String {
        return Build.MODEL
    }

    /**
     *
     * 获得设备的AndroidId 使用Android 0以上时,AndroidId的行为将发生变化，每个用户的每个应用程序的AndroidId都不一样
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private fun getAndroidId(context:Context) :String {
        return Settings.Secure.getString(context.contentResolver,Settings.Secure.ANDROID_ID)
    }


    /**
     * 获得硬件uuid(根据硬件相关属性，生成uuid) 数字0-10
     *
     */
    private fun getDeviceUUID() :String {
        val dev = "100001${Build.BOARD}${Build.BRAND}${Build.DEVICE}${Build.HARDWARE}${Build.ID}${Build.MODEL}${Build.PRODUCT}${Build.SERIAL}"
        return UUID(dev.hashCode().toLong(),Build.SERIAL.hashCode().toLong()).toString()
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    fun getSerial():String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return try {
                Build.getSerial()
            } catch (e:SecurityException) {
                e.printStackTrace()
                ""
            }
        }
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Build.getSerial() else Build.SERIAL
    }


    /**
     * 获取IMEI或者Meid
     *
     */
    @SuppressLint("MissingPermission")
    fun getImeiOrMeid(isImei:Boolean) :String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ""
        }
        val tm = getTelephontManager()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            return if (isImei) getMinOne(tm.getImei(0),tm.getImei(1)) else getMinOne(tm.getMeid(0),tm.getMeid(1))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val ids = getSystemPropertyByReflect(if (isImei) "ril.gsm.imei" else "ril.cdma.meid")
            if (ids.isNotEmpty()) {
                val idArr:Array<String> = ids.split(",".toRegex()).toTypedArray()
                return if (idArr.size == 2) {
                    getMinOne(idArr[0],idArr[1])
                } else {
                    idArr[0]
                }
            }
            var id0 = tm.deviceId
            var id1 = ""
            try {
                val method = tm.javaClass.getMethod("getDeviceId", Int::class.javaPrimitiveType)
                id1 = method.invoke(tm, if (isImei) TelephonyManager.PHONE_TYPE_GSM else TelephonyManager.PHONE_TYPE_CDMA) as String
            } catch (e : NoSuchMethodException) {
                e.printStackTrace()
            } catch (e:IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            if (isImei) {
                if (id0 != null && id0.length < 15) {
                    id0 = ""
                }

                if (id1.length < 15) {
                    id1 = ""
                }
            } else {
                if (id0 != null && id1.length == 14) {
                    id0 = ""
                }

                if (id1.length == 14) {
                    id1 = ""
                }

            }
            return getMinOne(id0,id1)
        } else {
            val deviceId = tm.deviceId
            if (isImei) {
                if (deviceId != null && deviceId.length >= 15) {
                    return deviceId
                }
            } else {
                if (deviceId != null && deviceId.length == 14) {
                    return deviceId
                }
            }
        }
        return ""
    }




    private fun getMinOne(s0 :String?, s1:String?) :String {
        s0?.let {
          s1?.let{
              return if (s0 <= s1) {
                  s0
              } else {
                  s1
              }
          }  ?:run {
              return s0
          }
        } ?:run{
            return if (s1.isNullOrEmpty()) {
                ""
            } else {
                s1
            }
        }
    }


    private fun getSystemPropertyByReflect(key:String) :String {
        try{
            @SuppressLint("PrivateApi")
            val clz = Class.forName("android.os.SystemProperties")
            val getMethod = clz.getMethod("get", String::class.java, String::class.java
            )
            return (getMethod.invoke(clz, key, "") as String)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     *
     * 判断是哪个运营商
     */
    fun getSimOperatorByMnc(): String {
        val tm = getTelephontManager()
        val operator = tm.simOperator
        return operator?.let {
            when (operator) {
                "46000", "46002", "46007", "46004" -> "中国移动"
                "46001", "46006", "46009" -> "中国联通"
                "46003", "46005", "46011" -> "中国电信"
                else -> operator
            }
        } ?:""
    }

    /**
     *
     * 获取Telephony管理器
     */
    private fun getTelephontManager(): TelephonyManager {
        return getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }
}