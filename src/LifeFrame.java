import javax.swing.JFrame;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import java.util.ArrayList;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class LifeFrame extends JFrame {
    Panel panelPanel;

    public LifeFrame(){

        new ParameterPanel(this);
        //new FieldPanel();
    }

    //Called by ParameterPanel for the first run of the simulation
    public void MakePanelPanel(ArrayList<Float> borderVals, ArrayList<Float> divVals, ArrayList<Float> delVals, boolean infLoop, int startSize){
        panelPanel = new Panel(borderVals, divVals, delVals, infLoop, startSize);

        add(panelPanel);

        setSize(1000, 1000);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void StartSimulation(ArrayList<Float> borderVals, ArrayList<Float> divVals, ArrayList<Float> delVals, boolean infLoop, int startSize){
        panelPanel.ResetPanel(borderVals, divVals, delVals, getGraphics(), infLoop, startSize);

        add(panelPanel);

        setSize(1000, 1000);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void CloseSimulation(){
        remove(panelPanel);
        dispose();
    }

    public static void main(String[] args) {
        new LifeFrame();
    }
}
