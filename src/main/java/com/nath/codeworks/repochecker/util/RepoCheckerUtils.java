package com.nath.codeworks.repochecker.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepoCheckerUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(RepoCheckerUtils.class);

    private static final String NEXT_URL_INDICATOR = "rel=\"next\"";
    private static final String PAGINATION_OPTIONS_SEPARATOR = ",";
    private static final String NAVIGATION_KEY_VALUE_SEPARATOR = ";";

    public static String parseNextUrl(String linkHeaderInfo) {
        String nextUrl = null;
        if (StringUtils.isNotBlank(linkHeaderInfo)) {
            String[] strArr1 = linkHeaderInfo.split(PAGINATION_OPTIONS_SEPARATOR);

            for (String str : strArr1) {
                if (str.contains(NEXT_URL_INDICATOR)) {
                    LOGGER.info("Parse next url info from link header - {}", linkHeaderInfo);
                    String s = str.split(NAVIGATION_KEY_VALUE_SEPARATOR)[0].trim();
                    nextUrl = s.substring(1, s.length() - 1);
                    LOGGER.info("Parsed next url - {}", nextUrl);
                    break;
                }
            }
        }
        return nextUrl;
    }

}
