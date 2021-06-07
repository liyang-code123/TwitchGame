package com.twitch.twitchgame.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static String TOKEN;
    public static String CLIENT_ID;

    public static String username;
    public static String password;
    public static String instance;
    public static String port_num;
    public static String db_name;


    static {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
            prop.load(inputStream);

            username = prop.getProperty("user");
            password = prop.getProperty("password");
            instance = prop.getProperty("instance");
            port_num = prop.getProperty("port_num");
            db_name = prop.getProperty("db_name");
            TOKEN = prop.getProperty("token");
            CLIENT_ID = prop.getProperty("client_id");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
