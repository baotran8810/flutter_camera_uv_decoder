package jp.co.uv.blind.common;

public class Def {
	public static String packageName = "jp.co.uv";
	public static String path = "/Android/data/";
	public static String TEMP = "temp";
	
	public static String IVC = "IVC";

	public static String JSONOBJECT = "Download";
	public static String ELEMENT_ID = "id";
	public static String ELEMENT_TITLE = "title";
	public static String FACILITY_TITLE = "brochureName";
	public static String FACILITY_JP_TITLE = "brochureNameJP";
	public static String NAVI_TITLE = "coursename";
	public static String NAVI_JP_TITLE = "coursenameJP";
	public static String ELEMENT_SOURCE_TEXT = "sourceText";
	public static String ELEMENT_TARGET_TEXT = "targetText";
	public static String ELEMENT_SOURCE_LANGUAGE = "sourceLanguage";
	public static String ELEMENT_TARGET_LANGUAGE = "targetLanguage";
	public static String IS_VOICE_CODE = "VoiceCode";
	public static String IS_CODE_LIST = "CodeList";
	public static String URL_TERMS = "https://www.uni-voice.co.jp/policies/terms/";
	public static String URL_PRIVACY = "http://uni-voice.co.jp/policy/";
	public static String VOICE_CODE_ID = "voice_code_id";
	public static String VOICE_COMPANY_ID = "voice_company_id";
	public static String VOICE_PROJECT_ID = "voice_project_id";
	
	public static String TAG_PRE_SETTING = "setting";
	public static String TAG_PUSH_NOTIFICATION = "push_notification";
	public static String TAG_FACILITY_MAP = "facilitymap";
	public static String TAG_PRE_FIRST_USED = "firstused";
	public static String TAG_USER_INFO = "hasinfo";
	public static String TAG_USER_LOCATION = "user_location";
	public static String TAG_LIKE_INFO = "like_info";

	public static String TAG_PUSH_TIME = "push_time";

	public static String TAG_PRE_UUID = "uuid";
	public static String TAG_PRE_BULK = "bulk";
	public static String TAG_PRE_TORCH = "torch";
	public static String TAG_PRE_SIGN = "sign";
	public static String TAG_PRE_TUTORIAL = "tutorial";
	public static String TAG_PRE_CONTINUOUS = "tutorial";
	public static String TAG_PRE_AUTO_SAVE = "autosave";
	public static String TAG_PRE_PREPROCESS = "preprocess";	// grape
	public static String TAG_PRE_GUIDE = "guide";
	public static String TAG_PRE_VOICE_GUIDE = "guide";
	public static String TAG_PRE_VIBE = "vibe";
	public static String TAG_SHAKE = "vibe";
	public static String TAG_PROGRESS = "progress";
	public static String TAG_PRE_LANGUAGE = "language";
    public static String TAG_RADIUS = "radius_distance";
    public static String TAG_DISTANCE = "out_of_distance";
	public static String VERSION_NUMBER_KEY = "VERSION_NUMBER_KEY";
	public static String SHOW_ARABIC_TTS_ALERT_KEY = "SHOW_ARABIC_TTS_ALERT_KEY";
	public static String SOUND_SETTINGS = "uv_sound";
	public static String TAG_VERSION_CODE = "tag_version_code";
	public static String TAG_ONESIGNAL = "tag_onesignal";
	public static String TAG_ALLOWED_PUSH = "allowed_push";
	public static String TAG_PUSH_SETTINGS_COUNT = "push_settings_count";

	public static String DOC_CLICKED = "doc_clicked";
	public static String NAVI_CLICKED = "navi_clicked";
	public static String SPOT_CLICKED = "spot_clicked";
	public static String FAV_CLICKED = "fav_clicked";
	public static String NAVI_SPEECH_SPEED = "navi_speech_speed";

	public static String TAG_DOWNLOAD = "download";
	public static String TAG_FACILITY = "facility";
	public static String TAG_NAVI = "navi";
	
	public static String LANG_JPN = ".jpn";
	public static String LANG_ENG = ".eng";
	public static String LANG_CHN = ".chi";
	public static String LANG_ZHO = ".zho";
	public static String LANG_KOR = ".kor";
	public static String LANG_TWN = ".twn";
	public static String LANG_BRA = ".bra";
	public static String LANG_ESP = ".esp";
	public static String LANG_FRA = ".fra";	
	public static String LANG_DEU = ".deu";	
	public static String LANG_JSL = ".jpn";
	// Additional languages
	public static String LANG_EGB = ".egb";
	public static String LANG_FRE = ".fre";
	public static String LANG_GER = ".ger";
	public static String LANG_SPA = ".spa";
	public static String LANG_ITA = ".ita";
	public static String LANG_POR = ".por";
	public static String LANG_RUS = ".rus";
	public static String LANG_TAI = ".tai";
	public static String LANG_VIE = ".vie";
	public static String LANG_IND = ".ind";
	public static String LANG_ARA = ".ara";
	public static String LANG_HINDI = ".hin";
	public static String LANG_DUTCH = ".dut";
	public static String LANG_MALAY = ".may";
	public static String LANG_TAGALOG = ".tgl";
	
	public static String PACKAGE_NAME_GOOGLE_TTS = "com.google.android.tts";
	public static String PACKAGE_NAME_VNSPEAK_TTS = "com.vnspeak.ttsengine.vitts";
	
	public static String GOOGLE_PLAY_SCHEME_GOOGLE_TTS = "https://play.google.com/store/apps/details?id=com.google.android.tts";
	public static String GOOGLE_PLAY_SCHEME_VNSPEAK_TTS = "https://play.google.com/store/apps/details?id=com.vnspeak.ttsengine.vitts";

	public static String URL_TTS_ARABIC_VOXYGEN_TTS = "https://demows.voxygen.fr/ws/tts1?text=%s&voice=Adel&header=headerless&coding=mp3%%3A128-0&user=anders.ellersgaard%%40mindlab.dk&hmac=21c86c9e4ca60c84165f4f7915fef6a7";
//	public static String URL_TTS_ARABIC_ISPEECH_TTS = "http://www.ispeech.org/p/generic/getaudio?text=%s&voice=arabicmale&speed=0&action=convert";
	public static String URL_TTS_ARABIC_ISPEECH_TTS = "https://api.ispeech.org/api/rest?deviceType=Android&apikey=6419ca8157cc8c715e58ceeaa12d4151&action=convert&voice=arabicmale&speed=0&pitch=100&text=%s";

	public static String URL_TTS_VIETNAMESE_GET_VOICE_DATA_ID = "http://cloudtalk.vn/tts";
	public static String URL_TTS_VIETNAMESE_GET_VOICE_DATA = "http://162.248.166.192/ttsoutput?id=%s";
	
	public static String SERVER_LOCALE_JAPAN = "Asia/Tokyo";
	public static String SERVER_DATE_FORMAT = " yyyyMMddHHmmss";
	public static String SERVER_STRING_FORMAT = "UTF-16LE";
	public static String SERVER_STRING_FORMAT_UTF8 = "UTF-8";
	public static String SERVER_TAG_ERROR = "error";
	public static String SERVER_URL = "http://translationsystem.uni-voice.jp";
	public static String ENCKEY = "ounisvoiceotranslationlsymstiems";
	
		public static String LOCAL_MODULE = "app";
	public static String ESPEAK_MODULE = "espeak";
	
	public static String SONY_DEVICE = "Sony";
	public static String FUJITSU_DEVICE = "FUJITSU";	
	public static String SAMSUNG_DEVICE = "samsung";
	public static String XIAOMI_DEVICE = "Xiaomi";
	
	public static String HTC_BUTTERFLY_DEVICE = "HTC Butterfly";
	public static String NEXUS_5X_DEVICE = "Nexus 5X";

	public static String STRING_FORMAT_JAPANESE = "Shift_JIS";
	
	public static String DIALOG_OK = "OK";
	public static String DIALOG_CANCEL = "Cancel";
	
	public static String FILE = "file://";
	
	public static int SERVER_TIMEOUT = -67890;
	
	public static boolean IS_DEBUG = true;
	
	public static String tempFile = "/univoice_temp_%d.wav";
	public static String savetempFile = "/univoice_savetemp_%d.wav";
	public static String fileName = "/univoice_%d_%d.wav";
	public static String fileType = ".wav";
	
	public static String EMAIL_TITLE = "Uni-Voice Code Data";
	
	public static int INT_INIT = 2000;
	
	public static final int STANDARD_SIZE = 688;

	public static final int FULL_WIDTH_LENGTH_LIMIT = 120;
	public static final int HALF_WIDTH_LENGTH_LIMIT = 300;

	public static final int GOOGLE_PLAY_VERSION_CODE = 62;

	public static String NAVI_CODE = "#nc";
	public static String FACILITY_CODE = "#nf";
	public static String CODE_ID = "%ci";

	public static String NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	public static String DIRECTION ="https://maps.googleapis.com/maps/api/directions/json?";
	public static String GOOGLE_KEY = "&key=AIzaSyBdx2vH8CUv7OO2pN64AUERhSVXmD50Bxg";

	//Request code
	public final static int REQUEST_CODE_HOME_ACTIVITY = 2;

	// Activity Result Code
	public final static int RESULT_CODE_HOME= 1;

	public final static int isFAVOURITE =1;
	public final static int NOT_FAVOURITE =0;

	public final static String SPEAK_INTRO ="intro";
	public final static String SPEAK_NEXT_POINT ="next_point";
	public final static String SPEAK_CHECK_IN ="check_in";
	public final static String SPEAK_INFO ="speak_info";
	public final static String FROM_FACILITY = "from_facity";
	public final static String NEW_DIRECTION = "newDirection";
	public final static String SOURCE_DIRECTION = "sourceDirection";
	public final static String IS_CHECK = "isCheck";
	public final static String IS_FLIP = "isFlip";

	//Firebase Analytics Event
	public final static String EVENT_SCAN = "scan_code";
	public final static String EVENT_START_NAVI = "start_navigate";
	public final static String EVENT_STOP_NAVI = "stop_navigate";
	public final static String EVENT_OPEN_FILE = "open_file";
	public final static String EVENT_OPEN_PUSH = "open_push";
	public final static String EVENT_RECEIVE_PUSH = "receive_push";
	public final static String EVENT_LIKE_PUSH = "like_push";
	public final static String EVENT_FINISH_SETTINGS = "finished_initialsetting";
	public final static String EVENT_DECODE_ATTACHEDCODE = "decode_attachedcode";
	public final static String EVENT_FAIL_SETTINGS = "fail_initialsetting";

	//Firebase Analytics Event param
	public final static String FILE_NAME_ITEM = "file_name";
	public final static String CODE_NAME_ITEM ="code_name";
	public final static String ROUTE_NAME_ITEM ="route_name";
	public final static String DESTINATION_NAME_ITEM ="destination_name";
	public final static String PUSH_TITLE = "push_title";
	public final static String PUSH_MESSAGE_ID  = "push_message_id";
	public final static String PUSH_SENDER  = "push_sender";
	public final static String PARAM_CODE_ID  = "code_id";
	public final static String PARAM_COMPANY_ID  = "company_id";
	public final static String PARAM_PROJECT_ID  = "project_id";

	//Firebase User properties
	public final static String PROPERTIES_SUCCESS  = "success";
	public final static String PROPERTIES_FAIL  = "fail";
	public final static String PROPERTIES_NOT_ALLOW  = "not_allowed";
//	public final static String PROPERTIES_NOT_SET  = "not_set_yet";



	//Notification
	public  final static String AD = "advertise";
	public  final static String STATUS = "status";
	public final static String OK  = "OK";
	public final static String DATA  = "data";
	public final static String TAG_DATA  = "tagData";

	public final static String WEBVIEW  = "webview_url";
	public final static String MESSAGEID  = "message_id";
	public final static String COMPANYID  = "company_id";
	public final static String NOTIFICATION  = "notification";
	public final static String PREFECTURE  = "prefectureName";
	public final static String CITY  = "cityName";
	public final static String QR_CODE = "qrcode";
	public final static String LAST_PUSH = "last_push";
	public final static String COMPANY_NAME ="company_name";

	public final static String RELOAD_LIST ="reload-list";


	//User information
	public  final static String GENDER = "gender";
	public  final static String AGE = "age";

	public final static String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


}
