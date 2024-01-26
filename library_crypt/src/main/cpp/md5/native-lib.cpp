#include <jni.h>
#include <string>
#include "md5.h"
//额外附加的字符串
static char* EXTRA_SIGNTURE = "DARRE";
using namespace std;

//extern "C" {
//JNIEXPORT jstring JNICALL
//Java_com_example_myapplication_SignatureUtils_signatureParams(JNIEnv *env, jobject thiz,
//                                                              jstring params_);
//
//}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_knight_library_cryption_SignatureUtils_signatureParams(JNIEnv *env, jobject thiz,
                                                          jstring params_) {
    const char *params = env->GetStringUTFChars(params_,0);



    //Md5签名规则,加点料
    //1、字符串前面加点料
    string signature_str(params);
    signature_str.insert(0,EXTRA_SIGNTURE);
    //2、后面去掉两位
    signature_str = signature_str.substr(0,signature_str.length()-2);

    //3、md5 加密 c++ 和 java 是一样的，唯一不同就是需要自己回收内存
    MD5_CTX *ctx = new MD5_CTX();
    MD5Init(ctx);
    MD5Update(ctx,(unsigned  char *)signature_str.c_str(),signature_str.length());
    unsigned char digest[16] = {0};
    MD5Final(digest,ctx);
    //生成32位的字符串
    char md5_str[33]={0};
    for(int i=0;i<16;i++){
        //不足的情况下补0
        sprintf(md5_str,"%s%02x",md5_str,digest[i]);
    }
    //释放资源
    env ->ReleaseStringUTFChars(params_,params);
    return env->NewStringUTF(md5_str);
}