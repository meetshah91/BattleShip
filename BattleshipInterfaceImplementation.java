/*
 * BattleshipInterfaceImplementation.java
 *
 * Version:
 *     $1.0$
 *
 * Revisions:
 *     $Log$
 */


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This program implements the methods in BattleshipInterface 
 * to play the Two player Battleship game
 * 
 *
 * @author      Meet Shah
 */


public class BattleshipInterfaceImplementation extends UnicastRemoteObject implements BattleshipInterface {

	//Array to display the ocean

	public String[][] displayOcean;

	//Check whether game is finished 

	public boolean isGameFinished;

	//Check whether client is disconnected

	public boolean isClientDisconnected;

	//X coordinate of hit

	public int xcoordinate = -1; 

	//Y coordinate of hit

	public int ycoordinate = -1;

	//Check whether coordinate is sent

	public boolean isCoordinateSent = false;

	//Check whether ocean is updated

	public boolean isOceanUpdated = false;

	/**
	 * Initialize BattleshipInterfaceImplementation
	 * 
	 * @throws RemoteException
	 */

	public BattleshipInterfaceImplementation() throws RemoteException {
		super();
	}

	/**
	 * Display ocean after a hit 
	 *
	 *
	 * @return   
	 */

	public String toString() {
		return "";
	}

	/**
	 * Reads the ocean sent by the other player after the hit
	 * 
	 * @return		String Array		Ocean to be displayed
	 */
	@Override
	public String[][] readOcean() throws RemoteException {
		while(!isOceanUpdated) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		isOceanUpdated = false;
		return displayOcean;

	}

	/**
	 * Sends the ocean after hit to other player
	 * 
	 * @param		ocean				String Array of ocean to be displayed
	 * 
	 * @param		isGameFinished		Checks whether game is finished
	 * 
	 */
	@Override
	public void sendOcean(String[][] ocean, boolean isGameFinished) throws RemoteException {
		this.displayOcean = ocean;
		this.isGameFinished = isGameFinished;	
		isOceanUpdated = true;
		this.isGameFinished = isGameFinished;
	}

	/**
	 * Send the coordinates to the other player
	 * 
	 * @param		xcoordinate			X coordinate of hit
	 * 
	 * @param		ycoordinate			Y coordinate of hit
	 * 
	 */
	@Override
	public void sendCoordinates(int xcoordinate, int ycoordinate) throws RemoteException {
		this.xcoordinate = xcoordinate;
		this.ycoordinate = ycoordinate;
		isCoordinateSent = true;
	}

	/**
	 * Read the coordinates sent by other players
	 * 
	 * @return		String Array		X and Y coordinate
	 * 
	 */
	@Override
	public String[] readCoordinates() throws RemoteException {
		String[] coordinate = new String[2];		
		while(!isCoordinateSent) {			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		coordinate[0] = Integer.toString(xcoordinate);
		coordinate[1] = Integer.toString(ycoordinate);
		isCoordinateSent = false;
		return coordinate;
	}

	/**
	 * Returns whether the game is finished
	 * 
	 * @return		true/false		Returns whether the game is finished
	 */
	@Override
	public boolean isGameFinished() throws RemoteException {
		return isGameFinished;
	}

	/**
	 * Returns whether the client is disconnected
	 * 
	 * @return		true/false		Returns whether the client is disconnected
	 * 
	 */
	@Override
	public boolean isClientDisconnected() {
		return isClientDisconnected;
	}

	/**
	 * Disconnects the client once the game is completed
	 * 
	 * @param		isClientDisconnected			Sets whether the client is disconnected
	 *  
	 */
	@Override
	public void setClientDisconnected(boolean isClientDisconnected) {
		this.isClientDisconnected = isClientDisconnected;
	}
}
