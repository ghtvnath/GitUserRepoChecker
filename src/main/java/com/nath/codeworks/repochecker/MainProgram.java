package com.nath.codeworks.repochecker;

import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import com.nath.codeworks.repochecker.service.ServiceInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainProgram implements CommandLineRunner {

    @Autowired
    ServiceInvoker serviceInvoker;

    private static Logger LOGGER = LoggerFactory.getLogger(MainProgram.class);

    public static void main(String[] args) {
        LOGGER.info("**************** Starting the GitUserRepoCheckerApplication ****************");
        SpringApplication.run(MainProgram.class, args);
        LOGGER.info("******************  GitUserRepoCheckerApplication Finished *****************");
    }

    @Override
    public void run(String... args)  {
        if (args.length == 0) {
            LOGGER.warn("No arguments provided when running the application.");
            System.err.println("No arguments provided when running the application. " +
                    "Please run the application with Github user ID to retrieve information.");
        } else{
            String username = args[0];
            serviceInvoker.displayUserGitRepoInformation(username);
        }
    }
}
