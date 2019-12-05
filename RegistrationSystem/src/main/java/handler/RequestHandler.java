package handler;

import data.Database;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class RequestHandler {
    private BufferedReader reader;
    private BufferedWriter writer;

    public RequestHandler(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void handle() {
        System.out.println("Reading request..");
        JSONObject request = readRequest();
        System.out.println("Request recieved");

        if (request == null ||
                !request.has("domain") ||
                !request.has("method") ||
                !request.has("body")) {

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("isSuccess", false);
            errorResponse.put("message", "Could not read request");
            writeResponse(errorResponse);
            return;
        }

        String domain = (String) request.get("domain");
        String method = (String) request.get("method");
        JSONObject body = (JSONObject) request.get("body");

        System.out.println("Domain: " + domain);
        System.out.println("Method: " + method);
        System.out.println("Body: " + body.toString());

        JSONObject response;

        if ("quickAdd".equals(method)) {
            response = Database.quickAdd(
                (String) body.get("studentNo"),
                (String) body.get("courseCode"),
                Integer.parseInt((String) body.get("section"))
            );
        } else if ("getCourses".equals(method)) {
            response = Database.getCourses();
        } else if ("getMyCourses".equals(method)) {
            response = Database.getMyCourses((String) body.get("studentNo"));
        } else if ("dropCourse".equals(method)) {
            response = Database.dropCourse((String) body.get("studentNo"),
                                           (String) body.get("courseCode"),
                                           (Integer) body.get("section"));
        } else if ("login".equals(method)) {
            response = Database.login((String) body.get("studentNo"),
                                      (String) body.get("password"));
        } else {
            response = new JSONObject();
            response.put("isSuccess", false);
            response.put("message", "Invalid request");
        }

        System.out.println("Sending response: " + response.toString());
        writeResponse(response);
        System.out.println("Response sent");
    }

    private JSONObject readRequest() {
        StringBuilder requestString = new StringBuilder();

        try {
            requestString.append(reader.readLine());
        } catch (IOException e) {
            System.out.println("Could not read");
            e.printStackTrace();
            return null;
        }

        System.out.println(requestString.toString());
        JSONObject request = new JSONObject(requestString.toString());
        return request;
    }

    // TODO take isSuccess, message and body as parameter
    private void writeResponse(JSONObject response) {
        try {
            writer.write(response.toString() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
