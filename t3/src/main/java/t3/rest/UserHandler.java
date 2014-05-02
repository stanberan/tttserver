package t3.rest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;

import t3.repository.Queries;
import t3.repository.UserConnection;


@Path("/user")
public class UserHandler {
UserConnection conn=UserConnection.getDB();
	
	
	
	@GET
	  @Path("/accepted/{device}/{iotdevice}")
	@Produces({MediaType.APPLICATION_JSON})
	  public Response accepted(@PathParam("device") String device, @PathParam("iotdevice") String iotDevice, @QueryParam("busurl") String busURL){
		
		try {
			//for stats!!
			conn.registerScan(device,iotDevice,new Date());
			//
			Queries.registerBustStopTag(iotDevice, busURL);
			String time=conn.accepted(device, iotDevice);
			if(time!=null){
				
				String accepted="{'accepted':'"+time +"'}";
				
				
				return Response.ok(accepted).build();
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
		    
	  }	
	
	@GET
	@Path("/accept/{device}/{iotdevice}/{nickname}")
	public Response accept(@PathParam("device") String device, @PathParam("iotdevice") String iotDevice, @PathParam("nickname")String nickname){
		try{
			conn.registerAccept(device, iotDevice, URLDecoder.decode(nickname,"UTF-8"));
			return Response.status(Response.Status.CREATED).build();
			}
		catch(Exception e){
			e.printStackTrace();
			return Response.status(Response.Status.ACCEPTED).build();
		}
		
	}
	
	@GET
	@Path("/decline/{device}/{iotdevice}")
	public Response decline(@PathParam("device") String device, @PathParam("iotdevice") String iotDevice){
		try{
			conn.decline(device, iotDevice);
			return Response.status(Response.Status.CREATED).build();
			}
		catch(Exception e){
			e.printStackTrace();
			return Response.status(Response.Status.ACCEPTED).build();
		}
		
	}
	
	@GET
	@Path("/checknick/{device}/{nickname}")
	public Response checkNick(@PathParam("device")String device, @PathParam("nickname") String nickname){
		
		try {
			if(conn.checkNickName(URLDecoder.decode(nickname,"UTF-8"), device)){
				return Response.status(215).build(); //215 server response
			}
			else{
			return Response.status(Response.Status.OK).build(); //200 response
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
	
	

	@GET
	@Path("/track/{device}/{message}")
	public Response track(@PathParam("device") String device, @PathParam("message") String message){
		try{
			conn.registerTrack(device, message,new Date());
			return Response.status(Response.Status.CREATED).build();
			}
		catch(Exception e){
			e.printStackTrace();
			return Response.status(Response.Status.ACCEPTED).build();
		}
		
	}
	
	
	
	@GET
	@Path("/remove/{device}/{iotdevice}")
	public Response remove(@PathParam("device") String device, @PathParam("iotdevice") String iotDevice){
		try{
			int result=conn.removeDevice(device, iotDevice);
			if(result>0){
			return Response.status(Response.Status.OK).build();
			}
			throw new WebApplicationException("Device not removed!");
			}
		catch(Exception e){
			e.printStackTrace();
			throw new WebApplicationException("Device not removed!");
		}
		
	}
	
	
	

	
}
