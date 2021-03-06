
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

 
public class Puzzle_Game extends JFrame {
	
    private JPanel panel = new JPanel(new GridLayout(4, 4, 2, 2));
    private static Random Random_Pos_Generetor = new Random();
    private int[][] num_array = new int[4][4];
    private  javax.swing.Timer t;
    private static long second , minute;
 

    
    
    public static void main(String[] args) {
    	 EventQueue.invokeLater(new Runnable() {
    	      public void run() {
    	        try {
    	          Puzzle_Game window = new Puzzle_Game();
    	          window.setVisible(true);
    	        } catch (Exception e) {
    	          e.printStackTrace();
    	        }
    	      }
    	    });
    }
    
    
    public Puzzle_Game() {
    	super("15_puzzle");
        init();
    }
    
    public void init(){
    	
    	 JLabel Time_Label = new JLabel(""); 
    	 Time_Label.setFont(new Font("Showcard Gothic", Font.ITALIC, 17));
    	 Time_Label.setHorizontalAlignment(SwingConstants.CENTER);
    	 
    	 
    
    	
    	final DecimalFormat dc = new DecimalFormat("00");
    	 //Time_Label.setText(dc.format(minute) + ":" + dc.format(second));
    	 
        t = new javax.swing.Timer( 1000,
        		new ActionListener() 
        	{
                public void actionPerformed(ActionEvent e) {
                    Time_Label.setText(dc.format(minute) + ":" + dc.format(second));
                    second++;
                    if (second >= 60) {
                        second %= 60;
                        minute++;
                    }
                }
            }
        );
        
        
        setVisible(true);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        int w=(int)d.getWidth();
        int h=(int)d.getHeight();
        setLocation((int)(w/2-getWidth()/2),(int)(h/2-getHeight()/2));
       
    
    	
        getContentPane().setBackground(SystemColor.menu);
        setBounds(200, 200, 500, 373);
        setResizable(false);
        
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = getContentPane();
      
        getContentPane().setLayout(null);
        panel.setBounds(169, 11, 312, 322);
        panel.setBackground(SystemColor.inactiveCaption);
        container.add(panel);
        
     
        
       
       
        JButton NewGameButton = new JButton("New Game");// New_game_button
        NewGameButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 11));
        NewGameButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		second =0;
        		minute = 0;
        		Time_Label.setText(dc.format(minute) + ":" + dc.format(second));
        		
        		t.start();
        		
        		panel.removeAll();
        		container.repaint();
        		
        	
        		Scramble();
        		repaintField();
        		
        	}
        });
        
        
        JButton ExitButton = new JButton("Exit"); //Exit_button
        ExitButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 11)); 
        ExitButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		t.stop();
        		
        		setVisible(false);
        		MainMenu m = new MainMenu();
        		m.setVisible(true);
        	}
        });
        
        NewGameButton.setBounds(31, 38, 106, 41);
        getContentPane().add(NewGameButton);
        
       
      
        ExitButton.setBounds(31, 134, 106, 41);
        getContentPane().add(ExitButton);
        
       
        Time_Label.setBounds(31, 258, 106, 75);
        getContentPane().add(Time_Label);
      
        
        
        repaintField();
    }
    
    

    public void Scramble() {
    	
    	
          
        int[] invariants = new int[16];
 do{
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                num_array[i][j] = 0;
                invariants[i*4+j] = 0;
            }
        }
 
        for (int i=1; i<16; i++) {
            int k;
            int l;
            do {
                k = Random_Pos_Generetor.nextInt(4) ;
                l = Random_Pos_Generetor.nextInt(4) ;
            }
            while (num_array[k][l] != 0);
            num_array[k][l] = i;
            invariants[k*4+l] = i;
        }
 }
 	while (!canBeSolved(invariants));
 	repaintField();
    }
    
    private boolean canBeSolved(int[] invariants) {
        int sum = 0;
        for (int i = 0; i < 16; i++) {
            if (invariants[i] == 0) {
                sum += i / 4;
                continue;
            }

            for (int j = i + 1; j < 16; j++) {
                if (invariants[j] < invariants[i])
                    sum ++;
            }
        }
        System.out.println(sum % 2 == 0);
        return sum % 2 == 0;
    }
 
    public void repaintField() {
        panel.removeAll();
 
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                JButton button = new JButton(Integer.toString(num_array[i][j]));
                button.setFocusable(false);
                panel.add(button);
                if (num_array[i][j] == 0) {
                    button.setVisible(false);
                } else
                    button.addActionListener(new ClickListener());
            }
        }
 
        //panel.validate();
    }
 
    public boolean Check_if_Win() {
        boolean status = true;
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                if (i == 3 && j>2)
                    break;
                if (num_array[i][j] != i*4+j+1) {
                    status = false;
                }
            }
        }
        return status;
    }
    
 
  
 
 
 
    private class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            button.setVisible(false);
            String name = button.getText();
            change(Integer.parseInt(name));
        }
    }
   
 
    

 
    public void change(int num) {
        int i=0;
        int j=0;
        for (int k=0; k<4; k++) {
            for (int l=0; l<4; l++) {
                if (num_array[k][l] == num) {
                    i = k;
                    j = l;
                }
            }
        }
        if (i>0) {
            if (num_array[i-1][j] == 0) {
                num_array[i-1][j] = num;
                num_array[i][j] = 0;
            }
        }
        if (i<3) {
            if (num_array[i+1][j] == 0) {
                num_array[i+1][j] = num;
                num_array[i][j] = 0;
            }
        }
        if (j>0) {
            if (num_array[i][j-1] == 0) {
                num_array[i][j-1] = num;
                num_array[i][j] = 0;
            }
        }
        if (j<3) 
        {
            if (num_array[i][j+1] == 0) {
                num_array[i][j+1] = num;
                num_array[i][j]=0;
            }
        }
        repaintField();
        if (Check_if_Win()) {
        	t.stop();
            JOptionPane.showMessageDialog(null, "YOU WON", "Congratz", 1);
            Scramble();
            repaintField();
            setVisible(false);
            setVisible(true);
        }
    }
}
  
