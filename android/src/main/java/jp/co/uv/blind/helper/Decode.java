package jp.co.uv.blind.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import org.mozilla.universalchardet.UniversalDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.uv.blind.common.Def;
import jp.co.uv.blind.common.LanguageSupport;
import jp.co.uv.blind.model.Direction;
import jp.co.uv.blind.model.Facility;
import jp.co.uv.blind.model.Information;
import jp.co.uv.blind.util.JapanesePattern;

public class Decode {
    // uvtb_type attribute bit
    private final static int UVTB_TYPE_MASK = 0x0F;
    private final static int UVTB_TYPE_ATTR_MASK = 0xF0;
    private final static int UVTB_TYPE_ATTR_NO_SPEAK = 0x80;
    private final static int UVTB_TYPE_ATTR_NO_SAVE = 0x40;

    private String deviceLanguage;
    private boolean isNavi = false;
    private boolean isFacility = false;
    private boolean isVoiceCode = true;

    private byte[] data;
    private int width;
    private int height;
    private Context context;

    protected byte[] contentByte;
    private String codeID;
    private String companyID;
    private String projectID;
    private boolean hasGotoResult = false;

    private int invalid;
    private int brightnessMin;
    private int brightnessMax;
    private int bufferType;

    private String strId = "";
    private int statusCode;
    private String currentLanguage = ".jpn";

    private String savedContent;
    private String speakContent;
    private String savedSpeakContent;
    private String content;
    private boolean isJapanese;

    public Decode(Context ctx, byte[] data, int width, int height) {
        this.context = ctx;
        this.data = data;
        this.width = width;
        this.height = height;


    }

    public String getAppID(){
        return this.strId;
    }

    public native void setUvDecodeSymMode(int mode);

    public native int getUvDecodeSymMode();

    public native int callJavis(int width, int height, byte[] bytes, int threshold, AssetManager manager);

    static {
        System.loadLibrary("uv_camera");
    }

    public void setSuggestedWord(String id, int code, byte[] c, int invalid, int bufferType) {
        if (c != null && id != null) {
            contentByte = Arrays.copyOfRange(c, 0, c.length - id.getBytes().length);
        } else {
            contentByte = c;
        }
        this.strId = id;
        this.statusCode=code;
        this.invalid=invalid;
        this.bufferType=bufferType;
    }

    public void setDeviceLanguage(String language) {
        this.deviceLanguage = language;
    }

    protected void detectNoTagCode() {
        //TODO: Show noTag Code
    }

    private boolean isDigitOnly(String str) {
        if (str.equals("")) {
            return false;
        } else {
            return TextUtils.isDigitsOnly(str);
        }
    }

    public void setBrightness(int min, int max) {    // grape
        brightnessMin = min;
        brightnessMax=max;
    }

    public String decodeImage(int mLumiaAverage) {
        setUvDecodeSymMode(0);
        AssetManager manager = context.getAssets();
        int javisReuslt = callJavis(width, height, data, mLumiaAverage, manager);
        Log.d("Decode", "javisReuslt = " + javisReuslt);
        StringBuilder codeData = null;
        if (javisReuslt > 0) {

            if (invalid == -45) {
//                detectInvalidUniCode();
            }
            // Old JAVIS
            if (bufferType == 3) {
                try {
                    String deString = new String(contentByte, Def.STRING_FORMAT_JAPANESE);
                    if (deString.contains("�")) {
                        deString = new String(contentByte, Def.SERVER_STRING_FORMAT);
                    }
                    if (deString.contains("<tag")) {
                        savedContent = deString;
                        speakContent = deString;
                        savedSpeakContent= deString;
                        content = deString;
                    } else {
                        if (!isDigitOnly(deString)) {
                            if (JapanesePattern.isJapanese(deString)) {
                                savedContent = deString;
                                speakContent = deString;
                                savedSpeakContent= deString;
                                content = deString;
                                isJapanese = true;
                            } else {
                                detectNoTagCode();
                                return null;
                            }

                        }
                    }
                    codeData = new StringBuilder(String.format("<tag lang=\"%s\">\r\n%s\r\n</tag>\r\n", "jpn", content));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bufferType == 4) { // New JAVIS
                int i = 0;
                String deString = "";
                String strSave = "";
                String strSpeak = "";
                String strSavedSpeak = "";
                String strCodeID = "";      // code ID

                while (i < contentByte.length - 1) {
                    int nType = 0;
                    String sCountry = "";
                    String content = "";
                    int nAttr = 0;
                    int nLength = 0;

                    boolean isSpeak = true;
                    boolean isSave = true;

                    // Attribute
                    nAttr = (int) (contentByte[i] & UVTB_TYPE_ATTR_MASK);
                    // DataType
//                    nType = (int) contentByte[i++];
                    nType = (int) (contentByte[i++] & UVTB_TYPE_MASK);
                    Log.i("grape", "type = " + nType);
                    if ((nAttr & (UVTB_TYPE_ATTR_NO_SPEAK | UVTB_TYPE_ATTR_NO_SAVE))
                            == UVTB_TYPE_ATTR_NO_SPEAK) {
                        Log.i("grape", "attr = no_speak");
                        isSpeak = false;
                    } else if ((nAttr & (UVTB_TYPE_ATTR_NO_SPEAK | UVTB_TYPE_ATTR_NO_SAVE))
                            == UVTB_TYPE_ATTR_NO_SAVE) {
                        Log.i("grape", "attr = no_save");
                        isSave = false;
                    } else if ((nAttr & (UVTB_TYPE_ATTR_NO_SPEAK | UVTB_TYPE_ATTR_NO_SAVE))
                            == (UVTB_TYPE_ATTR_NO_SPEAK | UVTB_TYPE_ATTR_NO_SAVE)) {
                        Log.i("grape", "attr = no_speak, no_save");
                        isSpeak = false;
                        isSave = false;
                    }
                    // CountryCode
                    sCountry = new String(new byte[]{contentByte[i++]});
                    sCountry += new String(new byte[]{contentByte[i++]});
                    sCountry += new String(new byte[]{contentByte[i++]});

                    // Length
                    int sByte1 = contentByte[i++];
                    int sByte2 = contentByte[i++];
                    if (sByte1 < 0) {
                        sByte1 += 256;
                    }
                    if (sByte2 < 0) {
                        sByte2 += 256;
                    }
                    nLength = sByte1;
                    nLength += sByte2 * 256;

                    // TextData
                    byte[] byteText = new byte[nLength];
                    for (int j = 0; j < nLength; j++) {
                        byteText[j] = contentByte[i];
                        i++;
                    }

                    // Convert
                    try {
                        // Japanese
                        if (nType == 1 || nType == 4) { // SJIS / Ascii(ISO/IEC-8859-1
                            if (LanguageSupport.checkLanguages("." + sCountry) == 0) {
                                String contentString = new String(byteText, Def.STRING_FORMAT_JAPANESE);
                                if(currentLanguage.equals("." + sCountry)){
                                    deString = contentString + "\n\n" + deString;
                                }else{
                                    deString = deString + "\n\n" + contentString;
                                }

                                Log.d("Scan Activity ", "deString " + deString);
                                if (isSpeak) {
                                    strSpeak = contentString + "\n\n" + strSpeak;
                                    Log.d("Scan Activity ", "strSpeak " + strSpeak);
                                }
                            } else {
                                deString += new String(byteText, Def.STRING_FORMAT_JAPANESE) + "\n\n";
                            }
                            content = new String(byteText, Def.STRING_FORMAT_JAPANESE);
                        } else if (nType == 2) { // UTF-8 // English
                            if (LanguageSupport.checkLanguages("." + sCountry) == 0) {
                                String contentString = new String(byteText, Def.SERVER_STRING_FORMAT);
                                if(currentLanguage.equals("." + sCountry)){
                                    deString = contentString + "\n\n" + deString;
                                }else{
                                    deString = deString + "\n\n" + contentString;
                                }
                                if (isSpeak) {
                                    strSpeak = strSpeak + "\n\n" + contentString;
                                }
                            } else {
                                deString += new String(byteText, Def.SERVER_STRING_FORMAT) + "\n\n";
                            }
                            content = new String(byteText, Def.SERVER_STRING_FORMAT);
                        } else if (nType == 3) { // UTF-16LE // Chinese & Korean
                            if (LanguageSupport.checkLanguages("." + sCountry) == 0) {
                                String contentString = new String(byteText, Def.SERVER_STRING_FORMAT);
                                if(currentLanguage.equals("." + sCountry)){
                                    deString = contentString + "\n\n" + deString;
                                }else{
                                    deString = deString + "\n\n" + contentString;
                                }

                                if (isSpeak) {
                                    strSpeak = strSpeak + "\n\n" + contentString;
                                }
                            } else {
                                deString += new String(byteText, Def.SERVER_STRING_FORMAT) + "\n\n";
                            }
                            content = new String(byteText, Def.SERVER_STRING_FORMAT);
                        } else if (nType == 0) { //Navi code & Facility code
                            boolean isContainUFT16 = false;
                            UniversalDetector detector = new UniversalDetector();
                            detector.handleData(byteText);
                            detector.dataEnd();

                            if (detector.getDetectedCharset() != null && detector.getDetectedCharset().equals("SHIFT_JIS")) {
                                deString += new String(byteText, Def.STRING_FORMAT_JAPANESE);
                                if (deString.contains("�")) {
                                    deString = new String(contentByte, Def.SERVER_STRING_FORMAT);
                                }
                            } else if (detector.getDetectedCharset() != null && detector.getDetectedCharset().equals("WINDOWS-1252")) {
                                deString = new String(contentByte, Def.SERVER_STRING_FORMAT);
                            } else {
                                //Detect UTF16
                                for (int it = 0; it < byteText.length - 1; it++) {
                                    if (it + 1 > byteText.length) break;
                                    if (byteText[it] == 0 && byteText[it + 1] != 0) {
                                        isContainUFT16 = true;
                                        break;
                                    }
                                }

                                if (isContainUFT16) {
                                    deString = new String(contentByte, Def.SERVER_STRING_FORMAT);
                                } else {
                                    deString = new String(contentByte, Def.SERVER_STRING_FORMAT_UTF8);
                                }
                            }

                            codeData = new StringBuilder(deString);
                            return codeData.toString();
                        } else if (nType == 10) { // code ID
                            strCodeID = new String(byteText, Def.STRING_FORMAT_JAPANESE);
                            Log.i("grape ", "strCodeID " + strCodeID);
                        }

                        if(codeData == null){
                            codeData = new StringBuilder(String.format("<tag lang=\"%s\">\r\n%s\r\n</tag>\r\n", sCountry, content));
                        }else{
                            if(sCountry.equals("%ci") && strCodeID != null){
                                codeData.append(String.format("<tag lang=\"%s\">\r\n%s\r\n</tag>\r\n", sCountry, strCodeID));
                            }
                            else {
                                codeData.append(String.format("<tag lang=\"%s\">\r\n%s\r\n</tag>\r\n", sCountry, content));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // If result is -17 or -18, that means the rectangle is similar as voice code
        } else if (javisReuslt == -17 || javisReuslt == -18) {
        }
        return codeData == null ? null : codeData.toString();
    }

    private void extractID(String strCodeID) {
        String[] ids = strCodeID.trim().split("\r\n");
        if (ids.length < 3) return;
        codeID = ids[0];
        companyID = ids[1];
        projectID = ids[2];
    }
}