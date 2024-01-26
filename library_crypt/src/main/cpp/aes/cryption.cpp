#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdint.h>
#include <android/log.h>
#include "base64.h"
#include "aes.h"
//
// Created by user on 2023/8/22.
//
extern "C"
JNIEXPORT jstring JNICALL
Java_com_knight_library_cryption_AesUtils_string2Base64(JNIEnv *env, jobject thiz, jbyteArray buf) {
    char *str = NULL;
    jsize alen = env->GetArrayLength(buf);
    jbyte *ba = env->GetByteArrayElements(buf, 0);
    str = (char *) malloc(alen + 1);
    memcpy(str, ba, alen);
    str[alen] = '\0';
    env->ReleaseByteArrayElements(buf, ba, 0);
    char *res = b64_encode((unsigned char *) str, alen);
    // 结果转换为utf-8格式字符串
    return env->NewStringUTF(res);
}


extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_knight_library_cryption_AesUtils_base642Byte(JNIEnv *env, jobject thiz, jstring hex_string) {
    const char *str = env->GetStringUTFChars(hex_string, 0);
    size_t size = (size_t) env->GetStringUTFLength(hex_string);//152
    size_t decsize = 0;
    char *result = (char *) b64_decode_ex(str, size, &decsize);
    env->ReleaseStringUTFChars(hex_string, str);
    jbyteArray jbArr = env->NewByteArray(decsize);
    env->SetByteArrayRegion(jbArr, 0, decsize, (jbyte *) result);
    return jbArr;
}

/*
* CBC模式初始化向量
*
*/
static const uint8_t AES_IV[] = "KXTUDEdBs9zGlvy7";
static uint8_t AES_KEY[] = "cretinzp**273846";
extern "C"
JNIEXPORT jstring JNICALL
Java_com_knight_library_cryption_AesUtils_encrypt(JNIEnv *env, jobject thiz, jstring src) {
    const char *str = (char *) env->GetStringUTFChars(src, 0);
    char *result = AES_ECB_PKCS7_Encrypt(str, AES_KEY);//AES ECB PKCS7Padding加密
    env->ReleaseStringUTFChars(src, str);
    //char *result = AES_CBC_PKCS7_Encrypt(str, AES_KEY, AES_IV);//AES CBC PKCS7Padding加密
    return env->NewStringUTF(result);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_knight_library_cryption_AesUtils_decrypt(JNIEnv *env, jobject thiz, jstring encrypted) {
    const char *str = (char *) env->GetStringUTFChars(encrypted, 0);
    const char *result = AES_ECB_PKCS7_Decrypt(str, AES_KEY);//AES ECB PKCS7Padding解密
    env->ReleaseStringUTFChars(encrypted, str);
    //char *result = AES_CBC_PKCS7_Decrypt(str, AES_KEY, AES_IV);//AES CBC PKCS7Padding解密
    return env->NewStringUTF(result);
}

/**
 * 重新设置密钥 key
 * @param key java层传输进来的key
 */
void setKey(const unsigned char *_key, size_t size) {
    for (int i = 0; i < size; i++) {//元素复制
        if (i > sizeof(AES_KEY) / sizeof(AES_KEY[0]) - 1)
            break;
        AES_KEY[i] = *_key;
        _key++;
    }
    for (int i = size; i < 16; i++) {//补齐key
        AES_KEY[i] = 0x30;
    }
    AES_KEY[16] = '\0';//结束符
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_knight_library_cryption_AesUtils_getAESKey(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF((const char *) AES_KEY);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_knight_library_cryption_AesUtils_setAESKey(JNIEnv *env, jobject thiz, jstring key) {
//接收java层传过来的String
//unsigned char *_key = (unsigned char *) env->GetStringUTFChars(key, 0);
const unsigned char *_key = reinterpret_cast<const unsigned char *>("cretinzp**273846");
//获取key的长度
size_t size = (size_t) env->GetStringUTFLength(key);
setKey(_key, size);
// 如果用了GetStringUTFChars()方法就对应ReleaseStringUTFChars
// env->ReleaseStringUTFChars(key, (char *) _key);
}
