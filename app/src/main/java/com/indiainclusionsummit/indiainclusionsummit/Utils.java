package com.indiainclusionsummit.indiainclusionsummit;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by I064893 on 11/1/2015.
 */
public class Utils {
    public static boolean checkForInternetConnection(Context ctx) {

        ConnectivityManager check = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check != null)
        {
            NetworkInfo[] info = check.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public static boolean checkForBlueToothSupport() {
        boolean result = false;
        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if adatpter is available, please note if you running
        //this application in emulator currently there is no support for bluetooth
        if(mBluetoothAdapter == null){
            return false;
        }
        return true;
    }

    public static boolean checkCurrentBlueToothStatus() {
        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled())
            return true;
        else
            return false;
    }

    public static boolean turnOnBlueTooth() {
        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean result = mBluetoothAdapter.enable();
        return result;
    }
}
