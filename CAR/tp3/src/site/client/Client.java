package site.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.util.Map;

import site.client.executor.ClientExecutorParameters;
import site.client.executor.DefaultClientExecutor;
import site.client.executor.GraphClientExecutor;
import site.client.executor.TreeClientExecutor;
import site.client.reader.SiteReader;
import site.shared.behavior.BehaviorManager;

public class Client {

	private Client() {
	}

	public static void main(String[] args) throws NotBoundException,
			InterruptedException, IOException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		final BehaviorManager<ClientExecutorParameters> manager = new BehaviorManager<ClientExecutorParameters>();
		final SiteReader siteReader = new SiteReader();
		final Map<Integer, Integer[]> nodesToSons = siteReader
				.getNodesFromFile(args[0]);
		final String nodeType = siteReader.getType();
		manager.addBehavior(new TreeClientExecutor());
		manager.addBehavior(new GraphClientExecutor());
		manager.addBehavior(new DefaultClientExecutor());
		final ClientExecutorParameters parameters = new ClientExecutorParameters(
				nodeType, nodesToSons, args);
		manager.execute(parameters);
	}

}
