package demon;

import java.io.IOException;  
import java.util.ArrayList;  
import java.util.List;  
import org.apache.http.Header;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;  
  
public class HttpClientLoginDemo {  
    private CloseableHttpClient httpclient;  
    private HttpPost httppost;// 用于提交登陆数据  
    private HttpGet httpget;// 用于获得登录后的页面  
    private String login_success;// 用于构造上面的HttpGet  
  
    public HttpClientLoginDemo() {  
        httpclient = HttpClients.createDefault();  
        // 人人的登陆界面网址  
        httppost = new HttpPost("http://www.renren.com/PLogin.do");  
    }  
  
    public void logIn(String name, String password) throws Exception {  
        // 打包将要传入的参数  
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("email", name));  
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("domain", "renren.com"));  
        params.add(new BasicNameValuePair("captcha_type", "web_login"));
        params.add(new BasicNameValuePair("origURL", "http://www.renren.com/home"));
        httppost.setEntity(new UrlEncodedFormEntity(params)); 
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.addHeader("Host","www.renren.com");
        httppost.addHeader("Referer","http://www.renren.com/SysHome.do");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
        httppost.addHeader("X-Requested-With","X-Requested-With");
       
        try {  
            // 提交登录数据  
            HttpResponse re = httpclient.execute(httppost);  
            // 获得跳转的网址  
            Header locationHeader = re.getFirstHeader("Location");
            System.out.println(re.getStatusLine().getStatusCode());
            // 登陆不成功  
            if (locationHeader == null) {  
                System.out.println("登陆不成功，请稍后再试!");  
                return;  
            } else// 成功  
            {  
                login_success = locationHeader.getValue();// 获取登陆成功之后跳转链接  
                System.out.println("成功之后跳转到的网页网址：" + login_success);  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void PrintText() throws IOException {  
        httpget = new HttpGet(login_success);  
        HttpResponse re2 = null;  
  
        try {  
            re2 = httpclient.execute(httpget);  
            // 输出登录成功后的页面  
            String str = EntityUtils.toString(re2.getEntity());  
            System.out.println(str);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            httppost.abort();  
            httpget.abort();  
            httpclient.close();  
        }  
    }  
  
    public static void main(String[] args) throws Exception {  
          
        String name = "13250277470", password = "214228";  
        // 自己的账号，口令  
        HttpClientLoginDemo httpClientLoginDemo = new HttpClientLoginDemo();  
        httpClientLoginDemo.logIn(name, password);  
        httpClientLoginDemo.PrintText();  
    }  
}  