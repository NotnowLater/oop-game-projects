package entity;

import main.GameWindow;
import main.Vector2;

public class EnemyFactory {

    private GameWindow gW;

    public EnemyFactory(GameWindow gW){
        this.gW = gW;
    }

    public Enemy getEnemy(int id, Vector2 worldPos){
        switch (id){
            case 0:
                return new BlueEnemy(gW, worldPos, "sprites/enemy.png");
            case 1:
                return new DolphinEnemy(gW, worldPos, "sprites/enemy2.png");
            case 2:
                return new PenguinEnemy(gW, worldPos, "sprites/enemy3.png");
            case 3:
                return new WhaleBossEnemy(gW, worldPos, "sprites/boss.png");
            case 4:
                return new BullEnemy(gW, worldPos, "sprites/enemy4.png");
            default:
                return new ExplosionEnemy(gW, worldPos, "sprites/explosion.png");
        }
    }

}
