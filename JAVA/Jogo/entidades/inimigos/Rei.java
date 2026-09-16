package entidades.inimigos;

public class Rei extends Enemy{
  public Rei(String nome) {
        super("Rei Carmesin", 100, "boss");
    }

    @Override
    public void distribuirAtributos() {
        this.forca = 10;
        this.resistencia = 100;
    }
}