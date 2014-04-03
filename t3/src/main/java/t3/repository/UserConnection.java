package t3.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import t3.core.Device;
import t3.core.User;

public class UserConnection {
static{
	System.setProperty("http.proxyHost", "proxy.abdn.ac.uk");
	  System.setProperty("http.proxyPort", "8080");
}
      Connection conn;
      ResultSet rs;
      static UserConnection singleton=null;
      public  UserConnection() {
    	  
    	  }

      public static UserConnection getDB(){
    	  if (singleton!=null){
    		  return singleton;
    	  }
    	  else{
    		singleton=new UserConnection();  
    	  
    	  try{
      Class.forName(Configuration.driver).newInstance();
      singleton.conn = DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
      return singleton;
    	  }
    	  catch(Exception e){
    		 
    		  e.printStackTrace();
    		  return null;
    	  }
    	  }
      }
      //checks if user exists based on email
	public boolean userExist(String email){
		try{
		PreparedStatement pStatement=conn.prepareStatement("SELECT * from users WHERE email= ? ");
		pStatement.setString(1, email);
		rs=pStatement.executeQuery();
		if(rs.next()){
			return true;
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
		}
		return false;
		
	}
	
	public User getUser(String email){
		User u=null;
		try{
			PreparedStatement pStatement=conn.prepareStatement("SELECT * FROM users WHERE email=?");
			pStatement.setString(1, email);	
			rs= pStatement.executeQuery();
			if(rs.next()){
				u=new User();
				u.setId(rs.getInt("userid"));
				u.setEmail(rs.getString("email"));
				u.setName(rs.getString("name"));
				u.setPassword(rs.getString("password"));				
			}
			return u;
		}
			catch(Exception e){
				e.printStackTrace();
				return u;
			}
		
		
	}
	public ArrayList<Device> getList(String user){
		
		ArrayList<Device> devices=new ArrayList<Device>();
		try{
			if(conn.isClosed()){
				conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
			}
			PreparedStatement pStatement=conn.prepareStatement("SELECT * FROM accepted WHERE deviceuid=?");
			pStatement.setString(1,user);	
			rs= pStatement.executeQuery();
			while(rs.next()){
				System.out.println("HAS NEXT DEVICE UID");
				Device d=new Device();
				d.setId(rs.getString("iotuid"));
				d.setNickname(rs.getString("nickname"));
					devices.add(d);	
			}
			return devices;
		}
			catch(Exception e){
				e.printStackTrace();
				return devices;
			}
		
		
	}
	
	
	
	// Integrity check constraint on EMAIL wont create if email is already registered
	public boolean registerUser(User u) {
		try{
		PreparedStatement pStatement=conn.prepareStatement("INSERT into users (password,email,name) values(?,?,? )");
		pStatement.setString(1, u.getPassword());
		pStatement.setString(2, u.getEmail());
		pStatement.setString(3, u.getName());
		int i= pStatement.executeUpdate();
		System.out.println("Result is"+i);
		return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean registerOrUpdateToken(int id, String token) throws Exception{
		PreparedStatement pStatement=conn.prepareStatement("INSERT into tokens values(?,?)");
		pStatement.setInt(1,id);
		pStatement.setString(2, token);
		int i= pStatement.executeUpdate();
		if(i>0){
			return true;
		}
		else{
			return false;
		}
		
	}
	public boolean registerAccept(String device, String iotDevice,String nickname ) throws Exception{
		Timestamp ts=new Timestamp(new Date().getTime());
		
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		if(accepted(device,iotDevice)==null){
		PreparedStatement pStatement=conn.prepareStatement("INSERT into accepted values(?,?,?,?)");
		pStatement.setString(1,device);
		pStatement.setString(2, iotDevice);
		pStatement.setString(3, nickname);
		pStatement.setTimestamp(4, ts);
		int i= pStatement.executeUpdate();
		if(i>0){
			return true;
		}
		else{
			return false;
		}
		}
		return true;
	}
	
	public boolean checkNickName(String nickname, String device){
		
		try{
			if(conn.isClosed()){
				conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
			}
			PreparedStatement pStatement=conn.prepareStatement("SELECT * from accepted WHERE deviceuid= ? AND nickname=?");
			pStatement.setString(1,device) ;
			pStatement.setString(2, nickname);
			rs=pStatement.executeQuery();
			if(rs.next()){
				return true;
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
			}
			return false;
		
		
		
	}
	
	
public int removeDevice(String device, String iotdevice){
		
		try{
			if(conn.isClosed()){
				conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
			}
			PreparedStatement pStatement=conn.prepareStatement("DELETE FROM accepted WHERE deviceuid=? AND iotuid=?");
			pStatement.setString(1,device) ;
			pStatement.setString(2, iotdevice);
			return pStatement.executeUpdate();
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
			}
			return -1;
		
		
		
	}
	
	
	public void registerInference(Date date, String user, long time) throws Exception{
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		Timestamp ts=new Timestamp(date.getTime());
		PreparedStatement pStatement=conn.prepareStatement("INSERT into inference values(?,?,?)");
		pStatement.setTimestamp(1, ts);
		pStatement.setString(2, user);
		pStatement.setLong(3, time);
		pStatement.executeUpdate();
		
	}
	
	public void registerScan(String device, String iotDevice, Date date) throws Exception{
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		Timestamp ts=new Timestamp(date.getTime());
		PreparedStatement pStatement=conn.prepareStatement("INSERT into scans values(?,?,?)");
		pStatement.setTimestamp(1, ts);
		pStatement.setString(2, device);
		pStatement.setString(3, iotDevice);
		pStatement.executeUpdate();
		
		
	}
	
	
	public void registerTrack(String device, String message, Date date) throws Exception{
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		Timestamp ts=new Timestamp(date.getTime());
		PreparedStatement pStatement=conn.prepareStatement("INSERT into track values(?,?,?)");
		pStatement.setTimestamp(1, ts);
		pStatement.setString(2, device);
		pStatement.setString(3, message);
		pStatement.executeUpdate();
		
		
	}
	
	
	
	public String accepted(String device, String iotDevice) throws Exception{
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		PreparedStatement pStatement=conn.prepareStatement("SELECT * FROM accepted WHERE deviceuid=? AND iotuid=? ");
		pStatement.setString(1, device);
		pStatement.setString(2,iotDevice);
		rs= pStatement.executeQuery();
		if(rs.next()){
			Timestamp ts=rs.getTimestamp("ts");
			return ts.toString();
		}
		return null;
			
	}
	
	public boolean decline(String device, String iotDevice)throws Exception{
		Timestamp ts=new Timestamp(new Date().getTime());
		if(conn.isClosed()){
			conn=DriverManager.getConnection(Configuration.url+Configuration.dbName,Configuration.userName,Configuration.password);
		}
		
			PreparedStatement pStatement=conn.prepareStatement("INSERT into declined values(?,?,?)");
			pStatement.setString(1,device);
			pStatement.setString(2, iotDevice);
			pStatement.setTimestamp(3, ts);
			int i= pStatement.executeUpdate();
			if(i>0){
				return true;
			}
			else{
				return false;
			}
	
		}
		
		
		
	
	public static void main(String[] args){
UserConnection connection=UserConnection.getDB();
		try{
		connection.decline("Device","IoT device");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}