package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.headers.Header;

import java.util.HashMap;

public class DataContainer {

    /**
     * Class used to parse the data requested by the Command to the view
     * while keeping the information organized for better handling.
     */

    private String createdBy;
    private Header header;
    private HashMap<DataEnum, Object> map = new HashMap<>();

    public DataContainer(String createdBy, Header header) {
        this.createdBy = createdBy;
        this.header = header;
    }

    /**
     * @return Returns the simple name of the class that generated this DataContainer
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return Returns the header in this data container
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Add the object to the data container
     * @param key DateEnum
     * @param obj Object to add
     */
    public void add(DataEnum key, Object obj) {
        map.put(key, obj);
    }

    /**
     * @param key DataEnum
     * @return Returns the object requested from the data container
     */
    public Object getData(DataEnum key) {
        return map.get(key);
    }

    /**
     * Update header after creating the view that is ready to be sent:
     * @param header Header
     */
    public void updateHeader(Header header) {
        this.header = header;
    }

    /**
     * Enum to be used to organize the DataContainer
     */
    public enum DataEnum {
        D_CINEMAS,          //Multiple cinemas
        D_CINEMA,           //Just ONE cinema

        D_THEATERS,         //Multiple theaters
        D_THEATER,          //Just ONE theater

        D_SESSIONS,         //Multiple sessions
        D_SESSION,          //Just ONE session

        D_MOVIES,           //Multiple movies
        D_MOVIE,            //Just ONE movie

        D_TICKETS,          //Multiple tickets
        D_TICKET,           //Just ONE ticket

        D_AVAILABLE_SEATS,  //Save integer value of number of seats available,
                            //if you using D_SESSIONS or D_SESSION, use its available seats variable instead

        D_DATE,             //Date value
        D_CID,              //Integer value cinemaID
        D_TID,              //Integer value theaterID
        D_SID,              //Integer value sessionID
        D_TKID,             //Integer value ticketID
        D_MID,              //Integer value movieID

        D_DELETE,           //Save boolean value to tell if could delete the requested info
        D_POST,             //Post information, save PostData in it

        ;
    }
}
