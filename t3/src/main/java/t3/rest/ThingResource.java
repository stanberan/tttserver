package t3.rest; 

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.json.JSONObject;

import t3.core.Company;
import t3.core.Device;
import t3.core.DeviceList;
import t3.core.IoT_Thing;
import t3.core.RegisterThing;
import t3.core.ThingInformation;
import t3.repository.Models;
import t3.repository.Queries;
import t3.repository.UserConnection;


@Path("/thing")
public class ThingResource {
	
String s="{'uri':'http://192.168.0.1/index.php','capabilities':[{'consumer':'dot.rural Digital Economy Hub','consumerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','consumerURL':'http://www.dotrural.ac.uk/dotrural/','consumes':'Environment temperature and humidity','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used for research purposes','URI':null},{'consumer':'dot.rural Digital Economy Hub','consumerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','consumerURL':'http://www.dotrural.ac.uk/dotrural/','consumes':'Pictures','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used for research purposes','URI':null},{'consumer':'dot.rural Digital Economy Hub','consumerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','consumerURL':'http://www.dotrural.ac.uk/dotrural/','consumes':'Location','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used for research purposes','URI':null}],'deviceDescription':'Smart Toy with embedded sensors.','deviceType':'Smart Toy','manufacturer':'dot.rural Digital Economy Hub ','manufacturerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','manufacturerURL':'http://www.dotrural.ac.uk/dotrural/','owner':'dot.rural Digital Economy Hub ','ownerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','ownerURL':'http://www.dotrural.ac.uk/dotrural','picture':'http://t3.abdn.ac.uk/images/h00t.jpeg','thingName':'H00T'}";
	

String sim="{'uri':'http://t3.abdn.ac.uk:8080/t3v2/1/device/simbbox001/prov','capabilities':[{'consumer':'SimBBox Insurance Ltd','consumerLogo':'http://t3.abdn.ac.uk/image/simbox.png','consumerURL':'http://www.t3.abdn.ac.uk/simbbox','consumes':'Environment temperature','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used to calculate your premium','URI':null},{'consumer':'SimBBox Insurance Ltd','consumerLogo':'http://t3.abdn.ac.uk/image/simbox.png','consumerURL':'http://www.t3.abdn.ac.uk/simbbox','consumes':'GPS location','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used to check driving patterns','URI':null},{'consumer':'SimBBox Insurance Ltd','consumerLogo':'http://t3.abdn.ac.uk/image/simbox.png','consumerURL':'http://www.t3.abdn.ac.uk/simbbox','consumes':'Accelerometer data','descriptionOfCapability':'Personal Data Collection','purpose':'The data is used to check for reckless driving','URI':null},{'consumer':'SimBBox Insurance Ltd','consumerLogo':'http://t3.abdn.ac.uk/image/simbox.png','consumerURL':'http://www.t3.abdn.ac.uk/simbbox','consumes':'Overall distance travelled','descriptionOfCapability':'Personal Data Generation','purpose':'The data is used to calculate your premium','URI':null}],'deviceDescription':'This device is used to capture driving behaviour of drivers in order to tailor their insurance premiums.','deviceType':'Telemetry Box','manufacturer':'Sony Europe Limited ','manufacturerLogo':'http://www.sony.co.uk/pro/assets/images/sony_logo_print.gif','manufacturerURL':'http://www.sony.co.uk','owner':'SimBBox Insurance Ltd','ownerLogo':'http://t3.abdn.ac.uk/images/dotrural.png','ownerURL':'http://www.t3.abdn.ac.uk/simbbox/','picture':'http://t3.abdn.ac.uk/image/simbox.png','thingName':'SimBBox Telemetry Tracking Device'}";

public ThingResource(){
}
public static void main(String[] args){
	ThingResource t=new ThingResource();
	JSONObject j=new JSONObject(t.s);
	System.out.println(j.getString("uri"));
	
	
}
//implement Error 

@GET
@Path("{device}/static/information")
public Response getStatic(@PathParam("device") String device){

	if(device.equals("h00t")){
		return Response.ok().entity(s).build();
	}
	else if(device.equals("simbbox001")){
		return Response.ok().entity(sim).build();
	}
	return Response.noContent().build();

}


  @GET
  @Path("{id}/{user}/information")
  @Produces({MediaType.APPLICATION_JSON })
  public ThingInformation getThing(@PathParam("id") String id, @PathParam("user") String user,@QueryParam("busstop") String busstop, @QueryParam("busurl") String busUrl) {
	  // if lookup is successful then get from hashtable
	  // else perform the op and put to hashtable
	  String iotid=id;
	  if(busstop!=null && busstop.equals("1")){
		//  iotid="MD5Hash";
		  Queries.registerBustStopTag(id,busUrl);
	  }
	  if(!Queries.exists(iotid)){
	      throw new RuntimeException("Get: Not found with " + id +  " not found");
	  }
	 ThingInformation thing=Queries.getDeviceInformation(iotid,user);
	 return thing;
  }

  @GET
  @Path("company/{company}")
  @Produces({MediaType.APPLICATION_JSON })
  public Company getCompany(@PathParam("company") String companyName) {
    Company c=null;
	try {
		c = Queries.getCompanyProfile(URLDecoder.decode(companyName, "UTF-8" ));
		
    if(c==null){
    	throw new WebApplicationException("Not found profile of this company");
    }
	}
    catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return c;
  }
  
  
  
  
  
  @GET
  @Path("/getresource/{prefix}/{resource}")
  public Response testInfernece(@PathParam("prefix") String prefix , @PathParam("resource") String resource){
	 String t3= "http://t3.abdn.ac.uk/ontologies/t3.owl#";
	 String iota="http://t3.abdn.ac.uk/ontologies/iota.owl#";
	 String instance="http://www.w3.org/ns/prov#";
	 System.out.println("From request:" +resource);
	 
	 if(prefix.equals("1")){
	 RegisterThing.test(t3,resource);}
	 else{
		 RegisterThing.test(instance, resource);
	 
	 }
	//	Queries.inferCapabilities("testuser");
	//	Queries.queryCapabilities("MD5Hash");
	  return Response.accepted("The inferences were infered").build();
  }
  @GET
  @Path("/list/{user}")
  @Produces({MediaType.APPLICATION_JSON})
  public DeviceList list(@PathParam("user") String user){
	DeviceList dev=new DeviceList();
	dev.setDevices(UserConnection.getDB().getList(user));
	System.out.println(dev.getDevices().size()+" SIZE");
	for(Device d: dev.getDevices()){
		d.setUrl(Queries.getDevicePictureURL(d.getId()));
		
	}
	 return dev;
  }

	 @GET
	  @Path("/infer")
	  public String infer(){
		
		 Queries.inferCapabilities("Testing inferencing");
		 return "Done";
		 
	  
  }
  
  

  
  

  
}
  
  


