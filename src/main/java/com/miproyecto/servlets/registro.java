package com.miproyecto.servlets;

import com.miproyecto.modelo.usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class registro extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Simplemente redirigir al formulario de registro
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar caracteres UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Obtener los parámetros del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validación básica
        if(nombre == null || apellido == null || email == null || password == null ||
           nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
           email.trim().isEmpty() || password.trim().isEmpty()) {
            
            request.setAttribute("mensaje", "Todos los campos son obligatorios");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto Usuario
        usuario nuevoUsuario = new usuario(nombre, apellido, email, password);
        
        // Obtener la sesión actual (crear si no existe)
        HttpSession session = request.getSession();
        
        // Obtener lista de usuarios desde la sesión o crear una nueva si no existe
        List<usuario> listaUsuarios = null;
        Object obj = session.getAttribute("listaUsuarios");
        
        if(obj != null && obj instanceof List) {
            listaUsuarios = (List<usuario>) obj;
        } else {
            listaUsuarios = new ArrayList<>();
        }
        
        // Verificar si el email ya existe
        boolean emailExiste = false;
        for (usuario u : listaUsuarios) {
            if (u != null && u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) {
                emailExiste = true;
                break;
            }
        }
        
        if(emailExiste) {
            request.setAttribute("mensaje", "El email ya está registrado");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }
        
        // Agregar el nuevo usuario a la lista
        listaUsuarios.add(nuevoUsuario);
        
        // Actualizar la lista en la sesión
        session.setAttribute("listaUsuarios", listaUsuarios);
        
        // Imprimir en la consola para depuración
        System.out.println("Usuario registrado: " + nuevoUsuario.getNombre() + " " + 
                            nuevoUsuario.getApellido() + " - " + nuevoUsuario.getEmail());
        System.out.println("Total usuarios en sesión: " + listaUsuarios.size());
        
        // Mensaje de éxito
        request.setAttribute("mensaje", "Usuario registrado correctamente");
        request.setAttribute("exito", true);
        
        // Redirigir a la página de registro con el mensaje
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }
}
