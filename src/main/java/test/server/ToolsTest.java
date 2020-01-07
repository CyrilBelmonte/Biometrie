package test.server;

import server.tools.Tools;


public class ToolsTest {
    public static void main(String[] args) {
        String hash = Tools.hmacMD5("Test", "4");
        System.out.println(hash);
    }
}
