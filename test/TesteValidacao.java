public class TesteValidacao {
    private static int total;
    private static void rejeita(String nome, String valor) {
        try {
            ValidacaoProduto.criar(nome, valor);
            throw new AssertionError("Entrada invalida aceita: " + nome + "/" + valor);
        } catch (IllegalArgumentException esperado) {
            total++;
        }
    }
    public static void main(String[] args) {
        rejeita("", "100");
        rejeita("   ", "100");
        rejeita(null, "100");
        rejeita("Produto", "");
        rejeita("Produto", "abc");
        rejeita("Produto", "10,50");
        rejeita("Produto", "10.50");
        rejeita("Produto", "0");
        rejeita("Produto", "-1");
        rejeita("Produto", "2147483648");
        ProdutosDTO produto = ValidacaoProduto.criar("  Console  ", " 1500 ");
        if (!"Console".equals(produto.getNome()) || produto.getValor() != 1500
                || !"A Venda".equals(produto.getStatus())) {
            throw new AssertionError("Produto valido foi convertido incorretamente");
        }
        total++;
        System.out.println("OK: " + total + " cenarios de validacao.");
    }
}
