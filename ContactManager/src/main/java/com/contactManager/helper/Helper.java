package com.contactManager.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.entities.Contact;

public class Helper {
	
	final private String profilePath = "static/img/profile/";
	final private String imageFormat = ".png";
	
	public boolean deleteProfileImage(String filename) {
		
		if(filename.equals("default.png")) {
			return false;
		}
			
		try {
			
			File saveFile = new ClassPathResource(this.profilePath).getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + filename);			
			Files.delete(path);
			return true;
			
		} catch (IOException e) {			
			e.printStackTrace();
			
		}		
		
		return false;
	}
	
	public boolean saveProfileImage(String filename , MultipartFile image) {
		
		try {
			
			filename = filename + ".png";
			System.out.println(filename);
			File saveFile = new ClassPathResource("static/img/profile").getFile();			
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + filename);			
			Files.copy(image.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
			return true;
			
		} catch (IOException e) {			
			e.printStackTrace();
			
		}		
		
		return false;
	}
}
