package controle;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import dominio.Usuario;
import modelo.dao.UsuarioDAO;

@WebServlet("/desbloquearTela")
public class ServletDesbloquearTela extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletDesbloquearTela() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("pass");

		UsuarioDAO dao = new UsuarioDAO();

		List<Usuario> usuarios = dao.listar();

		request.setAttribute(username, password);

		boolean seguir = false;

		for (Usuario user : usuarios) {
			if (user.getNomeUsuario().equals(username)) {
				password = DigestUtils.sha256Hex(password);
				if (user.getSenha().equals(password)) {
					seguir = true;
				}
			}
		}

		HttpSession session = request.getSession();

		if (!seguir) {
			response.sendRedirect("login.jsp");

		} else if ((request.getSession().getAttribute("usuario").equals(username))
				&& (request.getSession().getAttribute("senha").equals(password))) {

			session.setAttribute("username", username);
			String paginaEncaminhada = (String) request.getSession().getAttribute("pagina");
			response.sendRedirect(paginaEncaminhada);

		} else {
			response.sendRedirect("lockScreen.jsp");
		}
	}

}
