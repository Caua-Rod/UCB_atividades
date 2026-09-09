public abstract class Ave extends Animal {
    private boolean voa;

    public Ave(String nome, double peso, int idade, boolean voa) {
        super(nome, peso, idade);
        this.voa = voa;
    }

    public boolean isVoa() {
        return voa;
    }

    public void setVoa(boolean voa) {
        this.voa = voa;
    }

    public void colocarOvo() {
        System.out.println(getNome() + " colocou um ovo.");
    }
}