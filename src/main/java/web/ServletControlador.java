
package web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dominio.Cliente;
import java.util.List;
import datos.ClienteDaoJDBC;
import java.io.IOException;
import javax.servlet.ServletException;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet
{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("entre aqui");
        List<Cliente> clientes = new ClienteDaoJDBC().listar();
        System.out.println("Clientes = " + clientes);
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("clientes.jsp").forward(request, response);              
    }
}
