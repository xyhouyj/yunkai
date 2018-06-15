package com.hyj.zookeeper.subscribe;

public class ServerData {
//ip地址  id name
	
	private String address;
	
	private Integer id;
	
	private String name;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServerData(String address, Integer id, String name) {
		super();
		this.address = address;
		this.id = id;
		this.name = name;
	}
	
	
}
