package com.github.davidmoten.pn;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "create", urlPatterns = { "create" })
public class CreateServlet extends HttpServlet {

    private static final long serialVersionUID = -1313656605680750802L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JsonNode tree = new ObjectMapper().readTree(req.getReader());
        String id = tree.get("id").asText();
        String value = tree.get("encryptedValue").asText();
        Store.INSTANCE.put(id, value);
    }
}
