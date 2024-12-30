#include <jni.h>
#include <android/log.h>
#include <string>
#include <libqalculate/qalculate.h>

Calculator *getCalc(JNIEnv *env, jobject thiz) {
    auto field = env->GetFieldID(
            env->GetObjectClass(thiz),
            "calculatorPtr", "J"
    );
    long ptr = env->GetLongField(thiz, field);

    return reinterpret_cast<Calculator *>(ptr);
}

extern "C" JNIEXPORT jstring JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_stringFromJNI(
        JNIEnv *env,
        jobject thiz) {
    auto calc = getCalc(env, thiz);

    auto result = calc->calculateAndPrint("1 + 1");

    return env->NewStringUTF(result.c_str());
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