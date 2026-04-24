package erik.veiculos.utills;

import erik.veiculos.models.Veiculo;
import java.awt.Toolkit;
import java.awt.Dimension;



public class Utills {

    private Utills() {
        throw new UnsupportedOperationException("Está classe não pode ser instanciada");
    }


    public enum FiltroDono{
        SIM,
        NAO,
        AMBOS
    }


    public static boolean ehChassi(String chassi){
        if (chassi == null) {
            throw new IllegalArgumentException("Digite algo!");
        }

        if( chassi.length() < 17){
            throw new RuntimeException( "Tamanho inválido! Digite um tamanho de chassi corretamente! ");
        }

        //Pela minha experiência diária no setor automativo
        // E pelo que li em um forúm
        // Não é sempre permitido = acho que nem é - chassis iniciarem com 0
        String t1 = "^[A-HJ-NPR-Z0-9]{17}$";
        String t2 = chassi.toUpperCase();
        return t2.matches(t1);
    }

    public static boolean ehPlaca(String placa) {
        if (placa == null) {
            throw new IllegalArgumentException("Digite algo!");
        }

        if( placa.length() < 7 ){
            throw new RuntimeException( "Tamanho inválido! Digite um tamanho de placa corretamente!" );
        }
        String t1 = "^[A-Z]{3}[0-9]([A-Z]|[0-9])[0-9]{2}$";
        String t2 = placa.toUpperCase();

        return t2.matches(t1);
    }

    public static Dimension sizeScreen() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    } //Sei que não é a melhor forma, mas funciona

    public static boolean salvarArquivo( Veiculo car ){
        try (java.io.PrintWriter escrever = new java.io.PrintWriter( new java.io.FileWriter("veiculo.txt", true) )){
            escrever.printf(
                    "Veículo (ID) %d%n Nome: %s%n Cor: %s%n Ano: %d%n Modelo: %s%n Chassi: %s%n Placa: %s%n Único Dono? %s%n-----------------------------------%n",
                    car.getId(),
                    car.getNome(),
                    car.getCor(),
                    car.getAno(),
                    car.getModelo(),
                    car.getNumeroChassi(),
                    car.getPlaca(),
                    car.isUnicoDono() ? "Sim" : "Não"
            );

            System.out.println("Arquivo salvo em: " + new java.io.File("veiculo.txt").getAbsolutePath());

            return true;
        }catch( java.io.IOException e){
            System.out.println("Error ao salvar o arquivo do veículo! ");
            return false;
        }
    }



}

