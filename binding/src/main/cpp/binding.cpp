#include <jni.h>
#include <android/log.h>
#include <string>
#include <unicode/unistr.h>
#include <unicode/ucnv.h>
#include <libqalculate/qalculate.h>


Calculator *getCalc(JNIEnv *env, jobject thiz) {
    auto field = env->GetFieldID(
            env->GetObjectClass(thiz),
            "calculatorPtr", "J"
    );
    long ptr = env->GetLongField(thiz, field);

    return reinterpret_cast<Calculator *>(ptr);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_createCalculator(JNIEnv *env, jclass clazz) {
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
    std::unique_ptr<jchar> strData(new jchar[strLength]);

    env->GetStringRegion(expr, 0, strLength, strData.get());

    icu::UnicodeString str(strData.get(), strLength);
    std::string stdStr;
    str.toUTF8String(stdStr);

    auto result = calc->calculateAndPrint(calc->unlocalizeExpression(stdStr), 0,
                                          default_user_evaluation_options, default_print_options,
                                          AUTOMATIC_FRACTION_AUTO, AUTOMATIC_APPROXIMATION_AUTO,
                                          nullptr, -1, nullptr, true, darkTheme ? 2 : 1);

    auto resultCls = env->FindClass("cz/tttie/qalculate/binding/CalculationResult");
    auto resultConstructor = env->GetMethodID(resultCls, "<init>",
                                              "(Ljava/lang/String;[Ljava/lang/String;)V");
    auto arr = env->NewObjectArray(0, env->FindClass("java/lang/String"), nullptr);
    icu::UnicodeString resultStr = icu::UnicodeString::fromUTF8(result);

    // SAFE: it can be safely assumed that char16_t will have exactly 16 bits on the platforms we're compiling for
    auto resultBuffer = reinterpret_cast<const jchar *>(resultStr.getBuffer());
    auto javaResultStr = env->NewString(resultBuffer, resultStr.length());

    auto obj = env->NewObject(resultCls, resultConstructor, javaResultStr, arr);

    return obj;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_isBusy(JNIEnv *env, jobject thiz) {
    auto calc = getCalc(env, thiz);

    return calc->busy();
}