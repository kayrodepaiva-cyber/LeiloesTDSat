import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/** Teste do uso de JDBC com driver simulado; nao substitui teste no MySQL. */
public class TesteDAO {
    private static int conexoesFechadas, comandosFechados, resultadosFechados;
    private static int linhasAfetadas = 1;
    private static boolean falhar;
    private static Object[] gravado;
    private static Object proxy(Class<?> tipo, java.lang.reflect.InvocationHandler handler) {
        return Proxy.newProxyInstance(TesteDAO.class.getClassLoader(), new Class<?>[]{tipo}, handler);
    }
    private static void verifica(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }
    public static void main(String[] args) throws Exception {
        DriverManager.registerDriver(new Driver() {
            public boolean acceptsURL(String url) { return url.startsWith("jdbc:mysql:"); }
            public Connection connect(String url, Properties props) throws SQLException {
                if (!acceptsURL(url)) return null;
                if (falhar) throw new SQLException("Falha simulada");
                return (Connection) proxy(Connection.class, (obj, metodo, a) -> {
                    if (metodo.getName().equals("close")) { conexoesFechadas++; return null; }
                    if (metodo.getName().equals("prepareStatement")) {
                        String sql = (String) a[0];
                        Object[] parametros = new Object[3];
                        return proxy(PreparedStatement.class, (p, m, v) -> {
                            if (m.getName().equals("close")) { comandosFechados++; return null; }
                            if (m.getName().startsWith("set")) {
                                parametros[(Integer)v[0]-1]=v[1]; return null;
                            }
                            if (m.getName().equals("executeUpdate")) {
                                verifica(sql.equals("INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)"), "INSERT incorreto");
                                gravado=parametros.clone();return linhasAfetadas;
                            }
                            if (m.getName().equals("executeQuery")) {
                                verifica(sql.equals("SELECT id, nome, valor, status FROM produtos ORDER BY id"), "SELECT incorreto");
                                int[] linha = {-1};
                                Object[][] dados={{2,"PS4",1500,"Vendido"},{5,"PS2",400,"A Venda"}};
                                return proxy(ResultSet.class, (r, rm, rv) -> {
                                    if (rm.getName().equals("close")) { resultadosFechados++;return null; }
                                    if (rm.getName().equals("next")) return ++linha[0]<dados.length;
                                    int col=Arrays.asList("id","nome","valor","status").indexOf(rv[0]);
                                    return dados[linha[0]][col];
                                });
                            }
                            throw new UnsupportedOperationException(m.getName());
                        });
                    }
                    throw new UnsupportedOperationException(metodo.getName());
                });
            }
            public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) { return new DriverPropertyInfo[0]; }
            public int getMajorVersion() { return 1; }
            public int getMinorVersion() { return 0; }
            public boolean jdbcCompliant() { return false; }
            public Logger getParentLogger() { return Logger.getGlobal(); }
        });
        ProdutosDAO dao = new ProdutosDAO();
        verifica(dao.cadastrarProduto(ValidacaoProduto.criar("D'Art", "123")), "INSERT deve confirmar uma linha");
        verifica(Arrays.equals(gravado,new Object[]{"D'Art",123,"A Venda"}),"Parametros incorretos");
        linhasAfetadas=0;
        verifica(!dao.cadastrarProduto(ValidacaoProduto.criar("Teste", "1")), "Zero linhas nao pode indicar sucesso");
        ArrayList<ProdutosDTO> lista = dao.listarProdutos();
        verifica(lista.size()==2 && lista.get(0).getId()==2 && lista.get(1).getValor()==400,"Mapeamento incorreto");
        verifica(dao.listarProdutos().size()==2,"Consulta repetida duplicou linhas");
        verifica(conexoesFechadas==4 && comandosFechados==4 && resultadosFechados==2,"Recursos JDBC nao fechados");
        falhar=true;
        try { dao.cadastrarProduto(ValidacaoProduto.criar("Erro","1")); throw new AssertionError("Erro ocultado"); }
        catch (SQLException esperado) { }
        try { dao.listarProdutos(); throw new AssertionError("Erro de consulta ocultado"); }
        catch (SQLException esperado) { }
        System.out.println("OK: INSERT parametrizado, retorno sem insercao, SELECT, repeticao, fechamento e propagacao de falhas (JDBC simulado).");
    }
}
