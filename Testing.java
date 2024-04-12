import java.util.Map;
import java.util.HashMap;

public class Testing {
    public static void main(String[] args) {
        Map<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("x", 20);
        varMap.put("x", 10);
        System.out.println(varMap);
    }
}