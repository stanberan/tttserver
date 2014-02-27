package t3.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINModuleRegistry;

import t3.core.Capability;
import t3.core.Company;
import t3.core.Feature;
import t3.core.IoT_Thing;
import t3.core.ThingInformation;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class Queries {

	
	
//md5 hash of the T3 tag or other Tag used as entry point	
public static boolean exists(String id){
	Individual tag =Models.instanceModel.getIndividual(Models.INSTANCE_NS+id);	
		if(tag!=null){
			return true;
		}
		return false;
	}

public static boolean registerBustStopTag(String identifier){
	Resource tag = Repositories.getT3Instance().createResource(Models.INSTANCE_NS+identifier);
	Property identifies = ResourceFactory.createProperty(Models.IOTA_NS, "identifies");
	Resource md5= Repositories.getT3Instance().createResource(Models.INSTANCE_NS+"MD5Hash");
	tag.addProperty(identifies, md5);
	
	Resource tag1 = Models.instanceModel.createResource(Models.INSTANCE_NS+identifier);
	Resource md51= Models.instanceModel.createResource(Models.INSTANCE_NS+"MD5Hash");
	tag1.addProperty(identifies, md51);
	
	/*
	String queryString="prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
			+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
			+ "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "prefix instance:<http://t3.abdn.ac.uk/ontologies/instancedata.owl#>"
			+ "INSERT DATA {"
			+ " ?tag iota:identifies instance:MD5Hash . }";
	
	  ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
		queryStr.setIri("tag", Models.INSTANCE_NS+identifier);
		Query query = QueryFactory.create(queryStr.toString());
		UpdateFactory.
		QueryExecution qe = QueryExecutionFactory.create(query, Repositories.getT3Instance());
		qe.execConstruct();*/
		return true;
}

public static ThingInformation getDeviceInformation(String id,String user){
	
    ThingInformation thi=null;
    HashMap<String,String> bindings=new HashMap<String,String>();
	 
	 
    try{
    String queryString = "prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
					+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
					+ "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "prefix xsd:<http://www.w3.org/2000/10/XMLSchema#>"
					+ "prefix foaf:<http://xmlns.com/foaf/0.1/>  "
					+ "SELECT ?deviceDescription ?deviceTypeDescription ?thingName ?manufacturerLogo ?ownerLogo ?pictureURL ?manufacturerName ?manufacturerURL ?ownerName ?ownerURL  "
					+ "WHERE { ?tag iota:identifies ?device . "+
					"OPTIONAL{?device ttt:manufacturer ?manufacturer} ."+
					"OPTIONAL{?device ttt:owner ?owner }."+
					"OPTIONAL{?manufacturer foaf:name ?manufacturerName }."+
					"OPTIONAL{?device iota:isAttachedTo ?physicalThing }."+
					"OPTIONAL{?physicalThing foaf:name ?thingName }."+
					"OPTIONAL{?manufacturer foaf:homepage ?manufacturerURL } ."
					+ "OPTIONAL{?manufacturer foaf:logo ?manufacturerLogo }."+
					"OPTIONAL{?owner foaf:name ?ownerName }."+
					"OPTIONAL{?owner foaf:homepage ?ownerURL }."
					+ "OPTIONAL{?owner foaf:logo ?ownerLogo }."+
					"OPTIONAL{?device ttt:deviceDescription ?deviceDescription }."+
					"OPTIONAL{?device ttt:pictureURL ?pictureURL }."+
					"OPTIONAL{?device ttt:typeDescription ?deviceTypeDescription } }";
	
    //Bind URI values NOT literal
    ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
	queryStr.setIri("tag", Models.INSTANCE_NS+id);
	Query query = QueryFactory.create(queryStr.toString());
	QueryExecution qe = QueryExecutionFactory.create(query, Models.instanceModel);

		ResultSet rs = qe.execSelect();
		if (rs.hasNext()){
 thi=new ThingInformation();
		QuerySolution next= rs.next();
		thi.setDeviceDescription(next.get("deviceDescription").asLiteral().getString());
		thi.setDeviceType(next.get("deviceTypeDescription").asLiteral().getString());
		thi.setManufacturer(next.get("manufacturerName").asLiteral().getString());
		thi.setManufacturerURL(next.get("manufacturerURL").asResource().getURI());
		thi.setOwner(next.get("ownerName").asLiteral().getString());
		thi.setOwnerURL(next.get("ownerURL").asResource().getURI());
		thi.setOwnerLogo(next.get("ownerLogo").asResource().getURI());
		thi.setManufacturerLogo(next.get("manufacturerLogo").asResource().getURI());
		thi.setPicture(next.get("pictureURL").asLiteral().getString());
		thi.setThingName(next.get("thingName").asLiteral().getString());
		//INFER CAPABILITIES AND QUALITIES for ALL DEVICES
		inferCapabilities(user);
		thi.setCapabilities(queryCapabilities(id));
		thi.setFeatures(queryFeatures(id));
		//Clear them again
		Models.inferencesModel.removeAll();
		return thi;
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	return thi;
}
private static void printBindingSet(BindingSet s){
	Set<String> set=s.getBindingNames();
	for(String value :set){
		System.out.println("Printing Binding Set");
		System.out.println(value);
	}
	
}

public static Company getCompanyProfile(String companyName){
	Company c=new Company();
	
	String queryString="prefix instance:<http://t3.abdn.ac.uk/ontologies/instancedata.owl#>"
			+ "prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
			+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
			+ "prefix ns:<http://www.w3.org/2006/vcard/ns#>"
			+ "prefix foaf:<http://xmlns.com/foaf/0.1/>  "
			+ "SELECT ?mail ?company ?url ?logo ?telephone ?streetAddress ?postcode ?city  "
			+ "WHERE {"
			+ " ?company foaf:name ?name ." +
			"OPTIONAL{?company foaf:mbox ?mail } ."+
			"OPTIONAL{?company foaf:logo ?logo} ."+
			"OPTIONAL{?company foaf:homepage ?url } ."+
			"OPTIONAL{?company foaf:phone ?telephone } ."+
			"OPTIONAL{?company ns:hasAddress ?address} ."+
			"OPTIONAL{?address ns:locality ?city} ."+
			"OPTIONAL{?address ns:postal-code ?postcode} ."+
			"OPTIONAL{?address ns:street-address ?streetAddress} }";
	
	ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
	queryStr.setLiteral("name", companyName);
	Query query = QueryFactory.create(queryStr.toString());
	QueryExecution qe = QueryExecutionFactory.create(query, Models.instanceModel);
	ResultSet rs=qe.execSelect();
	
	if(rs.hasNext()){
		QuerySolution solution=rs.next();
		if(solution.get("mail")!=null){
		c.setEmail(solution.get("mail").asResource().getURI());
		}
		c.setLogo(solution.get("logo").asResource().getURI());
		c.setName(companyName);
		c.setUrl(solution.get("url").asResource().getURI());
		c.setResourceIdentifier(solution.get("company").asResource().getURI());
		c.setTelNumber(solution.get("telephone").asLiteral().getString());
		
		String address="";
		address+=solution.get("streetAddress").asLiteral().getString();
		address+="\n"+solution.get("city").asLiteral().getString();
		address+="\n"+solution.get("postcode").asLiteral().getString();
		c.setAddress(address);
		
		
	}
	
	
	return c;
}


public static String getDevicePictureURL(String id){
	String queryString="prefix instance:<http://t3.abdn.ac.uk/ontologies/instancedata.owl#>"
			+ "prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
			+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
			+ "SELECT ?pictureURL "
			+ "WHERE {"
			+ " ?tag iota:identifies ?device ."
			+ "?device ttt:pictureURL ?pictureURL . } ";
	
	 ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
		queryStr.setIri("tag", Models.INSTANCE_NS+id);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qe = QueryExecutionFactory.create(query, Models.instanceModel);
		ResultSet rs=qe.execSelect();
		if(rs.hasNext()){
			return rs.next().get("pictureURL").asLiteral().getString();
		}
		return "placeholder";
}



public static ArrayList<Capability> queryCapabilities(String id){
	ArrayList<Capability> capabilities=new ArrayList<Capability>();
	String queryString = "prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
			+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
			+ "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "prefix xsd:<http://www.w3.org/2000/10/XMLSchema#> "
			+ "prefix foaf:<http://xmlns.com/foaf/0.1/> "
			+ "SELECT ?purposeDesc ?consumerDesc ?consumesDesc ?consumerURL ?consumerLogo "
			+ "WHERE {?tag iota:identifies ?device . "
			+ "?device ttt:isCapableOf ?capability . "
			+ "?capability ttt:purpose ?purposeDesc . "
			+ "	?capability ttt:consumer ?consumer . "
			+ "	?capability ttt:consumes ?consumes ."
			+ "	?consumes ttt:description ?consumesDesc ."
			+ "  ?consumer foaf:name ?consumerDesc ."
			+ "?consumer foaf:logo ?consumerLogo .  "
			+ "	?consumer foaf:homepage ?consumerURL "
			+ "		 "
			+ "   }";
	
	ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
	queryStr.setIri("tag", Models.INSTANCE_NS+id);
	Query query = QueryFactory.create(queryStr.toString());
	QueryExecution qe = QueryExecutionFactory.create(query, Models.instanceModel);
	try{
		ResultSet rs = qe.execSelect();
		while (rs.hasNext()){
			Capability cap=new Capability();
		QuerySolution next= rs.next();
		cap.setPurpose(next.get("purposeDesc").asLiteral().getString());
		cap.setConsumer(next.get("consumerDesc").asLiteral().getString());
		cap.setConsumes(next.get("consumesDesc").asLiteral().getString());
		cap.setConsumerURL(next.get("consumerURL").asResource().getURI());
		cap.setConsumerLogo(next.get("consumerLogo").asResource().getURI());
		capabilities.add(cap);
		System.out.println("Printing capability"+cap.getConsumer()+cap.getPurpose()+cap.getConsumes()+cap.getConsumerURL());
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
			
	return capabilities;		
			
	
}

public static ArrayList<Feature> queryFeatures(String id){
	ArrayList<Feature> features=new ArrayList<Feature>();
	String queryString = "prefix ttt:<http://t3.abdn.ac.uk/ontologies/t3.owl#> "
			+ "prefix iota:<http://t3.abdn.ac.uk/ontologies/iota.owl#>"
			+ "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "prefix xsd:<http://www.w3.org/2000/10/XMLSchema#> "
			+ "prefix foaf:<http://xmlns.com/foaf/0.1/> " +
			"SELECT ?qualityDescription ?qualityProviderName ?qualityProviderURL ?qualityProviderLogo"+
			"WHERE{ "+
				"	?tag iota:identifies ?device ."+
				"	?device ttt:hasQuality ?quality ."+
				"	?quality ttt:qualityDescription ?qualityDescription ."+
				"	?quality ttt:provider ?provider ."+
				"	?provider foaf:name ?qualityProviderName ."
				+ "?provider foaf:logo ?qualityProviderLogo ."+
				"	?provider foaf:homepage ?qualityProviderURL ."+
				"	}";

	
	ParameterizedSparqlString queryStr= new ParameterizedSparqlString (queryString);
	queryStr.setIri("tag", Models.INSTANCE_NS+id);
	Query query = QueryFactory.create(queryStr.toString());
	QueryExecution qe = QueryExecutionFactory.create(query, Models.instanceModel);
	try{
		ResultSet rs = qe.execSelect();
		while (rs.hasNext()){
			Feature feature=new Feature();
		QuerySolution next= rs.next();
		feature.setDescription(next.get("qualityDescription").asLiteral().getString());
		feature.setProvider(next.get("qualityProviderName").asLiteral().getString());
		feature.setProviderURL(next.get("qualityProviderURL").asResource().getURI());
		//feature.setProviderLogo(next.get("qualityProviderLogo").asResource().getURI());
		features.add(feature);
		System.out.println("Printing quality feature: "+feature.getDescription()+feature.getProvider()+feature.getProviderURL()+feature.getClass());
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
			
	return features;		
			
	
}



public static void inferCapabilities(String user){
   
	System.out.println("INITIALIZE MODELS from inferCapabilities");
	Models.initialize();
	long before=System.currentTimeMillis();
SPINInferences.run(Models.instanceModel, Models.inferencesModel, null, null,true, null);   //issue when connected to sesame 
   long after=System.currentTimeMillis();
   try{
   UserConnection.getDB().registerInference(new Date(), user, after-before);
   }
   catch(Exception e){
	   e.printStackTrace();
	   
   }
System.out.println("Inferred triples: " + Models.inferencesModel.size());
 Models.inferencesModel.write(System.out, "N3");
 
 
 
 
}
	
}
