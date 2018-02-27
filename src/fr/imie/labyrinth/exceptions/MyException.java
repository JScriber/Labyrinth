package fr.imie.labyrinth.exceptions;

public class MyException extends Exception {
	private String errorMessage = "";
	
	public MyException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getError() {
		return this.errorMessage;
	}
}
