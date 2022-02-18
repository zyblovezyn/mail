package com.wupx.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileUtils {
    public static List<String> getListLines(File file) {
        List<String> list = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getCharSet("UTF-8")));
            list = new ArrayList<>();
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                list.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }

    public static Charset getCharSet(String charSetName) {
        return Charset.forName(charSetName);
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            System.out.println("删除单个文件" + fileName + "成功！");
            return true;
        } else {
            System.out.println("删除单个文件" + fileName + "失败！");
            return false;
        }
    }

    public static boolean createDirectory(String firName) {
        boolean hasSucceeded = false;
        try {
            File directory = new File(firName);
            //mkdirs
            hasSucceeded = directory.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasSucceeded;
    }

    public static void moveTotherFolders(String from, String template) {
        String dirPath = System.getProperty("user.dir");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String startPath = from;
        String endPath = dirPath + File.separator + template + File.separator + File.separator + year + File.separator + month + File.separator;
        try {
            File startFile = new File(startPath);
            File tmpFile = new File(endPath);//获取文件夹路径
            if (!tmpFile.exists()) {//判断文件夹是否创建，没有创建则创建新文件夹
                tmpFile.mkdirs();
            }
            System.out.println(endPath + startFile.getName());
            if (startFile.renameTo(new File(endPath + startFile.getName()))) {
                System.out.println("File is moved successful!");
            } else {
                System.out.println("File is failed to move!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
