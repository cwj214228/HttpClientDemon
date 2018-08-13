package demon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;



public class MyDemon {
	private HttpClient httpclient=HttpClients.createDefault();
	private HttpResponse response;
	private HttpPost post;
	private HttpGet get;
	private HttpEntity entity;
	private String login_success;
	static String lt;
	static String execution;
	
	public void login(String name,String password,String lt,String execution) throws ClientProtocolException, IOException{
		post=new HttpPost("http://auth.nfu.edu.cn/login?service=http%3A%2F%2Fmy.nfu.edu.cn%2Findex%2Flogin");
		//设置请求头信息
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		post.addHeader("Content-Type","application/x-www-form-urlencoded");
		post.addHeader("Host","auth.nfu.edu.cn");
		post.addHeader("Origin","http://auth.nfu.edu.cn");
		post.addHeader("Referer","http://auth.nfu.edu.cn/login?service=http%3A%2F%2Fmy.nfu.edu.cn%2Findex%2Flogin");
		post.addHeader("Upgrade-Insecure-Requests","1");
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
		
		//设置请求表单
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("service", "http://my.nfu.edu.cn/index/login"));
		params.add(new BasicNameValuePair("username", name));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("lt", lt));
		params.add(new BasicNameValuePair("execution", execution));
		params.add(new BasicNameValuePair("_eventId", "submit"));
		params.add(new BasicNameValuePair("submit", "登陆"));
		post.setEntity(new UrlEncodedFormEntity(params));
		
		//提交表单并获得返回的内容
		response=httpclient.execute(post);
		
		//获得跳转的网址
		Header locationHeader=response.getFirstHeader("Location");
		entity=response.getEntity();
		String text=EntityUtils.toString(entity,"utf-8");
		System.out.println(text);
		
		//登陆不成功
		if(locationHeader==null){
			System.out.println("登陆不成功");
			return;
		}
		else{
			login_success=locationHeader.getValue();
			System.out.println("登陆成功后的网址是:"+login_success);
			post.abort();
		}
		
	}
	public void getMessage() throws ClientProtocolException, IOException{
		//通过这个方法，获得登陆需要的lt和execution两个参数
		get=new HttpGet("http://auth.nfu.edu.cn/login?service=http%3A%2F%2Fmy.nfu.edu.cn%2Findex%2Flogin");
		response=httpclient.execute(get);
		entity=response.getEntity();
		String text=EntityUtils.toString(entity);
		//System.out.println(text);
		org.jsoup.nodes.Document document = Jsoup.parse(text);
		lt = document.select("input[name=lt]").attr("value");
		execution=document.select("input[name=execution]").attr("value");
		get.abort();
	}
	
	public void doGet() throws ClientProtocolException, IOException{
		//访问登陆后的网址，把网页内容打印出来
		get=new HttpGet("http://auth.nfu.edu.cn/login?service=http%3A%2F%2Fmy.nfu.edu.cn%2Findex%2Flogin");
		response=httpclient.execute(get);
		entity=response.getEntity();
		String text=EntityUtils.toString(entity,"utf-8");
		System.out.println(text);
		get.abort();
	}
	public static void main(String[] args) {
		MyDemon demon=new MyDemon();
		try {
			demon.getMessage();
			demon.login("162011050", "214228",lt,execution);
			demon.doGet();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
