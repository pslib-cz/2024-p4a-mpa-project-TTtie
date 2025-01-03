//
// Created by tttie on 02.01.25.
//


#include "functions.hpp"
#include "utils.hpp"
#include <libqalculate/Function.h>
#include <jni.h>

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_getFns(JNIEnv *env, jobject thiz) {
    auto calcFnCls = env->FindClass("cz/tttie/qalculate/binding/CalculatorFunction");
    auto konstructor = env->GetMethodID(calcFnCls, "<init>",
                                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    auto calc = getCalc(env, thiz);

    auto objArr = env->NewObjectArray(static_cast<jint>(calc->functions.size()), calcFnCls,
                                      nullptr);

    for (int i = 0; i < calc->functions.size(); i++) {
        auto fn = calc->functions[i];
        auto obj = env->NewObject(calcFnCls, konstructor, utf8ToString(env, fn->name()),
                                  utf8ToString(env, fn->title()),
                                  utf8ToString(env, fn->description()),
                                  utf8ToString(env, fn->category()));

        env->SetObjectArrayElement(objArr, i, obj);
    }

    return objArr;

}