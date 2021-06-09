package com.twitch.twitchgame.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequestBody {
    private final Item favoriteItem;

    // only need to convert Json to Java class.
    // This is only used for deserialization and we don't need to serialization to Json.
    @JsonCreator
    public FavoriteRequestBody(@JsonProperty("favorite") Item favoriteItem) {
        this.favoriteItem = favoriteItem;

    }

    public Item getFavoriteItem() {
        return favoriteItem;
    }
}
