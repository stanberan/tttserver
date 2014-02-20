package t3.core;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="thing")
public class ThingInformation {
	String thingName="Unknown";
	String deviceType="Unknown Device Type";
	String manufacturer="Unknown manufacturer";
	String manufacturerURL="Manufacturer website not provided";
	String ownerURL="Owner Website not provided";
	String owner="Unkown Owner";
	String picture="No image for this device/service";
	String deviceDescription="Maker has not specified description for this device .";
	String manufacturerLogo;
	String ownerLogo;
     
	ArrayList<Capability> capabilities=null;
	ArrayList<Feature> features=null;
	
	//@XmlElement(name="capability")
//	@XmlElementWrapper(name="capabilities")
	public ArrayList<Capability> getCapabilities() {
		return capabilities;
	}
	

	public void setCapabilities(ArrayList<Capability> capabilities) {
		this.capabilities = capabilities;
	}
	//@XmlElement(name="feature")
//	@XmlElementWrapper(name="features")
	public ArrayList<Feature> getFeatures() {
		return features;
	}
	
	
	public String getThingName() {
		return thingName;
	}


	public void setThingName(String thingName) {
		this.thingName = thingName;
	}


	public String getManufacturerLogo() {
		return manufacturerLogo;
	}


	public void setManufacturerLogo(String manufacturerLogo) {
		this.manufacturerLogo = manufacturerLogo;
	}


	public String getOwnerLogo() {
		return ownerLogo;
	}


	public void setOwnerLogo(String ownerLogo) {
		this.ownerLogo = ownerLogo;
	}


	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}
	public String getManufacturerURL() {
		return manufacturerURL;
	}
	public void setManufacturerURL(String manufacturerURL) {
		this.manufacturerURL = manufacturerURL;
	}
	public String getOwnerURL() {
		return ownerURL;
	}
	public void setOwnerURL(String ownerURL) {
		this.ownerURL = ownerURL;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getDeviceDescription() {
		return deviceDescription;
	}
	public void setDeviceDescription(String deviceDescription) {
		this.deviceDescription = deviceDescription;
	}
	

	
	
}
