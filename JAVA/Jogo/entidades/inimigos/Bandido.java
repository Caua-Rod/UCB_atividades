package entidades.inimigos;

public class Bandido extends Enemy{
  public Bandido(String nome) {
        super("Bandido", 7, "basico");
    }

    @Override
    public void distribuirAtributos() {
        this.forca = 4;
        this.resistencia = 15;
    }
}