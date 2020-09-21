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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
	private int resetButtonXLoc = 162;
	private int resetButtonYLoc = 610;
	private int watchButtonXLoc = 420;
	private int watchButtonYLoc = 530;
	private int saveButtonXLoc = 340;
	private int saveButtonYLoc = 585;
	private int anyRadioYLoc = 40;
	private int filteredRadioYLoc = anyRadioYLoc + 20;
	private int listViewXLoc = 40;
	private int listViewYLoc = 20;
	private String selectedItem = null;
	private String finishedWatching = null;
	private Random rand = new Random();
	private ArrayList<String> arrayOfMovies = new ArrayList<String>();				// list of movies to watch
	private ArrayList<String> arrayOfWatched = new ArrayList<String>();				// list of movies watched
	private ArrayList<String> arrayOfMoviesRolled = new ArrayList<String>();			// movies already rolled // use int[] to store indexes instead?
	private ListView<String> rolledLV = new ListView<String>();
	private ListView<String> watchedLV = new ListView<String>();
	private ListView<String> watchingLV = new ListView<String>();
	private RadioButton anyDuration = new RadioButton("Any length");
	private RadioButton filteredDuration = new RadioButton("Less than 2hrs");
	
	// TODO writing/deleting intems from Files works, but need to figure out how to replace real files with temps and delete temps
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws IOException {
		
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

		  //////////////////
		 /////// GUI //////
		//////////////////
		
		Pane pane = new Pane();

		Button roll = new Button("Roll");
		roll.setLayoutX(rollButtonXLoc);
		roll.setLayoutY(rollButtonYLoc);
		roll.setMinSize(60, 60);
		roll.setOpacity(5);
		roll.setStyle(
                "-fx-background-radius: 100px; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +															// TODO button should show shadow or highlight when hovered over, apply css to all buttons
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;" +
                "-fx-background-color:transparent;"
        );
		
		Button reset = new Button("Reset");
		reset.setLayoutX(resetButtonXLoc);
		reset.setLayoutY(resetButtonYLoc);
		reset.setMinSize(70, 10);
//		reset.setOpacity(0);
		
		Button watch = new Button("Watch");
		watch.setLayoutX(watchButtonXLoc);
		watch.setLayoutY(watchButtonYLoc);
		watch.setMinSize(30, 30);
//		watch.setOpacity(0);
		
		Button save = new Button("Save");
		save.setLayoutX(saveButtonXLoc);
		save.setLayoutY(saveButtonYLoc);
		save.setMinSize(80, 80);
//		save.setOpacity(0);
		

		ToggleGroup tg = new ToggleGroup();																	// used to detect toggle of radiobuttons
		anyDuration.setToggleGroup(tg);
		anyDuration.setLayoutX(100);
		anyDuration.setLayoutY(anyRadioYLoc);
		anyDuration.setSelected(true);
		filteredDuration.setToggleGroup(tg);
		filteredDuration.setLayoutX(100);
		filteredDuration.setLayoutY(filteredRadioYLoc);
		
		CheckBox genreFilter = new CheckBox();
		genreFilter.setLayoutX(350);																	// TODO dropdown for selecting a genre? maybe autofill based on all genres listed from each item
		genreFilter.setLayoutY(250);
		
		rolledLV.setMaxHeight(48);
		rolledLV.setMaxWidth(250);
		rolledLV.setLayoutX(listViewXLoc);
		rolledLV.setLayoutY(listViewYLoc);
		Label movieLVLabel = new Label("Rolled Movies");
		movieLVLabel.setLayoutX(rolledLV.getLayoutX());
		movieLVLabel.setLayoutY(rolledLV.getLayoutY() - 20);
		
		watchingLV.setMaxHeight(25);
		watchingLV.setMaxWidth(250);
		watchingLV.setLayoutX(listViewXLoc);
		watchingLV.setLayoutY(listViewYLoc + 100);
		Label watchingLVLabel = new Label("Tonight's Movie");
		watchingLVLabel.setLayoutX(watchingLV.getLayoutX());
		watchingLVLabel.setLayoutY(watchingLV.getLayoutY() - 20);
		
		watchedLV.setMaxHeight(200);
		watchedLV.setMaxWidth(250);
		watchedLV.setLayoutX(listViewXLoc);
		watchedLV.setLayoutY(listViewYLoc + 175);
		Label watchedLVLabel = new Label("Movies Watched");
		watchedLVLabel.setLayoutX(watchedLV.getLayoutX());
		watchedLVLabel.setLayoutY(watchedLV.getLayoutY() - 20);
//		watchedLV.getStylesheets().add("C:\\Users\\Bklolo\\workspace\\MoviePicker\\src\\test.css");
		
		for(String str : arrayOfWatched){
			watchedLV.getItems().add(str);
		}
		
		rolledLV.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent event) 
		    {
		    	selectedItem = rolledLV.getSelectionModel().getSelectedItem();						
		    	if(selectedItem != null)
		    	{
		    		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)							// if left mouse double clicked
			        {
			    		moveFromToLV(selectedItem, rolledLV, watchingLV);												// remove selected item from movie LV and add to watching LV
			        }	
		    	}
		    }
		});

		watchingLV.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		    @Override
		    public void handle(MouseEvent event) 
		    {
		    	selectedItem = watchingLV.getSelectionModel().getSelectedItem();						
		    	if(selectedItem != null)
		    	{
		    		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)							// if left mouse double clicked
			        {
			    		moveFromToLV(selectedItem, watchingLV, rolledLV);											// remove selected item from movie LV and add to watching LV
			        }	
		    	}
		    }
		});
		pane.getChildren().addAll(imageView, roll, reset, watch, save, anyDuration, filteredDuration, genreFilter, movieLVLabel, rolledLV, watchedLV, watchingLV, watchedLVLabel, watchingLVLabel);
		Scene scene = new Scene(pane);
//		scene.setFill(backgroundColor);
		
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
				if(watchingLV.getSelectionModel().getSelectedItem() != null){
					finishedWatching = watchingLV.getSelectionModel().getSelectedItem();
					if(!watchingLV.getItems().isEmpty()){
						moveFromToLV(finishedWatching, watchingLV, watchedLV);
						updateArrays(finishedWatching);
					}
				} else{
					Alert alert = new Alert(AlertType.INFORMATION);
					Toolkit.getDefaultToolkit().beep();
					alert.setHeaderText(null);
					alert.setContentText("Select a movie to watch.");
					alert.showAndWait();
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
		String genre = removedTitleTrimmed.substring(6, removedTitleTrimmed.length());			
		

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
	}

	private void Reroll() {
		System.out.println("rerolling");
		count -= 1;
		RollMovie();
	}
	
	private void moveFromToLV(String selectedItem, ListView<String> from, ListView<String> to) {
		// remove item from movieList
		from.getItems().remove(selectedItem);
           // add item to listViewOfWatched listview
		to.getItems().add(selectedItem);

	}
	
	private void updateArrays(String finishedWatching)
	{
		int index = 0;
		String movie = "";
		for(String item : arrayOfMovies)
		{																// for each string in arrayOfMovies
			if(item.contains(finishedWatching))
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