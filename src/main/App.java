package main;

public class App {
    public String APP_LOCA;
    public App()
    {
        if(System.getProperty("os.name").toLowerCase().contains("window"))
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("/","\\");
        else
            APP_LOCA=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        System.out.println("[INFO]Working in:"+APP_LOCA);

    }

            }
