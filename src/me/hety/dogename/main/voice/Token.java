package me.hety.dogename.main.voice;

import com.google.gson.annotations.SerializedName;

public class Token {

    /*
返回
需要根据 Content-Type的头部来确定是否服务端合成成功。

如果合成成功，返回的Content-Type以“audio”开头

aue =3 ，返回为二进制mp3文件，具体header信息 Content-Type: audio/mp3；
aue =4 ，返回为二进制pcm文件，具体header信息 Content-Type:audio/basic;codec=pcm;rate=16000;channel=1
aue =5 ，返回为二进制pcm文件，具体header信息 Content-Type:audio/basic;codec=pcm;rate=8000;channel=1
aue =6 ，返回为二进制wav文件，具体header信息 Content-Type: audio/wav；
如果合成出现错误，则会返回json文本，具体header信息为：Content-Type: application/json。其中sn字段主要用于DEBUG追查问题，如果出现问题，可以提供sn帮助确认问题。
 */

    private long expTime;

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private long expiresIn;

    public void setExpTime() {
        this.expTime = System.currentTimeMillis() + (expiresIn - 3600) * 1000;
    }

    public boolean isTokenTimeOut() {
        return System.currentTimeMillis() > expTime;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

}
