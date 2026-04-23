package erik.veiculos.view.options.pesquisar;

import erik.veiculos.controller.VeiculoController;
import erik.veiculos.models.Veiculo;

import erik.veiculos.utills.javafxutils.JavaFXUI;

import erik.veiculos.view.options.Salvar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;



public class Pesquisar  {

    private final TableColumn<Veiculo, Integer> colunaId = new TableColumn<>("Id");
    private final TableColumn<Veiculo, String> colunaNome = new TableColumn<>( "Nome" );
    private final TableColumn<Veiculo, String> colunaCor = new TableColumn<>("Cor");
    private final TableColumn<Veiculo, String> colunaAno = new TableColumn<>( "Ano" );
    private final TableColumn<Veiculo, String> colunaModelo = new TableColumn<>( "Modelo" );
    private final TableColumn<Veiculo, String> colunaChassi = new TableColumn<>( "Chassi" );
    private final TableColumn<Veiculo, String> colunaPlaca = new TableColumn<>( "Placa" );
    private final TableColumn<Veiculo, Boolean> colunaUnicoDono = new TableColumn<>( "Dono Único" );


    private final Button btnAlterar;
    private final Button btnExcluir;
    private final Button btnSalvarNovo;
    private final Button btnBuscar;
    private final Button btnAnterior;
    private final Button btnProximo;

    private final TextField campoBusca;

    private final ComboBox<Pair<String, String>> caixaSuspensaFiltro = JavaFXUI.opcoesComboBox();
    private final ComboBox<String> caixaSuspensaUnicoDono = new ComboBox<>();

    private int offSet = 0;

    public Pesquisar(){

        colunaId.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        colunaNome.setCellValueFactory( new PropertyValueFactory<>( "nome" ) );
        colunaCor.setCellValueFactory( new PropertyValueFactory<>( "cor" ) );
        colunaAno.setCellValueFactory( new PropertyValueFactory<>( "ano" ) );
        colunaModelo.setCellValueFactory( new PropertyValueFactory<> ( "modelo" ) );
        colunaChassi.setCellValueFactory( new PropertyValueFactory<>("numeroChassi") );
        colunaPlaca.setCellValueFactory( new PropertyValueFactory<>( "placa" ) );
        colunaUnicoDono.setCellValueFactory( new PropertyValueFactory<>( "unicoDono" ) );

        btnAlterar = new Button( "Alterar" );
        btnExcluir = new Button( " Excluir" );
        btnSalvarNovo = new Button("Salvar/Novo");
        btnBuscar = new Button("Buscar");
        btnAnterior = new Button("Anterior");
        btnProximo = new Button("Próximo");

        campoBusca = new TextField();
        JavaFXUI.setlimitChars( campoBusca, 20 );
        campoBusca.setPromptText("Pesquise algo");

        //Criamos uma caixa suspensa para o utilizador escolher se é único dono
        caixaSuspensaUnicoDono.getItems().addAll( "Ambos", "Sim", "Não" );//Ambos ignora os tipos padrões do boolean
        caixaSuspensaUnicoDono.getSelectionModel().selectFirst(); //Deixamos o primeiro selecionado por padrão


    }


    private void atualizarTabela(ObservableList<Veiculo> dadosTable){

        Pair<String, String> selecionado = caixaSuspensaFiltro.getValue();


        List<Veiculo> car;
        if (offSet >= 0 && campoBusca.getText().isEmpty() ) {
            car = VeiculoController.buscarTodos( offSet );

        }else{
            car = VeiculoController.buscarComFiltro(selecionado.getValue(), campoBusca.getText(), JavaFXUI.donoUnico( caixaSuspensaUnicoDono ), offSet );
        }

        dadosTable.clear(  );
        dadosTable.addAll( car );

        btnProximo.setDisable( car.size() < 15 ); // Verdade? Desabilita!
        btnAnterior.setDisable( offSet <= 0); //Menor por precaução
    }

    public Pane telaPesquisar( BorderPane telaPrincipal ) {

        if( telaPrincipal == null ){
            throw new IllegalArgumentException("Falha na criação da tela!");
        }
        VBox layout = new VBox( 10 );

        layout.setStyle("-fx-padding: 20px;");

        HBox barraBusca = new HBox(30);

        Label textDono = new Label("Único Dono: ");

        barraBusca.getChildren().addAll( campoBusca, btnBuscar, caixaSuspensaFiltro , textDono , caixaSuspensaUnicoDono );

        TableView<Veiculo> tabelaVeiculo = new TableView<>();

        //Para evitar erros de layout, por exemplo...
        /*
        * Fiz uns testes e notei que quando estava em tela cheia a tabela ficava cortada como se fosse ainda
        * uma tela dividida em 1.5
        * */
        VBox.setVgrow( tabelaVeiculo, Priority.ALWAYS );

        tabelaVeiculo.getColumns().addAll(
                colunaId,
                colunaNome,
                colunaCor,
                colunaAno,
                colunaModelo,
                colunaChassi,
                colunaPlaca,
                colunaUnicoDono
            );


        tabelaVeiculo.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS );

        ObservableList< Veiculo > dadosTable = FXCollections.observableArrayList( );

        tabelaVeiculo.setItems( dadosTable );

        List<Veiculo> carTemp = VeiculoController.buscarTodos( 0 );

        if( carTemp.isEmpty() ) {

            JavaFXUI.Alertas(Alert.AlertType.ERROR, "Vazio", "Nenhum veículo encontrado!");

        }

        dadosTable.addAll(carTemp);

        HBox botoes = new HBox(35, btnAnterior, btnSalvarNovo, btnAlterar, btnExcluir, btnProximo );
        botoes.setAlignment( Pos.BOTTOM_CENTER );

        //Vamos desativar os botões até que seja selecionado alguma linha na 'TableView'
        //Pegando o momento da seleção de alguma linha, para permitimos o botão aparecer
        btnAlterar.disableProperty().bind( tabelaVeiculo.getSelectionModel().selectedItemProperty().isNull() );
        btnExcluir.disableProperty().bind( tabelaVeiculo.getSelectionModel().selectedItemProperty().isNull() );
        btnAnterior.setDisable( offSet <= 0 );
        //Vamos desabilitar o botão de 'voltar' caso o utilizador tenha apenas, ou esteja, na primeira página



        btnBuscar.setOnAction( buscar -> {

            if( campoBusca.getText().isEmpty() ){

                if( JavaFXUI.donoUnico( caixaSuspensaUnicoDono ) != null ) {
                    dadosTable.clear();
                    Pair<String, String> selecionado = caixaSuspensaFiltro.getValue();
                    dadosTable.addAll(VeiculoController.buscarComFiltro( selecionado.getValue(), null, JavaFXUI.donoUnico( caixaSuspensaUnicoDono ), offSet) );
                    return;
                }else{
                    dadosTable.clear();
                    //A busca por todos, por padrão joga no começo, então aqui o offSet é desnecessário, pode ser 0 direto
                    dadosTable.addAll( VeiculoController.buscarTodos( 0 ));
                    return;
                }

            }

            Pair<String, String> selecionado = caixaSuspensaFiltro.getValue();
            if ( selecionado != null ){

                Boolean isUnico = JavaFXUI.donoUnico( caixaSuspensaUnicoDono );

                try {
                    List<Veiculo> filtrados = VeiculoController.buscarComFiltro(selecionado.getValue(), campoBusca.getText(), isUnico, offSet);
                    if( !filtrados.isEmpty()){

                        dadosTable.clear();
                        dadosTable.addAll( filtrados );

                    }
                }catch( IllegalArgumentException e){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha ao Pesquisar", e.getMessage() );
                }catch ( RuntimeException e){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha interna", e.getMessage() );
                }

            }
        });

        btnAlterar.setOnAction( alterar -> {

            Veiculo car = tabelaVeiculo.getSelectionModel().getSelectedItem();
            telaPrincipal.setCenter( new Salvar().getFormularioSalvar( telaPrincipal, car ) );

        });

        btnExcluir.setOnAction( excluir -> {
           Veiculo car = tabelaVeiculo.getSelectionModel().getSelectedItem();

           if( JavaFXUI.AlertaSimNao("Excluir Veículo", "Tem certeza que deseja excluir este veículo? Esta operação não poderá ser desfeita!" ) ){
               try{
                   boolean resultado = VeiculoController.excluirVeiculo( car.getId( ) );

                   if( resultado ) {
                       JavaFXUI.Alertas(Alert.AlertType.INFORMATION, "Sucesso", "Veículo excluído com sucesso!");
                       dadosTable.remove(car);
                   }

               }catch(RuntimeException e){
                   JavaFXUI.Alertas( Alert.AlertType.ERROR, "Error", e.getMessage() );
               }
           }

        });

        btnSalvarNovo.setOnAction(salvar -> {
                try{
                    telaPrincipal.setCenter(  new Salvar().getFormularioSalvar( telaPrincipal,null ) );
                    offSet = 0;
                    //Zeramos o offSet para que todas vezes que atualizamos a tela, ela não de erros como
                    // botões ativos estando no limite já
                    atualizarTabela( dadosTable );
                }catch (RuntimeException e){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha na Tela", e.getMessage() );
                }

        });

        btnProximo.setOnAction(avancar -> {
            offSet += 15;
            atualizarTabela( dadosTable );

        });

        btnAnterior.setOnAction(voltar -> {

            if( offSet >= 15){
                offSet -= 15;
                atualizarTabela( dadosTable );
            }
        });


        btnProximo.setAlignment( Pos.BOTTOM_RIGHT );
        btnAnterior.setAlignment( Pos.BOTTOM_LEFT );

        layout.getChildren().addAll(
                barraBusca,
                tabelaVeiculo,
                botoes
        );

        telaPrincipal.setCenter( layout );

        return telaPrincipal;
    }
}
