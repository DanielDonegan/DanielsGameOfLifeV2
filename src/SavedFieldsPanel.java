import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.*;
public class SavedFieldsPanel {

    /*

    We will save the border values first, then the divider values, then the delay values and after that
    all of the other smaller tweaks to the simulation

     */


    ArrayList<Float> borderVals = new ArrayList<Float>();

    String saveFileName = "SavedFieldValues.txt";

    public SavedFieldsPanel(JFrame frame, JTabbedPane tabbedPane){
        JPanel panel = new JPanel();

        tabbedPane.add("Saved fields", panel);
    }

    void LoadSavedFields(){
        try{
            PrintWriter fileSaver = new PrintWriter(saveFileName);
            System.out.println(clickedInts.get(0).intValue());

            fileName.close();

            //The first integer value to be saved is the dimensions of the cell input field
            fileSaver.println(radius);
            //The second integer value to be saved is the amount of following integers so we know how many times to loop later on
            fileSaver.println(clickedInts.size());

            //Now we write each individual cell index to the file line by line
            for (int i = 0; i < clickedInts.size(); i++){
                fileSaver.println(clickedInts.get(i).intValue());
            }

            fileSaver.close();

        }catch (FileNotFoundException e){
            System.out.println("ERROR OCCURED: could not find the expected file");
        }
    }
}
