package t3.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


public class Device {

	String id;
	String nickname;
	 String url;
	  public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	static java.util.ArrayList<Device> devices=new java.util.ArrayList<Device>();

	public static java.util.ArrayList<Device> getDevices() {
		return devices;
	}
	public static void setDevices(java.util.ArrayList<Device> devices) {
		Device.devices = devices;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
