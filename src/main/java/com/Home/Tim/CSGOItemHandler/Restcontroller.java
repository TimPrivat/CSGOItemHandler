package com.Home.Tim.CSGOItemHandler;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Restcontroller {
    private static final SkinHandler skinhander = new SkinHandler();
    private static final Logger logger = LogManager.getLogger("Logger");
    @GetMapping("/")
    public String StartSeite(){

        return "StartSeite";

    }

    /**
     * Simple TestMethod
     * @return Map with different Datatypes
     */
    @GetMapping("/JSON")
    public Map<String,Object> Jsontest(){

        HashMap<String,Object> m = new HashMap<>();
        m.put("String","String");
        m.put("Int", 10);
        m.put("Boolean",false);
        m.put("Double",1.234);
        m.put("CurrentTime",new Date().getTime());
        logger.debug("/JSON "+ m);

       return m;

    }

    /**
     * Updates a Skins pricevalues
     * If the Skin is not doesn't exist on the Server it is added to the tracked list.
     *
     * @param SkinHash
     * @param Steamprice
     * @param SkinBaronPrice
     * @param SkinPortPrice
     * @param BitSkinsPrice
     * @param PictureURL
     * @return List of passed Values as JSONObject
     */
    @PostMapping("/updateSkin")
    public Map<String,Object> updateSkin(@RequestParam("SkinHash") String SkinHash,
                                         @RequestParam("Steamprice") Double Steamprice,
                                         @RequestParam("SkinBaronPrice") Double SkinBaronPrice,
                                         @RequestParam("SkinPortPrice") Double SkinPortPrice,
                                         @RequestParam("BitSkinsPrice") Double BitSkinsPrice,
                                         @RequestParam("PictureURL") @Nullable String PictureURL){


        HashMap<String,Object> returnmap = new HashMap<>();
        returnmap.put("SkinHash",SkinHash);
        returnmap.put("Steamprice",Steamprice);
        returnmap.put("SkinBaronPrice",SkinBaronPrice);
        returnmap.put("SkinPortPrice",SkinPortPrice);
        returnmap.put("BitSkinsPrice",BitSkinsPrice);
        returnmap.put("PictureURL",PictureURL);

        logger.debug("/updateSkin" +" "+returnmap );

        return returnmap;

    }

    /**
     * Returns a JSONArray of all Tracked Skins with their according prices
     * @return
     */
    @GetMapping("/GetAllSkins")
    public Map<String,Object> GetAllSkins(){


        return null;
    }


}
