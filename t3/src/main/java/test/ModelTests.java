package test;

import java.util.Iterator;

import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;

public class ModelTests {

	final static String INSTANCE="http://t3.abdn.ac.uk/ontologies/instancedata.owl";
	final static String TTT="http://t3.abdn.ac.uk/ontologies/t3.owl";
	final static String PROV="http://www.w3.org/ns/prov";
	final static String IOTA="http://t3.abdn.ac.uk/ontologies/iota.owl";
	
	final static String INSTANCE_NS="http://t3.abdn.ac.uk/ontologies/instancedata.owl#";
	final static String TTT_NS="http://t3.abdn.ac.uk/ontologies/t3.owl#";
	final static String IOTA_NS="http://t3.abdn.ac.uk/ontologies/iota.owl#";
	final static String PROV_NS="http://www.w3.org/ns/prov#";
	final static String FOAF_NS="http://xmlns.com/foaf/spec/";
	
	final static String baseNS="http://t3.abdn.ac.uk/ontologies/export.rdf";
	
	
	
	public void loadModel (String url){
		Model tttModel= ModelFactory.createDefaultModel();
		tttModel.read(TTT);
		OntModel tttOntModel=ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, tttModel);
		tttOntModel.write(System.out,"N3");

		Model base= ModelFactory.createDefaultModel();
		base.read(baseNS);
		base.write(System.out,"N3");
			
		//InfModel modelReasoner=ModelFactory.createRDFSModel(tttOntModel,base);
		//modelReasoner.write(System.out,"N3");
		//Model difference =base.difference(modelReasoner);
		//System.out.println("DIFFERENCE---------");
		//difference.write(System.out, "N3");
		
			SPINModuleRegistry.get().init();
			//Model sesameModel=Repositories.getT3Instance();
			OntModel instanceModel=JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,base);
			instanceModel.write(System.out, "N3");	
			Model inferencesModel = ModelFactory.createDefaultModel();
			instanceModel.addSubModel(inferencesModel);
			
			// Register locally defined functions
			SPINModuleRegistry.get().registerAll(instanceModel, null);
			SPINInferences.run(instanceModel, inferencesModel, null, null, false, null);

			System.out.println("Inferred triples: " + inferencesModel.size());
			// Run all inferences	
		
	}
	
	public void validity(){
		 Model data =null;
		    InfModel infmodel = ModelFactory.createRDFSModel(data);
		    ValidityReport validity = infmodel.validate();
		    if (validity.isValid()) {
		        System.out.println("OK");
		    } else {
		        System.out.println("Conflicts");
		        for (Iterator i = validity.getReports(); i.hasNext(); ) {
		            System.out.println(" - " + i.next());
		        }
		    }
	}
	
	
public static void main(String[] args){
	ModelTests tests=new ModelTests();
	tests.loadModel("sd");
}
}
