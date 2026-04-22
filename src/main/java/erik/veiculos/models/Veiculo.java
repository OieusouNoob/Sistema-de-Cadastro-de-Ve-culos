package erik.veiculos.models;


public class Veiculo {
    private int id;
    private String nome;
    private String cor;
    private int ano;
    private String modelo;
    private String numeroChassi;
    private String placa;
    private boolean unicoDono;


    public Veiculo(){}

    public Veiculo(int id, String nome, String cor, int ano, String modelo, String numeroChassi, String placa, boolean unicoDono){
        this.id = id;
        this.nome = nome;
        this.cor = cor;
        this.ano = ano;
        this.modelo = modelo;
        this.numeroChassi = numeroChassi;
        this.placa = placa;
        this.unicoDono = unicoDono;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public void setAno(int ano){
        this.ano = ano;
    }
    public void setModelo(String modelo){
        this.modelo = modelo;
    }
    public void setNumeroChassi(String nc){
        this.numeroChassi = nc;
    }
    public void setPlaca(String placa){
        this.placa = placa;
    }
    public void setUnicoDono(boolean ud){
        this.unicoDono = ud;
    }

    //Getters
    public int getId(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
    public String getCor() {
        return this.cor;
    }
    public int getAno(){
        return this.ano;
    }
    public String getModelo(){
        return this.modelo;
    }
    public String getNumeroChassi() {
        return numeroChassi;
    }
    public String getPlaca() {
        return placa;
    }
    public boolean isUnicoDono() {
        return unicoDono;
    }

    @Override
    public String toString(){
        return "Veiculo {" +
                "id = " + id +
                ", nome = " +  nome + '\'' +
                ", placa = " + placa + '\'' +
                ", chassi = " + numeroChassi + '\''
                + " }";
    }
}
