package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by I055845 on 10/28/2015.
 */
public class SharedWrapperStatic {



    public static Patient getUser(Context ctx)
    {
        SharedPreferences settings;
        settings = ctx.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE); //1
        Patient loginObj = new Patient(ctx); //new Patient(textId,textName,textMobile,textMail);
        return loginObj;

    }



}
