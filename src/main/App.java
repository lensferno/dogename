package main;

import java.io.File;
import java.lang.management.ManagementFactory;

public class App {
    public String APP_LOCA;
    public String PID;
    public String JAR_FILE;
    private boolean debug=true;
    public App()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);

        PID=name.split("@")[0];

	File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

	debug = !file.isFile();

        if(System.getProperty("os.name").toLowerCase().contains("window")) {
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("/","\\");
            
            if(!debug) {
                JAR_FILE=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                String[] temp=APP_LOCA.split("\\\\");
                APP_LOCA=APP_LOCA.replace(temp[temp.length-1], "");
            }
            
        }else {
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            
            if(!debug) {
                JAR_FILE=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                String[] temp=APP_LOCA.split("/");
                APP_LOCA=APP_LOCA.replace(temp[temp.length-1], "");
            }
        }
        
        
        //System.out.println("[INFO]Working in:"+APP_LOCA);
        System.gc();

    }

}
