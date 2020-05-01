package com.github.davidmoten.otl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateServlet extends HttpServlet {

    private static final long serialVersionUID = -1313656605680750802L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JsonNode tree = new ObjectMapper().readTree(req.getReader());
        String id = tree.get("id").asText();
        String value = tree.get("encryptedValue").asText();
        Stores.instance().put(id, value, Long.MAX_VALUE);
    }
}
