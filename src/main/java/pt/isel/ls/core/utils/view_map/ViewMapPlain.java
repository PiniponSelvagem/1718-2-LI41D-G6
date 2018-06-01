package pt.isel.ls.core.utils.view_map;

import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.view.plain.*;

public class ViewMapPlain extends ViewMap {

    @SuppressWarnings("Duplicates")
    @Override
    void fillViewMap() {

        /* Commands related to MOVIES */
        viewMap.put(PostMovies.class.getSimpleName(), PostView.class.getName());       //"POST /movies"
        viewMap.put(GetMovies.class.getSimpleName(),  GetMoviesView.class.getName());  //"GET /movies"
        viewMap.put(GetMovieID.class.getSimpleName(), GetMovieIDView.class.getName()); //"GET /movies/{mid}"


        /* Commands related to CINEMAS */
        viewMap.put(PostCinemas.class.getSimpleName(), PostView.class.getName());        //"POST /cinemas"
        viewMap.put(GetCinemas.class.getSimpleName(),  GetCinemasView.class.getName());  //"GET /cinemas"
        viewMap.put(GetCinemaID.class.getSimpleName(), GetCinemaIDView.class.getName()); //"GET /cinemas/{cid}"


        /* Commands related to CINEMAS->THEATERS */
        viewMap.put(PostCinemaIDTheaters.class.getSimpleName(), PostView.class.getName());                 //"POST /cinemas/{cid}/theaters"
        viewMap.put(GetCinemaIDTheaters.class.getSimpleName(),  GetCinemaIDTheatersView.class.getName());  //"GET /cinemas/{cid}/theaters"
        viewMap.put(GetCinemaIDTheaterID.class.getSimpleName(), GetCinemaIDTheaterIDView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}"


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        viewMap.put(PostCinemaIDTheaterIDSessions.class.getSimpleName(),     PostView.class.getName());//"POST /cinemas/{cid}/theaters/{tid}/sessions"
        viewMap.put(GetCinemaIDTheaterIDSessions.class.getSimpleName(),      GetCinemaIDTheaterIDSessionsView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        viewMap.put(GetCinemaIDTheaterIDSessionsToday.class.getSimpleName(), GetCinemaIDTheaterIDSessionsView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"


        /* Commands related to CINEMAS->SESSIONS */
        viewMap.put(GetCinemaIDSessions.class.getSimpleName(),       GetCinemaIDSessionsView.class.getName());       //"GET /cinemas/{cid}/sessions"
        viewMap.put(GetCinemaIDSessionID.class.getSimpleName(),      GetCinemaIDSessionIDView.class.getName());      //"GET /cinemas/{cid}/sessions/{sid}"
        viewMap.put(GetCinemaIDSessionsToday.class.getSimpleName(),  GetCinemaIDSessionsDateIDView.class.getName()); //"GET /cinemas/{cid}/sessions/today"
        viewMap.put(GetCinemaIDSessionsDateID.class.getSimpleName(), GetCinemaIDSessionsDateIDView.class.getName()); //"GET /cinemas/{cid}/sessions/date/{dmy}"
        viewMap.put(GetMovieIDSessionsDateID.class.getSimpleName(),  GetMovieIDSessionsDateIDView.class.getName());  //"GET /movies/{mid}/sessions/date/{dmy}"


        /* Commands related to Tickets */
        viewMap.put(PostCinemaIDTheaterIDSessionIDTickets.class.getSimpleName(),         PostView.class.getName());                                          //"POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        viewMap.put(GetCinemaIDTheaterIDSessionIDTickets.class.getSimpleName(),          GetCinemaIDTheaterIDSessionIDTicketsView.class.getName());          //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        viewMap.put(GetCinemaIDTheaterIDSessionIDTicketID.class.getSimpleName(),         GetCinemaIDTheaterIDSessionIDTicketIDView.class.getName());         //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}"
        viewMap.put(GetCinemaIDTheaterIDSessionIDTicketsAvailable.class.getSimpleName(), GetCinemaIDTheaterIDSessionIDTicketsAvailableView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available"
        viewMap.put(DeleteCinemaIDTheaterIDSessionIDTicket.class.getSimpleName(),        DeleteView.class.getName());                                        //"DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
    }
}
