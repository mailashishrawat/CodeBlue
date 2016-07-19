package com.indiainclusionsummit.indiainclusionsummit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I064893 on 11/5/2015.
 */
public class MyCustomAdapter_TextCommentry extends BaseAdapter {
    private static ArrayList<TextComment> commentsList;
    private LayoutInflater mInflater;
    private Context context;

    public MyCustomAdapter_TextCommentry(Context context, List<TextComment> listComments) {
        commentsList = (ArrayList<TextComment>) listComments;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_textcomment, null);
            holder = new ViewHolder();
            holder.txtComment    = (TextView) convertView.findViewById(R.id.tv_textComment);
            holder.txtDateTime   = (TextView) convertView.findViewById(R.id.tv_textCommentDateTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.v("Avis","Adding position +" + position + " text as " + commentsList.get(position).getTextComment());
        holder.txtComment.setText(commentsList.get(position).getTextComment());
        holder.txtDateTime.setText(commentsList.get(position).getFeedTime());

        return convertView;
    }

    static class ViewHolder {
        TextView txtComment;
        TextView txtDateTime;
    }
}