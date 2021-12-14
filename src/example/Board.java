package example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Board extends JPanel {
	
	private static final int WIDTH_BOARD = 630, HEIGHT_BOARD = 630;
    private static final int WIDTH = 14, HEIGHT = 14;
    private static final int WIDTH_CELL = 45, HEIGHT_CELL = 45;
    
    
    private int x;	// tọa độ điểm đánh
    private int y;


    private EndGameListener endGame;
    
    private Image imgX;
    private Image imgO;
    private Image imgBoard;
    
    
    private Cell[][] matrix = new Cell[WIDTH][HEIGHT];
    private String currentPlayer = Cell.EMPTY_VALUE;

    public Board(String player){
        this();
        this.currentPlayer = player;
    }

    public Board(){
        this.init();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
             
                if(currentPlayer.equals(Cell.EMPTY_VALUE)){
                    return;
                }

                //phát ra âm thanh khi click
                soundClick();

                //tính toán xem đánh vào ô nào
                int xClick = e.getX();
                int yClick = e.getY();
                x = xClick / WIDTH_CELL;
                y = yClick / HEIGHT_CELL;
                
                if(matrix[x][y].getValue().equals(Cell.EMPTY_VALUE)) {
                	matrix[x][y].setValue(currentPlayer);
                	
                	// vẽ lại màn hình
                	repaint();
                	
                	// check win
                	if(checkWin(currentPlayer) && endGame != null) {
                		endGame.end(currentPlayer);
                	}
                	
                	// đổi lượt chơi
                	currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
                }
                
            }
        });
       
    }
    
    
    boolean checkWin(String currentPlayer) {
    	
    	if (checkRow(currentPlayer) || checkColum(currentPlayer) || checkCross(currentPlayer)) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    // check hàng
    boolean checkRow(String currentPlayer) {
    	int i = x;
    	int j = y;
    	int count = 1;
    	while((j - 1 >= 0) && matrix[i][j - 1].getValue().equals(currentPlayer)) {
    		j--;
    		count++;
    	}
    	j = y;
    	while((j + 1 <= WIDTH - 1) && matrix[i][j + 1].getValue().equals(currentPlayer)) {
    		j++;
    		count++;
    	}
    	if(count >= 5) {
    		return true;
    	}else {
    		return false;
    	}
    
    }
    
    // check cột
    boolean checkColum(String currentPlayer) {
    	int i = x;
    	int j = y;
    	int count = 1;
    	while((i - 1 >= 0) && matrix[i - 1][j].getValue().equals(currentPlayer)) {
    		i--;
    		count++;
    	}
    	i = x;
    	while((i + 1 <= WIDTH - 1) && matrix[i + 1][j].getValue().equals(currentPlayer)) {
    		i++;
    		count++;
    	}
    	if(count >= 5) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    // check đường chéo
    boolean checkCross(String currentPlayer) {
    	int i = x;
    	int j = y;
    	int count = 1;
    	while((i - 1 >= 0) && (j - 1 >= 0) && matrix[i - 1][j - 1].getValue().equals(currentPlayer)) {
    		i--;
    		j--;
    		count++;
    	}
    	i = x;
    	j = y;
    	while((i + 1 <= WIDTH - 1) && (j + 1 <= HEIGHT - 1) && matrix[i + 1][j + 1].getValue().equals(currentPlayer)) {
    		i++;
    		j++;
    		count++;
    		//System.out.println("fsdf");
    	}
    	if(count >= 5) {
    		return true;
    	}
    	
    	i = x;
    	j = y;
    	count = 1;
    	while((i - 1 >= 0) && (j + 1 <= HEIGHT - 1) && matrix[i - 1][j + 1].getValue().equals(currentPlayer)) {
    		i--;
    		j++;
    		count++;
    	}
    	i = x;
    	j = y;
    	while((i + 1 <= WIDTH - 1) && (j - 1 >= 0) && matrix[i + 1][j - 1].getValue().equals(currentPlayer)) {
    		i++;
    		j--;
    		count++;
    	}
    	if(count >= 5) {
    		return true;
    	}else {
    		return false;
    	}
    	
    	
    }
    
    
    // phát âm thanh khi click
    private synchronized void soundClick(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("soundClick.wav"));
                    clip.open(audioInputStream);
                    clip.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    // khởi tạo ma trận
    public void initMatrix(){
        for(int i = 0 ; i < WIDTH; i++){
            for(int j = 0 ; j < HEIGHT; j++){
                Cell cell = new Cell();
                matrix[i][j] = cell;
            }
        }
    }
    
    // khởi tạo dữ liệu ban đầu
    public void init() {
    	this.initMatrix();
    	 try{
             imgX = ImageIO.read(getClass().getResource("x.png"));
             imgO = ImageIO.read(getClass().getResource("o.png"));
             imgBoard = ImageIO.read(getClass().getResource("board.png"));
         }catch (Exception e){
             e.printStackTrace();
         }
    }
    


 
   // reset lại màn chơi
    public void reset(){
        this.initMatrix();
        this.setCurrentPlayer(currentPlayer);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
    	// vẽ board
    	g.drawImage(imgBoard, 0, 0, WIDTH_BOARD, HEIGHT_BOARD, null);
  
    	// vẽ O và X
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                if(matrix[i][j].getValue().equals(Cell.X_VALUE)){
                	g.drawImage(imgX, i * WIDTH_CELL + 10, j * HEIGHT_CELL + 10, WIDTH_CELL - 20, HEIGHT_CELL - 20, null);
                }else if(matrix[i][j].getValue().equals(Cell.O_VALUE)){
                    g.drawImage(imgO, i * WIDTH_CELL + 10, j * HEIGHT_CELL + 10, WIDTH_CELL - 20, HEIGHT_CELL - 20, null);
                }
            }
        }
    }

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setEndGame(EndGameListener endGame) {
		this.endGame = endGame;
	}

	
}
