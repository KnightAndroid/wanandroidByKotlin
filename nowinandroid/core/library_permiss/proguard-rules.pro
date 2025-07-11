# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 禁止混淆 IStartActivityDelegate 和 IFragmentMethodNative 接口及实现类涉及的方法名称
# 这是因为框架拿 Support 库中的 Fragment 和 App 包下的 Fragment 实现了这两个接口，
# 这样做的目的是为了将不同 Fragment 抽象化，然后框架就可以不用关心和判断具体是哪个 Fragment 来申请权限，
# 但是那样做会出现一个问题，有人反馈在混淆后会报崩溃异常，经过排查后发现，外层传入的是普通的 Activity 对象，
# 而非 FragmentActivity 对象，框架会使用 App 包下的 Fragment 来申请权限，从而引发了该问题，
# 如果外层传入的是 FragmentActivity 对象，框架会使用 Support 库中的 Fragment 来申请权限，则不会出现该问题，
# 相关的 issue 地址：https://github.com/getActivity/XXPermissions/issues/371，
# 添加这个混淆规则是为了一件事，为了不让编译器混淆 Support 库中的 Fragment 里面这些方法，
# 因为框架内部同时使用了 App 包下的 Fragment 和 Support 库中的 Fragment 来请求权限，
# 如果让编译器混淆了这些方法，会出现一个问题，那么就是 Support 库中的 Fragment 这些方法虽然被混淆了，
# 但是 App 包下的 Fragment 是系统类，是肯定不会被混淆的，那么就会导致调用的时候方法名对不上，从而报 AbstractMethodError，
# 要么不去混淆，要混淆就大家一起混淆，这样大家的方法名还能保持一致，调用方法的时候还能对得上（能找到对应的方法），
# 但问题就出在，有一方不能被混淆，这里的一方就是指 App 包下的 Fragment 类，所以唯一的解决方案就是不混淆这些方法名。
-keepclassmembers interface com.knight.kotlin.library_permiss.start.IStartActivityDelegate {
    <methods>;
}
-keepclassmembers interface com.knight.kotlin.library_permiss.fragment.IFragmentMethodNative {
    <methods>;
}

# 禁止混淆 Support 库中 Fragment 的 getActivity 方法名称
# 看到这里你可能会有一些几个疑问，我会在此处依次解答这些疑问：
# 1. 前面对 IFragmentMethodNative 接口的混淆规则不是已经包含了 getActivity 方法？
#    在这里为什么还要单独添加一遍重复作用的混淆规则，这不是脱裤子放屁多此一举？
#    这是因为这个方法比较特殊，混淆编译的时候没有正确识别到这个方法，
#    从而导致把 Support 库中 Fragment 类定义的 getActivity 方法给混淆了，
#    但是 IFragmentMethodNative 定义的 getActivity 方法没有给混淆；
#    这是因为 IFragmentMethodNative 接口定义是 A getActivity()，
#    而 Support 库中 Fragment 类定义的是 FragmentActivity getActivity()，
#    如果不禁止混淆 Fragment 中 getActivity 方法名，会导致它被混淆掉。
# 2. 为什么只写了 Support 库的混淆规则，你确定这个混淆规则在 AndroidX 上面能用？
#    结论是没有问题的，放心大胆用，我帮大家测试过了，是完全可行的，不信可以拿 apk 反编译看看，
#    在开启 android.enableJetifier=true 的时候编译器会自动帮你转换成 AndroidX 的包名，
#    所以完全没有必要在这里多写一个 AndroidX 的版本混淆规则，那样做才是多此一举，没有任何意义。
#-keepclassmembers class android.support.v4.app.Fragment {
#    android.support.v4.app.FragmentActivity getActivity();
#}
#-keepclassmembers class androidx.fragment.app.Fragment {
#    androidx.fragment.app.FragmentActivity getActivity();
#}