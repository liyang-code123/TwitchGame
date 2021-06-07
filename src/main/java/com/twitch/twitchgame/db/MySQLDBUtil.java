package com.twitch.twitchgame.db;

import com.twitch.twitchgame.config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class MySQLDBUtil {
//    private static final String INSTANCE =
//            "twitchgame-instance.cio8xzexnuiu.us-east-2.rds.amazonaws.com";
//    private static final String PORT_NUM = "3306";
//    private static final String DB_NAME = "twitchgame";

    public static String getMySQLAddress() throws IOException {
//        Properties prop = new Properties();
//        String propFileName = "config.properties";
//
//        InputStream inputStream = MySQLDBUtil.class.getClassLoader().getResourceAsStream(propFileName);
//        prop.load(inputStream);

        String username = Config.username;
        String password = Config.password;
        String instance = Config.instance;
        String port_num = Config.port_num;
        String db_name = Config.db_name;

        try {
            // Encode special characters in URL, e.g. Rick Sun -> Rick%20Sun
            password = URLEncoder.encode(password, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&serverTimezone=UTC&createDatabaseIfNotExist=true",
                instance, port_num, db_name, username, password);
    }
}
