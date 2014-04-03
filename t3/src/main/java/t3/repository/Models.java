package t3.repository;



import java.util.HashMap;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


import t3.core.IoT_Thing;
import t3.core.ThingInformation;

public class Models{
	final static String INSTANCE="http://t3.abdn.ac.uk/ontologies/instancedata.owl";
	final static String TTT="http://t3.abdn.ac.uk/ontologies/t3.owl";
	final static String PROV="http://www.w3.org/ns/prov";
	final static String IOTA="http://t3.abdn.ac.uk/ontologies/iota.owl";
	
	final static String INSTANCE_NS="http://t3.abdn.ac.uk/ontologies/instancedata.owl#";
	final static String TTT_NS="http://t3.abdn.ac.uk/ontologies/t3.owl#";
	final static String IOTA_NS="http://t3.abdn.ac.uk/ontologies/iota.owl#";
	final static String PROV_NS="http://www.w3.org/ns/prov#";
	
	static OntModel instanceModel=null;
    static Model inferencesModel=null;
    static Model sesameModel=null; 
	//OntModel provenanceModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, "http://www.w3.org/ns/prov");
	public Models(){
	
}

	public static void initialize(){
		System.setProperty("http.proxyHost", "proxy.abdn.ac.uk");
		  System.setProperty("http.proxyPort", "8080");

		System.out.println("Initializing Models");
		if(instanceModel==null || inferencesModel==null){
		SPINModuleRegistry.get().init();
		Model tttModel=ModelFactory.createDefaultModel();
		tttModel.read(TTT);
		OntModel tttOntModel=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, tttModel);
		System.out.println("GETTING instance from REpository");
		Model sesameModelTemp=Repositories.getT3Instance();
		System.out.println("From Repository to memory model");
		
		sesameModel= sesameModelTemp.difference(ModelFactory.createDefaultModel());
		//sesameModel=ModelFactory.createDefaultModel();
		//Model sesameModel=ModelFactory.createDefaultModel();
		//sesameModel.read("http://t3.abdn.ac.uk/ontologies/tdbbase.rdf");
		System.out.println("Creating ontology model from sesame");
		instanceModel=JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,sesameModel);
		System.out.println("Printing Ontology Instance Model");
		//instanceModel.write(System.out, "N3");	
		System.out.println("FINISHED FIRST PRINT");
		inferencesModel = ModelFactory.createDefaultModel();
		instanceModel.addSubModel(inferencesModel);
	instanceModel.addSubModel(tttOntModel);
	instanceModel.write(System.out,"N3");
		System.out.println("FINISHED SECOND PRINT");
		//tttOntModel.write(System.out,"N3");
		System.out.println("THIRD PRINT");
		// Register locally defined functions
		SPINModuleRegistry.get().registerAll(tttOntModel, null);
		System.out.println("This is a new file!!!");
		}
		// Run all inferences		

	}
	
	
	
	
	
	
	public ThingInformation getThingInformation(String id){
		//to implement - real pulling from Database
		
		
		
		
		/*
		Capability c1=new Capability ("Device capable of data collection","Data collection");
		Capability c2=new Capability ("Device is capable of collecting location data", "Data Collection");
		Capability c3=new Capability ("Device can share capability with third party","Third Party Sharing");
		Capability c4=new Capability ("Device is capable of sharing data with RSL Public","Third Party Sharing");
		ArrayList<Capability> capabilities=new ArrayList<Capability>();
		capabilities.add(c1);
		capabilities.add(c2);
		capabilities.add(c3);
		capabilities.add(c4);
		
		ThingInformation thing= new ThingInformation();
		thing.setCapabilities(capabilities);
		thing.setDeviceType("NFC Passsive Device");
		return thing;
		*/
		return new ThingInformation();
		
	}
	
	
	
	
}
