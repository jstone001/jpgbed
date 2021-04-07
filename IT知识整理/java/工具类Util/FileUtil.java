package com.wondersgroup.smpa.upload.util;

import java.io.File;
import java.text.DecimalFormat;

public class FileUtil {
	
	
	//获得文件的大小
	   public static String getFileSize(File file){
		    String size = ""; 
		    if(file.exists() && file.isFile()){
		    long fileS = file.length();
		      DecimalFormat df = new DecimalFormat("#.00"); 
		           if (fileS < 1024) {
		               size = df.format((double) fileS) + "B";
		           } else if (fileS < 1048576) {
		               size = df.format((double) fileS / 1024) + "KB";
		           } else if (fileS < 1073741824) {
		               size = df.format((double) fileS / 1048576) + "MB";
		           } else {
		               size = df.format((double) fileS / 1073741824) +"GB";
		           }
		    }else if(file.exists() && file.isDirectory()){
		    size = "";
		    }else{
		    size = "0B";
		    }
		    return size;
		   }
	   
	   //获得文件扩展名
	   public static String getExtName(String fileName) {
	        int index = fileName.lastIndexOf(".");
	 
	        if (index == -1) {
	            return "";
	        }
	        String result = fileName.substring(index + 1);
	        return result;
	    }
	   
	   //获得文件名
	   public static String getFileName(String fileName){
		    return fileName.substring(0,fileName.lastIndexOf("."));
	   }


}