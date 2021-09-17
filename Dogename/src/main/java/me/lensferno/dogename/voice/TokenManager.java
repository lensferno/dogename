package me.lensferno.dogename.voice;

import com.google.gson.Gson;
import me.lensferno.dogename.utils.NetworkUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenManager {

    public static final int TOKEN_OK = 0;
    public static final int TOKEN_BAD = 1;
    final String API_KEY = "dIHCtamVdD0ERO1yyFir2iI4";
    final String SEC_KEY = "HmpBQY3gG4PyZ0cmudnCbMeoMcMejuuW";
    final String TOKEN_API_URL = "https://openapi.baidu.com/oauth/2.0/token";
    private final int TOKEN_NULL = -2;
    private final int TOKEN_EXPIRED = -1;
    File tokenFile = new File("API_voice.token");
    private int tokenStatus = TOKEN_OK;
    private Token token = null;

    private void updateTokenStatus(int statusCode) {
        switch (statusCode) {
            case TOKEN_OK:
                tokenStatus = TOKEN_OK;
                break;

            case TOKEN_EXPIRED:

            case TOKEN_NULL:
                if (netAvailable()) {
                    refreshToken();
                }

                if (checkTokenAvailable() != 0) {
                    tokenStatus = TOKEN_BAD;
                }
                break;

            default:
                tokenStatus = TOKEN_BAD;
                break;
        }
    }

    private void refreshToken() {
        fetchToken();
        writeToken();
    }


    public void init() {

        if (tokenFile.exists()) {
            loadToken();
            updateTokenStatus(checkTokenAvailable());

        } else {

            if (netAvailable()) {

                refreshToken();

                updateTokenStatus(checkTokenAvailable());

            } else {
                tokenStatus = TOKEN_BAD;
            }

        }
    }

    public int getTokenStatus() {
        return tokenStatus;
    }

    public Token getToken() {
        return token;
    }

    private int checkTokenAvailable() {

        //token是空的就返回-2
        if (token == null || token.getAccessToken() == null) {
            System.out.println("Token was null");
            return -2;
        }

        //token过期了就返回-1
        if (token.isTokenTimeOut()) {
            System.out.println("Token expired.");
            return -1;
        }

        //正常的话就返回0
        System.out.println("Token OK.");
        return 0;
    }


    void fetchToken() {
        try {
            token = new Gson().fromJson(
                    NetworkUtil.getHtml(
                            TOKEN_API_URL
                                    + "?grant_type=client_credentials&client_id=" + API_KEY
                                    + "&client_secret=" + SEC_KEY,
                            true)
                    , Token.class);

            token.setExpTime();
        } catch (Exception e) {
            System.out.println("Error to get Token:" + e);
            token = null;
        }
    }

    private boolean netAvailable() {
        try {

            URL sourcesURL = new URL("http://www.baidu.com");
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
            connection.connect();

            InputStream stream = connection.getInputStream();
            stream.read();
            stream.close();

            return true;
        } catch (Exception e) {
            System.out.println("Network is not available.");
            return false;
        }
    }

    private void loadToken() {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(tokenFile));
            this.token = (Token) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Error in loading Token:" + e);
            this.token = null;
        }
    }

    private void writeToken() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tokenFile));
            oos.writeObject(token);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error in writing Token:" + e);
        }
    }

}
