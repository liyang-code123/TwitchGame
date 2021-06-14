package com.twitch.twitchgame.servlet;

import com.twitch.twitchgame.entity.Item;
import com.twitch.twitchgame.recommendation.ItemRecommender;
import com.twitch.twitchgame.recommendation.RecommendationException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RecommendationServlet", value = "/recommendation")
public class RecommendationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ItemRecommender itemRecommender = new ItemRecommender();
        Map<String, List<Item>> itemMap;

        // if the user has successfully logged in, recommend by the favorite records, otherwise by the top games.
        try {
            if(session == null) {
                itemMap = itemRecommender.recommendItemsByDefault();
            } else {
                String userId = (String)request.getSession().getAttribute("user_id");
                itemMap = itemRecommender.recommendItemsByUser(userId);
            }
        } catch(RecommendationException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        ServletUtil.writeData(response, itemMap);
    }

}
