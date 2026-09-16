package entidades.inimigos;

public class Yeti extends Enemy{
  public Yeti(String nome) {
        super("Yeti", 20, "boss");
    }

    @Override
    public void distribuirAtributos() {
        this.forca = 8;
        this.resistencia = 20;
    }
}