package Model;

/**
 * Created by dvillanueva on 26/06/2017.
 */

public class ItemFact {
    private  String  item;
    private  String  propaganda ;
    private  String   flagcontador ;
    private  int  segundos ;
    private  String descPromo ;



    public ItemFact() {
    }

    public ItemFact(String item, String propaganda, String flagcontador, int segundos , String DescPromo) {
        this.item = item;
        this.propaganda = propaganda;
        this.flagcontador = flagcontador;
        this.segundos = segundos;
        this.descPromo =  DescPromo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPropaganda() {
        return propaganda;
    }

    public void setPropaganda(String propaganda) {
        this.propaganda = propaganda;
    }

    public String getFlagcontador() {
        return flagcontador;
    }

    public void setFlagcontador(String flagcontador) {
        this.flagcontador = flagcontador;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    public String getDescPromo() {
        return descPromo;
    }

    public void setDescPromo(String descPromo) {
        this.descPromo = descPromo;
    }
}
