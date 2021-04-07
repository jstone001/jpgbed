package com.sw.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/12/15 0015.
 */
public class UrlUtil {
    /**
     * 获取网页源代码
     *
     * @param url
     * @return
     */
    public static String sendGet(String url) throws Exception {
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            // 将string转成url对象
            URL realUrl = new URL(url);

            // 初始化一个链接到那个url的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 开始实际的连接
//            connection.setConnectTimeout(4 * 1000);
            connection.setRequestMethod("GET");

//            connection.setRequestProperty("Accept-Language", "zh-CN");
//            connection.setRequestProperty("Charset", "UTF-8");
//            //设置浏览器类型和版本、操作系统，使用语言等信息
//            connection.setRequestProperty(
//                    "User-Agent",
//                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; " +
//                            ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; " +
//                            ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
//            //设置为长连接
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            //得到要下载文件的大小
//            int fileSize = connection.getContentLength();
//            System.out.println(connection.getContent());
//            System.out.println(fileSize);
//            connection.setDoOutput(false);
            connection.connect();

//            System.out.println(connection.getResponseCode());
//            System.out.println(connection.getResponseCode());
            // 初始化 BufferedReader输入流来读取URL的响应
            if (connection.getResponseCode()==200) {
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                // 用来临时存储抓取到的每一行的数据
                String line;
                while ((line = in.readLine()) != null) {
                    // 遍历抓取到的每一行并将其存储到result里面
                    result += line;
                }
            } else {
                result = "404";
            }
        }
//        catch (exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
//            e.printStackTrace();
//        }
        // 使用finally来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static InputStream getStreamFromUrl(String url) {
        InputStream is = null;

        try {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 开始实际的连接
            connection.connect();
            is = connection.getInputStream();
            return is;
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally来关闭输入流
        finally {
            try {
                if (is != null) {
//                    is.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return is;
    }

    public static String getResourceHtmlFromUrl(String url){
        InputStream is=getStreamFromUrl(url);
        String result="";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(is));
        // 用来临时存储抓取到的每一行的数据

        String line;

        try {
//            System.out.println(in.readLine());
            while ((line = in.readLine()) != null) {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 使用finally来关闭输入流
        finally {
            try {
                if(is!=null){
                    is.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 根据图片的URL下载的图片到本地的filePath
     * @param filePath 文件夹
     * @param imageUrl 图片的网址
     */
    public static String getImageFromUrl(String filePath,String imageUrl){

        // 截取图片的名称
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

        //创建文件的目录结构
        File files = new File(filePath);
        if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
            files.mkdirs();
        }
        //获得流
        InputStream is=getStreamFromUrl(imageUrl);

        File file = new File(filePath+fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            int i = 0;
            while((i = is.read()) != -1){
                out.write(i);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        fileName = fileName.substring(1);
        return fileName;
    }

}