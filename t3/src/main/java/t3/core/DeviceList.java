package t3.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
@XmlRootElement (name="devlist")
public class DeviceList {

	
	java.util.ArrayList<Device> devices=new java.util.ArrayList<Device>();
	  

	@XmlElement(name="device")
	@XmlElementWrapper(name="devices")
	 public  java.util.ArrayList<Device> getDevices() {
			return devices;
		}
		public  void setDevices(java.util.ArrayList<Device> devices) {
			this.devices = devices;
		}
}
