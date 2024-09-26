import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;

//This script generates a window in which the values for the rules of the CA can be changed
public class ParameterPanel {
    private JFrame frame;

    JCheckBox infiniteLoop;
    JTextField sizeOfNonInf;


    SavedSimulationsPanel SSP;
    SavedFieldsPanel SFP;


    private int amountOfBorders = 15;

    //The square area in which activated cells can spawn. It is initially at 0 to indicate that we are not running a non-infinite loop simulation
    private int startSize = 0;
    private int defaultStartSize = 40;

    //Not referring to this panel but the on running the cellular automata. I know, badly named class
    private Panel pan;
    private LifeFrame lf;

    //Depreciated
    public ArrayList<JTextField> stateChangeList = new ArrayList<JTextField>();

    //The borderValues are the text fields that hold the values for the 'border values' seperating the state changes of cells
    public ArrayList<JTextField> borderValues = new ArrayList<JTextField>();
    public ArrayList<JTextField> dividerValues = new ArrayList<JTextField>();
    public ArrayList<JTextField> delayValues = new ArrayList<JTextField>();

    boolean firstRunOfSim = true;

    public ParameterPanel(LifeFrame lfRef){
        lf = lfRef;
        MakePanel();
    }

    public void MakePanel(){
        //Panel setup
        frame = new JFrame();
        frame.setLayout(new BorderLayout(15, 5));
        frame.setTitle("Parameters for CA");
        frame.setSize(500, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        frame.add(panel, BorderLayout.CENTER);

        //Setup the panel with the text boxes
        SetupStateChanges(panel, amountOfBorders, amountOfBorders);

        //Add the buttons
       // JButton addStateChange = new JButton("Add state change");
        JButton resetButton = new JButton("Reset");
        JButton runButtton = new JButton("Run");
        JButton randomButton = new JButton("Random");
        infiniteLoop = new JCheckBox("Inf loop");
        sizeOfNonInf = new JTextField(4);
        JTextArea textOfSizeOfNonInf = new JTextArea("Start size");



        resetButton.addActionListener(e -> {
            lf.CloseSimulation();
        });

        runButtton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetupSimAndRun();
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RandomiseAll();
            }
        });

        infiniteLoop.setSelected(false);

       // panel.add(addStateChange, BorderLayout.SOUTH);
        panel.add(textOfSizeOfNonInf, BorderLayout.SOUTH);
        panel.add(sizeOfNonInf, BorderLayout.SOUTH);
        panel.add(resetButton, BorderLayout.SOUTH);
        panel.add(runButtton, BorderLayout.SOUTH);
        panel.add(randomButton, BorderLayout.SOUTH);
        panel.add(infiniteLoop, BorderLayout.SOUTH);

        tabbedPane.add("Parameters", panel);

        //Setting up the Saves panel
        SSP = new SavedSimulationsPanel(frame, tabbedPane);
        SFP = new SavedFieldsPanel(frame, tabbedPane);

        //Adding the sliders and buttons for the various values that we want to configure
        JSlider slider = new JSlider(0, 100, 0);
        panel.add(slider, BorderLayout.CENTER);

        frame.add(tabbedPane);

        frame.setVisible(true);
        panel.repaint();
    }

    //Just runs the simulation but makes the input values of type ArrayList<Float> before doing so
    public void SetupSimAndRun(){
        ArrayList<Float> borderVals = new ArrayList<Float>();
        ArrayList<Float> divVals = new ArrayList<Float>();
        ArrayList<Float> delVals = new ArrayList<Float>();

        //I use borderValues for no particular reason for the length of the loop as all 3 list should have the same length
        for (int i = 0; i < borderValues.size(); i++){
            borderVals.add(Float.valueOf(borderValues.get(i).getText()));
            divVals.add(Float.valueOf(dividerValues.get(i).getText()));
            delVals.add(Float.valueOf(delayValues.get(i).getText()));
        }

        //Check whether we're doing and infinite loop simulation or not
        boolean doInfLoop = false;
        if (infiniteLoop.isSelected() == true){
            doInfLoop = true;
        }else{
            startSize = defaultStartSize;
        }

        if (firstRunOfSim == true){
            lf.MakePanelPanel(borderVals, divVals, delVals, doInfLoop, startSize);
            firstRunOfSim = false;
        }else{
            lf.CloseSimulation();
            lf.StartSimulation(borderVals, divVals, delVals, doInfLoop, startSize);
        }
    }

    void SetupValuesInTextFields(){
        //Setting up border values
        for (int i = 0; i < amountOfBorders; i++){
            float textSubstitute = (float)(i+1) / (float)amountOfBorders;
            borderValues.get(i).setText(String.valueOf(textSubstitute));
        }

        //Setting up divider values
        for (int i = 0; i < dividerValues.size(); i++){
            dividerValues.get(i).setText("1");
        }

        //Setting up delay values
        for (int i = 0; i < delayValues.size(); i++){
            delayValues.get(i).setText("0");
        }
    }

    public void GetPanel(Panel panRef){
        pan = panRef;
    }

    public void SetupStateChanges(JPanel panel, int counter, int originalCounterVal){
        //Setting up the borderVal, divVal and the delVal
        JTextField borderValue = new JTextField(8);
        JTextField dividerValue = new JTextField(8);
        JTextField delayValue = new JTextField(8);
        borderValue.setVisible(true);
        dividerValue.setVisible(true);
        delayValue.setVisible(true);

        //Getting the text set up
        JTextArea borText = new JTextArea();
        borText.setText("Border");
        JTextArea divText = new JTextArea();
        divText.setText("Divider");
        JTextArea delText = new JTextArea();
        delText.setText("Delay");

        borderValues.add(borderValue);
        dividerValues.add(dividerValue);
        delayValues.add(delayValue);

        JTextArea borderText = new JTextArea();
        borderText.setText("Border " + String.valueOf(originalCounterVal - counter + 1));
        borderText.setForeground(Color.BLUE);

        borderText.setVisible(true);

        panel.add(borText, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(borderValue, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(divText, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(dividerValue, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(delText, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(delayValue, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(borderText, BorderLayout.WEST);

        counter--;

        if (counter > 0){
            SetupStateChanges(panel, counter, originalCounterVal);
        }else{
            SetupValuesInTextFields();
        }
    }

    //Randomise all of the input values
    void RandomiseAll(){
        ArrayList<Float> borderControl = new ArrayList<Float>();
        //Decides the right order for the border values
        for (int i = 0; i < amountOfBorders; i++){
            float ranNum = RandomNumber(1, 0);
            borderControl.add(ranNum);
        }

        for (int i = 0; i < amountOfBorders; i++){
            float smallestVal = Collections.min(borderControl);
            borderValues.get(i).setText(String.valueOf(smallestVal));
            borderControl.remove(smallestVal);
        }

        //Setting up divider values
        for (int i = 0; i < dividerValues.size(); i++){
            dividerValues.get(i).setText(String.valueOf((int)RandomNumber(14, 1)));
            //dividerValues.get(i).setText("1");
        }

        //Setting up delay values
        for (int i = 0; i < delayValues.size(); i++){
            delayValues.get(i).setText(String.valueOf((int)RandomNumber(6, 0)));
        }
    }

    float RandomNumber(float upperBound, float lowerBound){
        Random ran = new Random();
        float ranNum = ran.nextFloat(upperBound - lowerBound) + lowerBound;

        return ranNum;
    }

    //A cell state change is what I call it where a cell behaves a certain way when it is between two values
    public void AddCellStateChange(JPanel panel, Float inputValStateChange, Float inputValDivide, Float inputValDelay){
        System.out.println("Adding cell state change");
        JTextField inputFieldStateChange = new JTextField();
        JTextField inputFieldDivide = new JTextField();
        JTextField inputFieldDelay = new JTextField();
        JButton deleteButton = new JButton();

        //Assigning the text boxes their pre-allocated values
        inputFieldStateChange.setText(inputValStateChange.toString());
        inputFieldDivide.setText(inputFieldDivide.toString());
        inputFieldDelay.setText(inputFieldDelay.toString());

        //Setting them to visible
        inputFieldStateChange.setVisible(true);
        inputFieldDivide.setVisible(true);
        inputFieldDelay.setVisible(true);

        //Making all of the text boxes one array so I can easily return it from the subroutine
        ArrayList<JTextField> inputFieldList = new ArrayList<JTextField>();
        inputFieldList.add(inputFieldStateChange);
        inputFieldList.add(inputFieldDivide);
        inputFieldList.add(inputFieldDelay);

        //adding the components to the panel
        panel.add(inputFieldList.get(0), BorderLayout.CENTER);
        panel.add(inputFieldList.get(1), BorderLayout.CENTER);
        panel.add(inputFieldList.get(2), BorderLayout.CENTER);

        System.out.println("Haha");

        //Setting up the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Delete state change", "Delete", JOptionPane.INFORMATION_MESSAGE);
                inputFieldStateChange.setVisible(false);
                inputFieldStateChange.setText(null);
                deleteButton.setVisible(false);
            }
        });

        //panel.add(inputFieldStateChange, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.CENTER);

        stateChangeList = inputFieldList;
    }
}
