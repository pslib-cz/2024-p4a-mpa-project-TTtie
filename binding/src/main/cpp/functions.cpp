//
// Created by tttie on 02.01.25.
//

#include <numeric>
#include <vector>
#include <libqalculate/Function.h>
#include <jni.h>
#include "functions.hpp"
#include "utils.hpp"

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_getFns(JNIEnv *env, jobject thiz) {
    auto calcFnCls = env->FindClass("cz/tttie/qalculate/binding/CalculatorFunction");
    auto fromNativeMethod = env->GetStaticMethodID(calcFnCls, "fromNative",
                                                   "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Lcz/tttie/qalculate/binding/CalculatorFunction;");

    auto calc = getCalc(env, thiz);

    auto objArr = env->NewObjectArray(static_cast<jint>(calc->functions.size()), calcFnCls,
                                      nullptr);

    for (int i = 0; i < calc->functions.size(); i++) {
        auto fn = calc->functions[i];
        std::vector<std::string> params;
        std::vector<std::string> longParams;
        params.reserve(fn->lastArgumentDefinitionIndex());
        longParams.reserve(fn->lastArgumentDefinitionIndex());

        int argc = fn->maxargs();
        if (argc < 0) {
            argc = fn->minargs() + 1;
            if (static_cast<int>(fn->lastArgumentDefinitionIndex()) > argc) {
                argc = static_cast<int>(fn->lastArgumentDefinitionIndex());
            }
        }

        Argument defaultArg;

        for (int j = 1; j <= argc; j++) {
            auto arg = fn->getArgumentDefinition(j);
            std::string argName;
            std::string argType;
            std::string argDesc;

            if (arg && !arg->name().empty()) {
                argName = arg->name();
            } else {
                argName = "arg" + i2s(j);
            }

            if (arg) {
                argType = arg->print();
                argDesc = arg->printlong();
            } else {
                argType = defaultArg.print();
                argDesc = defaultArg.printlong();
            }

            if (argType.empty()) {
                argType = "any";
            }

            std::string argStr(argName);
            argStr.reserve(argName.length() + 2 + argType.length() + 2);

            argStr.append(": ");
            argStr.append(argType);

            if (j > fn->minargs()) {
                argStr.insert(0, "[");
                argStr.append("]");
            }

            std::string argLongStr(argName);
            argLongStr.reserve(argName.length() + 2 + argDesc.length() + 2);

            argLongStr.append(": ");
            argLongStr.append(argDesc);

            if (j > fn->minargs()) {
                argLongStr.insert(0, "[");
                argLongStr.insert(1 + argName.length(), "]");
            }

            params.emplace_back(argStr);
            longParams.emplace_back(argLongStr);
        }

        if (fn->maxargs() < 0) {
            params.emplace_back("…");
        }

        auto paramsArr = env->NewObjectArray(static_cast<jint>(params.size()),
                                             env->FindClass("java/lang/String"), nullptr);

        for (int j = 0; j < params.size(); j++) {
            env->SetObjectArrayElement(paramsArr, j, utf8ToString(env, params[j]));
        }

        std::string paramsString;

        paramsString.reserve(std::accumulate(longParams.begin(), longParams.end(), 0,
                                             [](size_t acc, const std::string &s) {
                                                 return acc + s.length() + 1;
                                             }));

        if (!longParams.empty()) {
            paramsString.append(*longParams.begin());
            for (auto it = longParams.begin() + 1; it != longParams.end(); ++it) {
                paramsString.append("\n");
                paramsString.append(*it);
            }
        }

        std::string desc(fn->description());
        if (!desc.empty()) {
            desc.append("\n\n");
        }
        desc.append(paramsString);

        auto obj = env->CallStaticObjectMethod(calcFnCls, fromNativeMethod, utf8ToString(env,
                                                                                         fn->preferredName().formattedName(
                                                                                                 TYPE_FUNCTION,
                                                                                                 true)),
                                               utf8ToString(env, fn->title()),
                                               utf8ToString(env, desc),
                                               utf8ToString(env, fn->category()), paramsArr);

        env->SetObjectArrayElement(objArr, i, obj);
    }

    return objArr;

}