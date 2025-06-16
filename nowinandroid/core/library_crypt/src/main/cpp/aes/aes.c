//
// Created by user on 2023/8/22.
//
/**
* 文件描述: aes具体实现 支持CBC 跟EBC模式  使用PKCS7填充
* 作者: Created by 向定权 on 2018/5/5
* 版本号: v1.0
* 组织名称: swifts.com.cn
* 包名: ${PACKAGE_NAME}
* 项目名称: AES
* 版权申明: 暂无
*/

#include "aes.h"
#include <android/log.h>
// AES中state的列数,这个值固定为4
#define Nb 4
// AES 分段的大小,这个值固定为16
#define BLOCK_SIZE 16

#ifndef MULTIPLY_AS_A_FUNCTION
#define MULTIPLY_AS_A_FUNCTION 1
#endif
typedef uint8_t state_t[4][4];
//状态矩阵
//volatile static  state_t *state;
//轮密钥
static uint8_t RoundKey[240];
// 输入的密钥
static const uint8_t *Key;

//密钥长度（32位比特字)
static char Nk;//aes128 为4
//加密轮数
static char Nr;//aes128 为10
//密钥长度 （8位）
static char KEYLEN;//aes128 为16

#if defined(CBC) && CBC
// 用于 CBC 模式的初始化向量
static uint8_t *Iv;
#endif
//s 盒
static const uint8_t sbox[256] = {
        //0     1    2      3     4    5     6     7      8    9     A      B    C     D     E     F
        0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
        0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
        0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
        0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
        0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
        0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
        0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
        0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
        0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
        0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
        0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
        0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
        0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
        0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
        0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
        0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
};
//s 盒逆
static const uint8_t rsbox[256] = {
        0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb,
        0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb,
        0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e,
        0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
        0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92,
        0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84,
        0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06,
        0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b,
        0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
        0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e,
        0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b,
        0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4,
        0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f,
        0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef,
        0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
        0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d
};

// 轮常量
static const uint8_t Rcon[15] = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36,
                                 0x6c, 0xd8, 0xab, 0x4d};

/*****************************************************************************/
/*内部函数                                                                    */
/*****************************************************************************/

//根据索引取值
static uint8_t getSBoxValue(uint8_t num) {
    return sbox[num];
}

//根据索引取值
static uint8_t getSBoxInvert(uint8_t num) {
    return rsbox[num];
}

// 通过密钥编排函数该密钥矩阵被扩展成一个44个字组成的序列W[0],W[1], … ,W[43],
// 该序列的前4个元素W[0],W[1],W[2],W[3]是原始密钥，用于加密运算中的初始密钥加
// 后面40个字分为10组，每组4个字（128比特）分别用于10轮加密运算中的轮密钥加
static void KeyExpansion(void) {
    uint32_t i, j, k;
    uint8_t tempa[4];

    Nk = KEYLEN / 4;
    Nr = 6 + Nk;

    // 第一轮为原始密钥
    for (i = 0; i < Nk; ++i) {
        RoundKey[(i * 4) + 0] = Key[(i * 4) + 0];
        RoundKey[(i * 4) + 1] = Key[(i * 4) + 1];
        RoundKey[(i * 4) + 2] = Key[(i * 4) + 2];
        RoundKey[(i * 4) + 3] = Key[(i * 4) + 3];
    }

    // 其他轮的密钥从前一轮获得
    for (; (i < (Nb * (Nr + 1))); ++i) {
        for (j = 0; j < 4; ++j) {
            tempa[j] = RoundKey[(i - 1) * 4 + j];
        }
        if (i % Nk == 0) {
            // [a0,a1,a2,a3] 变成 [a1,a2,a3,a0]
            {
                k = tempa[0];
                tempa[0] = tempa[1];
                tempa[1] = tempa[2];
                tempa[2] = tempa[3];
                tempa[3] = k;
            }
            //从s盒中取
            {
                tempa[0] = getSBoxValue(tempa[0]);
                tempa[1] = getSBoxValue(tempa[1]);
                tempa[2] = getSBoxValue(tempa[2]);
                tempa[3] = getSBoxValue(tempa[3]);
            }
            //轮常量异或
            tempa[0] = tempa[0] ^ Rcon[i / Nk];
        } else if (Nk > 6 && i % Nk == 4) {
            {
                tempa[0] = getSBoxValue(tempa[0]);
                tempa[1] = getSBoxValue(tempa[1]);
                tempa[2] = getSBoxValue(tempa[2]);
                tempa[3] = getSBoxValue(tempa[3]);
            }
        }
        RoundKey[i * 4 + 0] = RoundKey[(i - Nk) * 4 + 0] ^ tempa[0];
        RoundKey[i * 4 + 1] = RoundKey[(i - Nk) * 4 + 1] ^ tempa[1];
        RoundKey[i * 4 + 2] = RoundKey[(i - Nk) * 4 + 2] ^ tempa[2];
        RoundKey[i * 4 + 3] = RoundKey[(i - Nk) * 4 + 3] ^ tempa[3];
    }
}

// 轮转密钥添加到状态矩阵
static void AddRoundKey(uint8_t round, state_t *state) {
    uint8_t i, j;
    for (i = 0; i < 4; ++i) {
        for (j = 0; j < 4; ++j) {
            (*state)[i][j] ^= RoundKey[round * Nb * 4 + i * Nb + j];
        }
    }
}

// 字节代换 把该字节的高4位作为行值，低4位作为列值，取出S盒中对应的行的元素作为输出
static void SubBytes(state_t *state) {
    uint8_t i, j;
    for (i = 0; i < 4; ++i) {
        for (j = 0; j < 4; ++j) {
            (*state)[j][i] = getSBoxValue((*state)[j][i]);
        }
    }
}

// 行移位 是一个简单的左循环移位操作。当密钥长度为128比特时，状态矩阵的第0行左移0字节，第1行左移1字节，第2行左移2字节，第3行左移3字节，
static void ShiftRows(state_t *state) {
    uint8_t temp;

    temp = (*state)[0][1];
    (*state)[0][1] = (*state)[1][1];
    (*state)[1][1] = (*state)[2][1];
    (*state)[2][1] = (*state)[3][1];
    (*state)[3][1] = temp;

    temp = (*state)[0][2];
    (*state)[0][2] = (*state)[2][2];
    (*state)[2][2] = temp;

    temp = (*state)[1][2];
    (*state)[1][2] = (*state)[3][2];
    (*state)[3][2] = temp;

    temp = (*state)[0][3];
    (*state)[0][3] = (*state)[3][3];
    (*state)[3][3] = (*state)[2][3];
    (*state)[2][3] = (*state)[1][3];
    (*state)[1][3] = temp;
}

static uint8_t xtime(uint8_t x) {
    return ((x << 1) ^ (((x >> 7) & 1) * 0x1b));
}

// 列混合变换是通过矩阵相乘来实现的，经行移位后的状态矩阵与固定的矩阵相乘，得到混淆后的状态矩阵
static void MixColumns(state_t *state) {
    uint8_t i;
    uint8_t Tmp, Tm, t;
    for (i = 0; i < 4; ++i) {
        t = (*state)[i][0];
        Tmp = (*state)[i][0] ^ (*state)[i][1] ^ (*state)[i][2] ^ (*state)[i][3];
        Tm = (*state)[i][0] ^ (*state)[i][1];
        Tm = xtime(Tm);
        (*state)[i][0] ^= Tm ^ Tmp;
        Tm = (*state)[i][1] ^ (*state)[i][2];
        Tm = xtime(Tm);
        (*state)[i][1] ^= Tm ^ Tmp;
        Tm = (*state)[i][2] ^ (*state)[i][3];
        Tm = xtime(Tm);
        (*state)[i][2] ^= Tm ^ Tmp;
        Tm = (*state)[i][3] ^ t;
        Tm = xtime(Tm);
        (*state)[i][3] ^= Tm ^ Tmp;
    }
}

//乘法
#if MULTIPLY_AS_A_FUNCTION

static uint8_t Multiply(uint8_t x, uint8_t y) {
    return (((y & 1) * x) ^
            ((y >> 1 & 1) * xtime(x)) ^
            ((y >> 2 & 1) * xtime(xtime(x))) ^
            ((y >> 3 & 1) * xtime(xtime(xtime(x)))) ^
            ((y >> 4 & 1) * xtime(xtime(xtime(xtime(x))))));
}

#else
#define Multiply(x, y)                                \
      (  ((y & 1) * x) ^                              \
      ((y>>1 & 1) * xtime(x)) ^                       \
      ((y>>2 & 1) * xtime(xtime(x))) ^                \
      ((y>>3 & 1) * xtime(xtime(xtime(x)))) ^         \
      ((y>>4 & 1) * xtime(xtime(xtime(xtime(x))))))   \

#endif

//列混合
static void InvMixColumns(state_t *state) {
    int i;
    uint8_t a, b, c, d;
    for (i = 0; i < 4; ++i) {
        a = (*state)[i][0];
        b = (*state)[i][1];
        c = (*state)[i][2];
        d = (*state)[i][3];
        (*state)[i][0] =
                Multiply(a, 0x0e) ^ Multiply(b, 0x0b) ^ Multiply(c, 0x0d) ^ Multiply(d, 0x09);
        (*state)[i][1] =
                Multiply(a, 0x09) ^ Multiply(b, 0x0e) ^ Multiply(c, 0x0b) ^ Multiply(d, 0x0d);
        (*state)[i][2] =
                Multiply(a, 0x0d) ^ Multiply(b, 0x09) ^ Multiply(c, 0x0e) ^ Multiply(d, 0x0b);
        (*state)[i][3] =
                Multiply(a, 0x0b) ^ Multiply(b, 0x0d) ^ Multiply(c, 0x09) ^ Multiply(d, 0x0e);
    }
}

////s逆盒字节代换
static void InvSubBytes(state_t *state) {
    uint8_t i, j;
    for (i = 0; i < 4; ++i) {
        for (j = 0; j < 4; ++j) {
            (*state)[j][i] = getSBoxInvert((*state)[j][i]);
        }
    }
}

//行移位解密过程  右移
static void InvShiftRows(state_t *state) {
    uint8_t temp;
    temp = (*state)[3][1];
    (*state)[3][1] = (*state)[2][1];
    (*state)[2][1] = (*state)[1][1];
    (*state)[1][1] = (*state)[0][1];
    (*state)[0][1] = temp;

    temp = (*state)[0][2];
    (*state)[0][2] = (*state)[2][2];
    (*state)[2][2] = temp;

    temp = (*state)[1][2];
    (*state)[1][2] = (*state)[3][2];
    (*state)[3][2] = temp;

    temp = (*state)[0][3];
    (*state)[0][3] = (*state)[1][3];
    (*state)[1][3] = (*state)[2][3];
    (*state)[2][3] = (*state)[3][3];
    (*state)[3][3] = temp;
}


// Cipher 是加密明文的主要函数
static void Cipher(state_t *state) {
    uint8_t round = 0;
    AddRoundKey(0, state);

    for (round = 1; round < Nr; ++round) {
        SubBytes(state);//字节代换
        ShiftRows(state);//行移位
        MixColumns(state);//列混合
        AddRoundKey(round, state);//轮密钥加
    }
    //最后一轮不执行列混合
    SubBytes(state);
    ShiftRows(state);
    AddRoundKey(Nr, state);
}

//解密的主函数 即加密的逆过程
static void InvCipher(state_t *state) {
    uint8_t round = 0;

    AddRoundKey(Nr, state);

    for (round = Nr - 1; round > 0; round--) {
        InvShiftRows(state);//行移位
        InvSubBytes(state);//s逆盒字节代换
        AddRoundKey(round, state);//轮密钥加
        InvMixColumns(state);//列混合
    }
    InvShiftRows(state);
    InvSubBytes(state);
    AddRoundKey(0, state);
}

//分段块的复制
static void BlockCopy(uint8_t *output, const uint8_t *input) {
    uint8_t i;
    for (i = 0; i < BLOCK_SIZE; ++i) {
        output[i] = input[i];
    }
}

/*****************************************************************************/
/* 对外提供的函数                                                              */
/*****************************************************************************/

static inline int *findPaddingIndex(uint8_t *str, size_t length) {
    static int result[] = {-1, -1}, i, k;
    for (i = 0; i < length; ++i) {
        char c = str[length - i];
        if ('\0' != c) {
            result[0] = i;
            for (k = 0; k < BLOCK_SIZE; ++k) {
                if (HEX[k] == c) {
                    if (0 == k) {
                        k = BLOCK_SIZE;
                    }
                    result[1] = k;
                    return result;
                }
            }
            return result;
        }
    }
}

//对字符串（也就是待加密的字符串）进行pkcs7填充
static inline uint8_t *getPKCS7PaddingInput(const char *in) {
    int inLength = (int) strlen(in);//输入的长度
    int remainder = inLength % BLOCK_SIZE;
    uint8_t *paddingInput;
    int group = inLength / BLOCK_SIZE;
    int size = BLOCK_SIZE * (group + 1);
    paddingInput = (uint8_t *) malloc(size + 1);

    int dif = size - inLength;
    for (int i = 0; i < size; i++) {
        if (i < inLength) {
            paddingInput[i] = in[i];
        } else {
            if (remainder == 0) {
                //刚好是16倍数,就填充16个16
                paddingInput[i] = HEX[0];
            } else {    //如果不足16位 少多少位就补几个几  如：少4为就补4个4 以此类推
                paddingInput[i] = HEX[dif];
            }
        }
    }
    paddingInput[size] = '\0';
    return paddingInput;
}

//进行pkcs7移除操作
static inline void removePKCS7Padding(uint8_t *out, const size_t inputLength) {
    int *result = findPaddingIndex(out, inputLength - 1);
    int offSetIndex = result[0];
    int lastChar = result[1];
    //检查是不是padding的字符,然后去掉
    const size_t noZeroIndex = inputLength - offSetIndex;
    if (lastChar >= 0 && offSetIndex >= 0) {
        int success = 1;
        for (int i = 0; i < lastChar; ++i) {
            size_t index = noZeroIndex - lastChar + i;
            if (!HEX[lastChar] == out[index]) {
                success = 0;
            }
        }
        if (1 == success) {
            out[noZeroIndex - lastChar] = '\0';
            memset(out + noZeroIndex - lastChar + 1, 0, lastChar - 1);
        }
    } else {
        out[noZeroIndex] = '\0';
    }
}

#if defined(ECB) && ECB

/*****************************************************************************/
/*                         ECB模式                                            */
/*****************************************************************************/

//分段加密 结果输出到output中
static inline void
AES_ECB_encrypt(const uint8_t *input, const uint8_t *key, uint8_t *output, state_t *state) {
    // 复制 并且后续在output上进行操作
    BlockCopy(output, input);
    state = (state_t *) output;
    if (Key != key) {
        Key = key;
        KeyExpansion();//进行密钥拓展 生成10轮的轮转密钥 进行加密
    }
    //调用AES进行加密
    Cipher(state);
}

//分段解密 结果输出到output中
static inline void
AES_ECB_decrypt(const uint8_t *input, const uint8_t *key, uint8_t *output, state_t *state) {
    BlockCopy(output, input);
    state = (state_t *) output;
    if (Key != key) {
        Key = key;
        KeyExpansion();
    }
    InvCipher(state);
}

/**
 * 不定长加密,pkcs7padding，根据密钥长度自动选择128、192、256算法
 */
char *AES_ECB_PKCS7_Encrypt(const char *in, const uint8_t *key) {
    state_t *state = NULL;
    KEYLEN = strlen(key);//获取key的长度（字符串 不包括'\0'）
    uint8_t *paddingInput = getPKCS7PaddingInput(in);//填充
    int paddingInputLengt = strlen(paddingInput);
    int count = paddingInputLengt / BLOCK_SIZE;//分段数
    //开始分段加密
    char *out = (char *) malloc(paddingInputLengt);
    for (int i = 0; i < count; ++i) {
        AES_ECB_encrypt(paddingInput + i * BLOCK_SIZE, key, out + i * BLOCK_SIZE, state);//执行具体分段加密
    }
    char *base64En = b64_encode(out, paddingInputLengt);//转base64编码
    free(paddingInput);
    free(out);
    return base64En;//返回结果
}

/**
 * 不定长解密,pkcs7padding，根据密钥长度自动选择128、192、256算法
 */
const char *AES_ECB_PKCS7_Decrypt(const char *in, const uint8_t *key) {
    state_t *state = NULL;
    KEYLEN = strlen(key);
    size_t len = strlen(in);
    size_t inputLength = 0;
    uint8_t *inputDesBase64 = b64_decode_ex(in, len, &inputLength);//首先进行base64解码
    volatile uint8_t *out = malloc(inputLength);
    memset(out, 0, inputLength);
    __android_log_print(ANDROID_LOG_ERROR,"JIN_TAG","结果：%d",KEYLEN);
    size_t count = inputLength / BLOCK_SIZE;
    if (count <= 0) {
        count = 1;
    }

    //分段解密
    for (size_t i = 0; i < count; ++i) {
        AES_ECB_decrypt(inputDesBase64 + i * BLOCK_SIZE, key, out + i * BLOCK_SIZE, state);
    }

    //移除填充
    removePKCS7Padding(out, inputLength);
    free(inputDesBase64);
    return (const char *) out;
}

#endif // #if defined(ECB) && ECB

/*****************************************************************************/
/*                         CBC模式                                            */
/*****************************************************************************/

#if defined(CBC) && CBC

static void XorWithIv(uint8_t *buf) {
    uint8_t i;
    for (i = 0; i < BLOCK_SIZE; ++i) {
        buf[i] ^= Iv[i];
    }
}

void AES_CBC_encrypt(uint8_t *output, uint8_t *input, uint32_t length, const uint8_t *key,
                     const uint8_t *iv, state_t *state) {
    uintptr_t i;
    uint8_t remainders = length % BLOCK_SIZE;

    BlockCopy(output, input);
    state = (state_t *) output;

    if (0 != key) {
        Key = key;
        KeyExpansion();
    }

    if (iv != 0) {
        Iv = (uint8_t *) iv;
    }

    for (i = 0; i < length; i += BLOCK_SIZE) {
        XorWithIv(input);
        BlockCopy(output, input);
        state = (state_t *) output;
        Cipher(state);
        Iv = output;
        input += BLOCK_SIZE;
        output += BLOCK_SIZE;
    }

    if (remainders) {
        BlockCopy(output, input);
        memset(output + remainders, 0, BLOCK_SIZE - remainders);
        state = (state_t *) output;
        Cipher(state);
    }
}

void AES_CBC_decrypt(uint8_t *output, uint8_t *input, uint32_t length, const uint8_t *key,
                     const uint8_t *iv, state_t *state) {
    uintptr_t i;
    uint8_t remainders = length % BLOCK_SIZE;

    BlockCopy(output, input);
    state = (state_t *) output;

    if (0 != key) {
        Key = key;
        KeyExpansion();
    }

    if (iv != 0) {
        Iv = (uint8_t *) iv;
    }

    for (i = 0; i < length; i += BLOCK_SIZE) {
        BlockCopy(output, input);
        state = (state_t *) output;
        InvCipher(state);
        XorWithIv(output);
        Iv = input;
        input += BLOCK_SIZE;
        output += BLOCK_SIZE;
    }

    if (remainders) {
        BlockCopy(output, input);
        memset(output + remainders, 0, BLOCK_SIZE - remainders);
        state = (state_t *) output;
        InvCipher(state);
    }
}

/**
 * 不定长加密,pkcs7padding，根据密钥长度自动选择128、192、256算法
 */
char *AES_CBC_PKCS7_Encrypt(const char *in, const uint8_t *key, const uint8_t *iv) {
    KEYLEN = strlen(key);
    state_t *state = NULL;
    uint8_t *paddingInput = getPKCS7PaddingInput(in);
    int paddingInputLengt = strlen(paddingInput);
    char *out = (char *) malloc(paddingInputLengt);
    AES_CBC_encrypt(out, paddingInput, paddingInputLengt, key, iv, state);
    char *base64En = b64_encode(out, paddingInputLengt);
    free(paddingInput);
    free(out);
    return base64En;
}

/**
 * 不定长解密,pkcs7padding，根据密钥长度自动选择128、192、256算法
 */
char *AES_CBC_PKCS7_Decrypt(const char *in, const uint8_t *key, const uint8_t *iv) {
    KEYLEN = strlen(key);
    state_t *state = NULL;
    size_t len = strlen(in);
    size_t inputLength = 0;
    uint8_t *inputDesBase64 = b64_decode_ex(in, len, &inputLength);//首先进行base64解码
    uint8_t *out = malloc(inputLength);
    memset(out, 0, inputLength);
    AES_CBC_decrypt(out, inputDesBase64, inputLength, key, iv, state);

    removePKCS7Padding(out, inputLength);
    free(inputDesBase64);
    return (char *) out;
}

#endif // #if defined(CBC) && CBC