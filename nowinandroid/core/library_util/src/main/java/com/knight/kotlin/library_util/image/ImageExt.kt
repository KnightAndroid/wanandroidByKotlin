package com.knight.kotlin.library_util.image

/**
 * Author:Knight
 * Time:2024/5/13 14:24
 * Description:ImageExt
 */
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.splitString


//1、isNullOrEmpty : 为空指针或者字串长度为0时返回true，非空串与可空串均可调用。
//2、isNullOrBlank : 为空指针或者字串长度为0或者全为空格时返回true，非空串与可空串均可调用。
//3、isEmpty : 字串长度为0时返回true，只有非空串可调用。
//4、isBlank : 字串长度为0或者全为空格时返回true，只有非空串可调用。
//5、isNotEmpty : 字串长度大于0时返回true，只有非空串可调用。
//6、isNotBlank : 字串长度大于0且不是全空格串时返回true，只有非空串可调用

@BindingAdapter(
   value = ["imageUrl"]
)
fun ImageView.loadUrl(url:String?){
   if (!url.isNullOrEmpty()) {
      var loadImage = ""
      //这里后台返回数据部分多了ali 需要替换
      if (url.contains("ali-ali")) {
         loadImage = url.replace("ali-ali","ali")
      } else  {
         if (!url.contains("//ali")) {
            if (url.contains("img.kaiyanapp.com")) {
               if (url.startsWith("http://")) {
                  loadImage = "http://ali-" + url.splitString("http://").get(1)
               } else {
                  loadImage = "http://ali-" + url.splitString("https://").get(1)
               }
            } else {
               loadImage = url
            }
         } else {
            loadImage = url
         }
      }

      ImageLoader.loadStringPhoto(
         context,
         loadImage,
         this,
         callback = {
             if (!it) {
                this.setBackgroundColor(ColorUtils.alphaColor(ColorUtils.getRandColorCode(),0.6f))
             }
         }
      )
   }
}

@BindingAdapter(
   value = ["imageLocal","tintList"],
   requireAll = false
)
fun ImageView.loadLocalPathWithTint(drawable: Int, colorStateList: ColorStateList = ColorStateList.valueOf(
   CacheUtils.getThemeColor())){
   setImageResource(drawable)
   imageTintList = colorStateList
}

//@BindingAdapter(
//   value = ["imageTint"])
//fun ImageView.setTintColor(color: Int) {
//   setImageTintList(ColorStateList.valueOf(color))
//}


//@BindingAdapter("image")
//fun ImageView.setImage(resourceId: Int) {
//   setImageResource(resourceId)
//   imageTintList =  ColorStateList.valueOf(CacheUtils.getThemeColor())
//}
@BindingAdapter("imageTint")
fun ImageView.setImageTint(color: Int) {
   imageTintList = color.toColorStateList()
}

@BindingAdapter("imageTint")
fun ImageView.setImageTint(drawable: Drawable) {
   setImageDrawable(drawable)
}

private fun Int.toColorStateList() = android.content.res.ColorStateList.valueOf(this)