package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I065310 on 10/28/2015.
 */
public class NavigationSettingData {
    private String id;
    private String SettingId;
    private String SettingName;
    private String SettingValue;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    NavigationSettingData(String pid,String pSettingId,String pSettingName,String pSettingValue,String pextra1,String pextra2, String pextra3, String pextra4) {
        id = pid;
        SettingId = pSettingId;
        SettingName = pSettingName;
        SettingValue = pSettingValue;
        extra1 = pextra1;
        extra2 = pextra2;
        extra3 = pextra3;
        extra4 = pextra4;
    }

    public String getid(){        return id;    }
    public String getSettingId(){      return SettingId;  }
    public String getSettingName(){        return SettingName;    }
    public String getSettingValue(){        return SettingValue;    }
    public String getextra1(){        return extra1;    }
    public String getextra2(){        return extra2;    }
    public String getextra3(){        return extra3;    }
    public String getextra4(){        return extra4;    }


}
