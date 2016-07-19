package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by I064893 on 10/21/2015.
 */
public class Patient implements Parcelable {

    private String userId;
    private String userName;


    private String Contact_Number;
    private String sleepingHours;
    private String userEmail;
    private int age;
    private int weight;
    private int height;
    private String Address;
    private String temprature;
    private String bp;

    private String patientUri;
    private String emergencyContact_name_1;
    private String emergyencyContact_number_1;
    private String emergencyContact_uri_1;


    private String emergencyContact_name_2;
    private String emergyencyContact_number_2;

    public String getUserId() {
        return userId;
    }

    public String getUserMob()
    {
        return "";
    }
    public String getUserName() {
        return userName;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public String getSleepingHours() {
        return sleepingHours;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getAddress() {
        return Address;
    }

    public String getTemprature() {
        return temprature;
    }

    public String getBp() {
        return bp;
    }

    public String getPatientUri() {
        return patientUri;
    }

    public String getEmergencyContact_name_1() {
        return emergencyContact_name_1;
    }

    public String getEmergyencyContact_number_1() {
        return emergyencyContact_number_1;
    }

    public String getEmergencyContact_uri_1() {
        return emergencyContact_uri_1;
    }

    public String getEmergencyContact_name_2() {
        return emergencyContact_name_2;
    }

    public String getEmergyencyContact_number_2() {
        return emergyencyContact_number_2;
    }

    public String getEmergencyContact_uri_2() {
        return emergencyContact_uri_2;
    }

    private String emergencyContact_uri_2;
public Patient(Context context)
{
    SharedPreferences editor;

    editor = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE); //1

    userId= editor.getString("Patient_Id","");
    userName=editor.getString("Name","" );
    Contact_Number= editor.getString("Contact_Number","" );
    sleepingHours=editor.getString("SLEEPINGHOURS","" );
    userEmail=editor.getString("emailid", "");
    age= editor.getInt("Age", 0);
    Address=editor.getString("Address", "");
    patientUri=editor.getString("PATIENTURI", "");
    emergyencyContact_number_1= editor.getString("Emergency_Contact_1", "");
    emergencyContact_uri_1= editor.getString("emergency_contact1_uri", "");
    emergencyContact_name_1=editor.getString("emergency_contact1_name", "");
    emergyencyContact_number_2= editor.getString("Emergency_Contact_2", "");
    emergencyContact_uri_2=editor.getString("emergency_contact2_uri", "");
    emergencyContact_name_2=editor.getString("emergency_contact2_name", "");
    weight=editor.getInt("Weight",0);
    height=editor.getInt("Height",0);
}

    public Patient(JSONObject jsonObject, Context context) {




        try {
        userId = jsonObject.getString("Patient_Id");
        userName = jsonObject.getString("Name");
        Contact_Number = jsonObject.getString("Contact_Number");
        sleepingHours = jsonObject.getString("SLEEPINGHOURS");
        userEmail = jsonObject.getString("emailid");
        age = jsonObject.getInt("Age");
        weight =jsonObject.getInt("Weight");

        Address = jsonObject.getString("Address");


        patientUri = jsonObject.getString("PATIENTURI");
        emergyencyContact_number_1 = jsonObject.getString("Emergency_Contact_1");
        emergencyContact_uri_1 = jsonObject.getString("emergency_contact1_uri");
        emergencyContact_name_1 = jsonObject.getString("emergency_contact1_name");
        emergyencyContact_number_2 = jsonObject.getString("Emergency_Contact_2");
        emergencyContact_uri_2 = jsonObject.getString("emergency_contact2_uri");
        emergencyContact_name_2 = jsonObject.getString("emergency_contact2_name");
        height = jsonObject.getInt("Height");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PersistInSharedPreference(context);

       // Log.v("Avis","Storing in shared preference now with " +textEmail + textId + textMob + textName);
       // fireToGCM(textEmail, textMob, textId, textName);)

    }

    protected void PersistInSharedPreference(Context context)
    {    SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit();
        editor.putString("Patient_Id", userId);
        editor.putString("Name", userName);
        editor.putString("Contact_Number", Contact_Number);
        editor.putString("SLEEPINGHOURS", sleepingHours);
        editor.putString("emailid", userEmail);
        editor.putInt("Age", age);
        editor.putString("Address", Address);
        editor.putString("PATIENTURI", patientUri);
        editor.putString("Emergency_Contact_1", emergyencyContact_number_1);
        editor.putString("emergency_contact1_uri", emergencyContact_uri_1);
        editor.putString("emergency_contact1_name", emergencyContact_name_1);
        editor.putString("Emergency_Contact_2", emergyencyContact_number_2);
        editor.putString("emergency_contact2_uri", emergencyContact_uri_2);
        editor.putString("emergency_contact2_name", emergencyContact_name_2);
        editor.putInt("Weight",weight);
        editor.putInt("Height",height);

        editor.commit();


    }

    protected Patient(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        Contact_Number = in.readString();
        sleepingHours = in.readString();
        userEmail = in.readString();
        age = in.readInt();
        weight = in.readInt();
        height = in.readInt();
        Address = in.readString();
        temprature = in.readString();
        bp = in.readString();
        patientUri = in.readString();
        emergyencyContact_number_1 = in.readString();
        emergencyContact_uri_1 = in.readString();
        emergencyContact_name_1 = in.readString();
        emergyencyContact_number_2 = in.readString();
        emergencyContact_uri_2 = in.readString();
        emergencyContact_name_2 = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.userId, this.userName, this.Contact_Number,this.sleepingHours, this.userEmail,String.valueOf(this.age),String.valueOf(this.weight),
                String.valueOf(this.height),
        this.Address,this.temprature,this.bp,this.patientUri,this.emergencyContact_name_1,this.emergencyContact_uri_1,this.emergencyContact_name_1,this.emergyencyContact_number_2,
        this.emergencyContact_uri_2,this.emergencyContact_name_2});
    }
}

