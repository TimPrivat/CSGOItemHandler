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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
@EnableAsync
@Component
@EnableScheduling
public class SkinHandler {

    private static final Logger logger = LogManager.getLogger("Logger");
    //Stores all Skins
    public static TreeMap<String, Object> AllSkins = new TreeMap<>();

    private String FilePath;
    private String SkinnamePath;
    private final Gson gson = new Gson();

    public SkinHandler(@Value("${file.path}") String FilePath, @Value("${allskinnames.path}") String SkinnamePath) throws IOException {

        this.FilePath = FilePath;
        this.SkinnamePath = SkinnamePath;

        logger.debug("Allskins Path " + FilePath);

        try {

            AllSkins = (TreeMap<String, Object>) parseFile(FilePath);
            logger.debug("AllSkinsContent at Start: "+getAllSkinsAsJSON());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.debug("Syncing Skinnames in File " + SkinnamePath);
        SaveSkinNamesToFile();

    }

    /**
     * Parses the given File as TreeMap<String,Object>
     *
     * @return
     * @throws IOException
     */
    private Map<String, Object> parseFile(String FilePath) {
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

    public String getAllSkinNames() throws IOException {


        return Files.readString(Path.of(SkinnamePath), StandardCharsets.UTF_8);

    }
    @Async
    @Scheduled(fixedDelay = 1800000)
    /**
     * Gets a List of all Skinnames and Saves them commaseperated
     * to the File given in SkinnamePath
     * Executed every 30 Minutes
     */
    protected void SaveSkinNamesToFile() throws IOException {

        logger.debug("Saving Names to File");
        if (!new File(SkinnamePath).exists()) {
            logger.error("File " + SkinnamePath + " was not found... creating file");
            File f = new File(SkinnamePath);
            try {
                f.createNewFile();
                logger.info("SkinName File created");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }


        }

        RestTemplate r = new RestTemplate();
        JSONObject wholeReturn = r.getForObject("http://csgobackpack.net/api/GetItemsList/v2/", JSONObject.class);
        Map items_list = (Map) wholeReturn.get("items_list");
        ArrayList<String> allSkins = new ArrayList<>(items_list.keySet());
        String allSkinString = allSkins.get(0);

        for(int i=1;i<allSkins.size();i++){

            allSkinString = allSkinString+","+normalisieren(allSkins.get(i));

        }
        System.out.println(allSkinString);
        BufferedWriter bufferedwriter = Files.newBufferedWriter(Paths.get(SkinnamePath));
        bufferedwriter.write(allSkinString);
        bufferedwriter.flush();
        bufferedwriter.close();
        logger.debug("Sucessfully wrote to File");
    }

    /**
     * Help Method to normalize the String to be URL readable
     * @param s
     * @return
     */
    public static String normalisieren(String s) {

        s = s.replaceAll(" ", "%20");
        s = s.replaceAll("\\|", "%7C");

        URLEncoder.encode(s, StandardCharsets.UTF_8);

        return s;

    }

}
