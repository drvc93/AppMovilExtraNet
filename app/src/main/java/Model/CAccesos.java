package Model;

/**
 * Created by dvillanueva on 24/05/2017.
 */

public class CAccesos  {
    private  int nivel1;
    private  int nivel2;
    private  int nivel3;
    private  int nivel4;
    private  int nivel5;
    private  String  descripcion ;
    private  int nivelGN;

    public CAccesos() {

    }

    public CAccesos(int nivel1, int nivel2, int nivel3, int nivel4, int nivel5, String descripcion, int nivelGN) {
        this.nivel1 = nivel1;
        this.nivel2 = nivel2;
        this.nivel3 = nivel3;
        this.nivel4 = nivel4;
        this.nivel5 = nivel5;
        this.descripcion = descripcion;
        this.nivelGN = nivelGN;
    }

    public int getNivel1() {
        return nivel1;
    }

    public void setNivel1(int nivel1) {
        this.nivel1 = nivel1;
    }

    public int getNivel2() {
        return nivel2;
    }

    public void setNivel2(int nivel2) {
        this.nivel2 = nivel2;
    }

    public int getNivel3() {
        return nivel3;
    }

    public void setNivel3(int nivel3) {
        this.nivel3 = nivel3;
    }

    public int getNivel4() {
        return nivel4;
    }

    public void setNivel4(int nivel4) {
        this.nivel4 = nivel4;
    }

    public int getNivel5() {
        return nivel5;
    }

    public void setNivel5(int nivel5) {
        this.nivel5 = nivel5;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNivelGN() {
        return nivelGN;
    }

    public void setNivelGN(int nivelGN) {
        this.nivelGN = nivelGN;
    }
}
