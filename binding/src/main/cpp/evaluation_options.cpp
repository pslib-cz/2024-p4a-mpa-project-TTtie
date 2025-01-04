//
// Created by tttie on 02.01.25.
//

#include "evaluation_options.hpp"

bool qalcBinding::EvaluationOptions::expressionColorization() const noexcept {
    return _expressionColorization;
}

AutoPostConversion qalcBinding::EvaluationOptions::unitConversion() const noexcept {
    return _unitConversion;
}

int qalcBinding::EvaluationOptions::precision() const noexcept {
    return _precision;
}

ApproximationMode qalcBinding::EvaluationOptions::approxMode() const noexcept {
    return _approxMode;
}

qalcBinding::EvaluationOptions
qalcBinding::EvaluationOptions::fromJava(JNIEnv *env, jobject evalOptions) {
    auto cls = env->FindClass("cz/tttie/qalculate/binding/options/EvaluationOptions");
    auto approxEnumClass = env->FindClass(
            "cz/tttie/qalculate/binding/options/evaluation/ApproximationMode");
    auto unitConvEnumClass = env->FindClass(
            "cz/tttie/qalculate/binding/options/evaluation/UnitConversion");

    auto approxField = env->GetFieldID(cls, "approximation",
                                       "Lcz/tttie/qalculate/binding/options/evaluation/ApproximationMode;");
    auto precisionField = env->GetFieldID(cls, "precision", "I");
    auto conversionField = env->GetFieldID(cls, "unitConversion",
                                           "Lcz/tttie/qalculate/binding/options/evaluation/UnitConversion;");
    auto colorizationField = env->GetFieldID(cls, "expressionColorization",
                                             "Z");

    auto valFieldApprox = env->GetFieldID(approxEnumClass, "value", "I");
    auto valFieldUnitConv = env->GetFieldID(unitConvEnumClass, "value", "I");

    auto approxEnum = env->GetObjectField(evalOptions, approxField);
    auto convEnum = env->GetObjectField(evalOptions, conversionField);

    auto approx = env->GetIntField(approxEnum, valFieldApprox);
    auto precision = env->GetIntField(evalOptions, precisionField);
    auto unitConv = env->GetIntField(convEnum, valFieldUnitConv);
    auto exprColor = env->GetBooleanField(evalOptions, colorizationField);

    return {
            static_cast<ApproximationMode>(approx),
            precision,
            static_cast<AutoPostConversion>(unitConv),
            exprColor
    };
}

jobject qalcBinding::EvaluationOptions::toJava(JNIEnv *env) {
    auto cls = env->FindClass("cz/tttie/qalculate/binding/options/EvaluationOptions");

    auto fromNativeMethod = env->GetStaticMethodID(cls, "fromNative",
                                                   "(IIIZ)Lcz/tttie/qalculate/binding/options/EvaluationOptions;");

    return env->CallStaticObjectMethod(cls, fromNativeMethod,
                                       static_cast<jint>(_approxMode),
                                       _precision,
                                       static_cast<jint>(_unitConversion),
                                       static_cast<jboolean>(_expressionColorization));
}

qalcBinding::EvaluationOptions qalcBinding::EvaluationOptions::getDefault() {
    return {
            ::default_evaluation_options.approximation,
            ::default_print_options.max_decimals,
            ::default_evaluation_options.auto_post_conversion,
            false
    };
}