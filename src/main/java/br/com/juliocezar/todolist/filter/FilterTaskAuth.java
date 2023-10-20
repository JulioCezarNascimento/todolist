package br.com.juliocezar.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.juliocezar.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (request.getServletPath().startsWith("/tasks")) {

            System.out.println("Chegou no filtro");

            var authorization = request.getHeader("Authorization").substring("basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authorization);

            var authString = new String(authDecode);

            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {

                response.sendError(401);

            } else {

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {

                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);

                } else {

                    response.sendError(401);
                }

            }
        } else {

            filterChain.doFilter(request, response);

        }

        





    }
    
}
