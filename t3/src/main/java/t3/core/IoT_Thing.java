package t3.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class IoT_Thing {
    private String URL;
	private String id;
	private String description;
	
	public IoT_Thing(String id, String description){
		super();
		this.id=id;
		this.description=description;
		
	}
	public IoT_Thing(){
		super();
		this.id="default";
		this.description="Default Description";
		
	}
	@XmlElement(name="hasID")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name= "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

	 @XmlElement(name= "hasURL") 
	 public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	@Override public String toString() {
         return "Url: " + URL +
          "; Description: " + description+ "; Tag ID:"+ id;
      }
	
}
