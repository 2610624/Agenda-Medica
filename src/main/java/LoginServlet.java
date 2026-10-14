import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.com.psicologia.www.Usuario;
import mx.com.psicologia.www.UsuarioDAO;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // UTF-8
        request.setCharacterEncoding("UTF-8");

        // Obtener datos del formulario
        String usuario
                = request.getParameter("usuario");

        String password
                = request.getParameter("password");

        // Validar que los campos no estén vacíos
        if (usuario == null || usuario.trim().isEmpty()
                || password == null || password.isEmpty()) {

            response.sendRedirect("login.jsp?error=1");
            return;
        }

        // Buscar usuario en la base de datos
        UsuarioDAO dao
                = new UsuarioDAO();

        Usuario usuarioEncontrado
                = dao.buscarPorUsuario(usuario);

        // Verificar usuario
        if (usuarioEncontrado != null) {

            // Verificar que esté activo
            if (!usuarioEncontrado.isActivo()) {

                response.sendRedirect(
                        "login.jsp?error=1"
                );

                return;
            }

            // Verificar contraseña con BCrypt
            boolean passwordCorrecta = false;

            try {
                passwordCorrecta = BCrypt.checkpw(
                        password,
                        usuarioEncontrado.getPassword()
                );
            } catch (IllegalArgumentException e) {
                // El hash almacenado no tiene un formato válido.
                passwordCorrecta = false;
            }

            if (passwordCorrecta) {

                // Crear sesión
                HttpSession sesion
                        = request.getSession();

                sesion.setAttribute(
                        "usuario",
                        usuarioEncontrado.getUsuario()
                );

                sesion.setAttribute(
                        "nombre",
                        usuarioEncontrado.getNombre()
                );

                sesion.setAttribute(
                        "rol",
                        usuarioEncontrado.getRol()
                );

                // Evitar reutilizar sesión anterior
                request.changeSessionId();

                // Entrar al sistema
                response.sendRedirect(
                        "index.jsp"
                );

            } else {

                // Contraseña incorrecta
                response.sendRedirect(
                        "login.jsp?error=1"
                );
            }

        } else {

            // Usuario no encontrado
            response.sendRedirect(
                    "login.jsp?error=1"
            );
        }
    }
}



