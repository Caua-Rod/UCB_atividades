public abstract class Animal {
    private String nome;
    private double peso;
    private int idade;

    public Animal(String nome, double peso, int idade) {
        this.nome = nome;
        this.peso = peso;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public abstract void emitirSom();

    public abstract void mover();

    public String getDadosBasicos() {
        return "Nome: " + nome + " | Peso: " + peso + "kg | Idade: " + idade + " anos";
    }
}