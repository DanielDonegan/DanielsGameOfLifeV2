import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.io.File;

public class Panel extends JPanel implements ActionListener{

    int  xPanel = 1000;
    int yPanel = 1000;
    int size = 2;
    int radius = 2;
    int xWidth =  xPanel / size;
    int yHeight = yPanel / size;
    int fieldCount = 0;
    int fieldLimit = 1;
    int startSize;


    //References to outer scripts
    private ParameterPanel pp;


    int[][] cellPositions;
    int[][] delay = new int[xWidth][yHeight];
    //The amount we each alive sum by to keep the numbers with in the bounds of 0 - 1
    float minimiser = 16.5f;
    float highestAlive = 0f;
    float[][] life = new float[xWidth][yHeight];
    //I know it should be called afterLife blame the dang tutorial!
    float[][] beforeLife = new float[xWidth][yHeight];


    Timer theTimer;


    boolean infLoop = true;


    ArrayList<Float> borderVals = new ArrayList<Float>();
    ArrayList<Float> divVals = new ArrayList<Float>();
    ArrayList<Float> delVals = new ArrayList<Float>();

    boolean starts = true;
    public Panel(ArrayList<Float> borderValsRef, ArrayList<Float> divValsRef, ArrayList<Float> delValsRef, boolean infLoopRef, int starttSizeRef){
        borderVals = borderValsRef;
        divVals = divValsRef;
        delVals = delValsRef;

        infLoop = infLoopRef;
        startSize = starttSizeRef;

        setSize(1000, 1000);
        setLayout(null);
        setBackground(Color.BLACK);

        ReadFieldData();

        theTimer = new Timer(7, this);
        theTimer.start();
    }

    public void ResetPanel(ArrayList<Float> borderValsRef, ArrayList<Float> divValsRef, ArrayList<Float> delValsRef, Graphics g, boolean infLoopRef, int startSizeRef){
        borderVals = borderValsRef;
        divVals = delValsRef;
        delVals = delValsRef;

        infLoop = infLoopRef;
        startSize = startSizeRef;

        ReadFieldData();

        life = new float[xWidth][yHeight];
        beforeLife = new float[xWidth][yHeight];

        //theTimer = new Timer(3, this);
        theTimer.restart();

        paintComponent(g);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //grid(g);

        spawn(g, infLoop);
        display(g);
    }

    private void grid(Graphics g){
        g.setColor(Color.DARK_GRAY);

        for (int i = 0; i < life.length; i++){
            g.drawLine(0, i * size, xPanel, i * size); //column
            g.drawLine(i * size, 0, i * size, yPanel); //row

        }
    }

    private void spawn(Graphics g, boolean isInfinite){
        if (starts == true){
            if (isInfinite == true){
                for (int x = 0; x < life.length; x++){
                    for (int y = 0; y < (yHeight); y++){
                       // if ((int)(Math.random() * 2) == 0){
                        beforeLife[x][y] = (float)Math.random();
                        beforeLife[x][y] = Math.round(beforeLife[x][y] * 100f) / 100f;
                            //System.out.println(beforeLife[x][y]);
                        delay[x][y] = 0;
                       // }
                    }
                }
            }else{
                for (int x = 0; x < life.length; x++){
                    for (int y = 0; y < (yHeight); y++){
                       // if ((int)(Math.random() * 2) == 0){
                        //Setting the bounds for where a cell can have a higher activation than 1 so that half of the area is set definitely to inactive
                        if (x >= (life.length / 2) - startSize && x <= (life.length / 2) + startSize && y >= (yHeight / 2) - startSize && y <= (yHeight / 2) + startSize) {
                            beforeLife[x][y] = (float)Math.random();
                            beforeLife[x][y] = Math.round(beforeLife[x][y] * 100f) / 100f;
                            //System.out.println(beforeLife[x][y]);
                        }else{
                            beforeLife[x][y] = 0f;
                        }

                        delay[x][y] = 0;
                       // }
                    }
                }
            }
            starts = false;
        }
    }

    //fills in rects
    private void display(Graphics g){
        g.setColor(Color.GREEN);

        copyArray();

        for (int x = 0; x < life.length; x++){
            for (int y = 0; y < (yHeight); y++){
                if (life[x][y] > 0){
                    //System.out.println((int)(255 * life[x][y]));
                    int colourUsing = (int)(255 * life[x][y]);

                    if (colourUsing > 255){
                        colourUsing = 255;
                    }

                    g.setColor(new Color(0, colourUsing, 0));
                    g.fillRect(x * size, y * size, size, size);
                }
            }
        }
    }

    //Copies the values for the life array into before life
    private void copyArray(){
        for (int x = 0; x < life.length; x++){
            for (int y = 0; y < (yHeight); y++){
                life[x][y] = beforeLife[x][y]; //???
            }
        }
    }

    //a stands for alive and p stands for preAlive as we're are checking if we've found an alive cell
    private float[] aliveChangeCheck(float cellAliveValue, float alive, float preAlive, float sum){
        if (alive != preAlive){
            sum++;
        }

        //This is the equation which allows us to check the value of the neighbouring cell
        alive += cellAliveValue;

        float[] pair = {alive, (float)sum};

        return pair;
    }

    //I know the aliveNSum variable is super dumb but when u want to get round a problem you find any means necessary...
    //The variable aliveNSum is a combination of the alive and sum variables where sum should be an int so we will convert it to one later
    private float check3x3(int x, int y){
        float[] aliveNSum = {0, 0};
        float preAlive = 0;
        
        aliveNSum = aliveChangeCheck(life[(x + xWidth - 1) % xWidth][(y + yHeight - 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        aliveNSum = aliveChangeCheck(life[(x + xWidth) % xWidth][(y + yHeight - 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        
        aliveNSum = aliveChangeCheck(life[(x + xWidth + 1) % xWidth][(y + yHeight - 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        aliveNSum = aliveChangeCheck(life[(x + xWidth - 1) % xWidth][(y + yHeight) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        
        aliveNSum = aliveChangeCheck(life[(x + xWidth + 1) % xWidth][(y + yHeight) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        aliveNSum = aliveChangeCheck(life[(x + xWidth - 1) % xWidth][(y + yHeight + 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        
        aliveNSum = aliveChangeCheck(life[(x + xWidth) % xWidth][(y + yHeight + 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);
        aliveNSum = aliveChangeCheck(life[(x + xWidth + 1) % xWidth][(y + yHeight + 1) % yHeight], aliveNSum[0], preAlive, aliveNSum[1]);

        aliveNSum[0] /= aliveNSum[1];
        
        return aliveNSum[0];
    }

    private float check4x4(int x, int y){
        float alive = 0;

        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight - 1) % yHeight]; //This line of code ...
        alive += life[(x + xWidth) % xWidth][(y + yHeight - 1) % yHeight];

        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight - 1) % yHeight];
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight) % yHeight];

        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight) % yHeight];
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight + 1) % yHeight];

        alive += life[(x + xWidth) % xWidth][(y + yHeight + 1) % yHeight];
        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight + 1) % yHeight];

        //Bottom row
        alive += life[(x + xWidth) % xWidth][(y + yHeight - 2) % yHeight];


        //Top row
        alive += life[(x + xWidth) % xWidth][(y + yHeight + 2) % yHeight];

        alive += life[(x + xWidth + 2) % xWidth][(y + yHeight) % yHeight];

        alive += life[(x + xWidth - 2) % xWidth][(y + yHeight) % yHeight];

        return alive / minimiser;
    }

    //Making a radial form of checking system
    private float checkRadius(int x, int y, int radialSize){
        float alive = 0;
        float preAlive = 0;

        int counter = 0;

        //This should be explained in the notes and if not: i and j are the positions of the cells around x and y and r just tells them how many cells to check for each iteration
        for (int r = 0; r < radialSize; r++){
            if (r != 0){
                for (int i = -r; i < r; i++){
                    for (int j = -r; j < r; j++){
                        alive += life[(x + xWidth + r) % xWidth][(y + yHeight + j) % yHeight] / Math.pow(r, 2);

                        if (alive != preAlive){
                            counter++;
                        }

                        preAlive = alive;
                    }
                }
            }
        }

        return alive;  // (counter);
    }

    //Reads data from fieldValues.txt and sends the data to CustomCheck(); for the current cell to calculate its alive value
    private void ReadFieldData(){
        try{
           /* System.out.println("What is the name of the file you would like to read? ");
            Scanner fileName = new Scanner(System.in);*/
            File fileToRead = new File("fieldValues.txt");
            Scanner fileReader = new Scanner(fileToRead);

            //Just closing file for convenience sake
            //fileName.close();

            //The following lines are reading the dimensions and length of the following data
            int dim = fileReader.nextInt();
            int cellCount = fileReader.nextInt();

            cellPositions = new int[2][cellCount];

            for (int i = 0; i < cellCount; i++){
                ArrayList<Integer> xy = IndexToXY(fileReader.nextInt(), dim);

                cellPositions[0][i] = xy.get(0);
                cellPositions[1][i] = xy.get(1);
            }
        }catch (FileNotFoundException e){

        }
    }

    //Gathers all of the collective alive values from the surrounding cells to be processed and adapted into the current cell's alive value
    private float checkInput(int x, int y){
        float alive = 0;

        for (int i = 0; i < cellPositions[0].length; i++) {
            alive += CustomCheck(x, y, cellPositions[0][i], cellPositions[1][i]);
        }

        //System.out.println(cellPositions[0].length);

        return alive;
    }

    //This subroutine checks the cells (neighbouring pixels) around it regarding the ones chosen by the user using the FieldPanel.java script with its values
    //Saved in fieldValues.txt
    private float CustomCheck(int x, int y, int xCheck, int yCheck){
        //It returns the individual alive value regarding the one cell its checking
        return life[(x + xWidth + xCheck) % xWidth][(y + yHeight + yCheck) % yHeight];
    }

    //Converts the values from fieldValues.txt from indexes for the array to x and y co-ordinates to be used to check surrounding cells
    private ArrayList<Integer> IndexToXY(int index, int dimension){
        ArrayList<Integer> xandy = new ArrayList<Integer>(2);

        //Setting the x value
        xandy.add((index % dimension) - ((dimension - 1) / 2));
        //Setting the y value
        xandy.add((index / dimension) - ((dimension - 1) / 2));

        return xandy;
    }

    private float check5x5Circle(int x, int y){
        float alive = 0;

        /*alive += life[(x + xWidth - 1) % xWidth][(y + yHeight - 1) % yHeight]; //This line of code ...
        alive += life[(x + xWidth) % xWidth][(y + yHeight - 1) % yHeight];

        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight - 1) % yHeight];
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight) % yHeight];

        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight) % yHeight];
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight + 1) % yHeight]; //This line of code ...

        alive += life[(x + xWidth) % xWidth][(y + yHeight + 1) % yHeight];
        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight + 1) % yHeight];*/

        //Bottom row
        alive += life[(x + xWidth - 2) % xWidth][(y + yHeight - 2) % yHeight]; //This line of code ...
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight - 2) % yHeight];
        alive += life[(x + xWidth) % xWidth][(y + yHeight - 3) % yHeight]; //This line of code ...
        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight - 2) % yHeight];
        alive += life[(x + xWidth + 2) % xWidth][(y + yHeight - 2) % yHeight];

        //Top row
        alive += life[(x + xWidth - 2) % xWidth][(y + yHeight + 2) % yHeight]; //This line of code ...
        alive += life[(x + xWidth - 1) % xWidth][(y + yHeight + 2) % yHeight];
        alive += life[(x + xWidth) % xWidth][(y + yHeight + 3) % yHeight]; //This line of code ...
        alive += life[(x + xWidth + 1) % xWidth][(y + yHeight + 2) % yHeight];
        alive += life[(x + xWidth + 2) % xWidth][(y + yHeight + 2) % yHeight];

        alive += life[(x + xWidth + 2) % xWidth][(y + yHeight + 1) % yHeight];
        alive += life[(x + xWidth + 3) % xWidth][(y + yHeight) % yHeight];
        alive += life[(x + xWidth + 2) % xWidth][(y + yHeight - 1) % yHeight];

        alive += life[(x + xWidth - 2) % xWidth][(y + yHeight + 1) % yHeight];
        alive += life[(x + xWidth - 3) % xWidth][(y + yHeight) % yHeight];
        alive += life[(x + xWidth - 2) % xWidth][(y + yHeight - 1) % yHeight];

        //Checking if this is the highest value of alive we have already found
        if (alive > highestAlive){
            highestAlive = alive;
        }

        return alive;
    }

    public void actionPerformed(ActionEvent e){

        float alive;
        ArrayList<Float> aliveValues = new ArrayList<Float>();

        for (int x = 0; x < life.length; x++){
            for (int y = 0; y < yHeight; y++){
                alive = checkInput(x, y);
                alive /= 1;

                aliveValues.add(alive);

                //Checking which value is the highest value so we can normalise all of the alive values
                if (alive > highestAlive){
                    highestAlive = alive;
                }
            }
        }

        int count = 0;
        for (int x = 0; x < life.length; x++){
            for (int y = 0; y < (yHeight); y++){
                alive = aliveValues.get(count) / highestAlive;
                if (delay[x][y] == 0) {
                    CellNextStep(alive, x, y);
                    //ManualCellNextStep(alive, x, y);
                }

                delay[x][y]--;

                if (delay[x][y] < 0){
                    delay[x][y] = 0;
                }

                count++;
            }
        }

        highestAlive = 0f;

        repaint(); //Refreshes the page
    }

    public void CellNextStep(float alive, int x, int y){
        int cellStateNum = 0;

        //Essentially i represents the border area in which the alive value falls into
        //The loop finds what bound the alive value falls between
        for (int i = 0; i < borderVals.size(); i++){
            if (i == 0){
                if (alive < borderVals.get(i)){
                    cellStateNum = i;
                    beforeLife[x][y] = alive * divVals.get(cellStateNum);
                    delay[x][y] = delVals.get(cellStateNum).intValue();
                    return;
                }
            }else{
                if (alive >= borderVals.get(i - 1) && alive <= borderVals.get(i) && life[x][y] >= borderVals.get(i)){
                    cellStateNum = i;
                    beforeLife[x][y] = alive * divVals.get(cellStateNum);
                    delay[x][y] = delVals.get(cellStateNum).intValue();
                    return;
                }else if (alive > borderVals.get(borderVals.size() - 1)){
                    cellStateNum = i;
                    beforeLife[x][y] = alive * divVals.get(cellStateNum);
                    delay[x][y] = delVals.get(cellStateNum).intValue();
                    return;
                }
            }
        }

     //   System.out.println(cellStateNum);

    }

    //For use of 4 state changes
    void ManualCellNextStep(float alive,int x,int y){
        if (alive >= 0.6f && alive <= 1){
            beforeLife[x][y] = alive;
            delay[x][y] = 11;
        }else if (alive >= 0.4f && alive <= 0.6f){// && life[x][y] >= 0.3f){
            beforeLife[x][y] = alive / 8;
            delay[x][y] = 10;
        }else if (alive >= 0.4f && alive < 0.5f){
            beforeLife[x][y] = alive / 8;
            delay[x][y] = 8;
        }else if (alive < 0.3f){
            beforeLife[x][y] = alive;
            delay[x][y] = 2;
        }
    }
}
