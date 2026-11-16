import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class vendasVIEW extends JFrame {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Valor", "Status"}, 0) {

        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    private final JTable tabela = new JTable(modelo);
    private final JLabel resumo = new JLabel(" ");

    public vendasVIEW() {
        setTitle("Sistema de Leilões - Vendas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Produtos vendidos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        painel.add(titulo, BorderLayout.NORTH);

        tabela.setRowHeight(24);
        tabela.setAutoCreateRowSorter(true);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(260);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton atualizar = new JButton("Atualizar");
        atualizar.addActionListener(e -> carregarVendas());

        JButton fechar = new JButton("Fechar");
        fechar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(atualizar);
        botoes.add(fechar);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.add(resumo, BorderLayout.WEST);
        rodape.add(botoes, BorderLayout.EAST);
        painel.add(rodape, BorderLayout.SOUTH);

        setContentPane(painel);
        setSize(720, 420);
        setLocationRelativeTo(null);

        carregarVendas();
    }

    private void carregarVendas() {
        modelo.setRowCount(0);

        try {
            for (ProdutosDTO produto :
                    new ProdutosDAO().listarProdutosVendidos()) {

                modelo.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getValor(),
                    produto.getStatus()
                });
            }

            resumo.setText("Total de produtos vendidos: "
                    + modelo.getRowCount());

        } catch (SQLException e) {
            resumo.setText("Não foi possível carregar as vendas.");

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível consultar as vendas. "
                    + "Verifique a conexão com o banco.",
                    "Falha na consulta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new vendasVIEW().setVisible(true);
        });
    }
}