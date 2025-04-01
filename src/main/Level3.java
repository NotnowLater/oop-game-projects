package main;

import entity.*;

public class Level3 extends LevelData{

    public Level3(GameWindow gW, String tileDataPath){
        super(gW, tileDataPath, new Vector2(497, 112));

        setLevelId(2);

        setPlayer(new Player(gW, new Vector2(22*48, 49*48)));
//        setPlayer(new Player(gW, new Vector2(458*48, 33*48)));


        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(2352, 2028)));
//        enemies.add(gW.enemyFactory.getEnemy(12, new Vector2(2352, 2028)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(2640, 2028)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(2928, 2028)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(4080, 2412)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(6960, 2268)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(7488, 2076)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(7296, 2076)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(8256, 2076)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(9216, 1164)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(17760, 2172)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(18672, 2172)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(20208, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(20784, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(17136, 1884)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(14400, 1644)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(13, new Vector2(21168, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(3360, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(4320, 1824)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(4800, 1776)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(18864, 1968)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(21024, 1392)));
//        enemies.add(gW.enemyFactory.getEnemy(11, new Vector2(21744, 1392)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(11, new Vector2(13536, 1152)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(4944, 2400)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(5664, 2400)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(6480, 2400)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(10128, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(12960, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(7008, 2256)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(19536, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(14592, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(16512, 1776)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(18000, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(18432, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(20352, 1584)));
//        enemies.add(gW.enemyFactory.getEnemy(14, new Vector2(22032, 1584)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(19584, 3504)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(14, new Vector2(7440, 2064)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(11664, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(12144, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(12624, 1452)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(15072, 1692)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(15648, 1644)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(15360, 1644)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(10512, 1356)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(12, new Vector2(19728, 3516)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(15, new Vector2(472 * 48, 59 * 48)));

        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(453 * 48, 55 * 48), new Vector2(22 * 48, 6 * 48), true, new BossRoomEnterTriggerAction(new Vector2(464 * 48, 55 * 48))));
//        enemies.add(new PlayerAreaTrigger(gW, new Vector2(453 * 48, 55 * 48), new Vector2(22 * 48, 5 * 48), new BossRoomEnterTriggerAction(new Vector2(464 * 48, 55 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(180 * 48, 18 * 48), new Vector2(5 * 48, 8 * 48),false, new ChangePlayerRespawnTriggerAction(new Vector2(181 * 48, 24 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(391 * 48, 63 * 48), new Vector2(12 * 48, 15 * 48),false, new ChangePlayerRespawnTriggerAction(new Vector2(395 * 48, 73 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(95 * 48, 40 * 48), new Vector2(6 * 48, 12 * 48),false, new ChangePlayerRespawnTriggerAction(new Vector2(95 * 48, 50 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(293 * 48, 24 * 48), new Vector2(5 * 48, 11 * 48),false, new ChangePlayerRespawnTriggerAction(new Vector2(295 * 48, 34 * 48))));

        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(461 * 48, 33 * 48)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(4464, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(8496, 1152)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(17376, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(19152, 3504)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(19776, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(22560, 1584)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(4032, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(6816, 2256)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(8256, 1296)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(13344, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(15408, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(17376, 2016)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(21936, 1584)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(22800, 1584)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(20016, 1680)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(4656, 2400)));
    }

}
