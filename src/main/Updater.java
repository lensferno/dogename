package main;

public class Updater {
    
  Update updateInfo;
    
    public boolean checkUpdate(int versionNumb){
        try{

            updateInfo=new Gson().fromJson(getHtml("https://raw.githubusercontent.com/Het7230/DM_master/master/update.json",Update.class);

            if(versionNumb<updateInfo.getVersionNumber())
                return true;
            else
                return false;

        }catch(Exception e){
            return false;
            e.printStackTrace();
        }
    }
    
    public Update getUpdateInfo(){}
    
    public int downloadUpdate(String fileLocation){
        int i=downloader(updateInfo.getUpdateSource().get(0).getUpdateURL(),fileLocation);
        
        if(i==0)
            return 0;
        else
            return -1;
    }
    
    private int downloader(String URL,String fileLocation){
        try{
            URL sourcesURL = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
            connection.connect();

            InputStream stream = connection.getInputStream();
            FileOutputStream fileStream = new FileOutputStream(new File(fileLocation));

            for (int i = stream.read(); i != -1; i = stream.read())
               fileStream.write(i);

            fileStream.close();

            System.out.println("[INFO]文件下载成功。保存至："+fileLocation);
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return -1; 
        }
    } 

    private String getHtml(String URL){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bis;
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.connect();
 
            System.out.println("--------------------------------------------------------------------");
            System.out.println("URL："+conn.getURL());
            System.out.println("URL压缩类型："+conn.getContentEncoding());
 
            InputStream is = conn.getInputStream();
            String connEncoding=conn.getContentEncoding();
            if(connEncoding==null)
                connEncoding="none";
            switch (connEncoding) {
                case "deflate":
                    InflaterInputStream deflate = new InflaterInputStream(is, new Inflater(true));
                    bis = new BufferedReader(new InputStreamReader(deflate, "utf-8"));
                    break;
                case "gzip":
                    GZIPInputStream gzip = new GZIPInputStream(is);
                    bis = new BufferedReader(new InputStreamReader(gzip, "utf-8"));
                    break;
                default:
                    bis = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    break;
            }
            String temp;
            while ((temp = bis.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
        }catch(Exception e){e.printStackTrace();}
        System.out.println("得到网页："+sb.toString());
        return sb.toString();
    }

}
