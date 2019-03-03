package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InformationOutputServiceImplTest {

    private InformationOutputService informationOutputService;
    private ByteArrayOutputStream baos;
    private PrintStream ps;

    @Before
    public void setUp() {
        informationOutputService = new InformationOutputServiceImpl();
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
    }

    @Test
    public void testDisplayUserInformation() throws RepoCheckerAppException{
        User user = getUser();
        informationOutputService.displayUserInformation(ps, user);
        Assert.assertEquals("Github username : ghtvnath\n" +
                "Github user : Tharindu Vishwanath\n\n", baos.toString());
    }




    private User getUser () {
        User user = new User();
        user.setLogin("ghtvnath");
        user.setName("Tharindu Vishwanath");
        return user;
    }


}
