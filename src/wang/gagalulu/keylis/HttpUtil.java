package wang.gagalulu.keylis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
	public static String getContentByUrl(String urlstr){
		BufferedReader in;
		String result = "";
		try {
			URL url = new URL(urlstr);
			URLConnection conn = url.openConnection();
			StringBuffer sb = new StringBuffer("");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8") );   
		    String str = null;    
		    while((str = in.readLine()) != null) {    
		    	sb.append( str );     
		    }     
		    result = sb.toString();     
	        //System.out.println(result);   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
