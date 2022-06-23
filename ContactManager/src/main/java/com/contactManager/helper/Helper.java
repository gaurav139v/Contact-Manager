package com.contactManager.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;

public class Helper {
	
	final private String profilePath = "static/img/profile/";
	
	public boolean deleteProfileImage(String imageName) {
			
		try {
			
			File saveFile = new ClassPathResource(this.profilePath).getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + imageName);			
			Files.delete(path);
			return true;
			
		} catch (IOException e) {			
			e.printStackTrace();
			
		}		
		
		return false;
	}
}
