
import android.app.Activity
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_permiss.manifest.AndroidManifestInfo
import com.knight.kotlin.library_permiss.permission.base.IPermission
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Author:Knight
 * Time:2023/8/29 17:41
 * Description: 权限错误检测类
 */
object PermissionChecker {
    /**
     * 检查 Activity 的状态是否正常
     */
    fun checkActivityStatus( activity: Activity) {
        // 检查当前 Activity 状态是否是正常的，如果不是则不请求权限
        requireNotNull(activity) { "The instance of the context must be an activity object" }

        check(!activity.isFinishing()) {
            "The activity has been finishing, " +
                    "please manually determine the status of the activity"
        }

        check(!activity.isDestroyed()) {
            "The activity has been destroyed, " +
                    "please manually determine the status of the activity"
        }
    }


    /**
     * 检查 Fragment 的状态是否正常（App 包版本）
     */
    @Suppress("deprecation")
    fun checkAppFragmentStatus( appFragment: Fragment) {
        check(appFragment.isAdded()) {
            "This app fragment has no binding added, " +
                    "please manually determine the status of the app fragment"
        }

        check(!appFragment.isRemoving()) {
            "This app fragment has been removed, " +
                    "please manually determine the status of the app fragment"
        }
    }

    /**
     * 检查传入的权限是否符合要求
     */
    fun checkPermissionList(
         activity: Activity,
         requestPermissions: List<IPermission>,
         androidManifestInfo: AndroidManifestInfo
    ) {
        require(!(requestPermissions == null || requestPermissions.isEmpty())) { "The requested permission cannot be empty" }

        for (permission in requestPermissions) {
            // 检查权限序列化实现是否有问题
            checkPermissionParcelable(permission)
            // 让权限自己检查一下自己
            permission.checkCompliance(activity, requestPermissions, androidManifestInfo)
        }
    }

    /**
     * 检查权限序列化实现是否没问题
     */
    fun checkPermissionParcelable( permission: IPermission) {
        val clazz: Class<out IPermission> = permission.javaClass
        val className = clazz.name

        // 获取 CREATOR 字段
        var creatorField: Field? = null
        try {
            creatorField = permission.javaClass.getDeclaredField("CREATOR")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        requireNotNull(creatorField) { "This permission class does not define the CREATOR field" }

        // 获取 CREATOR 对象
        val creatorObject: Any
        try {
            // 静态字段使用 null 作为实例
            creatorObject = creatorField.get(null)
        } catch (e: Exception) {
            // 访问权限类中的 CREATOR 字段异常，请用 public static final 来修饰 CREATOR 字段
            throw IllegalArgumentException(
                "The CREATOR field in the " + className +
                        " has an access exception. Please modify CREATOR field with \"public static final\""
            )
        }

        require(creatorObject is Parcelable.Creator<*>) {
            "The CREATOR field in this " + className +
                    " is not of type " + Parcelable.Creator::class.java.name
        }

        // 获取字段的泛型类型
        val genericType: Type = creatorField.getGenericType()

        // 检查是否为参数化类型
        require(genericType is ParameterizedType) { "The generic type defined for the CREATOR field in this $className is empty" }

        // 获取泛型参数
        val parameterizedType = genericType as ParameterizedType
        val typeArguments: Array<Type> = parameterizedType.actualTypeArguments

        // 检查是否只有一个泛型参数
        require(typeArguments.size == 1) { "The number of generics defined in the CREATOR field of this $className can only be one" }

        // 获取泛型参数类型
        val typeArgument: Type = typeArguments[0]

        // 检查泛型参数是否为当前类
        require(typeArgument is Class<*> && clazz.isAssignableFrom(typeArgument as Class<*>)) { "The generic type defined in the CREATOR field of this $className is incorrect" }

        // 直接调用 newArray 方法创建数组
        val parcelableCreator = creatorObject as Parcelable.Creator<*>
        val array = parcelableCreator.newArray(0)
        requireNotNull(array) {
            "The newArray method of the CREATOR field in this " + className +
                    " returns an empty value. This method cannot return an empty value"
        }
    }
}