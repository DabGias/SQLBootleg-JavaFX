package br.com.fiap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {
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

    public void exec() {
        try {
            Connection con = DriverManager.getConnection(txtFieldUrl.getText(), txtFieldUser.getText(), passFieldPass.getText());
            PreparedStatement stm = con.prepareStatement(txtAreaComando.getText());

            if (txtAreaComando.getText().toUpperCase().trim().startsWith("SELECT")) {
                ResultSet result = stm.executeQuery();

                carregarDadosNaTabela(result);

            } else {
                stm.execute();
            }

            con.close();
            alertaConf("✔ O código foi executado com sucesso! ✔");
        } catch(SQLException e) {
            alertaErro("❌ " + e.getMessage() + " ❌");
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

    private void carregarDadosNaTabela(ResultSet result) throws SQLException {
        tbViewConsulta.getColumns().removeAll(tbViewConsulta.getColumns());

        int columnCount = result.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            var columnName = result.getMetaData().getColumnLabel(i);
            TableColumn<ArrayList<String>, String> tbColumn = new TableColumn<>(columnName);
            tbColumn.setCellValueFactory(new CallbackImp(i));
            tbViewConsulta.getColumns().add(tbColumn);
        }

        tbViewConsulta.getItems().clear();

        while (result.next()) {
            var lista = new ArrayList<String>();

            for (int i = 1; i <= columnCount; i++) {
                lista.add(result.getString(i));
            }

            tbViewConsulta.getItems().add(lista);
        }
    }
}
