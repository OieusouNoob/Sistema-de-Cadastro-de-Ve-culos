package erik.veiculos.utills.javafxutils;


import javafx.scene.control.Alert;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JavaFXUI {

    private JavaFXUI() {
        throw new IllegalArgumentException("Esta classe não pode ser instânciada!");
    }


    public static void Alertas(Alert.AlertType tipoDeAlert, String titulo, String conteudo) {
        Alert alerta = new Alert(tipoDeAlert);
        alerta.setTitle(titulo);
        alerta.setContentText(conteudo);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    public static boolean AlertaVazio(TextField... campos) {
        for (TextField i : campos)

            if (i.getText().isEmpty()) {

                Alertas(Alert.AlertType.ERROR, "Campos Vazios", "Digite algo nos campos!");
                return true;

            }

        return false;
    }

    public static boolean AlertaSimNao(String titulo, String conteudo) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);

        alerta.getButtonTypes().clear();
        alerta.getButtonTypes().addAll(btnNao, btnSim);

        alerta.setHeaderText(null);
        alerta.setTitle(titulo);
        alerta.setContentText(conteudo);

        Optional<ButtonType> click = alerta.showAndWait();


        return click.isPresent() && click.get() == btnSim;
    }

    public static TextField criarCampos(String TextoFundo) {
        if (TextoFundo.isEmpty()) {
            throw new IllegalArgumentException("Parâmetros inválidos!");
        }

        TextField campo = new TextField();
        campo.setPromptText(TextoFundo);
        campo.setMaxWidth(200);

        return campo;
    }

    public static void setlimitChars(TextField campo, int len) {

        if (campo == null || len <= 0) {
            throw new IllegalArgumentException("Valores inválidos!");
        }

        campo.setTextFormatter(new TextFormatter<String>(limite -> {
            if (limite.getControlNewText().length() <= len) {
                return limite;
            } else {
                return null;
            }
        }));
    }

    public static ComboBox<Pair<String, String>> opcoesComboBox() {

        ComboBox<Pair<String, String>> barraSuspensa = new ComboBox<>();
        List<Pair<String, String>> temp = new ArrayList<>();

        temp.add(new Pair<>("ID", "id"));
        temp.add(new Pair<>("Nome", "nome"));
        temp.add(new Pair<>("Cor", "cor"));
        temp.add(new Pair<>("Ano", "ano")); //Vou precisar tratar isto depois - ano e único dono
        temp.add(new Pair<>("Modelo", "modelo"));
        temp.add(new Pair<>("Chassi", "numero_chassi"));
        temp.add(new Pair<>("Placa", "placa"));


        barraSuspensa.setConverter(new StringConverter<>() {
            @Override
            public String toString(Pair<String, String> Pair) {
                return Pair == null ? "" : Pair.getKey();
            }

            @Override
            public Pair<String, String> fromString(String s) {
                return null;
            }
        });

        barraSuspensa.getItems().addAll(temp);
        barraSuspensa.getSelectionModel().selectFirst();

        return barraSuspensa;
    }
}
