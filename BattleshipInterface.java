/*
 * BattleshipInterface.java
 *
 * Version:
 *     $1.0$
 *
 * Revisions:
 *     $Log$
 */

/**
 * This interface will allow to declare methods
 * 
 *
 * @author      Meet Shah
 */


public interface BattleshipInterface extends java.rmi.Remote {

	/**
	 * Reads the ocean sent by the other player after the hit
	 * 
	 * @return		String Array		Ocean to be displayed
	 */

	String[][] readOcean() throws java.rmi.RemoteException;

	/**
	 * Sends the ocean after hit to other player
	 * 
	 * @param		ocean				String Array of ocean to be displayed
	 * 
	 * @param		isGameFinished		Checks whether game is finished
	 * 
	 */

	void sendOcean(String[][] ocean, boolean isGameFinished) throws java.rmi.RemoteException;

	/**
	 * Send the coordinates to the other player
	 * 
	 * @param		xcoordinate			X coordinate of hit
	 * 
	 * @param		ycoordinate			Y coordinate of hit
	 * 
	 */

	void sendCoordinates(int xcoordinate, int ycoordinate)  throws java.rmi.RemoteException;

	/**
	 * Read the coordinates sent by other players
	 * 
	 * @return		String Array		X and Y coordinate
	 * 
	 */

	String[] readCoordinates()  throws java.rmi.RemoteException;

	/**
	 * Returns whether the game is finished
	 * 
	 * @return		true/false		Returns whether the game is finished
	 */

	boolean isGameFinished()  throws java.rmi.RemoteException;

	/**
	 * Disconnects the client once the game is completed
	 * 
	 * @param		isClientDisconnected			Sets whether the client is disconnected
	 *  
	 */

	void setClientDisconnected(boolean isClientDisconnected)throws java.rmi.RemoteException;

	/**
	 * Returns whether the client is disconnected
	 * 
	 * @return		true/false		Returns whether the client is disconnected
	 * 
	 */

	boolean isClientDisconnected()throws java.rmi.RemoteException;
}
