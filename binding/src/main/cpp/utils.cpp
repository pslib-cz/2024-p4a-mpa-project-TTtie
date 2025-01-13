//
// Created by tttie on 02.01.25.
//

#include "utils.hpp"
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/configuration.h>
#include <android/log.h>
#include <android/log_macros.h>
#include <unicode/unistr.h>


jstring utf8ToString(JNIEnv *env, const std::string &str) {
    auto icuStr = icu::UnicodeString::fromUTF8(str);
    auto utf16StrBuffer = reinterpret_cast<const jchar *>(icuStr.getBuffer());

    return env->NewString(utf16StrBuffer, icuStr.length());
}

namespace qalcBinding {
    class Config {
    public:
        AConfiguration *config;

        Config(JNIEnv *env, jobject thiz) {
            auto cls = env->FindClass("cz/tttie/qalculate/binding/Qalculate");

            auto assetManagerField = env->GetFieldID(cls, "assets",
                                                     "Landroid/content/res/AssetManager;");

            auto assetManager = env->GetObjectField(thiz, assetManagerField);

            config = AConfiguration_new();
            AConfiguration_fromAssetManager(config, AAssetManager_fromJava(env, assetManager));
            ALOGD("Config created");
        }

        ~Config() {
            AConfiguration_delete(config);
            ALOGD("Config deleted");
        }
    };
}

bool isNightMode(JNIEnv *env, jobject thiz) {
    qalcBinding::Config cfg(env, thiz);
    auto nightMode = AConfiguration_getUiModeNight(cfg.config);

    return nightMode == ACONFIGURATION_UI_MODE_NIGHT_YES; // Anything else is considered as no
}

bool alwaysDisplayUnicode(const char *, void *) {
    return true;
}
