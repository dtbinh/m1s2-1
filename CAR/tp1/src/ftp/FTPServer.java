package ftp;

import java.io.IOException;
import java.net.ServerSocket;

import ftp.command.FTPCdupCommand;
import ftp.command.FTPCommandManager;
import ftp.command.FTPCwdCommand;
import ftp.command.FTPListCommand;
import ftp.command.FTPNotImplementedCommand;
import ftp.command.FTPPassCommand;
import ftp.command.FTPPasvCommand;
import ftp.command.FTPPortCommand;
import ftp.command.FTPPwdCommand;
import ftp.command.FTPQuitCommand;
import ftp.command.FTPRetrCommand;
import ftp.command.FTPStorCommand;
import ftp.command.FTPSystCommand;
import ftp.command.FTPTypeCommand;
import ftp.command.FTPUserCommand;
import ftp.configuration.FTPServerConfiguration;

/**
 * @author  diagne
 */
public class FTPServer extends FTPMessageSender {

	private FTPServerConfiguration _configuration;
	private static final int DEFAULT_PORT = 1500;
	private static final String DEFAULT_DIRECTORY = "/home/m1/diagne/workspace";

	public FTPServer(int port, String baseDirectory) {
		_configuration = new FTPServerConfiguration(port, baseDirectory);
	}

	public FTPServerConfiguration getConfiguration() {
		return _configuration;
	}

	public void connectToClient() {
		try {
			final ServerSocket serverSocket = _configuration.getServerSocket();
			_configuration.setConnection(serverSocket.accept());
		} catch (IOException e) {
			System.err.println("I/O error while waiting for a connection.");
		}
	}

	public void closeServer() {
		try {
			final ServerSocket serverSocket = _configuration.getServerSocket();
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("I/O exception while closing the socket.");
		}
	}

	public static void main(String[] args) {
		final FTPServer ftpServer = new FTPServer(DEFAULT_PORT,
				DEFAULT_DIRECTORY);
		final FTPServerConfiguration serverConfiguration = ftpServer
				.getConfiguration();
		final FTPCommandManager commandManager = new FTPCommandManager();
		commandManager.addCommand(new FTPCwdCommand());
		commandManager.addCommand(new FTPCdupCommand());
		commandManager.addCommand(new FTPListCommand());
		commandManager.addCommand(new FTPPassCommand());
		commandManager.addCommand(new FTPPasvCommand());
		commandManager.addCommand(new FTPPortCommand());
		commandManager.addCommand(new FTPPwdCommand());
		commandManager.addCommand(new FTPQuitCommand());
		commandManager.addCommand(new FTPRetrCommand());
		commandManager.addCommand(new FTPStorCommand());
		commandManager.addCommand(new FTPSystCommand());
		commandManager.addCommand(new FTPTypeCommand());
		commandManager.addCommand(new FTPUserCommand());
		commandManager.addCommand(new FTPNotImplementedCommand());
		System.out.println("--> FTP server opened on "
				+ serverConfiguration.getAddress() + ", port "
				+ serverConfiguration.getPort());
		while (true) {
			ftpServer.connectToClient();
			serverConfiguration.addClient();
			System.out.println("--> New client connected on this server.");
			System.out.println("--> total clients : "
					+ serverConfiguration.getNbClients() + ".");
			ftpServer.sendCommandWithDefaultMessage(serverConfiguration.getConnection(), 220);
			final FTPRequestHandler requestHandler = new FTPRequestHandler(
					serverConfiguration, commandManager);
			new Thread(requestHandler).start();
		}
	}

}
