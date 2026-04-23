package erik.veiculos.models.repository;

import erik.veiculos.models.Veiculo;

import java.sql.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class VeiculoRepository {

    private VeiculoRepository() {
        throw new IllegalArgumentException("Está classe não pode ser instânciada! ");
    }

    private final static String INS = "INSERT INTO veiculos ( nome, cor, ano, modelo, numero_chassi, placa, unicodono ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
    private final static String SEL = "SELECT * FROM veiculos ORDER BY veiculos.id ASC LIMIT 15 OFFSET ?";
    private final static String UPT = "UPDATE veiculos SET nome = ?, cor = ?, ano = ?, modelo = ?, numero_chassi = ?, placa = ?, unicodono = ? WHERE id = ?";
    private final static String DEL = "DELETE FROM veiculos WHERE id = ?";

    public static boolean salvar(Veiculo car)  {
        if (car == null) {
            throw new IllegalArgumentException("Error ao salvar os dados do carro!");
        }

        try (Connection conectar = ConexaoBancoDados.conexao() ) {
            PreparedStatement preparar = conectar.prepareStatement(INS);

            preparar.setString(1, car.getNome());
            preparar.setString(2, car.getCor());
            preparar.setInt(3, car.getAno());
            preparar.setString(4, car.getModelo());
            preparar.setString(5, car.getNumeroChassi());
            preparar.setString(6, car.getPlaca());
            preparar.setBoolean(7, car.isUnicoDono());

            int linhas = preparar.executeUpdate();

            return linhas > 0;


        }catch( SQLException e) {

            if( "23505".equals(e.getSQLState() ) ){
                throw new IllegalArgumentException("Erro! Este veículo já foi cadastrado no sistema!");
            }

            throw new IllegalArgumentException("Erro! Verifique se nenhum dos campos obrigatórios esta vazio!");

        }
    }

    public static List<Veiculo> pesquisarGeral( Integer offSet ) {

        try (Connection connection = ConexaoBancoDados.conexao()) {
            PreparedStatement temp;

            try{
                temp = connection.prepareStatement(SEL);
            } catch (SQLException e) {
                throw new RuntimeException("Falha interna: Erro ao tentar montar pesquisa!");

            }


            temp.setInt(1, offSet);

            List<Veiculo> car = new ArrayList<>();
            try (ResultSet resultadoPesquisa = temp.executeQuery()) {

                while (resultadoPesquisa.next()) {
                    Veiculo tempCar = new Veiculo();
                    tempCar.setId( resultadoPesquisa.getInt( "id" ) );
                    tempCar.setNome( resultadoPesquisa.getString( "nome" ) );
                    tempCar.setAno( resultadoPesquisa.getInt( "ano" ) );
                    tempCar.setCor( resultadoPesquisa.getString( "cor" ) );
                    tempCar.setModelo( resultadoPesquisa.getString( "modelo" ) );
                    tempCar.setNumeroChassi( resultadoPesquisa.getString( "numero_chassi" ) );
                    tempCar.setPlaca( resultadoPesquisa.getString( "placa" ));
                    tempCar.setUnicoDono( resultadoPesquisa.getBoolean( "unicodono" ) );

                    car.add(tempCar);

                }

            }

            return car;

        } catch (SQLException e) {
            System.out.println("Error ao pesquisar os dados de um carro! " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static List<Veiculo> pesquisarFiltro(String coluna, String valorDigitado, Boolean unicoDono, Integer offSet) {
        if (offSet < 0) {
            throw new RuntimeException("Pulo inválido!");
        }

        try (Connection con = ConexaoBancoDados.conexao()) {


            StringBuilder tipoSelect = new StringBuilder("SELECT * FROM veiculos WHERE 1=1 ");

            boolean temTextoNaBusca = valorDigitado != null && !valorDigitado.trim().isEmpty();
            boolean ehTexto = !coluna.equals("id") && !coluna.equals("ano");

            // Esse aqui foi gerado por IA, a minha versão ficou 'muito' perigosa, eu mesmo consegui 'bugar' ela haha
            if (temTextoNaBusca) {
                if (ehTexto) {
                    tipoSelect.append("AND ").append(coluna).append(" ILIKE ? ");
                } else {
                    tipoSelect.append("AND ").append(coluna).append(" >= ? ");
                }
            }

            // 2. Se ele escolheu "Sim" ou "Não" no combo, adiciona o filtro de dono
            if (unicoDono != null) {
                tipoSelect.append("AND unicodono = ? ");
            }

            if( coluna.equals( "ano" ) ) {
               tipoSelect.append("ORDER BY ano ASC LIMIT 15 OFFSET ?");

            }else {
                tipoSelect.append("ORDER BY id ASC LIMIT 15 OFFSET ?");

            }

            PreparedStatement p;
            try{
                p = con.prepareStatement( tipoSelect.toString() );

            }catch( NullPointerException e){
                throw new RuntimeException("Falha interna: Erro ao tentar montar uma pesquisa!");
            }

            int index = 1; //Antes eu fazai tudo na mão, usando uma variável como contadora foi bem mais simples

            if (temTextoNaBusca) {
                if (ehTexto) {
                    p.setString(index++, valorDigitado + "%");
                } else {
                    p.setInt(index++, Integer.parseInt(valorDigitado));
                }
            }

            if (unicoDono != null) { // Eu tenho que pensar em como fazer isso melhor, verifiquei duas vezes isso...
                p.setBoolean(index++, unicoDono);
            }

            // O offset é sempre o último!
            p.setInt(index, offSet);

            System.out.println("Query Executada: " + p); // Apenas para ver no terminal. Isso pode ser um comentário

            List<Veiculo> car = new ArrayList<>();
            try (ResultSet resultadoFiltro = p.executeQuery()) {
                while (resultadoFiltro.next()) {
                    Veiculo tempCar = new Veiculo();
                    tempCar.setId(resultadoFiltro.getInt("id"));
                    tempCar.setNome(resultadoFiltro.getString("nome"));
                    tempCar.setAno(resultadoFiltro.getInt("ano"));
                    tempCar.setCor(resultadoFiltro.getString("cor"));
                    tempCar.setModelo(resultadoFiltro.getString("modelo"));
                    tempCar.setNumeroChassi(resultadoFiltro.getString("numero_chassi"));
                    tempCar.setPlaca(resultadoFiltro.getString("placa"));
                    tempCar.setUnicoDono(resultadoFiltro.getBoolean("unicodono"));
                    car.add(tempCar);
                    /*Se ocorrer um BufferOverFlow aqui é erro do desenvolvedor.
                    Talvez eu mesmo errei e ainda não encontrei erro, haha.*/
                }
            }catch(SQLException e){
                throw new RuntimeException("Falha interna: Erro ao executar pesquisa");
            }
            return car;

        } catch (SQLException e) {
            throw new RuntimeException("Falha interna: Erro ao conectar ao banco de dados ");
        }
    }

    public static boolean update(Veiculo car) {
        if (car == null) {
            throw new IllegalArgumentException("Argumentos inválidos para update");
        }

        try( Connection conn = ConexaoBancoDados.conexao() ){
            PreparedStatement preparar = conn.prepareStatement( UPT );
            //Veja depois eu adiciono uma variável contadora aqui também
            preparar.setString( 1, car.getNome() );
            preparar.setString( 2, car.getCor() );
            preparar.setInt( 3, car.getAno() );
            preparar.setString( 4, car.getModelo() );
            preparar.setString( 5, car.getNumeroChassi() );
            preparar.setString( 6, car.getPlaca() );
            preparar.setBoolean( 7, car.isUnicoDono() );

            preparar.setInt(8, car.getId() );

            return preparar.executeUpdate( ) > 0;

        }catch (SQLException e){
            throw new RuntimeException("Falha interna: Erro ao atualizar dados de um véiculo!");
        }catch ( RuntimeException e){
            throw new RuntimeException("Falha interna: Erro ao tentar estabelecer uma conexão com o banco de dados");
        }

    }

    public static boolean delete( int id ){
       
        if( id <= 0){ // Nunca se sabe o que se passa na mente de um progamador(a) de madrugada
            throw new RuntimeException("Falha interna: Erro ao tentar excluir um veículo");
        }

        try( Connection conn = ConexaoBancoDados.conexao() ){

            assert conn != null;
            PreparedStatement querie = conn.prepareStatement( DEL );

            querie.setInt( 1, id );

            return querie.executeUpdate() > 0;

        }catch( SQLException e ){
            throw new RuntimeException( "Falha ao apagar um véiculo!" );
        }

    }
}
