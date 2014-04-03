package t3.core;



import java.util.HashSet;
import java.util.Set;

import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import t3.repository.Repositories;

public class RegisterThing {
	
	
	
	
	
	static OntModel model=null;
	static Model temp2=null;
	
public static void test(String prefix,String resource){
	
	Model tempmodel=Repositories.getT3Instance();
	temp2=tempmodel.difference(ModelFactory.createDefaultModel());
	
	model=JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM, temp2);
    StmtIterator stmts = model.listStatements(null,RDF.type,model.createResource(prefix+resource));
    while ( stmts.hasNext() ) {
        rdfDFS( stmts.next().getSubject(), new HashSet<RDFNode>(), "" );
    }
 //   model.write( System.out, "N3" ); 
    System.out.println("Finished for this resource:"+prefix+resource);
}

public static void rdfDFS( RDFNode node, Set<RDFNode> visited, String prefix ) {
    if ( visited.contains( node )) {
        return;
    }
    else {
        visited.add( node );
        System.out.println( prefix + node );
        if ( node.isResource() ) {
            StmtIterator stmts = node.asResource().listProperties();
            while ( stmts.hasNext() ) {
                Statement stmt = stmts.next();
                rdfDFS( stmt.getObject(), visited, prefix + node + " =[" + stmt.getPredicate() + "]=> " );
            }
        }
    }

	
	
	
	
	
	
}
	

}
