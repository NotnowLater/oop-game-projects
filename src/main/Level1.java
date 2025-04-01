package main;

import entity.*;

public class Level1 extends LevelData{

    public Level1(GameWindow gW, String tileDataPath){
        super(gW, tileDataPath, new Vector2(340, 84));

        setLevelId(0);

        setPlayer(new Player(gW, new Vector2(720, 3200)));
//        setPlayer(new Player(gW, new Vector2(260 * 48, 38 * 48)));
//        setPlayer(new Player(gW, new Vector2(307 * 48, 25 * 48)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(1968, 2948)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(3216, 2940)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(4080, 2940)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(4704, 2940)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(8976, 2796)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(10320, 1692)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(11280, 1836)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(10560, 588)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(0, new Vector2(10848, 588)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(2496, 2928)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(2976, 2928)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(5568, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(6528, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(7056, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(12816, 2784)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(12960, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(11424, 1824)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(7536, 1392)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(1, new Vector2(11856, 1200)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(8352, 2700)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(9408, 2940)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(11376, 3468)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(7296, 1404)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(10176, 3660)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(2, new Vector2(10896, 3468)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(3,new Vector2(322 * 48, 48 * 48)));

        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(301 * 48, 44 * 48), new Vector2(23 * 48, 6 * 48), true, new BossRoomEnterTriggerAction(new Vector2(313 * 48, 44 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(100 * 48, 55 * 48), new Vector2(5 * 48, 6 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(101 * 48, 59 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(190 * 48, 66 * 48), new Vector2(5 * 48, 6 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(191 * 48, 72 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(248 * 48, 31 * 48), new Vector2(5 * 48, 9 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(251 * 48, 38 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(233 * 48, 19 * 48), new Vector2(6 * 48, 6 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(236 * 48, 25 * 48))));

        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(5808, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(6096, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(6384, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(7680, 2496)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(7968, 2496)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(9408, 3168)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(9648, 3168)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(10896, 3168)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(5, new Vector2(11136, 3168)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(9744, 1488)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(10560, 1488)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(9072, 1536)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(8064, 816)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(8544, 528)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(9552, 432)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(10176, 384)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(11040, 432)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(12048, 864)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(12912, 816)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(6, new Vector2(12528, 912)));
//        enemies.add(gW.enemyFactory.getEnemy(257, new Vector2(1152, 3216)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(7536, 2796)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(7776, 1884)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(11424, 1212)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(11280, 2796)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(14496, 1212)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(4848, 2448)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(8208, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(9408, 2928)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(13056, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(15264, 1200)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(6720, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(7584, 1392)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(11952, 3456)));
    }

}
