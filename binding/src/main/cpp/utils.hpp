//
// Created by tttie on 02.01.25.
//

#ifndef QALCULATE_UTILS_HPP
#define QALCULATE_UTILS_HPP

#include <jni.h>
#include <string>
#include <libqalculate/Calculator.h>

Calculator *getCalc(JNIEnv *env, jobject thiz);
jstring utf8ToString(JNIEnv *env, const std::string &str);

#endif //QALCULATE_UTILS_HPP
