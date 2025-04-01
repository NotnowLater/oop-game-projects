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
                return new BlueEnemy(gW, worldPos, "sprites/enemy.png", 1);
            case 1:
                return new DolphinEnemy(gW, worldPos, "sprites/enemy2.png", 1);
            case 2:
                return new PenguinEnemy(gW, worldPos, "sprites/enemy3.png", 1);
            case 3:
                return new WaterBossEnemy(gW, worldPos, "sprites/water_boss.png", 1);
            case 4:
                return new BullEnemy(gW, worldPos, "sprites/enemy4.png", 2);
            case 5:
                return new ShootDownEnemy(gW, worldPos, "sprites/enemy5.png", 1);
            case 6:
                return new BirdEnemy(gW, worldPos, "sprites/enemy6.png", 1);
            case 9:
                return new BirdShootingEnemy(gW, worldPos, "sprites/enemy9.png", 2);
            case 10:
                return new FireBossEnemy(gW, worldPos, "sprites/fire_boss.png", 2);
            case 11:
                return new BeeEnemy(gW, worldPos, "sprites/enemy11.png", 3);
            case 12:
                return new FrogEnemy(gW, worldPos, "sprites/enemy12.png", 3);
            case 13:
                return new SlimeEnemy(gW, worldPos, "sprites/enemy13.png", 3);
            case 14:
                return new SunFlowerEnemy(gW, worldPos, "sprites/enemy14.png", 3);
            case 15:
                return new GrassBossEnemy(gW, worldPos, "sprites/grass_boss.png", 3);
            case 20:
                return new FireDolphinEnemy(gW, worldPos, "sprites/enemy8.png", 2);
            case 21:
                return new DarkBlueEnemy(gW, worldPos, "sprites/dark_enemy.png", 0);
            case 22:
                return new DarkDolphinEnemy(gW, worldPos, "sprites/dark_enemy8.png", 0);
            case 23:
                return new DarkPenguinEnemy(gW, worldPos, "sprites/dark_enemy3.png", 0);
            case 24:
                return new DarkShootDownEnemy(gW, worldPos, "sprites/dark_enemy5.png", 0);
            case 25:
                return new DarkBirdEnemy(gW, worldPos, "sprites/dark_enemy6.png", 0);
            case 26:
               return new DarkBirdShootingEnemy(gW, worldPos, "sprites/dark_enemy9.png", 2);
            case 27:
                return new DarkBullEnemy(gW, worldPos, "sprites/dark_enemy4.png", 0);
            case 28:
                return new DarkBeeEnemy(gW, worldPos, "sprites/dark_enemy11.png", 0);
            case 29:
                return new DarkFrogEnemy(gW, worldPos, "sprites/dark_enemy12.png", 0);
            case 30:
                return new DarkSunFlowerEnemy(gW, worldPos, "sprites/dark_enemy14.png", 0);
            case 31:
                return new DarkBossEnemy(gW, worldPos, "sprites/dark_boss.png", 0);
            case 32:
                return new RedEnemy(gW, worldPos, "sprites/enemy7.png", 2);
            case 257:
                return new HpPowerUpEnemy(gW, worldPos, "sprites/power_up.png");
            case 258:
                return new EnergyPowerUpEnemy(gW, worldPos, "sprites/power_up.png");
            case 2048:
                return new PlayerDiedEffectEnemy(gW, worldPos, "sprites/player_explosion.png");
            default:
                return new ExplosionEnemy(gW, worldPos, "sprites/explosion.png");
        }
    }

}
