package charlesli.com.personalvocabbuilder.sqlDatabase;

import java.util.Hashtable;

/**
 * Created by charles on 2016-05-25.
 */
public class LanguageOptions {

    public static final String [] FROM_LANGUAGE = {
            "Detect Language",
            "Afrikaans",
            "Albanian",
            "Arabic",
            "Armenian",
            "Azerbaijani",
            "Basque",
            "Belarusian",
            "Bengali",
            "Bosnian",
            "Bulgarian",
            "Catalan",
            "Cebuano",
            "Chichewa",
            "Chinese Simplified",
            "Chinese Traditional",
            "Croatian",
            "Czech",
            "Danish",
            "Dutch",
            "English",
            "Esperanto",
            "Estonian",
            "Filipino",
            "Finnish",
            "French",
            "Galician",
            "Georgian",
            "German",
            "Greek",
            "Gujarati",
            "Haitian Creole",
            "Hausa",
            "Hebrew",
            "Hindi",
            "Hmong",
            "Hungarian",
            "Icelandic",
            "Igbo",
            "Indonesian",
            "Irish",
            "Italian",
            "Japanese",
            "Javanese",
            "Kannada",
            "Kazakh",
            "Khmer",
            "Korean",
            "Lao",
            "Latin",
            "Latvian",
            "Lithuanian",
            "Macedonian",
            "Malagasy",
            "Malay",
            "Malayalam",
            "Maltese",
            "Maori",
            "Marathi",
            "Mongolian",
            "Myanmar (Burmese)",
            "Nepali",
            "Norwegian",
            "Persian",
            "Polish",
            "Portuguese",
            "Punjabi",
            "Romanian",
            "Russian",
            "Serbian",
            "Sesotho",
            "Sinhala",
            "Slovak",
            "Slovenian",
            "Somali",
            "Spanish",
            "Sudanese",
            "Swahili",
            "Swedish",
            "Tajik",
            "Tamil",
            "Telugu",
            "Thai",
            "Turkish",
            "Ukrainian",
            "Urdu",
            "Uzbek",
            "Vietnamese",
            "Welsh",
            "Yiddish",
            "Yoruba",
            "Zulu"
    };

    public static final String [] FROM_LANGUAGE_CODE = {
            "Detect Language",
            "af",
            "sq",
            "ar",
            "hy",
            "az",
            "eu",
            "be",
            "bn",
            "bs",
            "bg",
            "ca",
            "ceb",
            "ny",
            "zh-CN",
            "zh-TW",
            "hr",
            "cs",
            "da",
            "nl",
            "en",
            "eo",
            "et",
            "tl",
            "fi",
            "fr",
            "gl",
            "ka",
            "de",
            "el",
            "gu",
            "ht",
            "ha",
            "iw",
            "hi",
            "hmn",
            "hu",
            "is",
            "ig",
            "id",
            "ga",
            "it",
            "ja",
            "jw",
            "kn",
            "kk",
            "km",
            "ko",
            "lo",
            "la",
            "lv",
            "lt",
            "mk",
            "mg",
            "ms",
            "ml",
            "mt",
            "mi",
            "mr",
            "mn",
            "my",
            "ne",
            "no",
            "fa",
            "pl",
            "pt",
            "ma",
            "ro",
            "ru",
            "sr",
            "st",
            "si",
            "sk",
            "sl",
            "so",
            "es",
            "su",
            "sw",
            "sv",
            "tg",
            "ta",
            "te",
            "th",
            "tr",
            "uk",
            "ur",
            "uz",
            "vi",
            "cy",
            "yi",
            "yo",
            "zu"
    };

    public static final String [] TO_LANGUAGE = {
            "Afrikaans",
            "Albanian",
            "Arabic",
            "Armenian",
            "Azerbaijani",
            "Basque",
            "Belarusian",
            "Bengali",
            "Bosnian",
            "Bulgarian",
            "Catalan",
            "Cebuano",
            "Chichewa",
            "Chinese Simplified",
            "Chinese Traditional",
            "Croatian",
            "Czech",
            "Danish",
            "Dutch",
            "English",
            "Esperanto",
            "Estonian",
            "Filipino",
            "Finnish",
            "French",
            "Galician",
            "Georgian",
            "German",
            "Greek",
            "Gujarati",
            "Haitian Creole",
            "Hausa",
            "Hebrew",
            "Hindi",
            "Hmong",
            "Hungarian",
            "Icelandic",
            "Igbo",
            "Indonesian",
            "Irish",
            "Italian",
            "Japanese",
            "Javanese",
            "Kannada",
            "Kazakh",
            "Khmer",
            "Korean",
            "Lao",
            "Latin",
            "Latvian",
            "Lithuanian",
            "Macedonian",
            "Malagasy",
            "Malay",
            "Malayalam",
            "Maltese",
            "Maori",
            "Marathi",
            "Mongolian",
            "Myanmar (Burmese)",
            "Nepali",
            "Norwegian",
            "Persian",
            "Polish",
            "Portuguese",
            "Punjabi",
            "Romanian",
            "Russian",
            "Serbian",
            "Sesotho",
            "Sinhala",
            "Slovak",
            "Slovenian",
            "Somali",
            "Spanish",
            "Sudanese",
            "Swahili",
            "Swedish",
            "Tajik",
            "Tamil",
            "Telugu",
            "Thai",
            "Turkish",
            "Ukrainian",
            "Urdu",
            "Uzbek",
            "Vietnamese",
            "Welsh",
            "Yiddish",
            "Yoruba",
            "Zulu"
    };

    public static final String [] TO_LANGUAGE_CODE = {
            "af",
            "sq",
            "ar",
            "hy",
            "az",
            "eu",
            "be",
            "bn",
            "bs",
            "bg",
            "ca",
            "ceb",
            "ny",
            "zh-CN",
            "zh-TW",
            "hr",
            "cs",
            "da",
            "nl",
            "en",
            "eo",
            "et",
            "tl",
            "fi",
            "fr",
            "gl",
            "ka",
            "de",
            "el",
            "gu",
            "ht",
            "ha",
            "iw",
            "hi",
            "hmn",
            "hu",
            "is",
            "ig",
            "id",
            "ga",
            "it",
            "ja",
            "jw",
            "kn",
            "kk",
            "km",
            "ko",
            "lo",
            "la",
            "lv",
            "lt",
            "mk",
            "mg",
            "ms",
            "ml",
            "mt",
            "mi",
            "mr",
            "mn",
            "my",
            "ne",
            "no",
            "fa",
            "pl",
            "pt",
            "ma",
            "ro",
            "ru",
            "sr",
            "st",
            "si",
            "sk",
            "sl",
            "so",
            "es",
            "su",
            "sw",
            "sv",
            "tg",
            "ta",
            "te",
            "th",
            "tr",
            "uk",
            "ur",
            "uz",
            "vi",
            "cy",
            "yi",
            "yo",
            "zu"
    };
}
