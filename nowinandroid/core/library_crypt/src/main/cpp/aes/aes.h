//
// Created by user on 2023/8/22.
//

/**
* 文件描述: AES加密头文件
* 作者: Created by 向定权 on 2018/5/5
* 版本号: v1.0
* 组织名称: swifts.com.cn
* 包名: ${PACKAGE_NAME}
* 项目名称: AES
* 版权申明: 暂无
*/
#ifndef AES_AES_H
#define AES_AES_H

#include <stdint.h>
#include <string.h>
#include <stdlib.h>
#include "base64.h"

/*****************************************************************************/
/*                         宏定义                                             */
/*****************************************************************************/

/**
 * 将宏定义为1/0，以启用/禁用 CBC/ECB 加密模式。
 */
#ifndef CBC
#define CBC 1
#endif

#ifndef ECB
#define ECB 1
#endif

static const unsigned char HEX[16] = {0x10, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                                      0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};


#ifdef __cplusplus
extern "C" {
#endif

#if defined(ECB) && ECB

char *AES_ECB_PKCS7_Encrypt(const char *in, const uint8_t *key);

const  char *AES_ECB_PKCS7_Decrypt(const char *in, const uint8_t *key);

#endif // #if defined(ECB) && ECB


#if defined(CBC) && CBC

char *AES_CBC_PKCS7_Encrypt(const char *in, const uint8_t *key, const uint8_t *iv);

char *AES_CBC_PKCS7_Decrypt(const char *in, const uint8_t *key, const uint8_t *iv);

#endif // #if defined(CBC) && CBC


#ifdef __cplusplus
}
#endif

#endif //AES_AES_H