package controller;
import java.io.IOException;
import io.IIO;

import model.Agenda;
import classes.Contacto;

/**
 * Menú de la agenda
 * 
 * @since 2022-01-25
 * @author Amadeo
 */
public class Menu {

	/**
	 * Agenda a gestionar
	 */
	private Agenda a;
	
	/**
	 * Constructor
	 * 
	 * @param a
	 * @throws IOException
	 */
	public Menu(Agenda a) throws IOException {
		this.a = a;
		while (menu());
	}
	
	/**
	 * Interfaz de alta
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean alta() throws IOException {
		IIO.println("Usuario ? ");
		String usuario = IIO.readStringNotBlank();
		IIO.println("Nombre ? ");
		String nombre = IIO.readStringNotBlank();
		IIO.println("Tfno ? ");
		String telefono = IIO.readStringNotBlank();
		IIO.println("Edad ? ");
		int edad = IIO.readInt();
		Contacto c = new Contacto(usuario, nombre, telefono, edad);
		return a.create(c);
	}
	
	/**
	 * Interfaz de baja
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean baja() throws IOException {
		IIO.println("Usuario a borrar ? ");
		String usuario = IIO.readStringNotBlank();
		return a.delete(usuario);		
	}

	/**
	 * Interfaz de modificación
	 * No se puede modificar el campo usuario
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean modifica() throws IOException {
		IIO.println("Usuario a modificar ? ");
		String usuario = IIO.readStringNotBlank();
		Contacto c = a.read(usuario);
		IIO.println("Usuario [" + c.getNombre() + "] ? ");
		String nombre = IIO.readString();
		c.setNombre(nombre.isBlank() ? c.getNombre() : nombre);
		IIO.println("Tfno [" + c.getTelefono() + "] ? ");
		String telefono = IIO.readString();
		c.setTelefono(telefono.isBlank() ? c.getTelefono() : telefono);
		IIO.println("Edad [0= " + c.getEdad() + "] ? ");
		int edad = IIO.readInt();
		c.setEdad(edad == 0 ? c.getEdad() : edad);
		return a.update(c);
	}

	/**
	 * Interfaz de consulta
	 * 
	 * @return
	 * @throws IOException
	 */
	private String consulta() throws IOException {
		IIO.println("Usuario a buscar ? ");
		String usuario = IIO.readStringNotBlank();
		return a.read(usuario).toString();		
	}

	/**
	 * Menú de opciones (controlador)
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean menu() throws IOException {		
		IIO.print("Alta|Baja|Modifica|Consulta|Listado|Todo|Salir");
		switch (IIO.readUpperChar()) {
		case 'A':
			if (alta()) {
				IIO.println("Nuevo usuario dado de alta");
			} else {
				IIO.println("No se ha podido dar de alta a un nuevo usuario");
			}
			break;
		case 'B':
			if (baja()) {
				IIO.println("Usuario dado de baja");
			} else {
				IIO.println("No se ha podido dar de baja al usuario");
			}
			break;
		case 'M':
			if (modifica()) {
				IIO.println("Usuario modificado");
			} else {
				IIO.println("No se ha podido modificar al usuario");
			}
			break;
		case 'C':
			String c = consulta();
			if (c == null) {
				IIO.println("Usuario no encontrado");
			} else {
				IIO.println(c);
			}
			break;
		case 'L':
			IIO.println(a.list());
			break;
		case 'T':
			a.listRaw(); // DEBUG: Muestro todo
			break;
		case 'S':
			return false;
		default:
			IIO.println("Seleccione una opcion correcta");
		}
								
		return true;
	}

}
