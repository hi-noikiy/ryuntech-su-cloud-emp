package com.ryuntech.common.cxt.toos;/*
 * Created on 2005-9-21
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件工具
 * @author liugongliang
 * @version
 */
public class FileTools {

    /**
     * 读取文件内容
     * @param fileName - 文件名
     * @return 文件内容
     * @throws IOException
     */
    public static byte[] loadData(String fileName) throws IOException{
        File file = new File(fileName);
        if(!file.exists()){
            throw new FileNotFoundException("file["+fileName+"] is not exist!");
        }

        byte[] data = new byte[(int) file.length()];

        FileInputStream fin = new FileInputStream(file);

        fin.read(data);

        return data;

    }

    /**
     * 判断文件是否存在
     * @param fileName - 文件名
     * @return 文件是否存在
     */
    public static boolean isExits(String fileName){
        File file = new File(fileName);
        return file.exists();
    }


}
