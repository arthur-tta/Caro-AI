package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static int sec = 0;
    private static Timer timer = new Timer();
    private static JLabel lblTime;
    private static JButton  btnStart;
    private static Board board;

    public static void main(String[] args) {
        board = new Board();
        
        // thông báo endgame
        board.setEndGame(new EndGameListener() {
            @Override
            public void end(String player) {
            	JOptionPane.showMessageDialog(null, "Bên " + player + " thắng");
                stopGame();
            }
        });
        

        JPanel jPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(jPanel,BoxLayout.Y_AXIS);
        jPanel.setLayout(boxLayout);


        board.setPreferredSize(new Dimension(630, 630));

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(flowLayout);

        btnStart = new JButton("Start");
        //Timer và  TimerTask
        lblTime = new JLabel("00:00");
        bottomPanel.add(lblTime);
        bottomPanel.add(btnStart);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btnStart.getText().equals("Start")){
                    startGame();
                }else{
                    stopGame();
                }
            }
        });

        jPanel.add(board);
        jPanel.add(bottomPanel);

        JFrame jFrame = new JFrame("Cờ Caro ver 1.0");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.add(jPanel);


        jFrame.pack();
        //show ra frame
        jFrame.setVisible(true);
    }

    private static void startGame(){
        // hỏi ai chơi trước
        int choice = JOptionPane.showConfirmDialog(null, "O đi trước!", "Ai đi trước ?", JOptionPane.YES_NO_OPTION);
        board.reset();
        String currentPlayer = (choice == 0) ? Cell.O_VALUE : Cell.X_VALUE;
        board.setCurrentPlayer(currentPlayer);

        //đếm thời gian
        sec = 0;
        lblTime.setText("00:00");
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sec++;
                String s = String.format("%02d", (sec % 60)); 
                String m = String.format("%02d", (sec / 60)); 
                String value = m + ":" + s;
                lblTime.setText(value);
            }
        }, 1000, 1000);

        btnStart.setText("Stop");
    }

    
    // dừng trò chơi
    private static void stopGame(){
        btnStart.setText("Start");
        sec = 0;
        lblTime.setText("00:00");
        timer.cancel();
        timer = new Timer();
        board.reset();
    }
}
