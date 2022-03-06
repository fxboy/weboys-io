package io.github.fxboy.io;

/**
 * @author: Fanxing
 * @time: 2022/3/5 15:29
 * @description: This is a class object !!!
 * At first, only God and I knew what it meant. [2022/3/5 15:29]
 * Now, only God knows what it means. Oh, no, God doesn't know what it means. [Later]
 */
public class FileMonitorAdaptor{
    private Thread main;
    private volatile MonitorTick monitorTick;
    private Boolean enable = false;
    public FileMonitorAdaptor(FileExecutorAdaptor fileExecutorAdaptor){
        this.monitorTick = new MonitorTick(fileExecutorAdaptor);
    }

    public FileMonitorAdaptor(FileExecutorAdaptor fileExecutorAdaptor,long interval){
        this.monitorTick = new MonitorTick(fileExecutorAdaptor,interval);
    }

    public void start(){
        if(enable){
            throw new RuntimeException("定时器处于启动状态");
        }
        monitorTick.setEnable(true);
        main = new Thread(monitorTick);
        main.start();
        this.enable = true;
    }

    public void stop(){
        if(!enable){
            throw new RuntimeException("定时器已停止");
        }
        monitorTick.setEnable(false);
        enable = false;
    }
}
