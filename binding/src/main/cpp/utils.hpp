//
// Created by tttie on 02.01.25.
//

#ifndef QALCULATE_UTILS_HPP
#define QALCULATE_UTILS_HPP

#include <jni.h>
#include <string>
#include <libqalculate/Calculator.h>

#define CHECK_CALCULATOR_PRESENCE(env) \
    if (!CALCULATOR) { \
        (env)->ThrowNew((env)->FindClass("java/lang/IllegalStateException"), "Calculator not created"); \
        return; \
    }

#define CHECK_CALCULATOR_PRESENCE_RET(env, ret) \
    if (!CALCULATOR) { \
        (env)->ThrowNew((env)->FindClass("java/lang/IllegalStateException"), "Calculator not created"); \
        return ret; \
    }

jstring utf8ToString(JNIEnv *env, const std::string &str);

bool isNightMode(JNIEnv *env, jobject thiz);
bool alwaysDisplayUnicode(const char *, void *);

#endif //QALCULATE_UTILS_HPP
