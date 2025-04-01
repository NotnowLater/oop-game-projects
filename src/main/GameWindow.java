package main;

import entity.*;
import tilemap.TileManager;

import javax.swing.*;
import java.awt.*;
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
    public final int RENDER_TILE_SIZE = TILE_SIZE * TILE_SCALE;    // render tile size
    public final int SCREEN_TILE_WIDTH = 24;   // max tile on screen width
    public final int SCREEN_TILE_HEIGHT = 14;  // max tile on screen height
    public final int SCREEN_WIDTH = SCREEN_TILE_WIDTH * RENDER_TILE_SIZE; // actual screen pixel width
    public final int SCREEN_HEIGHT = SCREEN_TILE_HEIGHT * RENDER_TILE_SIZE;  // actual screen pixel height

    private final int GAME_MAX_FPS = 60;
    private Thread gameThread;

    private Random rng = new Random(System.currentTimeMillis());
    private Util util = new Util(this);
//    private TileManager tileManager = new TileManager(this);
    private KeyInputHandler keyInputHandler = new KeyInputHandler(this);
    private HUDManager hudManager = new HUDManager(this);
    private SoundManager soundManager = new SoundManager();

    private Vector2 viewportPosition = new Vector2(0, 0);
    private ViewportManager viewportManager = new ViewportManager(this);

//    private Player player;
//    public Player player = new Player(this, new Vector2(18571, 602));

    private EnemyFactory enemyFactory = new EnemyFactory(this);

    // 0 main menu
    // 1 level
    private int currentGameState = 0;

    private MainMenu mainMenu = new MainMenu(this);
    private LevelSelect levelSelect = new LevelSelect(this);
    private WinScreen winScreen = new WinScreen(this);
    private PasswordScreen passwordScreen = new PasswordScreen(this);
    private EndScreen endScreen = new EndScreen(this);

    private boolean debugEnable = false;

    private LevelData currentLevel;
    private LevelData level1 = new Level1(this, "water");
    private LevelData level2 = new Level2(this, "fire");
    private LevelData level3 = new Level3(this, "grass"); //dew
    private LevelData level4 = new Level4(this, "dark");

    public  GameWindow(JFrame frame){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(10, 10, 10));
        this.setDoubleBuffered(true);   // prevent screen tearing
        this.addKeyListener(getKeyInputHandler());
        frame.setIconImage(getUtil().loadGraphic("sprites/icon.png"));
        this.setFocusable(true);
    }

    public void initGameThread(){
        initGame();
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void initGame(){


        getSoundManager().playBGM("main");
//        setCurrentGameState(6);

    }

    @Override
    public void run() {
        double drawFrameInterval = 1000000000.0 / GAME_MAX_FPS;
        double drawDeltaTime = 0;
        long lastTime = System.nanoTime();
        long currTime;
        while (gameThread != null){
            currTime = System.nanoTime();
            drawDeltaTime += (currTime - lastTime) / drawFrameInterval;
            lastTime = currTime;
            if (drawDeltaTime >= 1.0){
                process();
                repaint();
                drawDeltaTime--;
            }
        }
    }

    public synchronized void process(){
        getKeyInputHandler().process();
        switch (getCurrentGameState()) {
            case 0:
                getMainMenu().process();
                break;
            case 1:
                getLevelSelect().process();
                break;
            case 2:
                // animate the tileset
                getTileManager().process();
                getViewportManager().process();
                // process all entity
                getPlayer().process();
                for (Projectile p : getProjectiles()) {
                    p.process();
                }
                for (Enemy e : getEnemies()) {
                    e.process();
                }
                for (Entity ef : getEffects()) {
                    ef.process();
                }
            case 3:
                getHudManager().process();
                break;
            case 4:
                getWinScreen().process();
                break;
            case 5:
                getPasswordScreen().process();
                break;
            case 6:
                getEndScreen().process();
                break;
        }
        getKeyInputHandler().process();
    }

    public synchronized void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        // render start
        switch (getCurrentGameState()) {
            case 0:
                getMainMenu().render(g2d);
                break;
            case 1:
                getLevelSelect().render(g2d);
                break;
            case 2:
                // spawn enemy
                getEnemies().addAll(getEnemyToSpawn());
                getEnemyToSpawn().clear();
                // delete entities
                getProjectiles().removeAll(getEntitiesToDelete());
                getEnemies().removeAll(getEntitiesToDelete());
                getEffects().removeAll(getEntitiesToDelete());
                getTileManager().render(g2d);
                getPlayer().render(g2d);
                for (Enemy e : getEnemies()) {
                    e.render(g2d);
                }
                for (Entity ef : getEffects()) {
                    ef.render(g2d);
                }
                for (Projectile p : getProjectiles()) {
                    p.render(g2d);
                }
                getHudManager().render(g2d);
                break;
            case 3:
                // spawn enemy
                getEnemies().addAll(getEnemyToSpawn());
                getEnemyToSpawn().clear();
                // delete entities
                getProjectiles().removeAll(getEntitiesToDelete());
                getEnemies().removeAll(getEntitiesToDelete());
                getEffects().removeAll(getEntitiesToDelete());

                getTileManager().render(g2d);
                getPlayer().render(g2d);
                for (Enemy e : getEnemies()) {
                    e.render(g2d);
                }
                for (Entity ef : getEffects()) {
                    ef.render(g2d);
                }
                for (Projectile p : getProjectiles()) {
                    p.render(g2d);
                }
                getHudManager().render(g2d);
                break;
            case 4:
                getWinScreen().render(g2d);
                break;
            case 5:
                getPasswordScreen().render(g2d);
                break;
            case 6:
                getEndScreen().render(g2d);
                break;
        }
        // render end
        g2d.dispose();
    }

    public Random getRng(){
        return rng;
    }

    public void changeBGColorTo(Color newColor){
        if (newColor.getRGB() != getBackground().getRGB()){
            setBackground(newColor);
        }
    }

    public void addEnemyToSpawn(Enemy e){
        getEnemyToSpawn().add(e);
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

    public int getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(int currentGameState) {
        this.currentGameState = currentGameState;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public Player getPlayer() {
        return currentLevel.getPlayer();
    }

    public void setPlayer(Player player) {
        currentLevel.setPlayer(player);
    }

    public TileManager getTileManager() {
        return getCurrentLevel().getTileManager();
    }

    public void setTileManager(TileManager tileManager) {
        getCurrentLevel().setTileManager(tileManager);
    }

    public List<Entity> getEntitiesToDelete() {
        return getCurrentLevel().getEntitiesToDelete();
    }

    public void setEntitiesToDelete(List<Entity> entitiesToDelete) {
        getCurrentLevel().setEntitiesToDelete(entitiesToDelete);
    }

    public List<Projectile> getProjectiles() {
        return getCurrentLevel().getProjectiles();
    }

    public void setProjectiles(List<Projectile> projectiles) {
        getCurrentLevel().setProjectiles(projectiles);
    }

    public List<Enemy> getEnemies() {
        return getCurrentLevel().getEnemies();
    }

    public void setEnemies(List<Enemy> enemies) {
        getCurrentLevel().setEnemies(enemies);
    }

    public List<Enemy> getEnemyToSpawn() {
        return getCurrentLevel().getEnemyToSpawn();
    }

    public void setEnemyToSpawn(List<Enemy> enemyToSpawn) {
        getCurrentLevel().setEnemyToSpawn(enemyToSpawn);
    }

    public List<Entity> getEffects() {
        return getCurrentLevel().getEffects();
    }

    public void setEffects(List<Entity> effects) {
        getCurrentLevel().setEffects(effects);
    }

    public LevelSelect getLevelSelect() {
        return levelSelect;
    }

    public void setLevelSelect(LevelSelect levelSelect) {
        this.levelSelect = levelSelect;
    }

    public LevelData getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(LevelData currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public void setDebugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable;
    }

    public Util getUtil() {
        return util;
    }

    public void setUtil(Util util) {
        this.util = util;
    }

    public KeyInputHandler getKeyInputHandler() {
        return keyInputHandler;
    }

    public void setKeyInputHandler(KeyInputHandler keyInputHandler) {
        this.keyInputHandler = keyInputHandler;
    }

    public HUDManager getHudManager() {
        return hudManager;
    }

    public void setHudManager(HUDManager hudManager) {
        this.hudManager = hudManager;
    }

    public EnemyFactory getEnemyFactory() {
        return enemyFactory;
    }

    public void setEnemyFactory(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
    }

    public LevelData getLevel1() {
        return level1;
    }

    public void setLevel1(LevelData level1) {
        this.level1 = level1;
    }

    public LevelData getLevel2() {
        return level2;
    }

    public void setLevel2(LevelData level2) {
        this.level2 = level2;
    }

    public LevelData getLevel3() {
        return level3;
    }

    public void setLevel3(LevelData level3) {
        this.level3 = level3;
    }

    public LevelData getLevel4() {
        return level4;
    }

    public void setLevel4(LevelData level4) {
        this.level4 = level4;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public WinScreen getWinScreen() {
        return winScreen;
    }

    public void setWinScreen(WinScreen winScreen) {
        this.winScreen = winScreen;
    }

    public PasswordScreen getPasswordScreen() {
        return passwordScreen;
    }

    public void setPasswordScreen(PasswordScreen passwordScreen) {
        this.passwordScreen = passwordScreen;
    }

    public EndScreen getEndScreen() {
        return endScreen;
    }

    public void setEndScreen(EndScreen endScreen) {
        this.endScreen = endScreen;
    }
}
