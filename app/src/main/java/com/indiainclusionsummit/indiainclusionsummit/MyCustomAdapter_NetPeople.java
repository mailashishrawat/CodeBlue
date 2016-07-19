package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I055845 on 10/14/2015.
 */
public class MyCustomAdapter_NetPeople extends BaseAdapter {
    private static ArrayList<NetworkingUser> peopleList;
    private LayoutInflater mInflater;
    private Context context;
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListener(int position, String valueEmail,String valueMobile , String custName , int actionCode);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListner = listener;
    }

    public MyCustomAdapter_NetPeople(Context context, List<NetworkingUser> listPeople) {
        peopleList = (ArrayList<NetworkingUser>) listPeople;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public Object getItem(int position) {
        return peopleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_netuser, null);
            holder = new ViewHolder();
            holder.txtUserName    = (TextView) convertView.findViewById(R.id.tv_netuser_name);
            //holder.txtUserPhone = (TextView) convertView.findViewById(R.id.tv_netuser_ph_value);
            //holder.txtUserMail  = (TextView) convertView.findViewById(R.id.tv_netuser_mail_value);
            holder.btnCall      = (ImageButton) convertView.findViewById(R.id.btnPhCall);
            holder.btnMail      = (ImageButton) convertView.findViewById(R.id.btnSendMail);
            holder.btnSave      = (ImageButton) convertView.findViewById(R.id.btnSave);

            convertView.setTag(holder);
            // convertView.setTag(R.id.checkbox, holder.chkBox);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NetworkingUser usertObj = (NetworkingUser) getItem(position);

        holder.txtUserName.setText(peopleList.get(position).getUserName());
        //holder.txtUserMail.setText(peopleList.get(position).getUserMail());
        if (usertObj.getUserMail() == " " || usertObj.getUserMail() == null || usertObj.getUserMail() == "" || usertObj.getUserMail().equals("") || usertObj.getUserMail().equals(" ")) {
            holder.btnMail.setVisibility(View.INVISIBLE);
            //    holder.btnMail.setVisibility(View.GONE);
            Log.v("Avis","setting mail invisible for " + peopleList.get(position).getUserName());
        }
        else {
            holder.btnMail.setVisibility(View.VISIBLE);
        }

        //holder.txtUserPhone.setText(peopleList.get(position).getUserMob());

        //holder.chkBox.setChecked(peopleList.get(position).getSelected());
        if (usertObj.getUserMob() == " " || usertObj.getUserMob() == null || usertObj.getUserMob() == "" || usertObj.getUserMob().equals("") || usertObj.getUserMob().equals(" ")) {
            holder.btnCall.setVisibility(View.INVISIBLE);
            //    holder.btnCall.setVisibility(View.GONE);
            Log.v("Avis", "setting phone invisible for " + peopleList.get(position).getUserName());
        }else {
            holder.btnCall.setVisibility(View.VISIBLE);
        }

        // added for checking response on click
        Log.v("Avis","mobile number" +usertObj.getUserMob());

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customListner != null) {
                    Log.v("Avis", "Setting listnere");
                    customListner.onButtonClickListener(position, usertObj.getUserMob(), usertObj.getUserMail(), usertObj.getUserName(), 1);
                } else {
                    Log.v("Avis", "bulb !");
                }
            }
        });

        holder.btnMail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    Log.v("Avis", "Setting listnere");
                    customListner.onButtonClickListener(position, usertObj.getUserMob(), usertObj.getUserMail(), usertObj.getUserName(), 2);
                } else {
                    Log.v("Avis", "bulb !");
                }

            }
        });

        holder.btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    Log.v("Avis","Setting listnere");
                    customListner.onButtonClickListener(position, usertObj.getUserMob() , usertObj.getUserMail() , usertObj.getUserName() , 3);
                }else {
                    Log.v("Avis","bulb !");
                }

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView txtUserName;
        TextView txtUserMail;
        TextView txtUserPhone;
        ImageButton btnCall;
        ImageButton btnMail;
        ImageButton btnSave;

    }

}


