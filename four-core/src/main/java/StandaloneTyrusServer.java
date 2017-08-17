import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.tyrus.server.Server;

import com.bsisoftware.mhu.four.Four;
import com.bsisoftware.mhu.four.endpoints.EventsEndpoint;

public class StandaloneTyrusServer {

	private static final String PATH = "/four";
	private static final int PORT = 3333;
	
	public static void main(String[] args) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(Server.STATIC_CONTENT_ROOT, "./src/main/webapp");
		
		Server server = new Server("localhost", PORT, PATH + Four.API_BASE, properties, EventsEndpoint.class);
		try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
	}
}
