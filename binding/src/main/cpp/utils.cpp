//
// Created by tttie on 02.01.25.
//

#include "utils.hpp"
#include <unicode/unistr.h>

jstring utf8ToString(JNIEnv *env, const std::string &str) {
    auto icuStr = icu::UnicodeString::fromUTF8(str);
    auto utf16StrBuffer = reinterpret_cast<const jchar *>(icuStr.getBuffer());

    return env->NewString(utf16StrBuffer, icuStr.length());
}

Calculator *getCalc(JNIEnv *env, jobject thiz) {
    auto cls = env->FindClass("cz/tttie/qalculate/binding/Qalculate");
    auto ptrField = env->GetFieldID(cls, "calculatorPtr", "J");

    auto ptr = env->GetLongField(thiz, ptrField);

    return reinterpret_cast<Calculator *>(ptr);
}