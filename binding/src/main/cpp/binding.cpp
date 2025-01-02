#include <vector>
#include <string>
#include <jni.h>
#include <android/log.h>
#include <unicode/unistr.h>
#include <unicode/ucnv.h>
#include <libqalculate/qalculate.h>
#include "evaluation_options.hpp"


Calculator *getCalc(JNIEnv *env, jobject thiz) {
    auto field = env->GetFieldID(
            env->GetObjectClass(thiz),
            "calculatorPtr", "J"
    );
    long ptr = env->GetLongField(thiz, field);

    return reinterpret_cast<Calculator *>(ptr);
}

jstring utf8ToString(JNIEnv *env, const std::string &str) {
    auto icuStr = icu::UnicodeString::fromUTF8(str);
    auto utf16StrBuffer = reinterpret_cast<const jchar *>(icuStr.getBuffer());

    return env->NewString(utf16StrBuffer, icuStr.length());
}

jobject convertCalculatorMessage(JNIEnv *env, CalculatorMessage *msg) {
    auto cls = env->FindClass("cz/tttie/qalculate/binding/CalculatorMessage");
    auto method = env->GetStaticMethodID(cls, "fromNative",
                                         "(ILjava/lang/String;)Lcz/tttie/qalculate/binding/CalculatorMessage;");

    auto javaStr = utf8ToString(env, msg->message());
    return env->CallStaticObjectMethod(cls, method, static_cast<jint>(msg->type()), javaStr);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_createCalculator(JNIEnv * /* env */, jclass /* clazz */) {
    __android_log_write(android_LogPriority::ANDROID_LOG_DEBUG, "C", "Test");
#pragma clang diagnostic push
#pragma ide diagnostic ignored "MemoryLeak" // This is leaked on purpose
    auto calc = new Calculator();
#pragma clang diagnostic pop
    calc->loadGlobalDefinitions();
    return reinterpret_cast<long>(calc);
}
extern "C"
JNIEXPORT void JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_deleteCalculator(JNIEnv *env, jobject thiz) {
    delete getCalc(env, thiz);
}
extern "C"
JNIEXPORT void JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_abortCalculation(JNIEnv *env, jobject thiz) {
    auto calc = getCalc(env, thiz);

    calc->abort();
}
extern "C"
JNIEXPORT jobject JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_calculate(JNIEnv *env, jobject thiz, jstring expr,
                                                    jboolean darkTheme) {
    auto calc = getCalc(env, thiz);

    auto strLength = env->GetStringLength(expr);
    auto strData = std::make_unique<jchar[]>(strLength);

    env->GetStringRegion(expr, 0, strLength, strData.get());

    icu::UnicodeString str(strData.get(), strLength);
    std::string stdStr;
    str.toUTF8String(stdStr);

    std::string parsedExpression;

    auto result = calc->calculateAndPrint(calc->unlocalizeExpression(stdStr), 0,
                                          default_user_evaluation_options, default_print_options,
                                          AUTOMATIC_FRACTION_AUTO, AUTOMATIC_APPROXIMATION_AUTO,
                                          &parsedExpression, -1, nullptr, true, darkTheme ? 2 : 0);

    auto resultCls = env->FindClass("cz/tttie/qalculate/binding/CalculationResult");
    auto resultConstructor = env->GetMethodID(resultCls, "<init>",
                                              "(Ljava/lang/String;[Lcz/tttie/qalculate/binding/CalculatorMessage;Ljava/lang/String;)V");


    auto javaResultStr = utf8ToString(env, result);
    auto javaParsedStr = utf8ToString(env, parsedExpression);

    std::vector<jobject> messages;
    while (calc->message()) {
        messages.push_back(convertCalculatorMessage(env, calc->message()));
        calc->nextMessage();
    }

    auto arr = env->NewObjectArray(static_cast<jint>(messages.size()),
                                   env->FindClass("cz/tttie/qalculate/binding/CalculatorMessage"),
                                   nullptr);

    for (int i = 0; i < messages.size(); i++) {
        env->SetObjectArrayElement(arr, i, messages[i]);
    }

    auto obj = env->NewObject(resultCls, resultConstructor, javaResultStr, arr, javaParsedStr);

    return obj;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_isBusy(JNIEnv *env, jobject thiz) {
    auto calc = getCalc(env, thiz);

    return calc->busy();
}
extern "C"
JNIEXPORT jobject JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_getDefaultEvaluationOptions(JNIEnv *env, jclass /* clazz */) {
    return qalcBinding::EvaluationOptions::getDefault().toJava(env);
}