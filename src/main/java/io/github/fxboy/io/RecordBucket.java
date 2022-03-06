package io.github.fxboy.io;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Fanxing
 * @time: 2022/3/5 14:28
 * @description: This is a class object !!!
 * At first, only God and I knew what it meant. [2022/3/5 14:28]
 * Now, only God knows what it means. Oh, no, God doesn't know what it means. [Later]
 *
 * 记录监听文件夹内容
 *
 */
public class RecordBucket {
   //private volatile Map<String,RecordFolder> recordFolderObjectMap     = new ConcurrentHashMap<>();
   private volatile Map<String,RecordFile>   recordFileObjectMap       = new ConcurrentHashMap<>();

   public void initialize(List<File> files){
       for (File file : files) {
           String md5 = md5Hash(file);
           String pth = file.getPath();
           RecordFile recordFile= new RecordFile(file.length(),file.getName(), pth.substring(pth.lastIndexOf("."),pth.length()),0L, file.lastModified(), pth,md5,0,file);
           recordFileObjectMap.put(file.getName(),recordFile);
       }
   }

   public Integer verify(File file){
        String md5 = md5Hash(file);
        if(!recordFileObjectMap.containsKey(file.getName())){
            String pth = file.getPath();
            RecordFile recordFile= new RecordFile(file.length(),file.getName(), pth.substring(pth.lastIndexOf("."),pth.length()),0L, file.lastModified(), pth,md5,1,file);
            recordFileObjectMap.put(file.getName(),recordFile);
            return 1;
        }
        RecordFile r = recordFileObjectMap.get(file.getName());
        if(r.getMd5().equals(md5)){
            r.setFlag(0);
        }else {
            r.setFlag(2);
            r.setMd5(md5);
        }
        return r.getFlag();
    }

    public List<RecordFile> delete(){
       List<RecordFile> dels = new ArrayList<>();
        for (RecordFile value : recordFileObjectMap.values()) {
            if(!value.isExists()){
                dels.add(value);
                // 清除删除记录
                recordFileObjectMap.remove(value.getFile().getName());
            }
        }
        return dels;
    }

    public String md5Hash(File file){
        FileInputStream fileInputStream = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(md5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class RecordFile{
    private Long size;
    private String name;
    private String type;
    private Long creationTime;
    private Long modificationTime;
    private String path;
    private String md5;
    // 0.未动 1.新增 2.修改 3.删除
    private Integer flag;
    private File file;

    public RecordFile(Long size, String name, String type, Long creationTime, Long modificationTime, String path, String md5, Integer flag,File file) {
        this.size = size;
        this.name = name;
        this.type = type;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.path = path;
        this.md5 = md5;
        this.flag = flag;
        this.file = file;
    }

    public Long getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public Long getModificationTime() {
        return modificationTime;
    }

    public String getPath() {
        return path;
    }

    public String getMd5() {
        return md5;
    }

    public Integer getFlag() {
        return flag;
    }

    public File getFile() {
        return file;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public void setModificationTime(Long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public Boolean isExists(){
        return this.file.exists();
    }
}

class RecordFolder{

}