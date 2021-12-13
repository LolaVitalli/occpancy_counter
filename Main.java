
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;


/**
 * Washroom counter simulation program
 * Team Swift
 * Nov 30, 2021
 */

/**
 * program to simulate people counter
 */
public class Main extends Application {
    private Button incButton, decButton, resetButton, resetAlarm;
    private Ellipse laser1, laser2;
    private int beam1 = 0, beam2 = 0;
    private Text valueText, alarmText, negativeAlarm, maxCap;
    private int val = 0;
    private int dirFlag = 999;
    private int ENTER = 1, EXIT = 0;
    private String timeStr;
    private Time time;
    private Timeline fiveSecondsWonder;


    /**
     * program starts
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Laser Sensor Simulation");

        // text to display total number
        valueText = new Text(230,60,""+val);
        valueText.setFont(Font.font(50));
        valueText.setFill(Color.ORANGERED);

        // manual button increment
        incButton = new Button("Increment");
        incButton.setLayoutX(150);
        incButton.setLayoutY(100);

        // manual button decrement
        decButton = new Button("Decrement");
        decButton.setLayoutX(255);
        decButton.setLayoutY(100);

        // manual reset button
        resetButton = new Button("Reset");
        resetButton.setLayoutX(215);
        resetButton.setLayoutY(140);

        // alarm reset button
        resetAlarm = new Button("Reset Alarm");
        resetAlarm.setLayoutX(200);
        resetAlarm.setLayoutY(180);

        // negative alarm txt display
        negativeAlarm = new Text("");
        negativeAlarm.setLayoutX(130);
        negativeAlarm.setLayoutY(85);
        negativeAlarm.setFont(Font.font(30));
        negativeAlarm.setFill(Color.SIENNA);

        // maximum capacity reaches display txt
        maxCap = new Text(120,85,"");
        maxCap.setFont(Font.font(20));
        maxCap.setFill(Color.SIENNA);

        // lasers
        laser1 = new Ellipse(180,270,40,40);
        laser1.setFill(Color.PALEVIOLETRED);
        laser2 = new Ellipse(305,270,40,40);
        laser2.setFill(Color.PALEVIOLETRED);

        laser1.setOnMouseClicked(new Laser1ClickEventHandler());
        laser2.setOnMouseClicked(new Laser2ClickEventHandler());

        // animation for 30 minutes timer
        time = new Time();
        timeStr = "PST " + time.getHours() + ": " + time.getMinute() + ": " + time.getSecond();
        Text timeTxt = new Text(50,60,timeStr);
        timeTxt.setFont(Font.font(60));
        timeTxt.setFill(Color.BISQUE);

        // display alarm txt
        alarmText = new Text(80,360,"");
        alarmText.setFont(Font.font(50));
        alarmText.setFill(Color.SIENNA);

        fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(10),
                        new EventHandler<ActionEvent>() {
                            /**
                             * display message after 10s no value change
                             * @param event after 10 seconds
                             */
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("this is called every 10 seconds on UI thread");
                                alarmText.setText("10 second alarm ");
                            }
                        })
        );
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();   // timer starts whe program opens


        Pane root = new Pane(valueText,incButton,decButton,resetButton,laser1,laser2,alarmText,resetAlarm,maxCap,negativeAlarm);

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

    /**
     * laser1 handler
     */
    private class Laser1ClickEventHandler implements EventHandler<MouseEvent> {
        /**
         * check direction and value change
         * @param e click on laser1
         */
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
                laser1.setFill(Color.PALEVIOLETRED);
            }else{
                laser1.setFill(Color.FORESTGREEN);
            }

            System.out.println("beam 1: " + beam1);

            e.consume();

            // set to zero if negative value
//            if(val < 0){
//                System.out.println("Negative number alarm...");
////                negativeAlarm.setText("Negative alarm");
//                val = 0;
//            }

            // set text value
            valueText.setText(val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");

            // reset maximum capacity alarm txt
            if(val < 5){
                maxCap.setText("");
                if(val < 0){
                    negativeAlarm.setText("Negative Alarm");
                }
            }

        }
    }

    /**
     * laser2 handler
     */
    private class Laser2ClickEventHandler implements EventHandler<MouseEvent> {
        /**
         * check direction and value change
         * @param e
         */
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

            // colour change
            if(beam2 % 2 == 0 ){
                laser2.setFill(Color.PALEVIOLETRED);
            }else{
                laser2.setFill(Color.FORESTGREEN);
            }

            System.out.println("beam 2: " + beam2);
            e.consume();
            valueText.setText(val+"");

            // rest alarm timer
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");

            // negative value check and reset
            if(val >= 0){
                negativeAlarm.setText(" ");
            }

            // maximum capacity check and rest txt
            if(val >= 5){
                maxCap.setText("Maximum Occupancy reaches!");

            }
            if(val < 5){
                maxCap.setText("");
            }
        }
    }

    /**
     * increment button handler
     */
    private class IncListener implements EventHandler<ActionEvent> {
        /**
         * increase value and check
         * @param e
         */
        @Override
        public void handle(ActionEvent e) {
            valueText.setText(++val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            alarmText.setText("");
            if(val >= 0){
                negativeAlarm.setText(" ");
            }
            if(val >= 5){
                maxCap.setText("Maximum Capacity reaches!");
            }
            if(val < 5){
                maxCap.setText("");

            }
        }
    }

    /**
     * decrement value handler
     */
    private class DecListener implements EventHandler<ActionEvent> {
        /**
         * decrease value, value check
         * @param e
         */
        @Override
        public void handle(ActionEvent e) {
            valueText.setText(--val+"");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();

            if(val < 5){
                maxCap.setText("");
//                if(val < 0){
//                    val = 0;
//                }
            }

            // reset alarm txt
            alarmText.setText("");
            if(val < 0){
                negativeAlarm.setText("Negative Alarm");
            }

        }
    }

    /**
     * reset button
     */
    private class ResetListener implements EventHandler<ActionEvent> {
        /**
         * reset value, alarm and laser counter
         * @param e
         */
        @Override
        public void handle(ActionEvent e) {
            val = 0;
            beam1 = 0;
            beam2 = 0;
            valueText.setText(val+"");
            alarmText.setText("");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            if(val >= 0){
                negativeAlarm.setText("");
            }
            if(val < 5){
                maxCap.setText("");

            }
        }
    }

    /**
     * reset alarm button
     */
    private class ResetAlarmListener implements EventHandler<ActionEvent> {
        /**
         * reset alarm handler
         * @param e
         */
        @Override
        public void handle(ActionEvent e) {
            alarmText.setText("");
            fiveSecondsWonder.stop();
            fiveSecondsWonder.play();
            if(val >= 0){
                negativeAlarm.setText(" ");
            }
        }
    }

    /**
     * direction check
     */
    public void checkDir() {
        // if beam1 hit first, enter
        if(beam1 > beam2){
            dirFlag = 1;
            System.out.println("Enter...");
            // beam 2 hit first, exit
        }else if(beam2 > beam1){
            dirFlag = 0;
            System.out.println("Exit...");
        }
    }

    /**
     * timer class
     */
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

    /**
     * program starts
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
