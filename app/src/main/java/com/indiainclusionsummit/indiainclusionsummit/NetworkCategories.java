package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I055845 on 10/14/2015.
 */
public class NetworkCategories {
    private String categoryId;
    private String categoryDesc;

    NetworkCategories(String pcategoryId,String pcategoryDesc) {
        categoryId = pcategoryId;
        categoryDesc = pcategoryDesc;
    }

    public String getCategoryId(){
        return categoryId;
    }

    public String getCategoryDesc(){
        return categoryDesc;
    }
}

