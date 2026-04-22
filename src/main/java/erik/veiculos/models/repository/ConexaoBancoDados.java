package erik.veiculos.models.repository;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

import io.github.cdimascio.dotenv.Dotenv;


public class ConexaoBancoDados{
    //Proibindo instâncias desta classe.
    private ConexaoBancoDados () {}

    private final static Dotenv dotenv = Dotenv.configure()
            .directory("/")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USUARIO = dotenv.get("DB_USER");
    private static final String SENHA = dotenv.get("DB_PASS");


    public static Connection conexao(){
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);


        }catch ( SQLException e){

            throw new RuntimeException("Erro ao tentar estabelecer a conexão com o banco de dados", e);
        }
    }
}