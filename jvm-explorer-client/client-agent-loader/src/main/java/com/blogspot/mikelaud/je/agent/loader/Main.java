package com.blogspot.mikelaud.je.agent.loader;

import java.util.Scanner;

import com.blogspot.mikelaud.je.agent.loader.common.RemoteAgentLoader;
import com.blogspot.mikelaud.je.common.CommonModule;
import com.blogspot.mikelaud.je.ssh.SshModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new CommonModule(), new SshModule(), new AgentLoaderModule());
		AgentLoaderFactory factory = injector.getInstance(AgentLoaderFactory.class);
		//
		//LocalAgentLoader localLoader = factory.newLocalLoader();
		//localLoader.loadAgent();
		//localLoader.loadAgent();
		//
		RemoteAgentLoader remoteLoader = factory.newRemoteLoader("192.168.10.101");
		remoteLoader.login("root", "1q2w3e");
		remoteLoader.getJvmList().forEach(System.out::println);
		remoteLoader.loadAgentByName("com.blogspot.mikelaud.je.agent.bios.Main");
		remoteLoader.loadAgentByName("com.blogspot.mikelaud.je.agent.bios.Main");
		//
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Press \"ENTER\" to continue...");
			scanner.nextLine();
			remoteLoader.logout();
		}
	}

}