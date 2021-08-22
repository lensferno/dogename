package me.lensferno.dogename.utils.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class OcrTool {

    public static final String APP_ID = "17411446";
    public static final String API_KEY = "R2ggZhk6nB7ORE4Ozy9iAPdc";
    public static final String SECRET_KEY = "9f6ECgrltz9v1rww2hQm3EQOl1FFHLGx";

    AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

    String result;
    int resultNum=0;

    public boolean requestOcrAPI(String imageFileLocation){
        JSONObject respondJSON=client.accurateGeneral(imageFileLocation, new HashMap<>());
        if (respondJSON==null){
            result="错误：返回了空的数据。";
            return false;
        }
        if(!respondJSON.has("words_result")){
            String errorCode=respondJSON.get("error_code").toString();
            result=findErrorMsg(errorCode);
            return false;
        }
        resultNum=respondJSON.getInt("words_result_num");
        System.out.println("total result:"+resultNum);
        JSONArray resultArray=respondJSON.getJSONArray("words_result");

        StringBuffer stringBuffer=new StringBuffer();

        for(int i=0;i<resultArray.length();i++){
            stringBuffer.append(resultArray.getJSONObject(i).getString("words")).append("\n");
        }
        result=stringBuffer.toString();

        return true;

    }

    public String getResult() {
        return result;
    }

    private String findErrorMsg(String errorCode){
        switch (errorCode){
            case "1":
                return "服务器内部错误，请尝试再次请求。";
            case "3":
                return "调用的API不存在。";
            case "4":
                return "集群超限额，请尝试再次请求。";
            case "17":
                return "每天请求量超限额。";
            case "SDK100":
                return "错误：图像文件过大。";
            case "SDK101":
                return "错误：图片边长不符合要求。";
            case "SDK102":
                return "错误：读取图片文件错误。";
            case "SDK106":
                return "错误：UID格式错误。";
            case "SDK107":
                return "错误：UID大小错误。";
            case "SDK108":
                return "错误：连接超时或读取数据超时，请检查网络连接。";
            case "SDK109":
                return "错误：图片格式不支持。";
            case "SDK110":
                return "错误：无效的请求ID。";
            case "SDK111":
                return "错误：等待异步请求结果超时。";
            case "SDK112":
                return "错误：数据下载失败。";
            case "282000":
                return "服务器内部错误，如果您使用的是\n高精度接口，原因可能是您上传的图片\n中文字过多，识别超时导致的\n建议您对图片进行切割后再识别，\n其他情况请再次请求。";
            default:
                return "未知错误。错误码："+errorCode;
        }

    }
}
