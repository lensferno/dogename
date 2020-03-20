package me.hety.dogename.main;

import me.hety.dogename.resources.*;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DataReleaser {

    public static InputStream getDogenameStream(){
        return new ByteArrayInputStream(Base64.decodeBase64(dogename.data));
    }

    public static InputStream getMainPicStream(){
        return new ByteArrayInputStream(Base64.decodeBase64(MainInterfaceImage.data));
    }


}
