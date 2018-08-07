package com.bat.system.service.storesetting.executor;

import java.util.Map;

import com.bat.system.api.base.SystemException;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 18:55
 */
public class CheckUtils {
    public static void checkSectionParams(Map<String, Object> map) {
        // 发布区域 0 国内 1 国外 2 国内国外
        short aShort = (short)map.get("sectionArea");
        if (aShort == 0) {
            checkDomestic(map);
        } else if (aShort == 1) {
            checkOverseas(map);
        } else if (aShort == 2) {
            checkDomestic(map);
            checkOverseas(map);
        }
    }

    private static boolean checkOverseas(Map<String, Object> map) {
        if (map.get("titleEn") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_TITLE_EN_NULL);
        }
        if (map.get("imageUrlEn") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_IMAGE_URL_EN_NULL);
        }
        if (map.get("extensionUrlEn") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_EXTENSION_URL_EN_NULL);
        }
        if (map.get("styleTypeEn") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_TYPE_EN_NULL);
        }
        for (int i = 2; i < 4; i++) {
            if ((short)map.get("styleTypeEn") == i + 1) {
                String j = i == 0 ? "" : i + "";
                if (map.get("styleUrl" + j) == null) {
                    throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_URL_EN_NULL);
                }
                if (map.get("styleExtensionUrl" + j) == null) {
                    throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_EXTENSION_URL_EN_NULL);
                }
            }
        }
        return true;
    }

    private static boolean checkDomestic(Map<String, Object> map) {
        if (map.get("title") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_TITLE_NULL);
        } else if (map.get("imageUrl") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_IMAGE_URL_NULL);
        } else if (map.get("extensionUrl") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_EXTENSION_URL_NULL);
        } else if (map.get("styleType") == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_TYPE_NULL);
        }
        for (int i = 0; i < 2; i++) {
            if ((short)map.get("styleType") == i + 1) {
                String j = i == 0 ? "" : i + "";
                if (map.get("styleUrl" + j) == null) {
                    throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_URL_NULL);
                }
                if (map.get("styleExtensionUrl" + j) == null) {
                    throw SystemException.buildException(ErrorCode.B_SECTION_STYLE_EXTENSION_URL_NULL);
                }
            }
        }
        return true;
    }

    public static boolean checkMobileParams(Map<String, Object> map) {
        // TODO
        return true;
    }
}
