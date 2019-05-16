package main;

import main.sourcesData.*;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;

public class releaseData {

    final static private String UI_FILE="D:\\DM_Master_sources-master\\sources\\UI.fxml";
    final static private String IAMGE_FILE="D:\\DM_Master_sources-master\\sources\\img1.png";
    final static private String BACKIAMGE_FILE="D:\\DM_Master_sources-master\\sources\\back.png";
    final static private String MAIN_DIR="D:\\DM_Master_sources-master\\sources\\";

    final static private String[] COLOREGGS={
            "D:\\DM_Master_sources-master\\sources\\0.来这里找什么呢？",
            "D:\\DM_Master_sources-master\\sources\\1.这里的文件动不得~",
            "D:\\DM_Master_sources-master\\sources\\2.不过动了也没什么大问题，DMmaster会校验文件的完整和正确",
            "D:\\DM_Master_sources-master\\sources\\3.所以说来这里想改文件，不如去我的Github主页逛逛(-_-)",
            "D:\\DM_Master_sources-master\\sources\\4.star几个项目给点面子嘛~",
            "D:\\DM_Master_sources-master\\sources\\5.主页在此.txt",
            "D:\\DM_Master_sources-master\\sources\\6.DMmaster的源代码在此.txt"};

    private final File mainDir=new File(MAIN_DIR);
    final static private File imageFile=new File(IAMGE_FILE);
    final static private File backImageFile=new File(BACKIAMGE_FILE);
    final static private File fxmlFile=new File(UI_FILE);

    private File file=null;

    private Base64 base64 =new Base64();

    public int releaseAllFile(){
        try{
            if(!mainDir.exists())
                mainDir.mkdirs();

            FileOutputStream UIfos =new FileOutputStream(fxmlFile);
            FileOutputStream imageFos =new FileOutputStream(imageFile);
            FileOutputStream backFos =new FileOutputStream(backImageFile);

            UIfos.write(base64.decode(UIFileData.data));
            UIfos.close();

            backFos.write(base64.decode(backData.data));
            backFos.close();

            StringBuffer sb =new StringBuffer();
            sb.append(img1Data_1.data);
            sb.append(img1Data_2.data);

            imageFos.write(base64.decode(sb.toString()));
            imageFos.close();

            for(int i=0;i<COLOREGGS.length;i++){
                file=new File(COLOREGGS[i]);
                file.createNewFile();
            }

            FileOutputStream fos =new FileOutputStream(new File(COLOREGGS[5]));
            fos.write("https://github.com/Het7230".getBytes());
            fos.close();

            fos =new FileOutputStream(new File(COLOREGGS[6]));
            fos.write("https://github.com/Het7230/DM_master".getBytes());
            fos.close();

            return 0;

        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

}
