package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I055845 on 10/28/2015.
 */
public class EventStream {

    private String eventId;
    private String eventTitle;
    private String eventDesc;

    EventStream(String peventId, String peventTitle, String peventDesc) {
        eventId = peventId;
        eventTitle = peventTitle;
        eventDesc = peventDesc;
    }

    public String getEventId() {
        return eventId;
    }
    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDesc() {
        return eventDesc;
    }


}
