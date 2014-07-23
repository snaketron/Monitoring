package system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import utils.GlobalConst;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * The UsbReader class is used to establish a connection to the predefined PORT. Using the
 * BuffererdReader as an attribute of this class, the Engine  class can read the lines 
 * received at that port.
 */
public class UsbReader {
	private BufferedReader buffReader;

	public UsbReader() {
		
	}
	
	public boolean connectToPort() {
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier portId = null;
		while(portIdentifiers.hasMoreElements()) {
			CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
		    if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL && pid.getName().equals(GlobalConst.USB_PORT)) {
		        portId = pid;
		        break;
		    }
		}
		
		if(portId == null) {
		    System.err.println("Could not find serial port " + GlobalConst.USB_PORT);
		    System.exit(1);
		}
		else {
			System.out.println("port: " + portId.getName());
		}
			
		SerialPort serialPort = null;
		try {
			serialPort = (SerialPort) portId.open(GlobalConst.USB_PORT, GlobalConst.PORT);
			serialPort.setSerialPortParams(GlobalConst.BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			this.buffReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		} 
		catch (UnsupportedCommOperationException ex) {
			System.err.println(ex.getMessage());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(PortInUseException e) {
			System.err.println("Port already in use: " + e);
			return false;
		}
		return true;
	}

	public BufferedReader getBuffReader() {
		return buffReader;
	}
}