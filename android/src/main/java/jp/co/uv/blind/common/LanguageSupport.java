package jp.co.uv.blind.common;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import jp.co.uv.blind.util.CheckVersion;

public class LanguageSupport {
    public static String TAG = LanguageSupport.class.getSimpleName();

    public static final int INIT_TTS_ERROR = -2;
    public static final int LANGUAGE_NOT_SUPPORTED = -1;
    public static final int LANGUAGE_SUPPORTED = 0;

    private static TextToSpeech googleTTSEngine;
    private static boolean onInitializing = false;
    private static boolean onInitDone = false;

    public static void initGoogleTTSEngine(final Context context) {
        if (!CheckVersion.isPackageInstalled(context.getPackageManager(), Def.PACKAGE_NAME_GOOGLE_TTS)) {
            return;
        }
        if (!onInitializing) {
            onInitializing = true;
            if (googleTTSEngine != null) {
                googleTTSEngine.stop();
                googleTTSEngine.shutdown();
                googleTTSEngine = null;
            }
            googleTTSEngine = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        onInitDone = true;
//                        MyApp.setGoogleTTSVersion(CheckVersion.getVersion(context.getPackageManager(), Def.PACKAGE_NAME_GOOGLE_TTS));
                    }
                    onInitializing = false;
                }
            }, Def.PACKAGE_NAME_GOOGLE_TTS);
        }
    }

    public static void initGoogleTTSIfNeeded(Context context) {
//        String storedGoogleTTSVersion = MyApp.getGoogleTTSVersion();
//        String currentGoogleTTSVersion = CheckVersion.getVersion(context.getPackageManager(), Def.PACKAGE_NAME_GOOGLE_TTS);
//        // This case is the first app usage, and Google TTS doesn't exist in device
//        if (storedGoogleTTSVersion == null) {
//            initGoogleTTSEngine(context);
//        }
//        // This case happens when user is using app and Google TTS is updated
//        if (storedGoogleTTSVersion != null && currentGoogleTTSVersion != null && !storedGoogleTTSVersion.equalsIgnoreCase(currentGoogleTTSVersion)) {
//            initGoogleTTSEngine(context);
//        }
    }

    /**
     * Checks if the specified language is available and supported by Google TTS engine
     *
     * @param flag
     * @return
     */
    public static int isLanguageAvailableByGoogleTTSEngine(final String flag) {
        if (!onInitDone) {
            return INIT_TTS_ERROR;
        }
        Locale locale;
        if (flag.equalsIgnoreCase(Def.LANG_JPN)) {
            locale = Locale.JAPAN;
        } else if (flag.equalsIgnoreCase(Def.LANG_ENG)) {
            locale = Locale.US;
        } else if (flag.equalsIgnoreCase(Def.LANG_CHN)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (flag.equalsIgnoreCase(Def.LANG_ZHO)) {
            locale = Locale.TRADITIONAL_CHINESE;
        } else if (flag.equalsIgnoreCase(Def.LANG_KOR)) {
            locale = Locale.KOREA;
        } else if (flag.equalsIgnoreCase(Def.LANG_FRE)) {
            locale = Locale.FRANCE;
        } else if (flag.equalsIgnoreCase(Def.LANG_GER)) {
            locale = Locale.GERMANY;
        } else if (flag.equalsIgnoreCase(Def.LANG_SPA)) {
            locale = new Locale("es", "ES");
        } else if (flag.equalsIgnoreCase(Def.LANG_ITA)) {
            locale = Locale.ITALY;
        } else if (flag.equalsIgnoreCase(Def.LANG_POR)) {
            locale = new Locale("pt", "PT");
        } else if (flag.equalsIgnoreCase(Def.LANG_RUS)) {
            locale = new Locale("ru", "RU");
        } else if (flag.equalsIgnoreCase(Def.LANG_TAI)) {
            locale = new Locale("th", "TH");
        } else if (flag.equalsIgnoreCase(Def.LANG_VIE)) {
            locale = new Locale("vi", "VN");
        } else if (flag.equalsIgnoreCase(Def.LANG_IND)) {
            locale = new Locale("id", "ID");
        } else if (flag.equalsIgnoreCase(Def.LANG_ARA)) {
            locale = new Locale("ar", "SA");
        } else if (flag.equalsIgnoreCase(Def.LANG_MALAY)) {
            locale = new Locale("ms", "MY");
        } else if (flag.equalsIgnoreCase(Def.LANG_HINDI)) {
            locale = new Locale("hi", "IN");
        } else if (flag.equalsIgnoreCase(Def.LANG_DUTCH)) {
            locale = new Locale("nl", "NL");
        } else if (flag.equalsIgnoreCase(Def.LANG_TAGALOG)) {
            locale = new Locale("fil");
        } else {
            locale = Locale.US;
        }
        if (googleTTSEngine.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
            return LANGUAGE_SUPPORTED;
        }
        return LANGUAGE_NOT_SUPPORTED;
    }

    public static int checkLanguages(String flag) {
        if (!onInitDone) {
            return INIT_TTS_ERROR;
        }
        if (flag.equalsIgnoreCase(Def.LANG_MALAY)  || flag.equalsIgnoreCase(Def.LANG_ARA)) {
            return LANGUAGE_NOT_SUPPORTED;
        }
        return LANGUAGE_SUPPORTED;
    }

    /**
     * Checks if the text in specified language is able to be synthesized by TTS engine
     *
     * @param language
     * @return
     */
    public static boolean isSupportedByTTSEngine(String language) {
        if (language.equalsIgnoreCase(Def.LANG_ARA)) {
            return false;
        } else {
            return true;
        }
    }
}