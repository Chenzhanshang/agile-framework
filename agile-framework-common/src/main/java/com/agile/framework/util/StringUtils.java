package com.agile.framework.util;

/**
 * @author chenzhanshang
 * @date 2025/9/1 11:48
 * @describe :
 */
public class StringUtils {
    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }


    /**
     * 初步判断字符串是否是json字符串
     * @param str
     * @return
     */
    public static boolean isJsonStr(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        str = str.trim();
        return str.startsWith("{") && str.endsWith("}");
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a'
                || c == '\u0000';
    }
}
