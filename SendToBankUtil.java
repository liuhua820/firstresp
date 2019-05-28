package com.open.noticeTransAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendToBankUtil {


    public static String sendHttpRequest(String serviceUrl,String parameter,String restMethod,String contentType){
        HttpURLConnection con = null;
        BufferedReader in = null;
        String resultStr = "";

        try {

            URL url= new URL(serviceUrl);
            con = (HttpURLConnection)url.openConnection();
            //if(serviceUrl.startsWith("https")){
            //
            //	  ((HttpsURLConnection) con).setHostnameVerifier(new RongzerHostnameVerifier());
            //}
            if(contentType == null || "".equals(contentType)){
                contentType = "application/json;charset=utf-8";
            }
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            con.setRequestProperty("Content-Type", contentType);//application/x-www-form-urlencoded
            con.setRequestMethod(restMethod);
            //如果请求方法为PUT,POST和DELETE设置DoOutput为真
            if(!"GET".equals(restMethod)){
                con.setDoOutput(true);
                con.setDoInput(true);
                if(!"DELETE".equals(restMethod)){ //请求方法为PUT或POST时执行
                    byte[] data = parameter.getBytes("GBK");
                    con.setRequestProperty("Content-Length",String.valueOf(data.length));
                    OutputStream os = con.getOutputStream();
                    con.setConnectTimeout(5*1000);
                    os.write(data);
                    os.flush();
                    os.close();
                }
            }
            StringBuffer buffer = new StringBuffer();

            in = new BufferedReader(new InputStreamReader(con.getInputStream(),"gb2312")); //响应response 为gb2312
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            in.close();
            resultStr = buffer.toString();


        } catch ( Exception e ) {
            throw new RuntimeException(e);
        } finally{
            if(con != null){
                con.disconnect();
                con = null;
            }
        }
        return resultStr;
    }
}
