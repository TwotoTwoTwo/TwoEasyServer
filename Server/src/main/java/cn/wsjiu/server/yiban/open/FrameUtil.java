package cn.wsjiu.server.yiban.open;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.lang.StringBuffer;
import java.lang.IllegalAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import cn.wsjiu.server.yiban.util.AESDecoder;

public class FrameUtil
{
	final String IAPP_AUTHURL = "https://openapi.yiban.cn/oauth/authorize";
	final String TEXT_ENCODING		= "UTF-8";
	
	final String PARAM_VERIFY = "verify4j_request";
	final String PARAM_ORIGIN = "verify_request";

	private HttpServletRequest  request;
	private HttpServletResponse response;

	String appid;
	String secrt;
	String url;

	String query;
	String result;

	boolean isAuthed;
	String  token;
	String  userid;
	String  username;
	int timestamp;
	int formheight;

	/**
	 * 构造函数
	 *
	 * 除了配置的AppID、AppSecret和回调URL外，
	 * 还需要传入HttpServlet中的Request与Response对象
	 *
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param String AppID值，用作AES解密的IV
	 * @param String AppSecret值，用作AES解密的KEY
	 * @param String 回调的URL地址
	 */
	public FrameUtil(HttpServletRequest request, HttpServletResponse response, String appid, String secret, String backurl)
	{
		this.request  = request;
		this.response = response;
		this.secrt    = secret;
		this.appid    = appid;
		this.url = backurl;
		this.token = null;
		this.isAuthed = false;
		this.timestamp = 0;
		this.formheight = 60;
	}

	/**
	 * 授权验证操作
	 *
	 *
	 */
	public boolean perform()
		throws Exception
	{
		if (appid.length() == 16)
		{
			query  = request.getParameter(PARAM_ORIGIN);
		}
		else
		{
			query  = request.getParameter(PARAM_VERIFY);
		}
		result = AESDecoder.dec(query, secrt, appid);
		if (result == null)
		{
			throw new IllegalAccessException("AES decrypt ERROR");
		}
		JSONObject json = JSONObject.fromObject(result);
		if (json == null)
		{
			throw new IllegalAccessException("Json parse ERROR");
		}
		if (json.has("visit_oauth") == false)
		{
			throw new IllegalAccessException("Json format ERROR");
		}
		try
		{
			isAuthed = json.getBoolean("visit_oauth");
		}
		catch (JSONException ex)
		{
			JSONObject json4a = json.getJSONObject("visit_oauth");
			if (json4a.has("access_token"))
			{
				token = json4a.getString("access_token");
			}
		}
		if (token != null && token.length() >= 8)
		{
			isAuthed = true;
		}
		if (isAuthed == false)
		{
			if (request.getMethod().toUpperCase().equals("GET"))
			{
				iappForAuth();
			}
			else
			{
				jumpForAuth();
			}
			return false;
		}
		if (json.has("visit_time"))
		{
			timestamp = json.getInt("visit_time");
		}
		if (json.has("visit_user"))
		{
			JSONObject json4u = json.getJSONObject("visit_user");
			if (json4u == null)
			{
				throw new IllegalAccessException("Json format ERROR, can not found user");
			}
			if (json4u.has("userid"))
			{
				userid = json4u.getString("userid");
			}
			if (json4u.has("username"))
			{
				username = json4u.getString("username");
			}
		}
		return true;
	}

	/**
	 * 设置授权弹窗高度
	 * 
	 *
	 */
	public void setHeight(int height)
	{
		this.formheight = height;
	}
	
	/**
	 * 加密的验证字符串
	 *
	 *
	 */
	public String getQuery()
	{
		return query;
	}

	/**
	 * 解密后的验证字符串（JSON格式）
	 *
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * 用户ID值
	 *
	 */
	public String getUserId()
	{
		return userid;
	}

	/**
	 * 用户名称
	 *
	 */
	public String getUserName()
	{
		return username;
	}
	
	/**
	 * ACCESS TOKEN 值
	 *
	 */
	public String getAccessToken()
	{
		return token;
	}

	/**
	 * 验证时间
	 *
	 */
	public int getTimestamp()
	{
		return timestamp;
	}

	/**
	 * 重定向到授权认证页面
	 *
	 * 在perform()方法中若未通过授权，
	 * 自动调用此方法重定向到授权服务器进行授权
	 */
	public void jumpForAuth()
		throws Exception
	{
		this.jumpForAuth(formheight);
	}
	
	public void iappForAuth()
	{
		try
		{
			StringBuffer buffer = new StringBuffer(IAPP_AUTHURL);
			buffer.append("?client_id=");
			buffer.append(appid);
			buffer.append("&redirect_uri=");
			buffer.append(URLEncoder.encode(url, TEXT_ENCODING));
			buffer.append("&display=html");
			response.sendRedirect(buffer.toString());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * 重定向到授权认证页面
	 *
	 * 在perform()方法中若未通过授权，
	 * 自动调用此方法重定向到授权服务器进行授权
	 *
	 * @param int 授权窗口高度
	 */
	public void jumpForAuth(int height)
		throws Exception
	{
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		out.println("<title>{YBLANG::WEB_APP_TITLE}</title>");
		out.println("<script src=\"http://f.yiban.cn/global/js/jquery1.11.0.min.js\" type=\"text/javascript\"></script>");
		out.println("<script src=\"http://f.yiban.cn/apps/js/authiframe.js\" type=\"text/javascript\"></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<script type=\"text/javascript\">");
		out.println("$(function() {");
		out.println("(function(){");
		out.println("App.AuthDialog.show({");
		out.printf("client_id: \"%s\",\n", this.appid);
		out.printf("redirect_uri: \"%s\",\n", this.url);
		out.printf("height: %d,\n", height);
		out.println("scope:  \"\"");
		out.println("});");
		out.println("})();");
		out.println("});");
		out.println("</script>");
		out.println("</body>");
		out.println("</html>");
	}
	
	/**
	 * 站内应用页面自适应代码
	 *
	 * 在页面中嵌入此代码可以进行页面自适应
	 * 返回字符串，可以选择是否使用此代码进行自适应
	 *
	 */
	public String adaptive()
	{
		return adaptive(1024, 768, "c_iframe");
	}

	/**
	 * 站内应用页面自适应代码
	 *
	 * 在页面中嵌入此代码可以进行页面自适应
	 * 返回字符串，可以选择是否使用此代码进行自适应
	 * 
	 * @param int 宽
	 * @param int 高
	 * @param String Frame名称
	 * @return String 页面自适应代码
	 */
	public String adaptive(int width, int height, String framename)
	{
		String source = "<iframe id=\"" + framename + "\" height=\"0\" width=\"0\" src=\"\" style=\"display:none\"></iframe>\n";
		source += "<script type=\"text/javascript\">\n";
		source += "function setSize(w, h) {\n";
		source += "	var c_iframe = document.getElementById(\"" + framename + "\");\n";
		source += "		c_iframe.src = \"http://f.yiban.cn/apps.html#\" + w + \"|\" + h;\n";
		source += "	}\n";
		source += "setSize(" + String.valueOf(width) + ", " + String.valueOf(height) + ");\n";
		source += "</script>\n";
		return source;
	}
	
	
}
