package com.message.websocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonRead {
	public static void main(String args[]){
		//getData();
		String jsName ="J:/work/pythonWorkSpace/nettyWeb/web/js/echarts.min.js";
		BufferedReader br = null ;
		StringBuilder sb = new StringBuilder();
		try {
			 br = new BufferedReader(new FileReader(new File(jsName)));
			String msg  ;
			while((msg = br.readLine()) != null){
				sb.append(msg).append("\n");
			}
			System.out.println(sb.toString());
			
			System.out.println("长度》》"+sb.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static String getData(){

		ObjectMapper mapper = new ObjectMapper();
		File file = new File("J:/work/data/600489-1.json");

		StringBuilder sb = new StringBuilder();

		BufferedReader br =null;
		try {
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			br= new BufferedReader(new FileReader(file));

			char[] st = new char[100];
			while(br.read(st)>-1){
				sb.append(st);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Map param = new HashMap();
		JsonNode rootNode  =null;
		String result ="";
		try {
			rootNode= mapper.readTree(file);
			JsonNode open = rootNode.get("open");
			JsonNode close = rootNode.get("close");
			JsonNode high = rootNode.get("high");
			JsonNode low = rootNode.get("low");
			Iterator<String> nameN =  open.getFieldNames();

			List<String> listDate = new ArrayList<String>();
			List<List> listData = new ArrayList<List>();
			
			while(nameN.hasNext()){
				listDate.add(nameN.next());
			}
			for(int i=0;i<listDate.size();i++ ){
				String date = listDate.get(i);
				List<Double> temList = new ArrayList<Double>();
				temList.add(open.get(date).asDouble());
				temList.add(close.get(date).asDouble());
				temList.add(low.get(date).asDouble());
				temList.add(high.get(date).asDouble() );
				listData.add(temList);
				listDate.set(i, date.replaceAll("-", "/"));
			}
			param.put("date", listDate);
			param.put("data", listData);
			result = mapper.writeValueAsString(param);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
		
	}
}
