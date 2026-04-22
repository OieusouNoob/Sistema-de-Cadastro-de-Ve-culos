package erik.veiculos.view;


import erik.veiculos.view.options.pesquisar.Pesquisar;
import erik.veiculos.utills.Utills;
// JavaFX

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;
import atlantafx.base.theme.*; // Essa pode carregar tudo, haha.


public class TelaPrincipal extends Application {


    @Override
    public void start(Stage stage) {
        //Vou usar o AtlantaFX para poder deixar 'bonito'
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // Criação da tela
        BorderPane telaPrincipal = new BorderPane();

        Scene cena = new Scene( new Pesquisar().telaPesquisar( telaPrincipal ), Utills.sizeScreen().width / 1.5f, Utills.sizeScreen().getHeight() / 1.5f);
        stage.setTitle("Sistema de Veículos");
        stage.setScene( cena );
        stage.show();
    }


}
