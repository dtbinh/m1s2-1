package ftp.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import ftp.FTPDatabase;
import ftp.configuration.FTPClientConfiguration;

/**
 * @author  diagne
 */
@PrepareForTest(MessageFormat.class)
public class FTPPwdCommandTest {
	
	/**
	 * @uml.property  name="_pwdCommand"
	 * @uml.associationEnd  
	 */
	private FTPCommand _pwdCommand;
	/**
	 * @uml.property  name="_database"
	 * @uml.associationEnd  
	 */
	private FTPDatabase _database; 
	
	@Before
	public void setUp() {
		_database = Mockito.mock(FTPDatabase.class);
		_pwdCommand = new FTPPwdCommand(_database);
	}

	@Test
	public void testAccept() {
		assertTrue(_pwdCommand.accept("PWD"));
		assertFalse(_pwdCommand.accept("DUMB"));
	}

	@Test
	@Ignore
	public void testExecute() {
		final String uselessArgument = "useless";
		final String workingDirectory = "home/m1/someGuy";
		final FTPClientConfiguration clientConfiguration = Mockito.mock(FTPClientConfiguration.class);
		Mockito.when(clientConfiguration.getWorkingDirectory()).thenReturn(workingDirectory);
		final Socket connection = Mockito.mock(Socket.class);
		final OutputStream outputStream = Mockito.mock(OutputStream.class);
		try {
			Mockito.when(connection.getOutputStream()).thenReturn(outputStream);
		} catch (IOException e) {
			fail();
		}
		PowerMockito.mockStatic(MessageFormat.class);
		Mockito.when(MessageFormat.format(Mockito.anyString(), Mockito.anyString())).thenReturn(workingDirectory);
		Mockito.when(clientConfiguration.getCommandSocket()).thenReturn(connection);
		_pwdCommand.execute(uselessArgument, clientConfiguration);
		Mockito.verify(_database).getMessage(257);
	}


}
