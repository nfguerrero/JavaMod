package edu.iastate.cs228.hw2;


public class FileConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	//TODO: implement appropriate message, etc. 
	
	public FileConfigurationException() {
		this.message = "The File is not configured properly to be sorted";
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}