package com.twitch.twitchgame.servlet;

import com.twitch.twitchgame.external.TwitchClient;
import com.twitch.twitchgame.external.TwitchException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameId = request.getParameter("game_id"); // use snakecase like JSON for request.
        if (gameId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TwitchClient client = new TwitchClient();
        try {
           ServletUtil.writeData(response, client.searchItems(gameId));
        } catch(TwitchException e) {
            throw new ServletException(e);
        }
    }
}
