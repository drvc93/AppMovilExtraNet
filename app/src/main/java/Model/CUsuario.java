package Model;

/**
 * Created by dvillanueva on 24/05/2017.
 */

public class CUsuario {

    private  String dni ;
    private  String clave ;
    private  String nombre;
    private  String apellido;
    private  String fechaNacimiento;
    private  String mail;
    private  String telefono ;
    private  String empruc;
    private  String empnom;

    public CUsuario() {
    }

    public CUsuario(String dni, String clave, String nombre, String apellido, String fechaNacimiento, String mail, String telefono, String empruc, String empnom) {
        this.dni = dni;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.mail = mail;
        this.telefono = telefono;
        this.empruc = empruc;
        this.empnom = empnom;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmpruc() {
        return empruc;
    }

    public void setEmpruc(String empruc) {
        this.empruc = empruc;
    }

    public String getEmpnom() {
        return empnom;
    }

    public void setEmpnom(String empnom) {
        this.empnom = empnom;
    }
}
