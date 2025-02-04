package main;

import entity.*;
import tilemap.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWindow extends JPanel implements Runnable {
    // ScreenSize
    // 1152 x 672
    // DO NOT CHANGE!!!
    public final int TILE_SIZE = 16;   // pixel tile size
    public final int TILE_SCALE = 3;   // tile scale
    // Game Standard Tile Size
    // DO NOT CHANGE!!!
    public int RENDER_TILE_SIZE = TILE_SIZE * TILE_SCALE;    // render tile size
    public final int SCREEN_TILE_WIDTH = 24;   // max tile on screen width
    public final int SCREEN_TILE_HEIGHT = 14;  // max tile on screen height
    public final int SCREEN_WIDTH = SCREEN_TILE_WIDTH * RENDER_TILE_SIZE; // actual screen pixel width
    public final int SCREEN_HEIGHT = SCREEN_TILE_HEIGHT * RENDER_TILE_SIZE;  // actual screen pixel height

    final int GAME_MAX_FPS = 60;
    final int GAME_MAX_UPS = 60;
    Thread gameThread;

    private Random rng = new Random(System.currentTimeMillis());
    public Util util = new Util(this);
    public TileManager tileManager = new TileManager(this);
    public KeyInputHandler keyInputHandler = new KeyInputHandler(this);
    public HUDManager hudManager = new HUDManager(this);

    private Vector2 viewportPosition = new Vector2(0, 0);
    private ViewportManager viewportManager = new ViewportManager(this);

    public Player player = new Player(this, new Vector2(780, 1402));
//    public Player player = new Player(this, new Vector2(18571, 602));

    public EnemyFactory enemyFactory = new EnemyFactory(this);

    public List<Entity> entitiesToDelete = new ArrayList<Entity>();
    public List<Projectile> projectiles = new ArrayList<Projectile>();
    public List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Enemy> enemyToSpawn = new ArrayList<Enemy>();
    public List<Enemy> effects = new ArrayList<Enemy>();

    public  GameWindow(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(10, 10, 10));
        this.setDoubleBuffered(true);   // prevent screen tearing
        this.addKeyListener(keyInputHandler);
        this.setFocusable(true);
    }

    public void initGameThread(){
        initGame();
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void initGame(){
//        player.setWorldPosition(new Vector2(18571, 602));
        enemies.add(enemyFactory.getEnemy(3,new Vector2(19585, 828)));
        enemies.add(new PlayerAreaCameraAxisLockTrigger(this, new Vector2(19200, 1455), new Vector2(2560, 48)));
        enemies.add(enemyFactory.getEnemy(2, new Vector2(1440, 1356)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(1680, 1356)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(1920, 1356)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(3264, 1356)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(5472, 1500)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(5760, 1500)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(6048, 1500)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7440, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7536, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7632, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7728, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7824, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(7920, 2076)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(10176, 1404)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(13776, 1308)));
        enemies.add(enemyFactory.getEnemy(0, new Vector2(17376, 540)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(2928, 1392)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(4176, 1392)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(6864, 2064)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(7344, 2064)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(7824, 2064)));
        enemies.add(enemyFactory.getEnemy(1, new Vector2(15792, 768)));
    }

    @Override
    public void run() {
        double drawFrameInterval = 1000000000 / GAME_MAX_FPS;
        double processFrameInterval = 1000000000 / GAME_MAX_UPS;
        double drawDeltaTime = 0;
        double updateDeltaTime = 0;
        long lastTime = System.nanoTime();
        long currTime;
        long fpsTimer = 0;
        // fps/ups
        int frameCount = 0;
        int updateCount = 0;

        while (gameThread != null){
            currTime = System.nanoTime();

            drawDeltaTime += (currTime - lastTime) / drawFrameInterval;
            updateDeltaTime += (currTime - lastTime) / processFrameInterval;
            fpsTimer += (currTime - lastTime);
            lastTime = currTime;
            if (updateDeltaTime >= 1.0){
                process();
                updateDeltaTime--;
                updateCount++;
            }
            if (drawDeltaTime >= 1.0){
//                process();
                repaint();
                drawDeltaTime--;
                frameCount++;
            }
            if (fpsTimer >= 1000000000){
//                System.out.println("FPS : " + frameCount + " UPS : " + updateCount);
                frameCount = 0;
                updateCount = 0;
                fpsTimer = 0;
            }

        }
    }

    // Main game loop process
    public void process(){
        // spawn enemy
        enemies.addAll(enemyToSpawn);
        enemyToSpawn.clear();

        viewportManager.process();
        // process all entity
        player.process();
        for (Projectile p : projectiles){
            p.process();
        }
        for (Enemy e : enemies){
            e.process();
        }
        for (Enemy ef : effects){
            ef.process();
        }
        // delete entities
        projectiles.removeAll(entitiesToDelete);
        enemies.removeAll(entitiesToDelete);
        effects.removeAll(entitiesToDelete);
    }
    // Main game Render
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
//        util.setGraphic2D(g2d);
        // render start
        tileManager.render(g2d);
        player.render(g2d);
        for (Enemy e : enemies){
            e.render(g2d);
        }
        for (Enemy ef : effects){
            ef.render(g2d);
        }
        for (Projectile p : projectiles){
            p.render(g2d);
        }
        hudManager.render(g2d);
        // render end
        g2d.dispose();
    }

    public Random getRng(){
        return rng;
    }

    public void addEnemyToSpawn(Enemy e){
        enemyToSpawn.add(e);
    }

    public Vector2 getViewportPosition() {
        return viewportPosition;
    }

    public void setViewportPosition(Vector2 newPos){
        viewportPosition = new Vector2(newPos);
    }

    public ViewportManager getViewportManager() {
        return viewportManager;
    }
}
