package com.twitch.twitchgame.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchgame.entity.Game;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class TwitchClient {

    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;

    // Build the request URL which will be used when calling Twitch APIs,
    // e.g. https://api.twitch.tv/helix/games/top when trying to get top games.

    private String buildGameURL(String url, String gameName, int limit) {
        if (gameName.equals("")) {
            return String.format(url, limit);
        } else {
            try {
                // Encode special characters in URL, e.g. Rick Sun -> Rick%20Sun
                gameName = URLEncoder.encode(gameName, "UTF-8");
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return String.format(url, gameName);
        }
    }

    // Send HTTP request to Twitch Backend based on the given URL,
    // and returns the body of the HTTP response returned from Twitch backend.
    private String searchTwitch(String url) throws TwitchException {
        ResponseHandler<String> responseHandler = response -> {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                System.out.println("Response status: " +
                        response.getStatusLine().getReasonPhrase());
                throw new TwitchException("Failed to get result from Twitch API: ");
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new TwitchException("Failed to get result from Twitch API: failed to get entity");
            }
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            return obj.getJSONArray("data").toString();
        };

        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", Config.TOKEN);
            request.setHeader("Client-Id", Config.CLIENT_ID);
            return httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to get result from Twitch API: failed to get entity.");
        }
    }

    // Convert JSON format data returned from Twitch to an Arraylist of Game objects
    private List<Game> getGameList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(data, Game[].class));
        } catch(JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse game data from Twitch API: " + data);
        }
    }

    // Integrate search() and getGameList() together, returns the top x popular games from Twitch.
    public List<Game> topGames (int limit) throws TwitchException {
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }
        return getGameList(
                searchTwitch(
                        buildGameURL(
                                TOP_GAME_URL, "", limit)));
    }

    // Integrate search() and getGameList() together, returns the dedicated game based on the game name.
    public Game searchGame(String gameName) throws TwitchException {
        List<Game> gameList = getGameList(
                searchTwitch(
                        buildGameURL(
                                GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        if(gameList.size() != 0) {
            return gameList.get(0);
        }
        return null;
    }
}
