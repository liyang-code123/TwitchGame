package com.twitch.twitchgame.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchgame.entity.Game;
import com.twitch.twitchgame.external.TwitchException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.TaggedIOException;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import com.twitch.twitchgame.external.TwitchClient;

@WebServlet(name = "GameServlet", value = "/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameName = request.getParameter("game_name");
        TwitchClient client = new TwitchClient();

        response.setContentType("application/json; charset = UTF-8");
        try {
            if(gameName != null) {
                response.getWriter().print(
                        new ObjectMapper().writeValueAsString(client.searchGame(gameName)));
            } else {
                response.getWriter().print(
                        new ObjectMapper().writeValueAsString(client.topGames(0)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }
}


/* Template Method */

//class Base {
//    public final void performOperation(){
//        performPreOperation();
//        doPerformOperation();
//        performPostOperation();
//    }
//
//    protected void doPerformOperation() {
//
//    }
//}
//
//class Child1{
//    @Override
//    protected void doPerformOperation() {
//        performPreOperation();
//        System.out.println("Operating child 1");
//        performPostOperation();
//    }
//
//}
//
//
//class Child2{
//    @Override
//    protected void doPerformOperation() {
//        performPreOperation();
//        System.out.println("Operating child 2");
//        performPostOperation();
//    }
//}
