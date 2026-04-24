package erik.veiculos.controller;

import erik.veiculos.models.Veiculo;
import erik.veiculos.models.repository.VeiculoRepository;
import erik.veiculos.utills.Utills;


import java.util.List;

public class VeiculoController {

    public static boolean salvarOuAtualizar(Veiculo carroOriginal, String nome, String cor, String anoStr, String modelo, String chassi, String placa, boolean unicoDono )  {
        int anoParser;
        try{
            anoParser = Integer.parseInt( anoStr );
        }catch( NumberFormatException nfe ){
            throw new IllegalArgumentException( "Digite um ano corretamente!" + nfe.getMessage() );
        }

        if( !Utills.ehChassi( chassi ) ){
            throw new IllegalArgumentException( "Chassi inválido ou com menos de 17 caracteres!" );
        }

        if( !Utills.ehPlaca( placa ) ){
            throw new IllegalArgumentException( "Placa inválida ou com menos de 7 caracteres!" );
        }
        Veiculo carro = new Veiculo();

        carro.setNome( nome );
        carro.setCor( cor );
        carro.setAno( anoParser );
        carro.setModelo( modelo );
        carro.setNumeroChassi( chassi );
        carro.setPlaca ( placa );
        carro.setUnicoDono( unicoDono );

        boolean sucesso;

        if( carroOriginal != null ){
            carro.setId( carroOriginal.getId() );
            sucesso = VeiculoRepository.update( carro );
        }else{
            try {
                sucesso = VeiculoRepository.salvar(carro);


                if (sucesso) {
                    Utills.salvarArquivo(carro);
                }
            }catch( IllegalArgumentException e ){
                throw new RuntimeException( e.getMessage() );
            }
        }
        return sucesso;
    }

    public static List<Veiculo> buscarTodos( int offSet ){
        return VeiculoRepository.pesquisarGeral( offSet );
    }

    public static List<Veiculo> buscarComFiltro( String coluna, String valorDigitado, Utills.FiltroDono unicoDono, int offSet ){
        return VeiculoRepository.pesquisarFiltro( coluna, valorDigitado, unicoDono, offSet );
    }

    public static boolean excluirVeiculo( int id  ){
        return VeiculoRepository.delete( id );
    }
}
