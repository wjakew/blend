/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin({"pl.jakubwawak"})
public class BlendApplication {

	public static String version = "v.1.0.0";
	public static String build = "BL24082023REV01";

	public static int globalFileIndex;

	/**
	 * Main application function
	 * @param args
	 */
	public static void main(String[] args) {
		globalFileIndex = 0;
		showHeader();
		SpringApplication.run(BlendApplication.class, args);
	}

	/**
	 * Function for showing header
	 */
	static void showHeader(){
		String header = " _     _                _ \n" +
				"| |__ | | ___ _ __   __| |\n" +
				"| '_ \\| |/ _ \\ '_ \\ / _` |\n" +
				"| |_) | |  __/ | | | (_| |\n" +
				"|_.__/|_|\\___|_| |_|\\__,_|\n";
		header = header + version + "/" + build;
		System.out.println(header);
	}

}
