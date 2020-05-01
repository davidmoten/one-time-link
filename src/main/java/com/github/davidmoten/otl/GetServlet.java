package com.github.davidmoten.otl;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetServlet extends HttpServlet {

    private static final long serialVersionUID = -5187556310639564365L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String value = Stores.instance().get(id);
        if (value == null) {
            resp.setContentType("text/plain");
            resp.setStatus(HttpURLConnection.HTTP_GONE);
            resp.getWriter().write("Value not found for key=" + id);
        } else {
            resp.getWriter().write(value);
        }
    }

}
