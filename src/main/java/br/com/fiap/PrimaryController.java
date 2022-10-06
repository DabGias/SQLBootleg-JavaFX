package br.com.fiap;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {
    @FXML
    private TextField txtFieldUrl;
    @FXML
    private TextField txtFieldUser;
    @FXML
    private PasswordField passFieldPass;
    @FXML
    private TextArea txtAreaComando;

    @FXML
    private TableView<ArrayList<String>> tbViewConsulta;
    @FXML
    private TableColumn<ArrayList<String>, String> columnVarchar;
    @FXML
    private TableColumn<ArrayList<String>, String> columnNumber;

    public void exec() {
        try {
            Connection con = DriverManager.getConnection(txtFieldUrl.getText(), txtFieldUser.getText(), passFieldPass.getText());
            PreparedStatement stm = con.prepareStatement(txtAreaComando.getText());

            if (txtAreaComando.getText().toUpperCase().trim().startsWith("SELECT")) {
                ResultSet result = stm.executeQuery();

                // TODO: Criar model e gerar um select genérico (Retorna todas as colunas de uma tabela)

                // while (result.next()) {
                //     ArrayList<String> array = new ArrayList<String>();
                //     array.add(result.getString("id_teste"));
                //     array.add(result.getString("nome_teste"));
                //     tbViewConsulta.setItems(array);
                // }

            } else {
                stm.execute();
            }

            con.close();
            alertaConf("✔ O código foi executado com sucesso! ✔");
        } catch(SQLException e) {
            int code = e.getErrorCode();

            if (code == 1017) {
                alertaErro("❌ Parâmetros de conexão incorretos ❌");
            }else if (code == 17104) {
                alertaErro("❌ Não há código para ser executado ❌");
            } else if (code == 900) { 
                alertaErro("❌ Existem erros no código ❌");
            } else {
                alertaErro("❌ Algo deu errado ❌");
            }
        } 
    }
    
    public void limpar() {
        txtAreaComando.setText("");
    }

    public void alertaErro(String msg) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setContentText(msg);
        alerta.setTitle("⚠ Erro ⚠");
        alerta.show();
    }

    public void alertaConf(String msg) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setContentText(msg);
        alerta.setTitle("🎉 Sucesso ✔");
        alerta.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //TODO: Arrumar o valor das colunas
        
        // columnNumber.setCellValueFactory(new PropertyValueFactory<>());
        // columnVarchar.setCellValueFactory(new PropertyValueFactory<>());
    }
}
