package com.test.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface HelloRmiService extends Remote {
	
	public String echo(String msg )throws RemoteException;
	
	public Date getTime() throws RemoteException ;
	
}
