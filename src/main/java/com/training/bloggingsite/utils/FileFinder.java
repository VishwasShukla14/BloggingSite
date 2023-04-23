package com.training.bloggingsite.utils;

import com.training.bloggingsite.contolleres.AdminController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFinder {

    @Value("${uploadDir}")
    private static String UPLOAD_DIR;

    static Logger logger = LoggerFactory.getLogger(AdminController.class);


    public static boolean checkProfileInImg(String name){
        File files = new File("/home/vishwas/Downloads/BloggingSite/src/main/resources/static/img");
        String[] listOfFilesInImg = files.list();
        Pattern pattern = Pattern.compile(name);

        for (String file : listOfFilesInImg){
            Matcher matcher = pattern.matcher(file);

            if(matcher.find() && matcher.group().equals(name)){
                logger.info("Profile Pic with name "+name+"found.");
                return true;
            }

        }

        logger.info("Profile Pic with name "+name+" not found.");
        return false;

    }

}
