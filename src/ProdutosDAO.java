import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {
    public boolean cadastrarProduto(ProdutosDTO produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
        try (Connection conn = new conectaDAO().connectDB();
             PreparedStatement prep = conn.prepareStatement(sql)) {
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            return prep.executeUpdate() == 1;
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() throws SQLException {
        ArrayList<ProdutosDTO> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, valor, status FROM produtos ORDER BY id";
        try (Connection conn = new conectaDAO().connectDB();
             PreparedStatement prep = conn.prepareStatement(sql);
             ResultSet resultado = prep.executeQuery()) {
            while (resultado.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultado.getInt("id"));
                produto.setNome(resultado.getString("nome"));
                produto.setValor(resultado.getInt("valor"));
                produto.setStatus(resultado.getString("status"));
                produtos.add(produto);
            }
        }
        return produtos;
    }
public boolean venderProduto(int id) throws SQLException {
    if (id <= 0) {
        throw new IllegalArgumentException("Informe um ID positivo.");
    }

    String sql = "UPDATE produtos SET status = ? "
            + "WHERE id = ? AND status = ?";

    try (Connection conn = new conectaDAO().connectDB();
         PreparedStatement prep = conn.prepareStatement(sql)) {

        prep.setString(1, "Vendido");
        prep.setInt(2, id);
        prep.setString(3, "A Venda");

        return prep.executeUpdate() == 1;
    }
}public ArrayList<ProdutosDTO> listarProdutosVendidos() throws SQLException {
    ArrayList<ProdutosDTO> produtos = new ArrayList<>();

    String sql = "SELECT id, nome, valor, status FROM produtos "
            + "WHERE status = ? ORDER BY id";

    try (Connection conn = new conectaDAO().connectDB();
         PreparedStatement prep = conn.prepareStatement(sql)) {

        prep.setString(1, "Vendido");

        try (ResultSet resultado = prep.executeQuery()) {
            while (resultado.next()) {
                ProdutosDTO produto = new ProdutosDTO();

                produto.setId(resultado.getInt("id"));
                produto.setNome(resultado.getString("nome"));
                produto.setValor(resultado.getInt("valor"));
                produto.setStatus(resultado.getString("status"));

                produtos.add(produto);
            }
        }
    }

    return produtos;
}}
