import java.sql.Connection;

public class PruebaConexion {
    public static void main(String[] args) {
        Connection conexion = Conexion.conectar();

        if (conexion != null) {
            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Conexion exitosa"
            );
            System.out.println(
                    "============================================"
            );

            try {
                conexion.close();
            } catch (Exception e) {

            }
        } else {
            System.out.println(
                    "============================================"
            );
            System.out.println(
                    "Error de conexion"
            );
            System.out.println(
                    "============================================"
            );

        }
    }
}
