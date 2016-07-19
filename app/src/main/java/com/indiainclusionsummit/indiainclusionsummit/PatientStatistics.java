package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by i038849 on 14/07/16.
 */
public class PatientStatistics extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.patientstatistics, container, false);
     //Patient card
        CardView cardView=(CardView) view.findViewById(R.id.card_view_patient);
        cardView.setElevation(5);
       Patient patient= SharedWrapperStatic.getUser(getContext());

       TextView txtAddr=(TextView) cardView.findViewById(R.id.Postaladress);
        txtAddr.setText("Address: "+patient.getAddress());

        ImageView imgPatient=(ImageView)cardView.findViewById(R.id.Image_Patient);
        imgPatient.setImageDrawable(Drawable.createFromPath(patient.getPatientUri()));

        TextView Weight=(TextView) cardView.findViewById(R.id.Weight);
        Weight.setText("Weight : "+ patient.getWeight());

        TextView Height=(TextView) cardView.findViewById(R.id.Height);
        Height.setText("Height : "+ patient.getHeight());

        TextView phone=(TextView) cardView.findViewById(R.id.PhoneNo);
        phone.setText(patient.getContact_Number());

        TextView dob=(TextView) cardView.findViewById(R.id.dob);
        dob.setText("Dateofbirth : "+patient.getAge());

        //NOTe importatnt
       // TextView textView=(TextView) cardView.findViewById(R.id.Patientdetails);

        //textView.setText(patient.getUserEmail()+"hi Patient details contact");

        //Emergency Contact card 1
        final CardView eccard1=(CardView)view.findViewById(R.id.card_view_emergencycontact_1);
        ImageView imgViewContact=(ImageView)eccard1.findViewById(R.id.Image_EmergencyContact);
        //imgViewContact.setImageDrawable(Drawable.createFromPath(patient.getEmergencyContact_uri_1()));
String image="http://hemacord.info/pub/img/pages/patient-overview.jpg";
        imgViewContact.setImageDrawable(Drawable.createFromPath(image));

        TextView textViewec=(TextView) eccard1.findViewById(R.id.emergencycontactName);
        textViewec.setText("Emergency contact: "+patient.getEmergencyContact_name_1());

        final TextView ec1phone=(TextView)eccard1.findViewById(R.id.ecPhone);
        ec1phone.setText(patient.getEmergyencyContact_number_1());
        ImageButton btnEmergencyContact= (ImageButton)eccard1.findViewById(R.id.emergencyContactPhone);
        btnEmergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                String strPhone= "tel:"+(String)(ec1phone.getText());
                phoneIntent.setData(Uri.parse(strPhone));
                startActivity(phoneIntent);
            }
        });

        //Emergency Contact card =2
        final CardView eccard2=(CardView)view.findViewById(R.id.card_view_emergencycontact_2);
        ImageView imgViewContact2=(ImageView)eccard2.findViewById(R.id.Image_EmergencyContact);
        imgViewContact2.setImageDrawable(Drawable.createFromPath(patient.getEmergencyContact_uri_2()));

        TextView textViewec2=(TextView) eccard2.findViewById(R.id.emergencycontactName);
        textViewec2.setText("Emergency contact: "+patient.getEmergencyContact_name_2());

        final TextView ec2phone=(TextView)eccard2.findViewById(R.id.ecPhone);
        ec1phone.setText(patient.getEmergyencyContact_number_2());
        ImageButton btnEmergencyContact2= (ImageButton)eccard2.findViewById(R.id.emergencyContactPhone);
        btnEmergencyContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                String strPhone= "tel:"+(String)(ec2phone.getText());
                phoneIntent.setData(Uri.parse(strPhone));
                startActivity(phoneIntent);
            }
        });




        return view;
    }
}
