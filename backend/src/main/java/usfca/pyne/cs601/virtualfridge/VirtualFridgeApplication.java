package usfca.pyne.cs601.virtualfridge;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VirtualFridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualFridgeApplication.class, args);
	}

}
