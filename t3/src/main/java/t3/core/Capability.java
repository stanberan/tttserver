package t3.core;



public class Capability {
	String descriptionOfCapability="Personal Data Consumption"; //TODO pull from REPO
	String purpose="Purpose was not find in provenance";
	String consumer="Consumer origin not found in provenance";
	String consumerURL="Consumer have not published URL";
	String consumes="";
	String consumerLogo;
	String URI;
	public Capability(){
	
	}

	public String getConsumerLogo() {
		return consumerLogo;
	}

	public void setConsumerLogo(String consumerLogo) {
		this.consumerLogo = consumerLogo;
	}

	public String getConsumerURL() {
		return consumerURL;
	}

	public void setConsumerURL(String consumerURL) {
		this.consumerURL = consumerURL;
	}

	public String getDescriptionOfCapability() {
		return descriptionOfCapability;
	}

	public void setDescriptionOfCapability(String descriptionOfCapability) {
		this.descriptionOfCapability = descriptionOfCapability;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getConsumes() {
		return consumes;
	}

	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}
	

}
