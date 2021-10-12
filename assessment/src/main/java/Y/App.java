package assessment.src.main.java.Y;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class App 
{
    static String time = "9:00 AM";
    static String timeAfter = "1:00 PM";
    //time format (H:m AM/PM)
    static DateTimeFormatter df = DateTimeFormatter.ofPattern("h:mm a");
    //the main algorithm, incorporating 2 buckets
    static int bucketOne = 180;
    static int bucketTwo = 240;
    public static void main( String[] args ) throws IOException
    {
        readFile();
    }

    //the main point of execution
    private static void readFile() throws IOException{
    //asking for user to input a file name to scan
       Scanner in = new Scanner(System.in);
       System.out.println("Enter file path: ");
       File file = new File( in.nextLine());
       //using Buffered Reader library to read through the file
       BufferedReader br = new BufferedReader(new FileReader(file));
       //line is our String representation of br.readLine()
       String line = "";
       int lineCounter = 1;
       //tracker variable to print which track we are in
       int tracker = 1;
       System.out.println("Track "+ tracker);
       while( (line = br.readLine()) != null){
            if(containsDigits(line) || line.contains("lightning")){
                if(line.contains("lightning")){
                    line = line.replaceAll("lightning", "5min");
                }
            String converter = line.replaceAll("[^0-9]", "");
            //replace all characters with numbers only(only possible because talk titles w/no numbers)
            int numberOfMins = Integer.parseInt(converter);
            //calling 2 methods before lunch time
            if(time != "lunch time" ){
                if(bucketOneAdd(time, numberOfMins) == false){
                    System.out.println("12:00 PM Lunch");
                    System.out.println(timeAfter+ " "+ line);
                    time = "lunch time";
                    timeAfter = getBucketTwo(numberOfMins, false);
                }
                else{
                    System.out.println(time + " " + line);
                    time = getBucketOne(numberOfMins, true);
                }
            
            //calling 2 methods in the afternoon
                
            }
            else if(timeAfter != "network"){
                if(bucketTwoAdd(timeAfter, numberOfMins) == false){
                    System.out.println(timeAfter + " Networking Event");
                    time = "9:00 AM";
                    timeAfter = "1:00 PM";
                    tracker++;
                    System.out.println("Track " + tracker);
                    System.out.println(time + " " + line);
                    time = getBucketOne(numberOfMins, false);
                }
                else{
                    System.out.println(timeAfter + " " + line);
                    timeAfter = getBucketTwo(numberOfMins, true);
                }
            }
            //checking to see if next track is needed
            else{
                System.out.println(timeAfter + " Networking Event");
                time = "9:00 AM";
                timeAfter = "1:00 PM";
                tracker++;
                System.out.println("Track " + tracker);
                System.out.println(time + " " + line);
                time = getBucketOne(numberOfMins, false);
            }
        }
            else{
                System.out.println("You need to declare time in mins for line " + lineCounter);
            }
        }
        System.out.println(timeAfter + " Networking Event");
        in.close();
        br.close();
    }

    //just checks if each line contains any digits, as that is very necessary!
    private static boolean containsDigits(String file){
        if(file != null && !file.isEmpty()){
            if(file.contains("lightning")){
                return true;
            }
            else{
                for(char c: file.toCharArray()){
                    if(Character.isDigit(c)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //the main bulk of the algorithm
    //set 1 bucket to 180 mins(9 AM - 12 PM) and keep adding time slots until bucket is less than 0
    private static String getBucketOne(int mins, Boolean val){
        if(val == false){
            bucketOne = 180;
            time = "9:00 AM";
        }
        if(bucketOne> 0){
            bucketOne = bucketOne - mins;
            LocalTime lt = LocalTime.parse(time, df);
            String times = df.format(lt.plusMinutes(mins));
            return times;
            
        }
        else {
            System.out.println("12:00 PM Lunch");
            bucketOne = 180;
            time = "9:00 AM";
            return ("lunch time");
        }
        
    }
    //same thing but now with "afternoon bucket"
    private static String getBucketTwo(int mins, Boolean val){
        if(val == false){
            bucketTwo = 240;
            timeAfter = "1:00 PM";
        }
        if(bucketTwo > 0){
            bucketTwo = bucketTwo - mins;
            LocalTime lt = LocalTime.parse(timeAfter, df);
            String times = df.format(lt.plusMinutes(mins));
            return times;
        }
        else{
            System.out.println("5:00 PM Networking Event");
            bucketTwo = 240;
            timeAfter = "1:00 PM";
            return ("network");
        
        }
    }

    //the code so far does not take into account any overlapping during lunch/networking event
    //checks if the time goes past 12
    private static Boolean bucketOneAdd(String s, int nums){
        LocalTime lt = LocalTime.parse(s, df);
        String checker = df.format(lt.plusMinutes(nums));
        LocalTime timeChecker = LocalTime.parse(checker, df);
        if(timeChecker.isAfter(LocalTime.parse("12:00 PM", df))){
            return false;
        }
        return true;
    }

    //checks if time goes past 5
    private static Boolean bucketTwoAdd(String s, int nums){
        LocalTime lt = LocalTime.parse(s, df);
        String checker = df.format(lt.plusMinutes(nums));
        LocalTime timeChecker = LocalTime.parse(checker, df);
        if(timeChecker.isAfter(LocalTime.parse("5:00 PM", df))){
            return false;
        }
        return true;
    }



}
