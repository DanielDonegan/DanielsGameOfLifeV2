import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
public class SavedSimulationsPanel {

    /*

    We will save the border values first, then the divider values, then the delay values and after that
    all of the other smaller tweaks to the simulation

     */

    public SavedSimulationsPanel(JFrame frame, JTabbedPane tabbedPane){
        JPanel panel = new JPanel();



        tabbedPane.add("Saved simulations", panel);
    }

    void LoadSavedSimulations(){

    }
}
