package com.flop.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WordGenerator {
	private static WordGenerator instance = new WordGenerator();

	private WordGenerator() {
	}

	public static WordGenerator getInstance() {
		return instance;
	}
	
	public void createWord(Map dataMap, String filePath, String fileName) {
		//输出文件
        File outFile = new File(filePath + File.separator + fileName + ".doc");        
        //如果输出目标文件夹不存在，则创建
        if (!outFile.getParentFile().exists()){
            outFile.getParentFile().mkdirs();
        }        
        //如果输出目标文件夹不存在，则创建
        if (outFile.exists()){
            return;
        }
        Writer out = null;
		//创建配置实例 
        Configuration configuration = new Configuration();        
        //设置编码
        configuration.setDefaultEncoding("utf-8");        
        //获取ftl模板路径
        configuration.setClassForTemplateLoading(WordGenerator.class, "/com/flop/utils/");        
        try {
        	//获取模板
			Template template = configuration.getTemplate("model.ftl");
			//将模板和数据模型合并生成文件 
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
			//生成文件
            template.process(dataMap, out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void compressDirectory(String filePath, String zipFilePath, String fileName) {
		FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null; 
		File dir = new File(filePath);
		if (!dir.exists()){  
	        return;    
		}
		
		File zipFile = new File(zipFilePath + File.separator + fileName +".zip");
		if (zipFile.exists()) {
			zipFile.delete();
		}
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));  
			byte[] bufs = new byte[1024*10];  
			File[] files = dir.listFiles();    
			for (int i = 0; i < files.length; i++) {    
				ZipEntry entry = new ZipEntry(files[i].getName());
				zos.putNextEntry(entry);
				fis = new FileInputStream(files[i]);
				bis = new BufferedInputStream(fis, 1024*10);
				int read = 0;
				while ((read = bis.read(bufs, 0 ,1024*10)) != -1) {
					zos.write(bufs, 0, read);					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭流  
            try {  
                if(null != bis) bis.close();  
                if(null != zos) zos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } 
		}
	}
}
