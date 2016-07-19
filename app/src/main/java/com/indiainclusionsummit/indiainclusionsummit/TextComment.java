package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I064893 on 11/4/2015.
 */
public class TextComment {

    TextComment( String pId, String pComment, String pDate, String pTime) {
        textId = pId;
        textComment = pComment;
        feedDate = pDate;
        feedTime = pTime;
    }
    private String textId;
    private String textComment;
    private String feedDate;
    private String feedTime;

    public String getTextId() {
        return textId;
    }

    public String getTextComment() {
        return textComment;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public String getFeedTime() {
        return feedTime;
    }
}
