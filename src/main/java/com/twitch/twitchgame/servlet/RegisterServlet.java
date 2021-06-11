package com.twitch.twitchgame.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchgame.db.MySQLConnection;
import com.twitch.twitchgame.db.MySQLException;
import com.twitch.twitchgame.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(request.getReader(), User.class);
        if (user == null) {
            System.err.println("User information incorrect.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isUserAdded = false;
        try (MySQLConnection conn = new MySQLConnection()) {
            user.setPassword(
                    ServletUtil.encryptPassword(user.getUserId(), user.getPassword())
            );

            isUserAdded = conn.addUser(user);
        } catch(MySQLException e) {
            throw new ServletException(e);
        }

        if(!isUserAdded) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}
