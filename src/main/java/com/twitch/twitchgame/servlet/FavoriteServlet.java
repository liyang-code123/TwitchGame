package com.twitch.twitchgame.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchgame.db.MySQLConnection;
import com.twitch.twitchgame.db.MySQLException;
import com.twitch.twitchgame.entity.FavoriteRequestBody;
import com.twitch.twitchgame.entity.Item;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", value = "/favorite")
public class FavoriteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request URL,
        // this is a temporary solution since we don’t support session now
        HttpSession session = request.getSession(false);
        if(session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);

        if (body == null) {
            System.err.println("Convert JSON to FavoriteRequestBody failed.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (MySQLConnection conn = new MySQLConnection()) {
            conn.setFavoriteItem(userId, body.getFavoriteItem());
        } catch(MySQLException e) {
            throw new ServletException();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);

        if(body == null) {
            System.err.println("Convert JSON to FavoriteRequestBody failed.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (MySQLConnection conn = new MySQLConnection()) {
            conn.unsetFavoriteItem(userId, body.getFavoriteItem().getId());
        } catch (MySQLException e) {
            throw new ServletException();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        try (MySQLConnection conn = new MySQLConnection()) {
            Map<String, List<Item>> itemMap = conn.getFavoriteItems(userId);
            ServletUtil.writeData(response, itemMap);
        } catch (MySQLException e) {
            throw new ServletException(e);
        }
    }
}
