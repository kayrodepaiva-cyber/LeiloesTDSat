import javax.swing.JOptionPane;

public class TesteVenda {
    public static void main(String[] args) throws Exception {
        String entrada = JOptionPane.showInputDialog(
                "Informe o ID do produto Teste venda Atividade 3:");

        if (entrada == null) {
            return;
        }

        int id = Integer.parseInt(entrada.trim());
        ProdutosDAO dao = new ProdutosDAO();

        boolean primeiraVenda = dao.venderProduto(id);
        boolean segundaVenda = dao.venderProduto(id);

        System.out.println("Primeira venda: " + primeiraVenda);
        System.out.println("Tentativa de vender novamente: " + segundaVenda);
    }
}