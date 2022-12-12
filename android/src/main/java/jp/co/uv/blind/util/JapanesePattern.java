package jp.co.uv.blind.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JapanesePattern {
    public static String JAPANESE = "[ぁ-んァ-ンｧ-ﾝﾞﾟ一-龯０-９]";
    public static boolean isJapanese(String input){
        Pattern p = Pattern.compile(JAPANESE);
        Matcher m = p.matcher(input);
        return m.find();
    }
}
