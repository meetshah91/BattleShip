/*
 * BattleshipRMIClient.java
 *
 * Version:
 *     $1.0$
 *
 * Revisions:
 *     $Log$
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Two Player Battleship game
 * 
 *
 * @author      Meet Shah
 */

public class BattleshipRMIClient {

	//Height of the Ocean

	int oceanHeight;

	//Width of the Ocean

	int oceanWidth;

	//Total Boats in the ocean

	int totalBoatSpace;

	//Initial ocean view

	String[][] ocean;

	//Current ocean view in game 

	String[][] displayOcean; 


	
	/**
	 * Read file from Scanner and store in ocean Array
	 *
	 * @param    args					Name of File
	 *
	 * @return   
	 */

	private void parseArg( String args ) throws FileNotFoundException {

		Scanner ocean = new Scanner( new File( args ) );
		fillOcean( ocean );
		ocean.close();
	}


	/**
	 * Read position of elements in an ocean and store in Ocean Array
	 *
	 * @param    ocean					Scanner variable
	 *
	 * @return   
	 */

	private void fillOcean( Scanner ocean )	{
		String oceanParam;
		int oceanHeightIndex = -1;
		int oceanWidthIndex = -1;
		while ( ocean.hasNext() ) {
			oceanParam = ocean.next();
			if( oceanParam.equals("width")) {
				this.oceanWidth = ocean.nextInt();
			}
			else if(oceanParam.equals("height")){
				this.oceanHeight = ocean.nextInt();

				/**
				 * Checks if the size of ocean is greater than 0 
				 * and initializing size of ocean
				 * 
				 * 
				 */

				if(this.oceanWidth > 0 && this.oceanHeight > 0) {
					this.ocean = new String[this.oceanHeight][this.oceanWidth];
					this.displayOcean = new String[this.oceanHeight][this.oceanWidth];
					for(String[] s : displayOcean) {
						Arrays.fill(s, ".");
					}
				}
			}
			else {
				if(oceanParam.equals("row")) {
					oceanHeightIndex++;
					oceanWidthIndex = 0;
				}
				else {
					oceanWidthIndex++;
				}
				this.ocean[oceanHeightIndex][oceanWidthIndex] = oceanParam.equals("row") ? ocean.next() : oceanParam;
				totalBoatSpace += this.ocean[oceanHeightIndex][oceanWidthIndex].equals("w") ? 0 : 1;
			}

		}
	}
	
	/**
	 * Validate the input coordinates 
	 *
	 * @param    column					X Coordinate of hit 
	 * 
	 * @param    row					Y Coordinate of hit
	 *
	 * @return   true or false			Checks whether input coordinates are valid
	 */

	private boolean isValidCoordinate(int column, int row) {
		return ( column >= 0 && column < oceanWidth ) && ( row >= 0 && row < oceanHeight); 
	}

	/**
	 * Get the input coordinates to hit 
	 *
	 * @param    oceanScanner					Scanner variable
	 *
	 * @return
	 *    
	 * @throws RemoteException 
	 */

	private void inputCoordinate(Scanner oceanScanner) throws RemoteException {
		int columnCoordinate = -1;
		int rowCoordinate = -1;
		BattleshipInterface obj = null;
		try {
			obj = (BattleshipInterface)Naming.lookup("//localhost/BattleshipServer");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		do {
			while( !isValidCoordinate(columnCoordinate, rowCoordinate) ) {
				System.out.println("column coordinate (0 <= column <" + oceanWidth +"): " );
				columnCoordinate = oceanScanner.nextInt();
				System.out.println("row coordinate (0 <= column <" + oceanHeight +"): " );
				rowCoordinate = oceanScanner.nextInt();
			}
			obj.sendCoordinates(columnCoordinate, rowCoordinate);			
			displayOcean(obj.readOcean());
			System.out.println("HIT");
			if(obj.isGameFinished())
				break;
			String[] coordinate = obj.readCoordinates();
			columnCoordinate = Integer.valueOf(coordinate[0].trim());
			rowCoordinate = Integer.valueOf(coordinate[1].trim());
			battleStrike(columnCoordinate, rowCoordinate);
			if( totalBoatSpace <= 0 ) {
				obj.sendOcean(displayOcean, true);
				break;
			}
			obj.sendOcean(displayOcean, false);

			columnCoordinate = -1;
			rowCoordinate = -1;
		}while(totalBoatSpace > 0);
		obj.setClientDisconnected(true);
	}

	/**
	 * Performs a strike and destroys a ship if it is a hit
	 *
	 * @param    column					X Coordinate of hit 
	 * 
	 * @param    row					Y Coordinate of hit 
	 *
	 * @return   
	 */

	private void battleStrike(int column, int row) {
		boolean isWater = false;
		if(ocean[row][column].equals("w")) {
			isWater = true;
			displayOcean[row][column] = "W";
		}
		for(int rowIndex = 0; rowIndex < ocean.length; rowIndex++) {
			for( int columnIndex = 0; columnIndex < ocean[rowIndex].length; columnIndex++) {
				if(ocean[row][column].equals(ocean[rowIndex][columnIndex]) && !isWater 
						&& !displayOcean[rowIndex][columnIndex].equals("X")) {
					displayOcean[rowIndex][columnIndex] = "X";
					totalBoatSpace--;
				}
			}
		}
	}
	
	/**
	 * Display ocean after a hit 
	 *
	 *
	 * @return   
	 */

	private void displayOcean(String[][] displayOcean) {
		System.out.printf("   ");
		for( int index = 0; index < displayOcean[0].length; index++) {
			System.out.print(index + " ");
		}
		System.out.println();
		for(int rowIndex = 0; rowIndex < displayOcean.length; rowIndex++) {
			System.out.print(rowIndex + ": ");
			for( int columnIndex = 0; columnIndex < displayOcean[rowIndex].length; columnIndex++) {
				System.out.print(displayOcean[rowIndex][columnIndex] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		BattleshipRMIClient client = new BattleshipRMIClient();
		Scanner oceanScanner = new Scanner(System.in);
		System.out.println("% java BattleShip");
		client.parseArg(args[ 0]);
		try {
			client.inputCoordinate(oceanScanner);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
