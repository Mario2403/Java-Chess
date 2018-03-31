package GUI;


import javax.swing.*;
import java.awt.*;

public class Table {


    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600) ;
    private final JFrame gameFrame;

    public Table(){
        this.gameFrame= new JFrame("Jchess");
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }


}
