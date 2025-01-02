//
// Created by tttie on 02.01.25.
//

#ifndef QALCULATE_EVALUATION_OPTIONS_HPP
#define QALCULATE_EVALUATION_OPTIONS_HPP

#include <jni.h>
#include <libqalculate/qalculate.h>

namespace qalcBinding {
    // Keep in sync with the Kotlin side
    struct EvaluationOptions {
    protected:
        ApproximationMode _approxMode;
        int _precision;
        AutoPostConversion _unitConversion;
        int _expressionColorization;
        EvaluationOptions(ApproximationMode approxMode, int precision,
                          AutoPostConversion unitConversion, int expressionColorization) :
                _approxMode(approxMode), _precision(precision), _unitConversion(unitConversion),
                _expressionColorization(expressionColorization) {}

    public:
        ApproximationMode approxMode() const noexcept;

        int precision() const noexcept;

        AutoPostConversion unitConversion() const noexcept;

        int expressionColorization() const noexcept;

        jobject toJava(JNIEnv *env);

        static EvaluationOptions fromJava(JNIEnv *env, jobject evalOptions);

        static EvaluationOptions getDefault();
    };

}


#endif //QALCULATE_EVALUATION_OPTIONS_HPP
