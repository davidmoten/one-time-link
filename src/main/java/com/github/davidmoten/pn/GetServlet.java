package com.github.davidmoten.pn;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "get", urlPatterns = { "get" })
public class GetServlet extends HttpServlet {

    private static final long serialVersionUID = -5187556310639564365L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(Store.INSTANCE.get(req.getParameter("id")));
    }

}
