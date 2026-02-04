package unics.ExecUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString("ok"));
    }
}