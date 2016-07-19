package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I065310 on 10/21/2015.
 */
public class BeaconData {
    private String id;
    private String beaconId;
    private String locName;
    private String xcord;
    private String ycord;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;



    BeaconData(String pid,String pbeaconId,String plocName,String pxcord,String pycord,String pextra1,String pextra2, String pextra3, String pextra4) {
        id = pid;
        beaconId = pbeaconId;
        locName = plocName;
        xcord = pxcord;
        ycord = pycord;
        extra1 = pextra1;
        extra2 = pextra2;
        extra3 = pextra3;
        extra4 = pextra4;
    }

    public String getid(){        return id;    }
    public String getbeaconId(){      return beaconId;  }
    public String getlocName(){        return locName;    }
    public String getxcord(){        return xcord;    }
    public String getycord(){        return ycord;    }
    public String getextra1(){        return extra1;    }
    public String getextra2(){        return extra2;    }
    public String getextra3(){        return extra3;    }
    public String getextra4(){
        return extra4;
    }


}
