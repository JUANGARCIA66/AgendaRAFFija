package main;

import java.io.IOException;

import controller.Menu;
import io.IIO;
import model.Agenda;

/**
 * Agenda de contactos
 * 
 * @since 2022-01-25
 * @author Amadeo
 *
 */
public class Main {

	public static void main(String[] args) {
		try {
			Agenda a = new Agenda();
			// a.init(); // DEBUG
			new Menu(a);
			a.close();
		} catch (IOException e) {
			IIO.println("No se puede acceder a la agenda");
		}
	}
}