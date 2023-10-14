package com.Home.Tim.CSGOItemHandler;


import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

public class SkinHandler {

    private static final Logger logger = LogManager.getLogger("Logger");

    //Stores all Skins
    public final static TreeMap<String,Object> AllSkins  = new TreeMap<>();
    private final String FilePath = getClass().getResource("/AllSkins.json").getPath();

    //Wenn live
    //private final String FilePath = "/opt/csgoskins/Allskins.json";
    // /var/log/myprograms/CSGOItemHandler/CSGOItemHandler.log
    public SkinHandler(){
       System.out.println("Allskins Path " + FilePath );


    }

    private JSONArray parseFile() throws IOException {


            JSONParser parser = new JSONParser();

            Object obj = null;

            try {
                FileInputStream fis = new FileInputStream("");
                String data = IOUtils.toString(fis, "UTF-8");
                obj = parser.parse(data);
            } catch (FileNotFoundException e){

                File f = new File(FilePath);
                f.createNewFile();
                parseFile();

            } catch (Exception e) {
               e.printStackTrace();
            }

        JSONArray wert = (JSONArray) obj;

            return wert;



    }


}
