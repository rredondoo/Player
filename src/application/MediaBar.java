package application;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class MediaBar extends HBox{
	
	Slider vol;
	Slider time;
	
	Button playButton = new Button("||");
	
	Label volume = new Label("Volume: ");
	
	MediaPlayer player;
	
	public MediaBar(MediaPlayer player){
		this.player = player;
		
		setAlignment(Pos.CENTER);
		setPadding(new Insets(10, 5, 10, 5));
		
		vol.setPrefWidth(70);
		vol.setMinWidth(40);
		
		HBox.setHgrow(time, Priority.ALWAYS);
		
		playButton.setPrefWidth(30);
		
		getChildren().add(vol);
		getChildren().add(time);
		getChildren().add(playButton);
		getChildren().add(volume);
		
		playButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				Status status = player.getStatus();
				
				if (status == Status.PLAYING){
					if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())){
					player.seek(player.getStartTime());	
					} else {
						playButton.setText(">");
						player.pause();
					}
						
				} 
				if (status != Status.PLAYING){
					player.play();
					playButton.setText("||");
				}
			}
		});
		
		//Add listener to update the value of the time slider
		player.currentTimeProperty().addListener(new InvalidationListener(){
			
			public void invalidated(Observable ob){
				updateValues();
			}
		});
		
		//Make the video jump to specific specified place in slider
		time.valueProperty().addListener(new InvalidationListener(){

			public void invalidated(Observable ob){
				if (time.isPressed()){
					player.seek(player.getMedia().getDuration().multiply(time.getValue()/100));
				}
			}
		});
		
		//Change the volume of the player according to vol slider value
		vol.valueProperty().addListener(new InvalidationListener(){
			
			public void invalidated(Observable ob){
				if (vol.isPressed()){
					player.setVolume(vol.getValue()/100);
				}
			}
		});
	}
	
	protected void updateValues(){
		Platform.runLater(new Runnable(){
			public void run(){
				time.setValue(player.getCurrentTime().toMillis()/player.getTotalDuration().toMillis()*100);
			}
		});
	}

}
