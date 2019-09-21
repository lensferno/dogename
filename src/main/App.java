package main;

import java.io.File;

public class App {
    public String APP_LOCA;
    private boolean debug=true;
    public App()
    {
	
	File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
	debug = file.isFile();

        if(System.getProperty("os.name").toLowerCase().contains("window")) {
            
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("/","\\");
            
            if(!debug) {
                String[] temp=APP_LOCA.split("\\");
                APP_LOCA=APP_LOCA.replace(temp[temp.length-1], "");
            }
            
        }else {
            
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            
            if(!debug) {
                String[] temp=APP_LOCA.split("/");
                APP_LOCA=APP_LOCA.replace(temp[temp.length-1], "");
            }
        }
        
        
        System.out.println("[INFO]Working in:"+APP_LOCA);
        System.gc();

    }

}
