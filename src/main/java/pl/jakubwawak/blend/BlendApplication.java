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

	public static String version = "v.1.2.1";
	public static String build = "BL16102023REV02";

	public static String hexMainColor = "#6880e7";
	public static String hexSecondaryColor = "#56b6d5";

	/**
	 * Main application function
	 * @param args
	 */
	public static void main(String[] args) {
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
