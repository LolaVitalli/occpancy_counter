import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;
import java.time.LocalDateTime;

public class Main extends Application {
    private Button incButton, decButton, resetButton, resetAlarm;
    private Ellipse laser1, laser2;
    private int beam1 = 0, beam2 = 0;
    private Text valueText, alarmText;
    private int val = 0;
    private int dirFlag = 999;
    private int ENTER = 1, EXIT = 0;
    private String timeStr, alarm;
    private Time time;
    private Timeline fiveSecondsWonder;



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Laser Sensor Simulation");

        valueText = new Text(230,60,""+val);
        valueText.setFont(Font.font(50));
        valueText.setFill(Color.ORANGERED);

        incButton = new Button("Increment");
        incButton.setLayoutX(150);
        incButton.setLayoutY(100);

        decButton = new Button("Decrement");
        decButton.setLayoutX(255);
        decButton.setLayoutY(100);

        resetButton = new Button("Reset");
        resetButton.setLayoutX(215);
        resetButton.setLayoutY(140);

        resetAlarm = new Button("Reset Alarm");
        resetAlarm.setLayoutX(200);
        resetAlarm.setLayoutY(180);

        laser1 = new Ellipse(180,270,40,40);
        laser1.setFill(Color.RED);
        laser2 = new Ellipse(305,270,40,40);
        laser2.setFill(Color.RED);

        laser1.setOnMouseClicked(new Laser1ClickEventHandler());
        laser2.setOnMouseClicked(new Laser2ClickEventHandler());

        time = new Time();
        timeStr = "PST " + time.getHours() + ": " + time.getMinute() + ": " + time.getSecond();
        Text timeTxt = new Text(50,60,timeStr);
        timeTxt.setFont(Font.font(60));
        timeTxt.setFill(Color.BISQUE);

        alarmText = new Text(50,360,"");
        alarmText.setFont(Font.font(50));
        alarmText.setFill(Color.SIENNA);

        fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(10),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("this is called every 30 seconds on UI thread");
                                alarmText.setText("10 second alarm ");

                            }
                        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();



        Pane root = new Pane(valueText,incButton,decButton,resetButton,laser1,laser2,alarmText,resetAlarm);

        IncListener inclistener = new IncListener();
        DecListener declistener = new DecListener();
        ResetListener resetListener = new ResetListener();
        ResetAlarmListener resetAlarmListener = new ResetAlarmListener();

        incButton.setOnAction(inclistener);
        decButton.setOnAction(declistener);
        resetButton.setOnAction(resetListener);
        resetAlarm.setOnAction(resetAlarmListener);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private class Laser1ClickEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e){
            beam1++;

            // check direction
            checkDir();

            System.out.println("dirFlag is: " + dirFlag);

            // if exit flagged, val--
            if(dirFlag == EXIT){
                val--;
                System.out.println("value decrease by 1");
                dirFlag = 999;
            }

            // color change of button
            if(beam1 % 2 == 0 ){
                laser1.setFill(Color.RED);
            }else{
                laser1.setFill(Color.GREEN);
            }

            System.out.println("beam 1: " + beam1);

            e.consume();

            // set text value
            valueText.setText(val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");


        }
    }
    private class Laser2ClickEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e){
            beam2++;
            checkDir();
            System.out.println("dirFlag is: " + dirFlag);

            // if enter flagged, val++
            if(dirFlag == ENTER){
                System.out.println("value increase by 1");
                val++;
                dirFlag = 999;
            }

            if(beam2 % 2 == 0 ){
                laser2.setFill(Color.RED);
            }else{
                laser2.setFill(Color.GREEN);
            }

            System.out.println("beam 2: " + beam2);
            e.consume();
            valueText.setText(val+"");


            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");

        }
    }

    private class IncListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            valueText.setText(++val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");

        }
    }
    private class DecListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            valueText.setText(--val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");

        }
    }
    private class ResetListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            val = 0;
            beam1 = 0;
            beam2 = 0;
            valueText.setText(val+"");
            alarmText.setText("");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
        }
    }
    private class ResetAlarmListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            alarmText.setText("");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
        }
    }
    public void laserBeam() {
//        System.out.println("Beam 1");
//        System.out.println(beam1);
//
//        System.out.println("Beam 2");
//        System.out.println(beam2);
//
//        if(beam1 % 2 == 0){
//            laser1.setFill(Color.RED);
//        }
//		if (beam2 % 2 == 0){
//			laser2.setFill(Color.RED);
//		}
//
//		if(beam1 > beam2){
//            if((beam1-beam2) % 2 == 0){
//                val++;
//            }
//        }else if(beam2 > beam1){
//            if((beam2-beam1) % 2 == 0){
//                val--;
//            }
    }

    public void checkDir() {
        if(beam1 > beam2){
            dirFlag = 1;
            System.out.println("Enter...");
        }else if(beam2 > beam1){
            dirFlag = 0;
            System.out.println("Exit...");
        }
    }

    class Time {
        private LocalDateTime now = LocalDateTime.now();
        private int hours = now.getHour() % 12;
        private int minutes = now.getMinute();
        private int seconds = now.getSecond();

        public int getHours() {
            return hours;
        }
        public int getMinute() {
            return minutes;
        }
        public int getSecond() {
            return seconds;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
