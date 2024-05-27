import java.util.*;
boolean firstFrameClick=true;
Boolean[] keys= new Boolean[14];
Table table;

int gameMode = 0;
float camX;
float camY;
Player player;
float xp=0;
int scrap=0;
float money=0;
float level=0;
int mapCode;
int respawnCode=0;
boolean noSave = true;
int day;
int month;

float spawnRate=1;
float enemyStrength;
float time;

ArrayList<MapObj> smallMap = new ArrayList<MapObj>();
ArrayList<MapObj> bigMap = new ArrayList<MapObj>();
ArrayList<Bullet> bullets = new ArrayList<Bullet>();
ArrayList<Prey> prey = new ArrayList<Prey>();
ArrayList<Prey> kills = new ArrayList<Prey>();
//ArrayList<Item> items = new ArrayList<Item>();
ArrayList<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();
ArrayList<Item> items = new ArrayList<Item>();
ArrayList<Mark> marks = new ArrayList<Mark>();

BossAltar activeAltar;
Boss activeBoss;
int bossKills=0;


void setup(){
  fullScreen(P2D);
  smooth(8);
  rectMode(CENTER);
  textAlign(CENTER, CENTER);
  frameRate(120);
  imageMode(CENTER);
  loadImg();
  table= loadTable("player.csv","header,csv");
  if(1==table.getInt(7,"value")) noSave=false;
  
  for(int i=0;i<keys.length;i++){
    keys[i]=false;
  }
  //marks.add(new FirstBossKill());
}

void draw(){
  background(255);
  if(gameMode==0){
    if(noSave) fill(100);
    else fill(200);
    rect(960, 450, 400, 100);
    textC("start",960, 450, 30, 0);
    if(buttonPressed(960, 450, 400, 100) && !noSave)startGame();
    
    for(int i = 0; i<marks.size();i++){
      marks.get(i).drawScreen(i);
    }
  }
  
  if(gameMode==1){
    time++;
    background(140,216,179);
    
    float camSpeedX=(player.posX-960-camX+(mouseX-960)*0.2)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
    float camSpeedY=(player.posY-540-camY+(mouseY-540)*0.4)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
    camX=camX+camSpeedX;
    camY=camY+camSpeedY;

        if(random(120)<=5){
          smallMap.clear();
          for(int i=0;i<bigMap.size();i++){
            if(distance(player.posX,player.posY,bigMap.get(i).posX,bigMap.get(i).posY) <2200) smallMap.add(bigMap.get(i));
          }
          Collections.sort(smallMap);
        }
        if(prey.size()<500 && (random(100)<40 || activeBoss==null)) spawnEnemy(false);
        
        for(int i=0;i<smallMap.size();i++){
          smallMap.get(i).draw();
        }
        for(int i=0;i<items.size();i++){
            items.get(i).drawMap();
        }
        if(keys[11] && firstFrameClick){ gameMode=5;firstFrameClick=false;}
        
        kills.clear();
        player.bulletHitsFrame=0;
        for(int i=0;i<bullets.size();i++){
          bullets.get(i).draw();
        }
        for(int i=0;i<enemyBullets.size();i++){
          enemyBullets.get(i).draw();
        }
        for(int i=0;i<prey.size();i++){
          prey.get(i).draw();
        }
        for(int i = 0; i<marks.size();i++){
          marks.get(i).drawIngame();
        }
        
        player.draw();
        if(activeBoss!=null) activeBoss.drawHP();
        player.drawHud();
      
      
      textC("fps:"+int(frameRate),50, 30, 18, 0);
      textC("x:"+int(player.posX),50, 50, 18, 0);
      textC("y:"+int(player.posY),50, 70, 18, 0);
      textC("timer: "+int(time/120/60)+":"+int((time/120)%60),50, 90, 18, 0);
    }
    
    if(gameMode==5){
      //playermenu
      if(keys[11] && firstFrameClick && player.kapaUsed<=level*2 && player.redKapaUsed<=(int)(level/5)){ gameMode=1;firstFrameClick=false;}
      player.drawMenu();
    }
    
  if (!keyPressed && !mousePressed)firstFrameClick=true;
  if (keyPressed || mousePressed )firstFrameClick=false;
}

void createMap(){
  bigMap.clear();
  smallMap.clear();
  for(int i = 0; i<210000;i++){
    bigMap.add(new MapObj());
  }
  for(int i = 0; i<6600;i++){
    bigMap.add(new CursedTree());
  }
  for(int i = 0; i<3300;i++){
    bigMap.add(new Mushroom());
  }
  for(int i = 0; i<11000;i++){
    bigMap.add(new Sapling());
  }
  for(int i = 0; i<45000;i++){
    bigMap.add(new Tree());
  }
  
  for(int i = 0; i<4400;i++){
    bigMap.add(new Chest());
  }
  
  for(int i = 0; i<30;i++){
    bigMap.add(new Hive());
  }
  for(int i = 0; i<4000;i++){
    bigMap.add(new Bombfass());
  }
  for(int i = 0; i<3000;i++){
    bigMap.add(new PoisonField());
  }

  spawnEnemy(true);
  for(int i=0;i<120*4;i++){
    spawnEnemy(false);
  }
  //Collections.sort(bigMap);
}

void startGame(){
  prey.clear();
  time=0;
  enemyBullets.clear();
  bullets.clear();
  player=new Player();
  bossKills=0;
  
  createMap();
  activeAltar = new BossAltar();
  activeBoss=null;
  bigMap.add(activeAltar);

  gameMode=1;
  
  for(int i=0;i<4;i++){
    new Crawler();
  }
  camX=-960;
  camY=-540;
  for(int i = 0; i<marks.size();i++){
    marks.get(i).startGame();
  }
  //money+=1000;
}
