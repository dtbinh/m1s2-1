package ftp.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ftp.FTPDatabase;
import ftp.configuration.FTPClientConfiguration;

/**
 * @author  diagne
 */
public class FTPUserCommandTest {
	
	/**
	 * @uml.property  name="_userCommand"
	 * @uml.associationEnd  
	 */
	private FTPCommand _userCommand;
	/**
	 * @uml.property  name="_database"
	 * @uml.associationEnd  
	 */
	private FTPDatabase _database; 
	
	@Before
	public void setUp() {
		_database = Mockito.mock(FTPDatabase.class);
		_userCommand = new FTPUserCommand(_database);
	}

	@Test
	public void testAccept() {
		assertTrue(_userCommand.accept("USER"));
		assertFalse(_userCommand.accept("DUMB"));
	}

	@Test
	public void testExecute() {
		final String username = "test";
		final FTPClientConfiguration clientConfiguration = Mockito.mock(FTPClientConfiguration.class); 
		final Socket connection = Mockito.mock(Socket.class);
		final OutputStream outputStream = Mockito.mock(OutputStream.class);
		try {
			Mockito.when(connection.getOutputStream()).thenReturn(outputStream);
		} catch (IOException e) {
			fail();
		}
		Mockito.when(clientConfiguration.getCommandSocket()).thenReturn(connection);
		_userCommand.execute(username, clientConfiguration);
		Mockito.verify(clientConfiguration).setUsername(username);
		Mockito.verify(_database).getMessage(331);
	}

}
