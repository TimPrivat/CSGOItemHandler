package com.Home.Tim.CSGOItemHandler;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGeneratorFactory;

@RestController
public class Restcontroller {
    private static final Logger logger = LogManager.getLogger("Logger");
    @Value("${file.path}")
    String FilePath;

    @Value("${allskinnames.path}")
    String SkinnamePath;
    @Autowired
    private SkinHandler skinhander;//= new SkinHandler(FilePath);

    //private final SkinHandler skinhandler = new SkinHandler();
    @GetMapping("/")
    public String StartSeite() {

        return "StartSeite";

    }

    /**
     * Debug to get path
     * @return
     */


    /**
     * Simple TestMethod
     *
     * @return Map with different Datatypes
     */
    @GetMapping("/JSON")
    public Map<String, Object> Jsontest() {

        HashMap<String, Object> m = new HashMap<>();
        m.put("String", "String");
        m.put("Int", 10);
        m.put("Boolean", false);
        m.put("Double", 1.234);
        m.put("CurrentTime", new Date().getTime());
        logger.debug("/JSON " + m);

        return m;

    }

    /**
     * Updates a Skins pricevalue
     * If the Skin is not doesn't exist on the Server it is added to the tracked list.
     *
     * @param SkinHash
     * @param Steamprice
     * @return List of passed Values as JSONObject
     */
    @PostMapping("/updateSkin")
    public Map<String, Object> updateSkin(@RequestParam("SkinHash") String SkinHash,
                                          @RequestParam("Steamprice") Double Steamprice) {

        logger.debug("SkinHash: "+SkinHash);
        logger.debug("Steamprice: "+Steamprice);


        SkinHash = skinhander.normalisierenBack(SkinHash);
        HashMap<String, Object> returnmap = new HashMap<>();
        returnmap.put("SkinHash", SkinHash);
        returnmap.put("Steamprice", Steamprice);
        returnmap.put("Success", skinhander.updateSkin(SkinHash, Steamprice));

        logger.debug("/updateSkin" + " " + returnmap);

        return returnmap;

    }

    @PostMapping("/restartHost")
    public boolean restartHost(@RequestParam("DockerID") String dockerID,
                               @RequestParam("offset") String offset,
                               @RequestParam("serverPort") String serverPort) throws IOException, InterruptedException {

        return skinhander.restartHost(dockerID,serverPort,offset);
    }

    /**
     * Returns all Skindata with their according prices that is locally stored as JSON
     *
     * @return
     */
    @GetMapping("/GetAllSkins")
    public String GetAllSkins() {
        String allSkinsString = skinhander.getAllSkinsAsJSON().toString();
        logger.debug("/GetAllSkins ");
        return allSkinsString;
    }


    @GetMapping("/GetAllSkinNames")
    public String GetAllSkinNames() throws IOException {
      //  String allSkinNames = skinhander.getAllSkinNames();
       String retString = skinhander.getAllSkinNames();
        String allSkinNames="yes";
        logger.debug("/GetAllSkinNames " );
        return retString;
    }


}
