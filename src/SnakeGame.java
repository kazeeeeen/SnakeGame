import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Tile bigFood = null;
    Tile poisonFood = null;

    Random random;
    Timer gameLoop;
    Timer bigFoodTimer;
    Timer poisonFoodTimer;
    Timer poisonFoodRemoveTimer;
    Timer bigFoodRemoveTimer;

    // For High Scoring
    int highScore = 0;

    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

        // Spawn big food every 60s
        bigFoodTimer = new Timer(30000, e -> spawnBigFood());
        bigFoodTimer.start();

        // Spawn poison food every 30s
        poisonFoodTimer = new Timer(40000, e -> spawnPoisonFood());
        poisonFoodTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw normal food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Draw big food
        if (bigFood != null) {
            g.setColor(Color.orange);
            g.fill3DRect(bigFood.x * tileSize, bigFood.y * tileSize, tileSize + 10, tileSize + 10, true);
        }

        // Draw poison food
        if (poisonFood != null) {
            g.setColor(new Color(138, 43, 226)); // purple
            g.fill3DRect(poisonFood.x * tileSize, poisonFood.y * tileSize, tileSize, tileSize, true);
        }

        // Draw snake head
        g.setColor(Color.white);
        int headX = snakeHead.x * tileSize;
        int headY = snakeHead.y * tileSize;
        g.fill3DRect(headX, headY, tileSize, tileSize, true);

        // Draw eyes
        g.setColor(Color.black);
        int eyeSize = 4;
        int padding = 4;
        g.fillOval(headX + padding, headY + padding, eyeSize, eyeSize);
        g.fillOval(headX + tileSize - eyeSize - padding, headY + padding, eyeSize, eyeSize);

        // Draw snake body
        g.setColor(Color.blue);
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Draw score
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize);
        } else {
            g.setColor(Color.green);
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }
        // Always show high score (even during game over)
        g.setColor(Color.orange);
        g.drawString("High Score: " + highScore, boardWidth - 150, tileSize);
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public void spawnBigFood() {
        bigFood = new Tile(random.nextInt(boardWidth / tileSize), random.nextInt(boardHeight / tileSize));

        // Stop existing timer if running
        if (bigFoodRemoveTimer != null) {
            bigFoodRemoveTimer.stop();
        }

        // Start 10-second timer to remove big food
        bigFoodRemoveTimer = new Timer(10000, e -> {
            bigFood = null;
            repaint();
        });
        bigFoodRemoveTimer.setRepeats(false);
        bigFoodRemoveTimer.start();
    }

    public void spawnPoisonFood() {
        poisonFood = new Tile(random.nextInt(boardWidth / tileSize), random.nextInt(boardHeight / tileSize));

        // Start 10-second timer to remove the poison food
        if (poisonFoodRemoveTimer != null) {
            poisonFoodRemoveTimer.stop();
        }

        poisonFoodRemoveTimer = new Timer(10000, e -> {
            poisonFood = null;
            repaint();
        });
        poisonFoodRemoveTimer.setRepeats(false);
        poisonFoodRemoveTimer.start();

    }

    public boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    public void move() {
        // Eat normal food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Eat big food
        if (bigFood != null && collision(snakeHead, bigFood)) {
            for (int i = 0; i < 5; i++) {
                snakeBody.add(new Tile(bigFood.x, bigFood.y));
            }
            bigFood = null;
            if (bigFoodRemoveTimer != null) {
                bigFoodRemoveTimer.stop();
            }
        }

        // Eat poison food
        if (poisonFood != null && collision(snakeHead, poisonFood)) {
            for (int i = 0; i < 3 && !snakeBody.isEmpty(); i++) {
                snakeBody.remove(snakeBody.size() - 1);
            }
            poisonFood = null;

            if (poisonFoodRemoveTimer != null) {
                poisonFoodRemoveTimer.stop();
            }
        }

        // Move body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile curr = snakeBody.get(i);
            if (i == 0) {
                curr.x = snakeHead.x;
                curr.y = snakeHead.y;
            } else {
                Tile prev = snakeBody.get(i - 1);
                curr.x = prev.x;
                curr.y = prev.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over on collision with self
        for (Tile t : snakeBody) {
            if (collision(snakeHead, t)) {
                gameOver = true;
                break;
            }
        }

        // Game over on wall hit
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
                snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            if (snakeBody.size() > highScore) {
                highScore = snakeBody.size();
            }
            gameLoop.stop();
            bigFoodTimer.stop();
            poisonFoodTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                // Reset game state
                snakeBody.clear();
                snakeHead.x = 5;
                snakeHead.y = 5;
                velocityX = 0;
                velocityY = 0;
                placeFood();
                bigFood = null;
                poisonFood = null;
                gameOver = false;

                // Restart timers
                gameLoop.start();
                bigFoodTimer.start();
                poisonFoodTimer.start();

                if (bigFoodRemoveTimer != null) {
                    bigFoodRemoveTimer.stop();
                    bigFoodRemoveTimer = null;
                }
                if (poisonFoodRemoveTimer != null) {
                    poisonFoodRemoveTimer.stop();
                    poisonFoodRemoveTimer = null;
                }
                repaint();
            }
            return; // Prevent movement keys from working during game over except SPACE
        }

        // Normal controls when game is running
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
