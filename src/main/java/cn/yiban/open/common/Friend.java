package cn.yiban.open.common;

import cn.yiban.util.HTTPSimple;

/**
 * 易班API的好友接口管理
 *
 * 查看自己的好友关系
 */
public class Friend
{
	
	private final String YIBAN_OPEN_URL		= "https://openapi.yiban.cn/";
	
	private final String YIBAN_FRIEND_LIST	= "friend/me_list";
	private final String YIBAN_FRIEND_CHECK	= "friend/check";
	
	/**
	 * 已经通过授权的访问令牌
	 */
	private String token;
	
	/**
	 * 构造函数
	 *
	 * @param	String 访问令牌
	 */
	public Friend(String token)
	{
		this.token = token;
	}
	
	/**
	 * 查看好友列表
	 * 
	 * 查看个人所有的好友，用page与count分页
	 *
	 * @param	int		分页码
	 * @param	int		页数
	 * @return	String	JSON字符串
	 */
	public String list(int page, int count)
	{
		String query = YIBAN_OPEN_URL;
		query += YIBAN_FRIEND_LIST;
		query += "?access_token=";
		query += token;
		query += "&page=";
		query += String.valueOf(page);
		query += "&count=";
		query += String.valueOf(count);
		return HTTPSimple.GET(query);
	}
	
	/**
	 * 查询与指定用户的好友关系
	 *
	 * @param	int		指定用户ID
	 * @return	String	JSON字符串
	 */
	public String check(int friendid)
	{
		String query = YIBAN_OPEN_URL;
		query += YIBAN_FRIEND_CHECK;
		query += "?access_token=";
		query += token;
		query += "&yb_friend_uid=";
		query += String.valueOf(friendid);
		return HTTPSimple.GET(query);
	}
	
}