
package main.update.pojos;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Resource {

    @Expose
    private List<String> md5;
    @Expose
    private String name;
    @Expose
    private List<String> urls;

    public List<String> getMd5() {
        return md5;
    }

    public void setMd5(List<String> md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}
