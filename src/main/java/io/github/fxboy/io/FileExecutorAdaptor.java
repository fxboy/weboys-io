package io.github.fxboy.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fanxing
 * @time: 2022/3/5 15:29
 * @description: This is a class object !!!
 * At first, only God and I knew what it meant. [2022/3/5 15:29]
 * Now, only God knows what it means. Oh, no, God doesn't know what it means. [Later]
 */
public class FileExecutorAdaptor {
    private volatile static RecordBucket recordBucket = new RecordBucket();
    private FileListenerAdaptor fileListenerAdaptor;
    private File baseFile;
    private FileObFilter filter;

    public FileExecutorAdaptor(FileListenerAdaptor fileListenerAdaptor,String path){
        this.fileListenerAdaptor = fileListenerAdaptor;
        this.baseFile = new File(path);
        initialize();
    }

    public FileExecutorAdaptor(FileListenerAdaptor fileListenerAdaptor,String path,String filter){
        this.fileListenerAdaptor = fileListenerAdaptor;
        this.baseFile = new File(path);
        this.filter = new FileObFilter(filter);
        initialize();
    }

    private void initialize(){
        File[] files = baseFile.listFiles();
        List<File> list = new ArrayList<>();
        for (File file : files) {
            if(file.isDirectory()){ continue;}

            if(filter == null){
                list.add(file);
                continue;
            }
            if(filter.isFilter(file)){
                list.add(file);
            }
        }
        recordBucket.initialize(list);
    }

    public synchronized void run(){
        // 优先监测是否被删除
        List<RecordFile> deletedFiles = recordBucket.delete();
        deletedFiles.forEach(e ->{
            fileListenerAdaptor.onDeleteFile(e.getFile());
        });
        File[] files =  baseFile.listFiles();
        for (File file : files) {
            // 不监控文件夹
            if(file.isDirectory()){ continue;}
            if( filter == null || filter.isFilter(file)){
                Integer y =  recordBucket.verify(file);
                if(y == 1){
                    fileListenerAdaptor.onCreateFile(file);
                }
                if(y == 2){
                    fileListenerAdaptor.onChangeFile(file);
                }
            }
        }
    }

    public void onStart(){
        fileListenerAdaptor.onStart();
    }

    public void onStop(){
        fileListenerAdaptor.onStop();
    }
}
