package Servlet;

import utils.Queries;
//import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("user");
        String password = request.getParameter("password");

        String passwordnotencrypted = password;
        String encryptedpassword = passwordnotencrypted;

        Queries co = new Queries();
        String user_id = co.authentication(username, encryptedpassword);
        if (user_id!= null) {
            System.out.println("logged in user with id: "+user_id);
            HttpSession objsession = request.getSession(true);
            objsession.setAttribute("username", username);
            objsession.setAttribute("user_id",user_id);
            response.sendRedirect("menu.html");
        } else
            response.sendRedirect("error.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HLa");
    }
}
