import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class conectaDAO {
    public Connection connectDB() throws SQLException {
        Properties config = new Properties();
        Path arquivo = Paths.get("config.properties");
        if (Files.exists(arquivo)) {
            try (InputStream entrada = Files.newInputStream(arquivo)) {
                config.load(entrada);
            } catch (IOException e) {
                throw new SQLException("Nao foi possivel ler config.properties.", e);
            }
        }
        String url = config.getProperty("db.url",
                "jdbc:mysql://localhost:3306/uc11?connectTimeout=5000&socketTimeout=10000");
        String usuario = config.getProperty("db.user", "root");
        String senha = config.getProperty("db.password", "");
        return DriverManager.getConnection(url, usuario, senha);
    }
}
