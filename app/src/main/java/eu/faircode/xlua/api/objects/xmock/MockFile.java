package eu.faircode.xlua.api.objects.xmock;

import java.util.HashMap;

public class MockFile extends MockFileBase {
    public MockFile() { }
    public MockFile(String name, String contents) { super(name, contents); }

    public static class Table {
        public static final String name = "files";
        public static final HashMap<String, String> columns = new HashMap<String, String>() {{
            put("name", "TEXT");
            put("contents", "TEXT");
        }};
    }
}
