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
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils


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
      //这里后台返回数据部分多了ali 需要替换
      val loadImage = url.replace("ali-ali","ali")
      ImageLoader.loadStringPhoto(
         context,
         loadImage,
         this,
         callback = {
             if (!it) {
                this.setBackgroundColor(ColorUtils.getRandColorCode())
             }
         }
      )
   }
}

@BindingAdapter(
   value = ["imageLocal","tintList"],
   requireAll = false
)
fun ImageView.loadLocalPathWithTint(drawable: Int, colorStateList: ColorStateList = ColorStateList.valueOf(CacheUtils.getThemeColor())){
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