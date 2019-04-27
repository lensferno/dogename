package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import sun.rmi.runtime.Log;


interface DownloadTaskListener
{
    public void downloadCompleted();
}


public class Downloader {

    /**
     * 开始下载
     *
     * @throws Exception
     */

    long contentLength;
    String url;
    boolean acceptRanges;
    int threadCount;
    String localPath;
    List<Thread> threads;
    long receivedCount;
    //DownloaderListener listener;
    public Downloader(String url, String localPath, int threadCount) {
        contentLength = -1;
        this.url = url;
        acceptRanges = false;
        this.threadCount = threadCount;
        this.localPath = localPath;
        threads = new ArrayList<Thread>();
        receivedCount = 0;
    }

    /*
    private void addDownloaderListener(DownloaderListener listener) {
        this.listener = listener;
    }*/

    public void startDown() throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient httpClient = httpClientBuilder.build();
        try {
            getDownloadFileInfo(httpClient);
            startDownloadThread();
        } catch (Exception e) {
            throw e;
        } finally {
            httpClient.close();
        }
    }

    public static boolean getDebug() {
        return false;
    }

    /**
     * @return  the progree between 0 and 100;return -1 if download not started
     *
     */
    public float getDownloadProgress()
    {
        float progress = 0;
        if(contentLength==-1)
        {
            return -1;
        }
        synchronized (this) {
            progress = (float) (Downloader.this.receivedCount *100.0/contentLength);
        }
        return progress;
    }
    public long getContentLength()
    {
        return contentLength;
    }
    public long getDownload()
    {
        long download;
        synchronized (this) {
            download = Downloader.this.receivedCount;
        }
        return download;
    }
    /**
     * 获取下载文件信息
     */
    private void getDownloadFileInfo(HttpClient httpClient) throws IOException,
            ClientProtocolException, Exception {
        HttpHead httpHead = new HttpHead(url);
        System.out.println(url);
        HttpResponse response = httpClient.execute(httpHead);
        // 获取HTTP状态码
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200)
            throw new Exception("资源不存在!");
        if (getDebug()) {
            for (Header header : response.getAllHeaders()) {
                System.out.println(header.getName() + ":" + header.getValue());
            }
            System.out.println("-----------------------------http head----------------");
        }

        // Content-Length
        Header[] headers = response.getHeaders("Content-Length");
        if (headers.length > 0)

        {
            contentLength = Long.valueOf(headers[0].getValue());
            System.out.println("length : "+contentLength);
        }

        if(contentLength<1024*100)
        {
            threadCount =1;
        }
        else if(contentLength<1024*1024)
        {
            threadCount =2;
        }
        else if(contentLength<1024*1024*10)
        {
            threadCount =5;
        }
        else if(contentLength<1024*1024*100)
        {
            threadCount =10;
        }
        else
        {
            threadCount =20;
        }
        System.out.println("thread Count = " + threadCount);
        httpHead.abort();
        httpHead = new HttpHead(url);
        httpHead.addHeader("Range", "bytes=0-" + (contentLength - 1));
        response = httpClient.execute(httpHead);
        if (response.getStatusLine().getStatusCode() == 206) {
            acceptRanges = true;
            System.out.println("support range");
        }
        else {
            acceptRanges = false;
            System.out.println("not support range");
        }
        httpHead.abort();
    }

    /**
     * 启动多个下载线程
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    // DownloadThreadListener listener;
    private void startDownloadThread() throws IOException,
            FileNotFoundException {
        // 创建下载文件
        final File file = new File(localPath);
        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(contentLength);
        raf.close();

        // 定义下载线程事件实现类

        final Calendar time = Calendar.getInstance();
        final long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
        final DownloadThreadListener threadListener = new DownloadThreadListener() {
            public void afterPerDown(DownloadThreadEvent event) {
                // 下载完一个片段后追加已下载字节数
                synchronized (this) {
                    Downloader.this.receivedCount += event.getCount();
                }
            }

            public void downCompleted(DownloadThreadEvent event) {
                // 下载线程执行完毕后从主任务中移除


                threads.remove(event.getTarget());
                if(threads.size()==0)
                {
                    long endMili=System.currentTimeMillis();
                    Calendar time1 = Calendar.getInstance();
                    System.out.println(file + "总耗时为："+(endMili-startMili)+"毫秒");
                    System.out.println("time------------- ："+file+time1.compareTo(time));
                    System.out.println("Complete!");
                    //listener.downloadCompleted();
                }
                if (getDebug()) {
                    System.out.println("剩余线程数：" + threads.size());
                }
            }
        };

        // 不支持多线程下载时
        if (!acceptRanges) {
            if (true) {
                System.out.println("该地址不支持多线程下载");
            }
            // 定义普通下载
            DownloadThread thread = new DownloadThread(url, 0, contentLength,
                    file, false);
            thread.addDownloadListener(threadListener);
            thread.start();
            threads.add(thread);
            return;
        }

        // 每个请求的大小
        long perThreadLength = contentLength / threadCount + 1;
        long startPosition = 0;
        long endPosition = perThreadLength;
        // 循环创建多个下载线程
        do {
            if (endPosition >= contentLength)
                endPosition = contentLength - 1;

            DownloadThread thread = new DownloadThread(url, startPosition,
                    endPosition, file);
            thread.addDownloadListener(threadListener);
            thread.start();
            threads.add(thread);

            startPosition = endPosition + 1;// 此处加 1,从结束位置的下一个地方开始请求
            endPosition += perThreadLength;
        } while (startPosition < contentLength);
    }
}

interface DownloadThreadListener extends EventListener {
    public void afterPerDown(DownloadThreadEvent event);
    public void downCompleted(DownloadThreadEvent event);

}

class DownloadThreadEvent extends EventObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Object sourObject;
    long count;

    // public DownloadThreadEvent()
    // {
    //
    // }
    public DownloadThreadEvent(Object sourceObject, long count) {
        super(sourceObject);
        this.sourObject = sourceObject;
        this.count = count;
    }

    long getCount() {
        return count;
    }
    Object getTarget() {
        return sourObject;
    }
}
class DownloadThread extends Thread {

    String url;
    long startPosition;
    long endPosition;
    boolean isRange;
    File file;
    DownloadThreadListener listener;
    long downloaded;

    void addDownloadListener(DownloadThreadListener listener) {
        this.listener = listener;
    }

    public  long getdownLoaded()
    {
        return this.downloaded;
    }
    DownloadThread(String url, long startPosition, long endPosition, File file) {
        this.url = url;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isRange = true;
        this.file = file;
        this.downloaded=0;
    }

    DownloadThread(String url, long startPosition, long endPosition, File file,
                   boolean isRange) {
        this.url = url;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isRange = isRange;
        this.file = file;
        this.downloaded=0;
    }

    public void run() {

        //System.out.println("Start:" + startPosition + "-" + endPosition);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();
        try {
            HttpGet httpGet = new HttpGet(url);
            if (isRange) {// 多线程下载
                httpGet.addHeader("Range", "bytes=" + startPosition + "-"
                        + endPosition);
            }
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 206 || (statusCode == 200 && !isRange)) {
                java.io.InputStream inputStream = response.getEntity()
                        .getContent();
                RandomAccessFile outputStream = new RandomAccessFile(file, "rw");
                outputStream.seek(startPosition);
                int count = 0;
                byte[] buffer = new byte[10240];
                while ((count = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    outputStream.write(buffer, 0, count);
                    downloaded += count;
                    listener.afterPerDown(new DownloadThreadEvent(this, count));
                }
                outputStream.close();
            }
            httpGet.abort();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.downCompleted(new DownloadThreadEvent(this, endPosition));
            if (Downloader.getDebug()) {
                System.out.println("End:" + startPosition + "-" + endPosition);
            }
            try {
                httpClient.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }
}
