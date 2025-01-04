//
// Created by tttie on 03.01.25.
//

#include <libqalculate/Variable.h>
#include "variables.hpp"
#include "utils.hpp"
#include "evaluation_options.hpp"
#include <jni.h>
#include <android/log_macros.h>


extern "C"
JNIEXPORT jobjectArray JNICALL
Java_cz_tttie_qalculate_binding_Qalculate_getVariables(JNIEnv *env, jobject thiz,
                                                       jobject jEvalOpts) {
    auto calc = getCalc(env, thiz);

    auto evalOpts = qalcBinding::EvaluationOptions::fromJava(env, jEvalOpts);


    auto fnCls = env->FindClass("cz/tttie/qalculate/binding/CalculatorVariable");
    auto vars = calc->variables;

    auto fromNativeMethod = env->GetStaticMethodID(fnCls, "fromNative",
                                                   "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcz/tttie/qalculate/binding/CalculatorVariable;");

    auto arr = env->NewObjectArray(static_cast<jint>(vars.size()),
                                   fnCls, nullptr);

    auto isNight = isNightMode(env, thiz);


    for (int i = 0; i < vars.size(); i++) {
        auto varDfn = vars[i];

        auto name = varDfn->preferredName(true, true, false, false, alwaysDisplayUnicode)
                .formattedName(TYPE_VARIABLE, true);
        auto title = varDfn->title();
        std::string desc;

        if (varDfn->isKnown()) {
            auto dfn = dynamic_cast<KnownVariable *>(varDfn);

            if (dfn->get().isMatrix() && dfn->get().columns() * dfn->get().rows() > 16) {
                desc = "a matrix";
            } else if (dfn->get().isVector() && dfn->get().size() > 10) {
                desc = "a vector";
            } else {
                bool isApproximate;

                PrintOptions po(default_print_options);
                po.interval_display = INTERVAL_DISPLAY_PLUSMINUS;
                po.allow_non_usable = true;
                po.can_display_unicode_string_function = alwaysDisplayUnicode;
                po.is_approximate = &isApproximate;
                po.number_fraction_format = FRACTION_DECIMAL_EXACT;
                po.use_unicode_signs = true;
                po.max_decimals = evalOpts.precision();

                auto result = calc->print(dfn->get(), -1, po, true,
                                          evalOpts.qalcColorization(isNight), TAG_TYPE_HTML);
                desc = (varDfn->isApproximate() || isApproximate) ? SIGN_ALMOST_EQUAL " " : "= ";
                desc += result;
            }
        } else {
            auto dfn = dynamic_cast<UnknownVariable *>(varDfn);

            if (dfn->assumptions()) {
                if (dfn->assumptions()->type() != ASSUMPTION_TYPE_BOOLEAN) {
                    switch (dfn->assumptions()->sign()) {
                        case ASSUMPTION_SIGN_POSITIVE: {
                            desc = "positive";
                            break;
                        }
                        case ASSUMPTION_SIGN_NONPOSITIVE: {
                            desc = "non-positive";
                            break;
                        }
                        case ASSUMPTION_SIGN_NEGATIVE: {
                            desc = "negative";
                            break;
                        }
                        case ASSUMPTION_SIGN_NONNEGATIVE: {
                            desc = "non-negative";
                            break;
                        }
                        case ASSUMPTION_SIGN_NONZERO: {
                            desc = "non-zero";
                            break;
                        }
                        default: {
                        }
                    }
                }
                if (!desc.empty() && dfn->assumptions()->type() != ASSUMPTION_TYPE_NONE) {
                    desc += " ";
                }
                switch (dfn->assumptions()->type()) {
                    case ASSUMPTION_TYPE_BOOLEAN: {
                        desc += "boolean";
                        break;
                    }
                    case ASSUMPTION_TYPE_INTEGER: {
                        desc += "integer";
                        break;
                    }
                    case ASSUMPTION_TYPE_RATIONAL: {
                        desc += "rational";
                        break;
                    }
                    case ASSUMPTION_TYPE_REAL: {
                        desc += "real";
                        break;
                    }
                    case ASSUMPTION_TYPE_COMPLEX: {
                        desc += "complex";
                        break;
                    }
                    case ASSUMPTION_TYPE_NUMBER: {
                        desc += "number";
                        break;
                    }
                    case ASSUMPTION_TYPE_NONMATRIX: {
                        desc += "non-matrix";
                        break;
                    }
                    default: {
                    }
                }

                if (desc.empty()) {
                    desc = "unknown";
                }
            } else {
                desc = "Default assumptions";
            }
        }

        auto cat = varDfn->category();

        auto obj = env->CallStaticObjectMethod(fnCls, fromNativeMethod,
                                               utf8ToString(env, name),
                                               utf8ToString(env, title),
                                               utf8ToString(env, desc),
                                               utf8ToString(env, cat));

        env->SetObjectArrayElement(arr, i, obj);
    }

    return arr;
}