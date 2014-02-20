package t3.repository;

import java.util.Map;

import org.openjena.jenasesame.JenaSesame;
import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Repositories {

	

	private static RepositoryConnection instanceConnection = null;
	private static RepositoryConnection userConnection = null;
	private static Repository instanceRepository = null; // T3 Instance Repository
															
	private static Model t3Model = null; // Jena Model

	// User Repository
	private static Repository userRepository = null;
	private static Model userModel = null;

	public Repositories() {

	}

	public static Model getT3Instance() {
		if (t3Model == null) {

			try {

				instanceRepository = new HTTPRepository(Configuration.sesameServer,
						Configuration.repositoryT3);
				instanceConnection = instanceRepository.getConnection();
				t3Model = JenaSesame.createModel(instanceConnection);
			} catch (RepositoryException e) {

				e.printStackTrace();
			}
		}
		return t3Model;

	}

	public static Model getUserInstance() {
		if (userModel == null) {

			try {

				userRepository = new HTTPRepository(Configuration.sesameServer,
						Configuration.repositoryUser);
				userConnection = userRepository.getConnection();
				userModel = JenaSesame.createModel(userConnection);
			} catch (RepositoryException e) {

				e.printStackTrace();
			}
		}
		return userModel;

	}

	public static TupleQueryResult query(String query,
			Map<String, String> bindings) {
		//if not initializied
		if (instanceConnection == null) {
			getT3Instance();
		}
		TupleQuery tupleQuery;
		try {
			tupleQuery = instanceConnection.prepareTupleQuery(
					QueryLanguage.SPARQL, query);
			if (bindings != null) {
				for (Map.Entry<String, String> entry : bindings.entrySet()) {
					tupleQuery.setBinding(entry.getKey(), instanceConnection
							.getValueFactory().createURI(entry.getValue()));
				}
			}
		
			return tupleQuery.evaluate();
		} catch (RepositoryException | MalformedQueryException
				| QueryEvaluationException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		Repositories.getT3Instance().write(System.out, "N3");

	}

}
