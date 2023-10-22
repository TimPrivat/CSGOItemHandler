package com.Home.Tim.CSGOItemHandler;


import com.google.common.base.Splitter;
import com.google.gson.Gson;
import jakarta.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
@Component
public class SkinHandler {

    private static final Logger logger = LogManager.getLogger("Logger");
    //Stores all Skins
    public static TreeMap<String, Object> AllSkins = new TreeMap<>();

    private String FilePath;
    private final Gson gson = new Gson();

    public SkinHandler(@Value("${file.path}") String FilePath ) {

        this.FilePath = FilePath;

        logger.debug("Allskins Path " + FilePath);

        try {

            AllSkins = (TreeMap<String, Object>) parseFile();
            logger.debug("AllSkinsContent at Start: "+getAllSkinsAsJSON());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }


    }

    /**
     * Parses the given File as TreeMap<String,Object>
     *
     * @return
     * @throws IOException
     */
    private Map<String, Object> parseFile() {
        logger.info("Parsing file");

        if (!new File(FilePath).exists()) {
            logger.error("File " + FilePath + " was not found... creating file");
            File f = new File(FilePath);
            try {
                f.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            logger.info("File created");
            return new TreeMap<String, Object>();


        } else if (new File(FilePath).length() == 0) {
            logger.info("File was empty returning new Map");
            return new TreeMap<String, Object>();

        }

        // HIer zu treemap MACHEN
        JSONObject object = null;
        JSONParser parser = new JSONParser();
        TreeMap<String,Object> TreeMapRet = new TreeMap<>();
        try {
            object = (JSONObject) parser.parse(Files.readString(Paths.get(FilePath)));
            TreeMapRet = new TreeMap<>(object);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return TreeMapRet;
    }

    /**
     * Returns AllSkins as JSONObject
     * @return
     */
    public org.json.JSONObject getAllSkinsAsJSON(){

        return new org.json.JSONObject(AllSkins);

    }

    public boolean updateSkin(String SkinHash, Double SteamPrice) {


        AllSkins.put(SkinHash, SteamPrice);
        try {

            BufferedWriter bufferedwriter = Files.newBufferedWriter(Paths.get(FilePath));
            logger.debug("Writing Whole Map to file");
            String AllSkinsString = gson.toJson(AllSkins);
            //Müllt Logfile nur voll
            // logger.debug("AllSkinsstring: " + AllSkinsString);
            bufferedwriter.write(AllSkinsString);
            bufferedwriter.flush();
            bufferedwriter.close();
        } catch (Exception e) {

            logger.error(e.getMessage());
            return false;
        }


        return true;

    }

}
