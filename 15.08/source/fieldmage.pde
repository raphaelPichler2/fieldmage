import java.util.*;
boolean firstFrameClick=true;
Boolean[] keys= new Boolean[14];
Table table;

///beeboss, new skills
int gameMode = 0;
float camX;
float camY;
Player player;
float xp=0;
int scrap=0;
float money=0;
int timeChrystal;
int greenChrystal;
float diamonds=0;
int enemiesKilled=0;
int moneyRound;
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
ArrayList<Skin> skins = new ArrayList<Skin>();
ArrayList<Pickup> pickups = new ArrayList<Pickup>();
int skinPicked=0;

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
  addSkins();
  table= loadTable("player.csv","header,csv");
  if(1==table.getInt(7,"value")) noSave=false;
  loadGameState();
  for(int i=0;i<keys.length;i++){
    keys[i]=false;
  }
  
  //marks.add(new FirstBossKill());
}

void draw(){
  pushMatrix();
  
  background(255);
  if(gameMode==0){
    image(menuI,1920/2,1080/2);
    if(buttonPressed(960, 450, 400, 100) && !noSave && skins.get(skinPicked).unlocked )startGame();
    
    if(buttonPressed(960, 850, 400, 100) ){
      save();
      exit();
    }
    
    textC((int)money+"$",120, 100, 30, 0);
    textC((int)diamonds+"",120, 140, 30, 0);
    textC((int)timeChrystal+"",120, 180, 30, 0);
    textC((int)greenChrystal+"",120, 220, 30, 0);
    
    if(player!=null){
      
      textC("timer: "+int(time/120/60)+":"+int((time/120)%60),180, 260, 24, 0);
      textC((int)moneyRound+" coins",180, 290, 24, 0);
      textC((int)bossKills+" Bosses killed",180, 320, 24, 0);
      textC((int)enemiesKilled+" enemies killed",180, 350, 24, 0);
      textC((int)player.itemsEquipt.size()+" gray items",180, 380, 24, 0);
      
      for(int i=0;i<player.itemsGood.size();i++){
        float posXM=1220+80*(i%9);
        float posYM=350+80*(int)(i/9);
        player.itemsGood.get(i).drawSkinMenu(posXM,posYM);
      }
    }
    
    skins.get(skinPicked).drawMenu();
    if(buttonPressed(1920/2+200,200,150,150))skinPicked++;
    if(buttonPressed(1920/2-200,200,150,150))skinPicked--;
    if(skinPicked<0)skinPicked=skins.size()+skinPicked;
    skinPicked=skinPicked%skins.size();
    
    if(!skins.get(skinPicked).unlocked){
      if(diamonds<10) tint(155,155);
      else{
        if(buttonPressed(1090,105,100,100)){
          skins.get(skinPicked).unlocked=true;
          diamonds-=10;
        }
      }
      image(diamondBuyI,1920/2,1080/2);
      noTint();
    }
    
    newSkin();
  }
  
  if(gameMode==1){
    time++;
    background(140,216,179);
    
    float camSpeedX=(player.posX-960-camX+(mouseX-960)*0.1)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
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
        spawnEnemy(false);
        
        for(int i=0;i<smallMap.size();i++){
          smallMap.get(i).draw();
        }
        for(int i=0;i<items.size();i++){
            items.get(i).drawMap();
        }
        for(int i=0;i<pickups.size();i++){
            pickups.get(i).drawMap();
        }
        if(keys[11] && firstFrameClick){ gameMode=5;firstFrameClick=false;}
        
        kills.clear();
        player.bulletHitsFrame=0;
        
        player.gotDamaged=false;
        for(int i=0;i<enemyBullets.size();i++){
          enemyBullets.get(i).draw();
        }
        for(int i=0;i<prey.size();i++){
          prey.get(i).draw();
        }
        for(int i=0;i<bullets.size();i++){
          bullets.get(i).draw();
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
      if(int((time/120)%60)<10) textC("timer: "+int(time/120/60)+":0"+int((time/120)%60),50, 90, 18, 0);
      else textC("timer: "+int(time/120/60)+":"+int((time/120)%60),50, 90, 18, 0);
    }
    
    if(gameMode==5){
      //playermenu
      if(keys[11] && firstFrameClick){ gameMode=1;firstFrameClick=false;}
      player.drawMenu();
    }
    noCursor();
    float markSize=32;
    if(player!=null) markSize+=player.bulletSize*2-24;
    image(markI,mouseX,mouseY,markSize,markSize);
    if(markSize>55)image(markI,mouseX,mouseY,25,25); 
    
  if (!keyPressed && !mousePressed)firstFrameClick=true;
  if (keyPressed || mousePressed )firstFrameClick=false;
  popMatrix();
}

void createMap(){
  bigMap.clear();
  smallMap.clear();
  for(int i = 0; i<350100;i++){
    bigMap.add(new MapObj());
  }
  for(int i = 0; i<9500;i++){
    bigMap.add(new CursedTree());
  }
  for(int i = 0; i<3500;i++){
    bigMap.add(new Mushroom());
  }
  for(int i = 0; i<25000;i++){
    bigMap.add(new Sapling());
  }
  for(int i = 0; i<55000;i++){
    bigMap.add(new Tree());
  }
  
  for(int i = 0; i<4100;i++){
    bigMap.add(new Chest());
  }
  
  for(int i = 0; i<30;i++){
    bigMap.add(new Hive());
  }
  for(int i = 0; i<5000;i++){
    bigMap.add(new Bombfass());
  }
  for(int i = 0; i<3000;i++){
    bigMap.add(new PoisonField());
  }
  for(int i = 0; i<12000;i++){//+1000 pro item
    bigMap.add(new Trader());
  }
  
  for(int i = 0; i<1400;i++){
    bigMap.add(new Vase());
  }
  for(int i = 0; i<800;i++){
    bigMap.add(new Camp());
  }
  
  for(int i = 0; i<14;i++){
    bigMap.add(new CrazyTrader());
  }
  
  new Wanderer(-16000,-100,+22000,-100,new Dash(0,0));
  new Wanderer(+22000,22000,-22000,100,new RocketShooter(0,0));
  
  /*new Wanderer(-100,-32000,-100,+32000,new Dash(0,0));
  new Wanderer(100,+32000,100,-32000,new RocketShooter(0,0));*/

  spawnEnemy(true);

  //Collections.sort(bigMap);
}

void startGame(){
  prey.clear();
  time=0  +timeChrystal*9*120  + greenChrystal*45*120;//120*60*36
  moneyRound=0;
  enemyBullets.clear();
  bullets.clear();
  items.clear();
  pickups.clear();
  player=new Player();
  bossKills=0   +greenChrystal;//18
  enemiesKilled=0;
  
  createMap();
  activeAltar = new BossAltar();
  activeBoss=null;
  bigMap.add(activeAltar);

  gameMode=1;
  
  for(int i=0;i<5;i++){
    new Crawler();
  }
  camX=-960;
  camY=-540;
  for(int i = 0; i<marks.size();i++){
    marks.get(i).startGame();
  }
  //money+=1000;
  float angle=0;
  for(int i = 0; i<timeChrystal;i++){
    items.add(randomLoot(0+200*cos(angle),0+200*sin(angle)));
    angle+=TWO_PI/timeChrystal;
  }
  timeChrystal=timeChrystal/4;
  
  angle=0;
  for(int i = 0; i<greenChrystal;i++){
    items.add(randomGoodLoot(0+140*cos(angle),0+140*sin(angle)));
    angle+=TWO_PI/greenChrystal;
  }
  greenChrystal=greenChrystal/4;
}
