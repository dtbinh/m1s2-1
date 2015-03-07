package ftp.configuration;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class FTPServerConfiguration {

	private ServerSocket _serverSocket;
	private int _port;
	private Socket _connection;
	private String _baseDirectory;
	private AtomicInteger _idGenerator;
	private String _directorySeparator;
	
	public FTPServerConfiguration(int port, String baseDirectory) {
		try {
			_port = port;
			_serverSocket = new ServerSocket(port);
			_baseDirectory = new File(baseDirectory).getAbsolutePath();
			_idGenerator = new AtomicInteger();
			_directorySeparator = System.getProperty("file.separator");
		} catch (IOException e) {
			System.err.println("I/O error while opening the socket.");
		}
	}
	
	public String getAddress() {
		return _serverSocket.getInetAddress().getHostAddress();
	}
	
	public String getDirectorySeparator() {
		return _directorySeparator;
	}
	
	public AtomicInteger getIdGenerator() {
		return _idGenerator;
	}

	public int getPort() {
		return _port;
	}

	public ServerSocket getServerSocket() {
		return _serverSocket;
	}

	public String getBaseDirectory() {
		return _baseDirectory;
	}
	
	public Socket getConnection() {
		return _connection;
	}
	
	public void setConnection(Socket connection) {
		_connection = connection;
	}

}
