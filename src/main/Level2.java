package main;

import entity.*;

public class Level2 extends LevelData{

    public Level2(GameWindow gW, String tileDataPath){
        super(gW, tileDataPath, new Vector2(208, 160));

        setLevelId(1);

        setPlayer(new Player(gW, new Vector2(57*48, 45*48)));
//        setPlayer(new Player(gW, new Vector2(31*48, 126*48)));

         
         getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3840, 2172)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4464, 2700)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4992, 3468)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(7200, 3228)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(8352, 2748)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(6240, 2220)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5472, 2220)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4656, 1116)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3456, 1212)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3024, 1260)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(2160, 1260)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(2160, 2748)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3216, 4524)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4992, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5328, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5616, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5952, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5808, 5964)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(5136, 5484)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4704, 5484)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(4224, 5484)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3792, 5484)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3408, 5388)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(3024, 5292)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(2640, 5196)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(2112, 5100)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(1776, 5100)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(32, new Vector2(1392, 5100)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(5808, 3216)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(6240, 3216)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(6960, 1920)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(4224, 1152)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(3744, 1104)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(3312, 1152)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(2928, 1248)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(3696, 4560)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(3696, 5424)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(3312, 5280)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(2928, 5184)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(2544, 5088)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(9, new Vector2(4032, 4560)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(6144, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(5616, 4572)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(1536, 2028)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(4464, 1500)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(5904, 2220)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(4224, 2700)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(7488, 3228)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(4, new Vector2(6672, 6012)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(2112, 1248)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(4560, 4512)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(4176, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(7536, 5088)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(6240, 6000)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(1440, 5088)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(20, new Vector2(3072, 4512)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(7152, 1968)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(5952, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(2928, 3264)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(1728, 4512)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(2352, 6048)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(864, 5616)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(8544, 2736)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(8496, 3216)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(3984, 1152)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(2928, 3264)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(1248, 4512)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(4464, 5472)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(864, 5616)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(2208, 5904)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(5568, 3216)));

        getEnemies().add(gW.getEnemyFactory().getEnemy(10, new Vector2(45 * 48, 145 * 48)));

        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(25 * 48, 141 * 48), new Vector2(22 * 48, 6 * 48), true, new BossRoomEnterTriggerAction(new Vector2(36 * 48, 141 * 48))));

        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(142 * 48, 60 * 48), new Vector2(5 * 48, 9 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(140 * 48, 67 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(53 * 48, 86 * 48), new Vector2(6 * 48, 9 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(44 * 48, 94 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(150 * 48, 117 * 48), new Vector2(6 * 48, 10 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(153 * 48, 125 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(45 * 48, 105 * 48), new Vector2(8 * 48, 20 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(29 * 48, 126 * 48))));
        
        
        
    }

}
