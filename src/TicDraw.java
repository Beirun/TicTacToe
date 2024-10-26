import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TicDraw extends JPanel {
    final int height = 600;
    final int width = 600;
    boolean isGameOver = false;
    int turns = 0;
    int xCoordinate = 10;
    int yCoordinate = 10;
    Graphics2D g2;
    JPanel[][] area = new JPanel[3][3];
    String[] turn = {"X","O"};
    String[][] table = {
                        {"", "", ""},
                        {"", "", ""},
                        {"", "", ""}
                        };
    TicDraw() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(null);
        this.setFocusable(true);
    }
    public void Draw(Graphics g){
        g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g.setColor(Color.black);
        g.drawLine(width/3,0,width/3,height);
        g.drawLine(2*width/3,0,2*width/3,height);
        g.drawLine(0,height/3,width,height/3);
        g.drawLine(0,2*height/3,width,2*height/3);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        areaDraw(g);
        turnDraw(g);
        Draw(g);
        //ifGameOver(g);
    }

    public void ifGameOver(Graphics g){
        if(isGameOver) {
            JPanel panel = new JPanel();
            panel.setBounds(180, 220, 240, 160);
            panel.setBackground(new Color(255, 255, 255));
            JButton button = new JButton();
            button.setText("Restart");
            g.setColor(new Color(255, 255, 255));
            g.fillRect(180, 220, 240, 160);

        }
    }

    public void turnDraw(Graphics g){
        g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        int h = height/3;
        int w = width/3;
        for(int i = 0; i<table.length; i++){
            for(int j = 0; j<table[i].length;j++){
                if(table[i][j].equals(turn[0])){
                    g.setColor(Color.BLUE);
                    g.drawLine(40+(w*j),40+(h*i),160+(w*j),160+(h*i));
                    g.drawLine(160+(w*j),40+(h*i),40+(w*j),160+(h*i));
                }
                else if(table[i][j].equals(turn[1])){
                    g.setColor(Color.RED);
                    g.drawOval(w/6+7+(w*j),h/6+(h*i),width/5,height/5);
                }
            }
        }

        for(int i = 0; i<table.length; i++){
            for(int j = 0; j<table[i].length;j++){
                g2.setStroke(new BasicStroke(5));
                g.setColor(Color.black);
                if((table[i][0].equals(turn[0]) && table[i][1].equals(turn[0]) && table[i][2].equals(turn[0])) ||
                        table[i][0].equals(turn[1]) && table[i][1].equals(turn[1]) && table[i][2].equals(turn[1])){
                    isGameOver = true;
                    g.drawLine(width/30,height/6+(h*i),width-20,height/6+(h*i));
                }
                if((table[0][j].equals(turn[0]) && table[1][j].equals(turn[0]) && table[2][j].equals(turn[0])) ||
                        table[0][j].equals(turn[1]) && table[1][j].equals(turn[1]) && table[2][j].equals(turn[1])){
                    isGameOver = true;
                    g.drawLine(width/6+(h*j),height/30,width/6+(h*j),height-20);
                }
                if((table[0][0].equals(turn[0]) && table[1][1].equals(turn[0]) && table[2][2].equals(turn[0])) ||
                        table[0][0].equals(turn[1]) && table[1][1].equals(turn[1]) && table[2][2].equals(turn[1])){
                    isGameOver = true;
                    g.drawLine(width/30,height/30,width-20,height-20);
                }
                if((table[0][2].equals(turn[0]) && table[1][1].equals(turn[0]) && table[2][0].equals(turn[0])) ||
                        table[0][2].equals(turn[1]) && table[1][1].equals(turn[1]) && table[2][0].equals(turn[1])){
                    isGameOver = true;
                    g.drawLine(width-20,height/30,width/30,height-20);
                }
            }
        }
    }

    public void playerTurn(int i,int x, int y){
        if(turns==10) isGameOver = true;
        if(!table[x][y].isEmpty()){
            turns--;
            return;
        }
        if(i%2==0) table[x][y] = turn[0];
        else table[x][y] = turn[1];
    }
    public void areaDraw(Graphics g){
        int h = height/3, w = width/3;
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                area[i][j] = new JPanel();
                area[i][j].setBounds(w*j,h*i,w,h);
                area[i][j].setOpaque(false);
                if((i==xCoordinate && j==yCoordinate) && table[i][j].isEmpty() && !isGameOver)g.setColor(Color.lightGray);
                else g.setColor(new Color(240,240,240));
                area[i][j].addMouseListener(new PanelMouseListener(i,j));
                g.fillRect(w*j,h*i,w,h);
                add(area[i][j]);

            }
        }
    }
    public class PanelMouseListener extends MouseAdapter {
        private final int row;
        private final int col;
        public PanelMouseListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public void hoverSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
            File file = new File("resources\\hover.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }
        public void pressSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
            File file = new File("resources\\press.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(!isGameOver) {
                int temp = turns;
                playerTurn(turns++, row, col);

                if(temp!=turns) {
                    try {
                        pressSound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                repaint();
                System.out.println(Arrays.deepToString(table));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            xCoordinate = row;
            yCoordinate = col;
            if(table[row][col].isEmpty() && !isGameOver) {
                try {
                    hoverSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            xCoordinate = 10;
            yCoordinate = 10;
            repaint();
        }
    }
}
