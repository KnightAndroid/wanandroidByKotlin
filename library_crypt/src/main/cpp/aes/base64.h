//
// Created by user on 2023/8/22.
//

/**
* 文件描述: base64头文件 编码与解码的函数声明 已经定义base64编码索引表
* 作者: Created by 向定权 on 2018/5/5
* 版本号: v1.0
* 组织名称: swifts.com.cn
* 包名: ${PACKAGE_NAME}
* 项目名称: SwiftsAES
* 版权申明: 暂无
*/
#ifndef AES_BASE64_H
#define AES_BASE64_H 1
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
/**
 * base64索引表   url安全的
 */
static const char b64_table[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'
};
#ifdef __cplusplus
extern "C"{
#endif
/*****************************************************************************/
/*                         函数声明                                           */
/*****************************************************************************/
/***
 * 进行base64编码
 * @return 返回base64编码后的字符串
 */
char *b64_encode (const unsigned char *, size_t);
/**
 * base64解码
 * @return 返回无符号base64解码后的字符串 同时还返回解码后的大小
 */
unsigned char *b64_decode_ex (const char *, size_t, size_t *);
#ifdef __cplusplus
}
#endif
#endif //AES_BASE64_H