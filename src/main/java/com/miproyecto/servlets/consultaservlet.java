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
import java.util.stream.Collectors;


@WebServlet(name = "ConsultaServlet", urlPatterns = {"/consulta"})
public class consultaservlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        
        HttpSession session = request.getSession(false);
        
       
        List<usuario> listaUsuarios = new ArrayList<>();
        
        
        if(session != null) {
            Object obj = session.getAttribute("listaUsuarios");
            if(obj != null && obj instanceof List) {
                listaUsuarios = (List<usuario>) obj;
            }
        }
        
        
        String buscarEmail = request.getParameter("buscarEmail");
        List<usuario> resultados;
        
        if(buscarEmail != null && !buscarEmail.trim().isEmpty()) {
            
            resultados = listaUsuarios.stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().toLowerCase().contains(buscarEmail.toLowerCase()))
                    .collect(Collectors.toList());
            
            
            if(resultados.isEmpty()) {
                request.setAttribute("mensaje", "No se encontraron usuarios con ese email");
            } else {
                request.setAttribute("mensaje", "Se encontraron " + resultados.size() + " resultado(s)");
                request.setAttribute("busqueda", true);
            }
        } else {
            
            resultados = listaUsuarios;
        }
        
         
        request.setAttribute("usuarios", resultados);
        
        
        System.out.println("Número de usuarios encontrados: " + resultados.size());
        for (usuario u : resultados) {
            System.out.println("Usuario: " + u.getNombre() + " " + u.getApellido() + " - " + u.getEmail());
        }
        
        
        request.getRequestDispatcher("/consulta.jsp").forward(request, response);
    }
}
