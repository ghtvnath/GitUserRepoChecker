package com.nath.codeworks.repochecker.util;

import org.junit.Assert;
import org.junit.Test;

public class RepoCheckerUtilsTest {

    private static final String VALID_LINK_HEADER_VALUE = "<https://api.github.com/user/2946769/repos?page=1>; rel=\"prev\"," +
            " <https://api.github.com/user/2946769/repos?page=3>; rel=\"next\"," +
            " <https://api.github.com/user/2946769/repos?page=6>; rel=\"last\"," +
            " <https://api.github.com/user/2946769/repos?page=1>; rel=\"first\"";

    private static final String LINK_HEADER_VALUE_WITHOUT_NEXT_OPTION = "<https://api.github.com/user/2946769/repos?page=5>; rel=\"prev\"," +
            " <https://api.github.com/user/2946769/repos?page=6>; rel=\"last\"," +
            " <https://api.github.com/user/2946769/repos?page=1>; rel=\"first\"";

    private static final String BLANK_STR = " ";


    @Test
    public void testParseNextUrl() {
        String nextUrl = RepoCheckerUtils.parseNextUrl(VALID_LINK_HEADER_VALUE);
        Assert.assertEquals("https://api.github.com/user/2946769/repos?page=3", nextUrl);
    }

    @Test
    public void testParseNextUrlNoNextOption() {
        String nextUrl = RepoCheckerUtils.parseNextUrl(LINK_HEADER_VALUE_WITHOUT_NEXT_OPTION);
        Assert.assertNull(nextUrl);
    }

    @Test
    public void testParseNextUrlBlankString() {
        String nextUrl = RepoCheckerUtils.parseNextUrl(BLANK_STR);
        Assert.assertNull(nextUrl);
    }

    @Test
    public void testParseNextUrlNullString() {
        String nextUrl = RepoCheckerUtils.parseNextUrl(null);
        Assert.assertNull(nextUrl);
    }

}
