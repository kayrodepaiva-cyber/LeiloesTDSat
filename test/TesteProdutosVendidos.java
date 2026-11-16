public class TesteProdutosVendidos {
    public static void main(String[] args) throws Exception {
        ProdutosDAO dao = new ProdutosDAO();
        java.util.ArrayList<ProdutosDTO> vendidos =
                dao.listarProdutosVendidos();

        for (ProdutosDTO produto : vendidos) {
            if (!"Vendido".equals(produto.getStatus())) {
                throw new AssertionError(
                        "A consulta retornou um produto não vendido.");
            }

            System.out.println(
                    produto.getId() + " | "
                    + produto.getNome() + " | "
                    + produto.getValor() + " | "
                    + produto.getStatus());
        }

        System.out.println("Total de produtos vendidos: " + vendidos.size());
    }
}