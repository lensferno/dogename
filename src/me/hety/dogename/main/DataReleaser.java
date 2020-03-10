package me.hety.dogename.main;

import me.hety.dogename.main.sourcesData.*;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DataReleaser {


    public static InputStream getUIStream(){
        return new ByteArrayInputStream(Base64.decodeBase64(UIFileData.data));
    }

    public static InputStream getMainImageStream(){
        StringBuffer sb =new StringBuffer();
        sb.append(img1Data_1.data);
        sb.append(img1Data_2.data);

        return new ByteArrayInputStream(Base64.decodeBase64(sb.toString()));
    }
    
    public static InputStream getBackBtnStream(){
        return new ByteArrayInputStream(Base64.decodeBase64(backData.data));
    }
	
    public static InputStream getDogenameStream(){
        return new ByteArrayInputStream(Base64.decodeBase64(dogename.data));
    }
    

}
