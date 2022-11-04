package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.List;
import data.User;
import fileIO.FileIO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			ArrayList<List> lists = FileIO.readPersonalListsFromFile();
			User.setLists(lists);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Erro a tentar ler o ficheiro.dat!");
			//e.printStackTrace();
		}
		
		try {
			Image icon = new Image("/teste_icon.png");
			primaryStage.setTitle("ShortTrack");
			primaryStage.getIcons().add(icon);
			primaryStage.setMinWidth(820.0);
			primaryStage.setMinHeight(410.0);
			primaryStage.setMaxWidth(820.0);
			
			Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			
			primaryStage.setOnCloseRequest(event -> {
				
				if(User.getLists() == null)
					System.out.println("null!");
				else
					System.out.println("not null!");
			    
				try {
					FileIO.writePersonalListsToFile(User.getLists());
				} catch (IOException e) {
					System.out.println("Erro a tentar escrever o ficheiro.dat!");
					//e.printStackTrace();
				}
				
			});
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
