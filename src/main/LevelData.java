package main;

import entity.*;
import tilemap.TileManager;

import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private int levelId;

    private Player player;

    private GameWindow gW;

    private TileManager tileManager;

    private List<Entity> entitiesToDelete = new ArrayList<Entity>();
    private List<Projectile> projectiles = new ArrayList<Projectile>();
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Enemy> enemyToSpawn = new ArrayList<Enemy>();
    private List<Entity> effects = new ArrayList<Entity>();

    public LevelData(GameWindow gW, String tileDataPath, Vector2 mapSize){
        this.setgW(gW);
        setTileManager(new TileManager(gW, tileDataPath, mapSize));
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameWindow getgW() {
        return gW;
    }

    public void setgW(GameWindow gW) {
        this.gW = gW;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public void setTileManager(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public List<Entity> getEntitiesToDelete() {
        return entitiesToDelete;
    }

    public void setEntitiesToDelete(List<Entity> entitiesToDelete) {
        this.entitiesToDelete = entitiesToDelete;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Enemy> getEnemyToSpawn() {
        return enemyToSpawn;
    }

    public void setEnemyToSpawn(List<Enemy> enemyToSpawn) {
        this.enemyToSpawn = enemyToSpawn;
    }

    public List<Entity> getEffects() {
        return effects;
    }

    public void setEffects(List<Entity> effects) {
        this.effects = effects;
    }
}
