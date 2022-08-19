package io.github.fxboy.io;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Fanxing
 * @time: 2022/3/6 13:40
 * @description: This is a class object !!!
 * At first, only God and I knew what it meant. [2022/3/6 13:40]
 * Now, only God knows what it means. Oh, no, God doesn't know what it means. [Later]
 */
public class FileObFilter {
    private Map<String,Object> type = new HashMap<>();
    public FileObFilter(String type){
        if(type != null){
            this.type = Arrays.stream(type.split(",")).collect(Collectors.toMap(Function.identity(), v->0));
        }
    }

    public Boolean isFilter(File file){
        if(type.size() == 0) return true;
        String s  = file.getPath();
        s = s.substring(s.lastIndexOf(".")+1,s.length());
        return type.containsKey(s);
    }
}
