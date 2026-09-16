package entidades.inimigos;

public class Goblins extends Enemy{
  public Goblins(String nome) {
        super("Goblin", 5, "basico");
    }

    @Override
    public void distribuirAtributos() {
        this.forca = 2;
        this.resistencia = 8;
    }
}