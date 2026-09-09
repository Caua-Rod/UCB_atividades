public class Main {

    public static void main(String[] args) {

        Animal[] animais = {
            new Cachorro("Rex", 12.5, 3),
            new Gato("Mimi", 4.2, 2),
            new Tartaruga("Tuca", 1.5, 10),
            new Iguana("Iggy", 2.3, 4),
            new Galinha("Cocota", 1.8, 1),
            new Calopsita("Piu", 0.1, 2),
            new Pardal("Zeca", 0.03, 1),
            new Arara("Bela", 1.2, 5)
        };

        for (Animal a : animais) {
            System.out.println("-------------------------------");
            System.out.println(a.getDadosBasicos());
            System.out.println("Classe: " + a.getClass().getSimpleName());
            a.emitirSom();
            a.mover();
        }
    }
}