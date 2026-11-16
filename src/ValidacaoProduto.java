public final class ValidacaoProduto {
    private ValidacaoProduto() { }
    public static ProdutosDTO criar(String nome, String valor) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do produto.");
        }
        int numero;
        try {
            numero = Integer.parseInt(valor == null ? "" : valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Informe um valor inteiro positivo, sem centavos.");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        ProdutosDTO produto = new ProdutosDTO();
        produto.setNome(nome.trim());
        produto.setValor(numero);
        produto.setStatus("A Venda");
        return produto;
    }
}
