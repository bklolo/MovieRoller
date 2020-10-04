import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Movies extends Application {
	private int max = 0;
	private int min = 0;
	private int count = 0;
	private int stageWidth = 500;
	private int stageHeight = 750;
	private int rollButtonXLoc = 287;
	private int rollButtonYLoc = 455;
	private int resetButtonXLoc = 147;
	private int resetButtonYLoc = 573;
	private int watchButtonXLoc = 394;
	private int watchButtonYLoc = 496;
	private int saveButtonXLoc = 329;
	private int saveButtonYLoc = 575;
	private int radioButtonsXLoc = stageWidth - 130;
	private int anyRadioYLoc = stageHeight - 410;
	private int filteredRadioYLoc = anyRadioYLoc + 20;
	private int listViewXLoc = 60;
	private int rolledListViewYLoc = 100;
	private int watchingListViewYLoc = rolledListViewYLoc + 100;
	private int watchedListViewYLoc = watchingListViewYLoc + 50;
	private Random rand = new Random();
	private ArrayList<String>  selectedItem = new ArrayList<String>();
	private ArrayList<String> toWatchedList = new ArrayList<String>();
	private ArrayList<String> arrayOfMovies = new ArrayList<String>();				// list of movies to watch
	private ArrayList<String> arrayOfWatched = new ArrayList<String>();				// list of movies watched
	private ArrayList<String> arrayOfMoviesRolled = new ArrayList<String>();		// movies already rolled // use int[] to store indexes instead?
	private ListView<String> rolledLV = new ListView<String>();
	private Label watchedLVLabel = new Label("Movies Watched");
	private Label watchingLVLabel = new Label("Tonight's Movie");
	private Label movieLVLabel = new Label("Rolled Movies");
	private ListView<String> watchedLV = new ListView<String>();
	private ListView<String> watchingLV = new ListView<String>();
	private ToggleButton filteredDuration = new RadioButton("Less than 2hrs");		// able to be selected/deselected (versus radiobutton that cannot)
	
	// TODO writing/deleting intems from Files works, but need to figure out how to replace real files with temps and delete temps
	// TODO button should show shadow or highlight when hovered over, apply css to all buttons
	// TODO dropdown for selecting a genre? maybe autofill based on all genres listed from each item
	//rolled movie todos
	//TODO elseif... check condition watched has been successfully clicked and roll clicked
	//TODO decrease size of lists
	//TODO set radio buttons to buttons?
	//TODO for Watch button, if list has item: movetolist, else: warning
	
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage stage) throws IOException {
		System.out.println(javafx.scene.text.Font.getFamilies());
		FileInputStream inputstream = new FileInputStream("C:\\Users\\Bklolo\\workspace\\MoviePicker\\src\\BMO.jpg"); 
		Image BMO = new Image(inputstream);
		ImageView imageView = new ImageView(BMO);
		imageView.setFitHeight(stageHeight-39);
		imageView.setFitWidth(stageWidth-7);
		imageView.setX(0);
		imageView.setY(0);
//		imageView.setPreserveRatio(true); 
		// Filepath: C:\Users\Bklolo\workspace\MoviePicker
		File movieList = new File("C:\\Users\\Bklolo\\workspace\\MoviePicker\\testmovielist.txt");					// the .txt list of movies
		File watchedMovieList = new File("C:\\Users\\Bklolo\\workspace\\MoviePicker\\testmovieswatched.txt");		// the .txt list of movies watched

		movieList.canRead();
		movieList.canWrite();
		watchedMovieList.canRead();
		watchedMovieList.canWrite();
		
		// obtain MOVIE list
		try {
			Scanner moviesFileReader = new Scanner(movieList);	
			// store movie list in array
			while (moviesFileReader.hasNextLine()) { 															// reads MOVIE list and adds each item to arrayOfMovies
				String data = moviesFileReader.nextLine();
				arrayOfMovies.add(data);
				
			}
			moviesFileReader.close();																		// close scanner
		} catch (FileNotFoundException e) {
			System.out.println("File not found or movieList is empty.");
		}
		
		// obtain WATCHED list
		try {
			Scanner watchedFileReader = new Scanner(watchedMovieList);
			while (watchedFileReader.hasNextLine()) { 															// reads WATCHED movie list and adds each item to arrayOfWatched
				String data = watchedFileReader.nextLine();
				arrayOfWatched.add(data);
			}
			watchedFileReader.close();																		// close scanner
		} catch (FileNotFoundException e) {
			System.out.println("File not found or watchedMovieList is empty.");
		}
		
		max = arrayOfMovies.size();																				// update size of array to match size of movie list
		for(String str : arrayOfWatched){
			watchedLV.getItems().add(str);
		}
		
		 /////// GUI //////
		
		Pane pane = new Pane();
		
		Button roll = new Button("Roll");
		buttonProperties(roll, rollButtonXLoc, rollButtonYLoc, 5, "100","100","100","100","100","700");
		Button reset = new Button("Reset");
		buttonProperties(reset, resetButtonXLoc, resetButtonYLoc, 5, "15","100","25","40","10","700");
		Button watch = new Button("Watch");
		buttonProperties(watch, watchButtonXLoc, watchButtonYLoc, 5, "50","50","50","50","50","700");
		Button save = new Button("Save");
		buttonProperties(save, saveButtonXLoc, saveButtonYLoc, 5, "100","100","100","100","100","700");

		filteredDuration.setLayoutX(radioButtonsXLoc);
		filteredDuration.setLayoutY(filteredRadioYLoc);

//		CheckBox genreFilter = new CheckBox();
//		genreFilter.setLayoutX(350);												// still needs to be implemented; removed from pane until then											
//		genreFilter.setLayoutY(250);
		
		// ListView properties
		listViewProperties(rolledLV, movieLVLabel, 48, 250, listViewXLoc, rolledListViewYLoc);
		listViewProperties(watchingLV, watchingLVLabel, 25, 250, listViewXLoc, watchingListViewYLoc);
		listViewProperties(watchedLV, watchedLVLabel, 48, 250, listViewXLoc, watchedListViewYLoc);
		
		rolledLV.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent event) 
		    {
		    	if(!rolledLV.getSelectionModel().isEmpty())														// if Rolled LV isn't empty
		    	{
		    		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)					// and if left mouse double clicked
			        {
				    	if(watchingLV.getSelectionModel().isEmpty())											// and if Watching LV is empty
				    	{
					    	String qwer = rolledLV.getSelectionModel().getSelectedItem();						// get selected item
					    	selectedItem.add(qwer);																// add it to Selected LV
				    		moveFromToLV(selectedItem, rolledLV, watchingLV);									// remove selected item from movie LV and add to watching LV
				        }
		    		}
		    	}
		    }
		});

		watchingLV.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent event) 
		    {
				for(String asdf : watchingLV.getItems()){
					toWatchedList.add(asdf);
				}
		    	if(selectedItem != null)
		    	{
		    		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)							// if left mouse double clicked
			        {
			    		moveFromToLV(selectedItem, watchingLV, rolledLV);											// remove selected item from movie LV and add to watching LV
			    		selectedItem.clear();
			        }	
		    	}
		    }
		});

		// BUTTON handlers
		roll.setOnAction(new EventHandler<ActionEvent>() {									
			@Override
			public void handle(ActionEvent e) {
				if(watchingLV.getItems().isEmpty()){															// roll only if watchingLV is empty
					RollMovie();
				}
			}
		});
		reset.setOnAction(new EventHandler<ActionEvent>() 
		{									
			@Override
			public void handle(ActionEvent e) {
				if(watchingLV.getItems().isEmpty()){
					rolledLV.getItems().clear();
					count = 0;
				}
			}
		});
		watch.setOnAction(new EventHandler<ActionEvent>() 
		{ 
			@Override
			public void handle(ActionEvent e) {

					if(watchingLV.getItems().isEmpty()){
						Alert alert = new Alert(AlertType.INFORMATION);
						Toolkit.getDefaultToolkit().beep();
						alert.setHeaderText(null);
						alert.setContentText("Select a movie to watch.");
						alert.showAndWait();
					} else{
					for(String asdf : watchingLV.getItems()){
						toWatchedList.add(asdf);
					}
					
					updateArrays(toWatchedList);
					moveFromToLV(toWatchedList, watchingLV, watchedLV);
					
				}
			}
		});
		save.setOnAction(new EventHandler<ActionEvent>() {								
			@Override
			public void handle(ActionEvent e) {
					try {
						saveToFiles(movieList, watchedMovieList);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});

		pane.getChildren().addAll(imageView, roll, reset, watch, save, filteredDuration, movieLVLabel, rolledLV, watchedLV, watchingLV, watchedLVLabel, watchingLVLabel);
		Scene scene = new Scene(pane);
		// reference CSS file
		scene.getStylesheets().add("test.css");
//		scene.setFill(backgroundColor);
		
		stage.setMinWidth(stageWidth + 8);
		stage.setMaxWidth(stageWidth);
		stage.setMinHeight(stageHeight);
		stage.setMaxHeight(stageHeight);
		stage.setTitle("Movie Roller");
		stage.setScene(scene);
		stage.show();
	}

	private void RollMovie() {
		max = arrayOfMovies.size();	
		int selection = rand.nextInt(max - min) + min; 											// random number used to retrieve movie from list
		int firstHour;
		String selectedMovie = arrayOfMovies.get(selection);									// retrieve movie from array with random rolled index
		String selectedMovieTitle = selectedMovie.split("\"")[1]; 								// store only movie title portion of selected movie
		int indexOfEndQuote = selectedMovie.indexOf(selectedMovie.split("\"")[2]); 				// location of second apostrophe in string
		String removedTitle = selectedMovie.substring(indexOfEndQuote + 1);						// remove up to second apostrophe of string
		String removedTitleTrimmed = removedTitle.trim().replaceAll("\\s", "");					// remove all whitespace
		String time = removedTitleTrimmed.substring(0, 5);										// store duration of movie
//		String genre = removedTitleTrimmed.substring(6, removedTitleTrimmed.length());			

		if (count < 2) {
			count += 1;
			if (!arrayOfMoviesRolled.contains(selectedMovieTitle)) {
				if (filteredDuration.isSelected()) 
				{
					if (Character.isDigit(time.charAt(0))) 
					{
						firstHour = Integer.parseInt(time.substring(0, 1));
						if (firstHour < 2) 
						{ 																		// if duration is <2hrs
							rolledLV.getItems().add(selectedMovieTitle); 						// add movie to viewable list
							arrayOfMoviesRolled.add(selectedMovieTitle);						// and add movie to list of rolled movies
						} else {
							Reroll();
						}
					} else {
						firstHour = 0;
						Reroll();
					}
				} else { 																		// else, if filter is not pressed
					rolledLV.getItems().add(selectedMovieTitle);									// add the movie to list
					arrayOfMoviesRolled.add(selectedMovieTitle);								// and to the list of rolled movies
				}
			} else {
				Reroll();
			}
		}
		else{

			
			Alert alert = new Alert(AlertType.INFORMATION);
			Toolkit.getDefaultToolkit().beep();
			alert.setHeaderText(null);
			alert.setContentText("Select a movie.");
			alert.showAndWait();
		}
	}

	private void Reroll() {
		System.out.println("rerolling");
		count -= 1;
		RollMovie();
	}
	
	private void moveFromToLV(ArrayList<String> fromSelectedItem, ListView<String> from, ListView<String> to) {
		String asdf = fromSelectedItem.get(0);
		// remove item from movieList
		from.getItems().remove(asdf);
		// add item to listViewOfWatched listview
		to.getItems().add(asdf);
		
	}
	
	private void updateArrays(ArrayList<String> watchingSelected)
	{
		int index = 0;
		String movie = "";
		for(String item : arrayOfMovies)
		{																// for each string in arrayOfMovies
			String finished = watchingSelected.get(0);
			if(item.contains(finished))
			{															// if 'a' contains "selectedItem"
				index = arrayOfMovies.indexOf(item);					// store the index
				movie = arrayOfMovies.get(index);
				break;
			}
		}
		arrayOfMovies.remove(index);									// use index to remove item from arraylist
		arrayOfWatched.add(movie);										// add movie to watched arraylist
	
	
		for(String st : arrayOfMovies){
			System.out.println(st);
		}
		System.out.println("\n\n\n\n~~~~~~~~~~");
		
		for(String str : arrayOfWatched){
			System.out.println(str);
		}
	
	}
	
	private void listViewProperties(ListView<String> listview, Label label, int maxHeight, int maxWidth, int layoutX, int layoutY){
		listview.setMaxHeight(maxHeight);
		listview.setMaxWidth(maxWidth);
		listview.setLayoutX(layoutX);
		listview.setLayoutY(layoutY);
		listview.setStyle("-fx-font-family: Marlett;" + 
				"-fx-font-size: 12;"
				);
		label.setLayoutX(listview.getLayoutX());
		label.setLayoutY(listview.getLayoutY() - 20);
		label.setStyle("-fx-font-family: Bell MT;" + 
				"-fx-font-size: 12;" +
				"-fx-font-weight: bold;"
				);
	}
	
	private void buttonProperties(Button button, int buttonXLoc, int buttonYLoc, int opacity, String radius,
									String minWidth, String minHeight, String maxWidth, String maxHeight, String fontWeight){
		button.setLayoutX(buttonXLoc);
		button.setLayoutY(buttonYLoc);
		button.setOpacity(opacity);
		button.setStyle("-fx-background-radius: 100px; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +														
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;" +
                "-fx-font-family: Marlett;" + "-fx-font-size: 12;" +
                "-fx-background-color: transparent;"
                );
//        "-fx-effect: innershadow( gaussian, rgba( 0, 0, 0, 0.5 ), 10, 0, 5, 5 );"
	}

	private void saveToFiles(File movieList, File watchedMovieList) throws IOException
	{
		BufferedWriter movieWriter = null;
		BufferedWriter watchedMovieWriter = null;
		File movieTemp = new File("movieTemp.txt");
		File watchedTemp = new File("watchedTemp.txt");
		
		try 
		{
			movieWriter = new BufferedWriter(new FileWriter(movieTemp));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		for(String a : arrayOfMovies)
       	{
    	   try 
    	   {																						
			movieWriter.write(a+"\n");									// write updated movieList array to movieList.txt
    	   } catch (IOException e) 
    	   {
    		   e.printStackTrace();
    	   }
       	}
		movieWriter.close();
		movieList.delete();
		movieTemp.renameTo(movieList);
		
		try 
		{
			watchedMovieWriter = new BufferedWriter(new FileWriter(watchedTemp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String b : arrayOfWatched)
       	{
    	   try 
    	   {																						
    		   watchedMovieWriter.write(b+"\n");									// write updated movieList array to movieList.txt
    	   } catch (IOException e) 
    	   {
    		   e.printStackTrace();
    	   }
       	}
		watchedMovieWriter.close();
		watchedMovieList.delete();
		System.out.println(watchedTemp.renameTo(watchedMovieList));
	}
}