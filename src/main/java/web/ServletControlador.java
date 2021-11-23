
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
        String accion = request.getParameter("accion");
        System.out.println("Accion = " + accion);
        
        if(accion != null)
        {
            switch(accion)
            {
                case "editar": this.editarCliente(request, response); break; 
                case "eliminar": this.eliminarCliente(request, response); break;
                default: accionDefault(request, response); break; 
                    
            }
        }
        else 
        {
            accionDefault(request, response);
        }
    }
    
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Cliente> clientes = new ClienteDaoJDBC().listar();
        System.out.println("Clientes = " + clientes);
        HttpSession session = request.getSession();
        session.setAttribute("clientes", clientes);
        session.setAttribute("totalClientes", clientes.size()); 
        session.setAttribute("saldoTotal", calcularSaldoTotal(clientes));
        
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);   
        response.sendRedirect("clientes.jsp");
    }
    
    private double calcularSaldoTotal(List<Cliente> clientes)
    {
        double saldoTotal = 0;
        
        for(Cliente cliente : clientes)
        {
            saldoTotal += cliente.getSaldo();
        }
        
        return saldoTotal;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String accion = request.getParameter("accion");
        System.out.println("Accion = " + accion);
        
        if(accion != null)
        {
            System.out.println("vamos a: " + accion);
            switch(accion)
            {
                case "insertar": this.insertarCliente(request, response); break;
                case "modificar": this.modificarCliente(request, response); break;              
                default: accionDefault(request, response); break; 
                    
            }
        }
        else 
        {
            accionDefault(request, response);
        }
    }
    
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //1. Recuperar datos del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        
        if(saldoString != null && !"".equals(nombre))
        {
            saldo = Double.parseDouble(saldoString);
        }
        
        //2. Creamos el objeto cliente
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        
        //3. Insertamos un nuevo objeto en la bd
        int registrosModificados = new ClienteDaoJDBC().insertar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        //4. Redirigimos hacia la accion por default
        accionDefault(request, response);
    }
    
    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        //1. Recuperar datos del formulario
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        
        if(saldoString != null && !"".equals(nombre))
        {
            saldo = Double.parseDouble(saldoString);
        }
        
        
        //2. Creamos el objeto cliente
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        System.out.println("Nuevos datos: " + cliente);
        //3. Actualizar un nuevo objeto en la bd
        int registrosModificados = new ClienteDaoJDBC().actualizar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        //4. Redirigimos hacia la accion por default
        accionDefault(request, response);
    }
    
    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        
        System.out.println("EDICION DE: " + cliente);
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);      
    }
    
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        //1. Recuperar datos del formulario
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));

        //2. Creamos el objeto cliente
        Cliente cliente = new Cliente(idCliente);
       
        //3. Eliminamos el objeto en la bd
        int registrosModificados = new ClienteDaoJDBC().eliminar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        //4. Redirigimos hacia la accion por default
        accionDefault(request, response);
    }
}
