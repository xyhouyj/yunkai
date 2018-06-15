package com.hyj.zookeeper.subscribe;

import java.io.Serializable;

public class ServerConfig implements Serializable{
//数据库的连接地址 用户名 密码
	private String dbUrl;
	
	private String dbPwd;
	
	private String dbUser;

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public ServerConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServerConfig(String dbUrl, String dbPwd, String dbUser) {
		super();
		this.dbUrl = dbUrl;
		this.dbPwd = dbPwd;
		this.dbUser = dbUser;
	}


}
