import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.*;

public class FieldPanel extends JFrame implements MouseListener{
    //Make sure that the radius is always odd
    int radius = 13;

    ArrayList<Integer> clickedInts = new ArrayList<Integer>();

    ArrayList<JLabel> cells = new ArrayList<JLabel>();
    ArrayList<Integer> usedCellsX = new ArrayList<Integer>();
    ArrayList<Integer> usedCellsY = new ArrayList<Integer>();

    public FieldPanel(){
        this.setSize(radius * 40, radius * 40);
        this.setLayout(null);
        this.setVisible(true);
        //this.setBackground(Color.BLACK);

        MakeField();
        SaveCells();
    }

    void MakeField(){
        for (int x = 0; x < radius; x++){
            for (int y = 0; y < radius; y++){
                JLabel label = new JLabel();
                label.setBounds(40 * x, 40 * y, 40, 40);
                label.setBackground(Color.black);
                label.setOpaque(true);
                label.addMouseListener(this);

                this.add(label);
                cells.add(label);
            }
        }
    }

    void SaveCells(){
        try{
            System.out.print("Please enter the file name: ");
            Scanner fileName = new Scanner(System.in);
            PrintWriter fileSaver = new PrintWriter(fileName.next());
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

    @Override
    public void mouseClicked(MouseEvent e){
        //TODO

        System.out.println("you clicked here");
        JLabel l = (JLabel)e.getComponent();

        boolean cellAlreadyClicked = false;

        //This loop will search through all of the cells to give the index of the one you've clicked
        for (int i = 0; i < cells.size(); i++){
            if (l == cells.get(i)){
                Integer num = i;
                for (int a = 0; a < clickedInts.size(); a++){
                    if (num == clickedInts.get(a)){
                        cellAlreadyClicked = true;
                        l.setBackground(Color.black);
                        clickedInts.remove(num);
                        break;
                    }
                }

                //If the recent cell we've clicked on hasn't been clicked before then change its colour and add its index to the array of clicked cells
                if (cellAlreadyClicked == false){
                    l.setBackground(Color.lightGray);
                    clickedInts.add(num);
                }

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e){

    }
}
