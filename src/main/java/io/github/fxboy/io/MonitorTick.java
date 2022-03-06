package io.github.fxboy.io;

/**
 * @author: Fanxing
 * @time: 2022/3/5 16:08
 * @description: This is a class object !!!
 * At first, only God and I knew what it meant. [2022/3/5 16:08]
 * Now, only God knows what it means. Oh, no, God doesn't know what it means. [Later]
 */
public class MonitorTick implements Runnable{
    private long interval;
    private FileExecutorAdaptor fileExecutorAdaptor;
    private volatile boolean enable;

    public MonitorTick(FileExecutorAdaptor fileExecutorAdaptor){
        this.interval = 1000L;
        this.fileExecutorAdaptor = fileExecutorAdaptor;
    }

    public MonitorTick(FileExecutorAdaptor fileExecutorAdaptor,long interval){
        this.interval = interval;
        this.fileExecutorAdaptor = fileExecutorAdaptor;
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }

    @Override
    public void run() {
        fileExecutorAdaptor.onStart();
        while (enable){
            fileExecutorAdaptor.run();
            // 执行完后间隔
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fileExecutorAdaptor.onStop();
    }
}
