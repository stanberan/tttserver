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
	
public ThingResource(){
}

//implement Error 

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
  
  


