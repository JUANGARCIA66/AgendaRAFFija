package model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import classes.Contacto;

/**
 * Agenda
 * 
 * Fichero de registros de longitud fija
 * 
 * Los datos de cada contacto son:
 * usuario String 10,
 * nombre String 100,
 * telefono String 13,
 * edad int
 * 
 * Para almacenar los String Se utiliza writeUTF.
 * 
 * @see Contacto
 * @since 2022-03-15
 * @author Amadeo
 */
public class Agenda {

	/**
	 * Ruta del fichero que almacena la agenda
	 */
	private final String FIL_AGENDA = "./agenda.fijo.dat";

	/**
	 * Marca de registro borrado
	 */
	private final String MRK_DELETED = "#";

	/**
	 * Fichero de datos de la agenda
	 */
	private RandomAccessFile fichero;

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public Agenda() throws IOException {
		fichero = new RandomAccessFile(FIL_AGENDA, "rw");
	}

	/**
	 * Añade un contacto a la agenda
	 * 
	 * @param c
	 * @throws IOException
	 */
	public boolean create(Contacto c) throws IOException {
		long posicion = fichero.length();
		fichero.seek(posicion);
		write(c);
		return true;
	}

	/**
	 * Devuelve el contacto de la agenda que corresponde al usuario
	 * 
	 * @param usuario
	 * @return contacto correspondiente al usuario o null si no existe
	 * @throws IOException
	 */
	public Contacto read(String usuario) throws IOException {
		long posicion = 0L;
		fichero.seek(posicion);
		while (fichero.getFilePointer() < fichero.length()) {
			Contacto c = read();
			if (c != null && c.getUsuario().equals(usuario)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Actualiza un registro
	 * 
	 * @param Contacto
	 * @return true si actualizado
	 * @throws IOException
	 */
	public boolean update(Contacto c) throws IOException {
		long posicion = 0L;
		fichero.seek(posicion);
		while (fichero.getFilePointer() < fichero.length()) {
			posicion = fichero.getFilePointer();
			Contacto r = read();
			if (r != null && r.getUsuario().equals(c.getUsuario())) {
				fichero.seek(posicion);
				write(c);
				return true;
			}
		}
		return false;
	}

	/**
	 * Marca como borrado un registro de la agenda
	 * 
	 * @param usuario
	 * @return true si borrado
	 * @throws IOException
	 */
	public boolean delete(String usuario) throws IOException {
		long posicion = 0L;
		fichero.seek(posicion);
		while (fichero.getFilePointer() < fichero.length()) {
			posicion = fichero.getFilePointer();
			Contacto r = read();
			if (r != null && r.getUsuario().equals(usuario)) {
				fichero.seek(posicion);
				fichero.writeUTF(MRK_DELETED);
				return true;
			}
		}
		return false;
	}

	/**
	 * Escribe un contacto en la agenda
	 * 
	 * @param c
	 * @throws IOException
	 */
	private void write(Contacto c) throws IOException {
		fichero.writeByte(c.getUsuario().length() + c.getNombre().length() + c.getTelefono().length() + 4);
		fichero.writeByte(c.getUsuario().length());
		fichero.writeUTF(c.getUsuario());
		fichero.writeByte(c.getNombre().length());
		fichero.writeUTF(c.getNombre());
		fichero.writeByte(c.getTelefono().length());
		fichero.writeUTF(c.getTelefono());
		fichero.writeInt(c.getEdad());
	}

	/**
	 * Lee un contacto de la agenda
	 * 
	 * @return Contacto o null si está borrado
	 * @throws IOException
	 */
	private Contacto read() throws IOException {
		int len_total = fichero.readByte();
		int len_us = fichero.readByte();
		String us = fichero.readUTF();
		int len_nom = fichero.readByte();
		String num = fichero.readUTF();
		int len_tel = fichero.readByte();
		String tel = fichero.readUTF();
		int edad = fichero.readInt();
		System.out.println(
				"len_total: " + len_total + " len_us: " + len_us + " len_nom: " + len_nom + " len_tel: " + len_tel);
		Contacto c = new Contacto(
				us,
				num,
				tel,
				edad);
		return c.getUsuario().equals(MRK_DELETED) ? null : c;
	}

	/**
	 * Lista todos los contactos activos
	 * 
	 * @return
	 * @throws IOException
	 */
	public String list() throws IOException {
		String out = "";
		long posicion = 0L;
		fichero.seek(posicion);
		while (fichero.getFilePointer() < fichero.length()) {
			Contacto r = read();
			if (r != null) {
				out += r + "\n";
			}
		}
		return out;
	}

	/**
	 * Cierra la agenda
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		fichero.close();
	}

	/**
	 * Añade espacios a un String hasta la longitud indicada
	 * o trunca la información si es necesario
	 * 
	 * @param str
	 * @param length
	 * 
	 *               private String format(String str, int length) {
	 *               str = str.trim();
	 *               str += " ".repeat(Math.max(length - str.length(), 0));
	 *               return str.substring(0, length);
	 *               }
	 */
	/**
	 * DEBUG: Limpia el fichero de datos y añade algunos registros
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		fichero.close();
		File agenda = new File(FIL_AGENDA);
		agenda.delete();
		agenda.createNewFile();
		fichero = new RandomAccessFile(agenda, "rw");

		create(new Contacto("luis", "Luis Rato", "34612345678", 27));
		create(new Contacto("ana", "Ana Mota", "915432145", 34));
	}

	/**
	 * DEBUG: Muestra toda la agenda incluyendo borrados
	 * 
	 * @throws IOException
	 */
	public void listRaw() throws IOException {
		String debug = "";

		fichero.seek(0L);
		for (int i = 0; i < fichero.length(); i++) {
			Contacto c = read();
			debug += "\n" + i + " " + (c == null ? "#borrado#" : c);
		}

		System.err.println("\n[[ DEBUG >> \n" + debug + "\n >> DEBUG ]]\n");
	}

}
