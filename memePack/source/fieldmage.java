import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class fieldmage extends PApplet {


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
int highscore;


public void setup(){
  
  
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

public void draw(){
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
    
    
    
    if(PApplet.parseInt((time/120)%60)<10) textC("timer: "+PApplet.parseInt(time/120/60)+":0"+PApplet.parseInt((time/120)%60),180, 260, 24, 0);
    else textC("timer: "+PApplet.parseInt(time/120/60)+":"+PApplet.parseInt((time/120)%60),180, 260, 24, 0);
    textC((int)moneyRound+" coins",180, 290, 24, 0);
    textC((int)bossKills+" Bosses killed",180, 320, 24, 0);
    textC((int)highscore+" highscore",180, 350, 24, 0);
    textC((int)enemiesKilled+" enemies killed",180, 380, 24, 0);
    if(player!=null){
      textC((int)player.itemsEquipt.size()+" gray items",180, 410, 24, 0);
      
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
    
    float camSpeedX=(player.posX-960-camX+(mouseX-960)*0.1f)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
    float camSpeedY=(player.posY-540-camY+(mouseY-540)*0.4f)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
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
      
      
      textC("fps:"+PApplet.parseInt(frameRate),50, 30, 18, 0);
      textC("x:"+PApplet.parseInt(player.posX),50, 50, 18, 0);
      textC("y:"+PApplet.parseInt(player.posY),50, 70, 18, 0);
      if(PApplet.parseInt((time/120)%60)<10) textC("timer: "+PApplet.parseInt(time/120/60)+":0"+PApplet.parseInt((time/120)%60),50, 90, 18, 0);
      else textC("timer: "+PApplet.parseInt(time/120/60)+":"+PApplet.parseInt((time/120)%60),50, 90, 18, 0);
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

public void createMap(){
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
  
  for(int i = 0; i<3500;i++){
    bigMap.add(new Chest());
  }
  for(int i = 0; i<1500;i++){
    bigMap.add(new BloodChest());
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
  for(int i = 0; i<13000;i++){//+1000 pro item
    bigMap.add(new Trader());
  }
  
  for(int i = 0; i<1400;i++){
    bigMap.add(new Vase());
  }
  for(int i = 0; i<800;i++){
    bigMap.add(new Camp());
  }
  
  for(int i = 0; i<15;i++){
    bigMap.add(new CrazyTrader());
  }
  
  new Wanderer(22000,22000,-22000,-22000,new RocketShooter(0,0));
  
  new Wanderer(-25000,0-400,+25000,0-400,new Dash(0,0));
  new Wanderer(+25000,0+400,-25000,0+400,new Dash(0,0));
  
  new Wanderer(0-400,-25000,0-400,+25000,new NinjaDash(0,0));
  new Wanderer(0+400,+25000,0+400,-25000,new NinjaDash(0,0));
  

  new Wanderer(-18000,-12000-400,+18000,-12000-400,new AxeThrow(0,0));
  new Wanderer(+18000,-12000+400,-18000,-12000+400,new AxeThrow(0,0));
  
  new Wanderer(-18000,12000-400,+18000,12000-400,new ItemDice(0,0));
  new Wanderer(18000,12000+400,-18000,-12000+400,new ItemDice(0,0));
  
  new Wanderer(-12000-400,18000,-12000-400,-18000,new Rage(0,0));
  new Wanderer(-12000+400,-18000,-12000+400,+18000,new Rage(0,0));
  
  new Wanderer(12000-400,18000,12000-400,-18000,new WhiteMonster(0,0));
  new Wanderer(12000+400,+18000,12000+400,18000,new WhiteMonster(0,0));
  
  
  /*new Wanderer(-100,-32000,-100,+32000,new Dash(0,0));
  new Wanderer(100,+32000,100,-32000,new RocketShooter(0,0));*/

  spawnEnemy(true);

  //Collections.sort(bigMap);
}

public void startGame(){
  prey.clear();
  time=0  +timeChrystal*8.5f*120  + greenChrystal*46*120;//120*60*36
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
    items.add(randomLoot(0+160*cos(angle),0+160*sin(angle)));
    angle+=TWO_PI/timeChrystal;
  }
  timeChrystal=timeChrystal/4;
  
  angle=0;
  for(int i = 0; i<greenChrystal;i++){
    items.add(randomGoodLoot(0+120*cos(angle),0+120*sin(angle)));
    angle+=TWO_PI/greenChrystal;
  }
  greenChrystal=greenChrystal/4;
}
class Bullet{
  
  float posX;
  float posY;
  float speedX;
  float speedY;
  float size;
  float dmg;
  float slowDuration;
  boolean main;
  float knockback;
  float maxRange=800;
  
  Bullet(){
    knockback=player.knockback;
    size=player.bulletSize;
    posX=player.posX;
    posY=player.posY+player.size*0.7f;
    float dist = distance(posX,posY,mouseX+camX,mouseY+camY);
    speedX=player.bulletSpeed*(mouseX+camX-posX)/dist;
    speedY=player.bulletSpeed*(mouseY+camY-posY)/dist;
    speedX=random(-player.spray,player.spray)/100*speedY+speedX;
    speedY=random(-player.spray,player.spray)/100*speedX+speedY;
    posX=posX + speedX*2;
    posY=posY + speedY*2;
    dmg=player.dmg+player.bonusShotdmg;
    slowDuration = player.slowDuration;
    main=true;
    bullets.add(this);
  }
  
  Bullet(int type){
    if(type == 0){
      size=18;
      dmg=player.dmg;
      slowDuration = player.slowDuration;
      posX=player.posX;
      posY=player.posY+player.size*0.7f;
      float angle = angle(posX,posY,camX+mouseX,camY+mouseY);
      angle+=random(TWO_PI*0.12f) - TWO_PI*0.12f*0.5f;
      speedX=player.bulletSpeed*cos(angle);
      speedY=player.bulletSpeed*sin(angle);
      posX=posX + speedX*2;
      posY=posY + speedY*2;
    }
    knockback=0;
    main=false;
    bullets.add(this);
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY) >maxRange)bullets.remove(this);
    move();
    hitPrey();
  }
  
  public void move(){
    posX+=speedX;
    posY+=speedY;
  }
  
  public void skin(){
    turnImgD(grayBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
  
  public void hitPrey(){
    for(int i=0;i<prey.size();i++){    
      if(posX+size+prey.get(i).size>prey.get(i).posX && posX<prey.get(i).posX+size+prey.get(i).size && posY+size+prey.get(i).size>prey.get(i).posY && posY<prey.get(i).posY+size+prey.get(i).size &&
      distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getHit(this);
        trigger();
      }
    }
  }
  
  public void trigger(){
    bullets.remove(this);
    if(main) player.bulletHitsFrame++;
  }
}

class SchrottBullet extends Bullet{
  float angle;
  SchrottBullet(float dmg){
    super(0);
    this.dmg=dmg;
    maxRange=330;
    knockback=player.knockback/6;
    speedX*=0.6f;
    speedY*=0.6f;
  }
  
  public void skin(){
    angle+=0.1f;
    tint(255, 100);
    turnImgD(explosionI,posX,posY,size*2,size*2,angle);
    tint(255, 255);
  }
}

class DaggerBullet extends Bullet{
  
  DaggerBullet(float angle){
    size=player.bulletSize+6;
    dmg=player.dmg*0.3f;
    angle+=random(TWO_PI*0.08f) - TWO_PI*0.08f*0.5f;
    speedX=0.6f*player.bulletSpeed*cos(angle);
    speedY=0.6f*player.bulletSpeed*sin(angle);
    posX=player.posX + speedX*2;
    posY=player.posY + speedY*2;
  }
  
  public void skin(){
    turnImgD(DaggerBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}

class Bubble extends PiercingBullet{
  float speed;
  float curveture;
  float ccurve;
  
  Bubble(){
    maxRange=1000;
    speed=random(1.1f)+1.1f;
    size=player.bulletSize+random(12);
    curveture= (random(10)-5.0f)/120.0f;
    ccurve=0;
  }
  
  public void move(){
    posX+=speedX*speed;
    posY+=speedY*speed;
    
    posX+=speedY*ccurve*speed;
    posY+=-speedX*ccurve*speed;
    ccurve+=curveture;
    
    speed*=1-(7.0f/120.0f);
    if(speed<0.04f)bullets.remove(this);
  }
  
  public void skin(){
    tint(255, 100);
    turnImgD(blueBulletI,posX,posY,size*3,size*3,0);
    tint(255, 255);
  }
}

class CheesBullet extends Bullet{
  float angle;
  CheesBullet(){
    main=false;
    //angle=random(TWO_PI);
    size=player.bulletSize+30;
    dmg=player.dmg;
    knockback=player.knockback;
    maxRange=1000;
  }
  
  public void skin(){
    //angle+=0.01;
    turnImgD(cheeseBulletI,posX,posY,size*2,size*2,angle);
  }
}

class BoltBullet extends Bullet{
  Prey p;
  float dist;
  BoltBullet(float dmg, float posX,float posY,Prey p){
    this.dmg=dmg;
    this.p=p;
    this.posX=posX;
    this.posY=posY;
    this.size=16;
    knockback=0;
    main=false;
    dist=400;
  }
  BoltBullet(float dmg, float posX,float posY,float dist){
    this.dmg=dmg;
    this.dist=dist;
    this.posX=posX;
    this.posY=posY;
    newEnemy();
    this.size=16;
    knockback=0;
    main=false;
  }
  
  public void newEnemy(){
    if(prey.size()>0){
      Prey nearest=prey.get(0);
      for(int j = 1; j<prey.size();j++){
        if( distance(posX,posY,prey.get(j).posX,prey.get(j).posY) < distance(posX,posY,nearest.posX,nearest.posY)){
          nearest=prey.get(j);
        }
      }
      if(distance(posX,posY,nearest.posX,nearest.posY)<dist){
        this.p=nearest;
      }else{
        bullets.remove(this);
      }
    }else bullets.remove(this);  
  }
  
  public void draw(){
    skin();
    if(!prey.contains(p))newEnemy();
    float currdist = distance(posX,posY,p.posX,p.posY);
    speedX=6*(p.posX-posX)/currdist;
    speedY=6*(p.posY-posY)/currdist;
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    hitPrey();
  }
  public void skin(){
    turnImgD(streamBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
}

class BloodSpark extends BoltBullet{
  BloodSpark(float dmg,float dist){
    super(dmg,player.posX,player.posY,dist);
  }
  public void skin(){
    imageD(redBulletI,posX,posY,size*2);
  }
}

class Rocket extends Bullet{
  float explosionRadius;

  Rocket(){
    super();
    size=20+player.bulletSize;
    dmg=player.dmg;
    explosionRadius=180+player.bulletSize*4;
    maxRange=1000;
    main=true;
    posX+=speedX*2;
    posY+=speedY*2;
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >maxRange)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if( distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        bullets.remove(this);
        new Explosion(posX,posY,dmg,explosionRadius);
      }
    }
    
    for(int i=0;i<kills.size();i++){
      if( distance(posX,posY,kills.get(i).posX,kills.get(i).posY) < (size+kills.get(i).size)/2 ){
        bullets.remove(this);
        new Explosion(posX,posY,dmg,explosionRadius);
      }
    }
  }
  public void trigger(){
    bullets.remove(this);
    new Explosion(posX,posY,dmg,explosionRadius);
  }
  public void skin(){
    turnImgD(rocketTechI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
}

class StickyBomb extends Bullet{
  float explosionRadius;
  boolean stick;
  int timer;
  Prey p;

  StickyBomb(){
    super();
    size=18;
    dmg=player.dmg;
    explosionRadius=65+player.bulletSize*2;
    maxRange=1000;
    main=true;
    posX+=speedX*2;
    posY+=speedY*2;
    timer=120*2;
  }
  
  public void draw(){
    skin();
    if(stick){
      posX=p.posX+speedX;
      posY=p.posY+speedY;
      if(p==null || kills.contains(p) || p.hp<1){
        stick=false;
        speedX=0;
        speedY=0;
      }
    }else{
      posX+=speedX;
      posY+=speedY;
      for(int i=0;i<prey.size();i++){
      if( distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        p=prey.get(i);
        stick=true;
        speedX=posX-p.posX;
        speedY=posY-p.posY;
      }
    }
    }
    timer--;
    if(timer<=0){
      new Explosion(posX,posY,dmg,explosionRadius);
      bullets.remove(this);
    }
    if(distance(posX,posY,player.posX,player.posY) >maxRange)bullets.remove(this);
  }

  public void skin(){
    turnImgD(spikeBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
  
  public void trigger(){
    if(stick==false){
      speedX=0;
      speedY=0;
    }
  }
}

class Explosion extends Bullet{
  float timer;
  ArrayList<Prey> hits = new ArrayList<Prey>();
  
  Explosion(float posX,float posY,float dmg,float explosionRadius){
    super();
    this.posX=posX;
    this.posY=posY;
    speedX=0;
    speedY=0;
    this.dmg=dmg;
    this.size=explosionRadius;
    timer=15;
    knockback=0;
  }
  
  public void draw(){
    timer--;
    if(timer<=0) bullets.remove(this);
    imageD(explosionI,posX,posY,size*2);
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        hits.add(prey.get(i));
        prey.get(i).getHit(this);
      }
    }
  }
  public void trigger(){
    if(main) player.bulletHitsFrame++;
  }
}

class Gas extends Bullet{
  float timer;
  
  Gas(float posX,float posY,float dmg,float explosionRadius){
    super();
    this.posX=posX;
    this.posY=posY;
    speedX=0;
    speedY=0;
    this.dmg=dmg;
    this.size=explosionRadius;
    timer=2.7f*120;
    knockback=0;
  }
  
  public void draw(){
    timer--;
    if(timer<=0) bullets.remove(this);
    size+=100.0f/120/2.7f;
    
    tint(255,5+180*timer/2.7f/120);
    imageD(posionI,posX,posY,size*2);
    noTint();
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if( distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getPoisond(dmg/120.0f);
      }
    }
  }
  public void trigger(){
    if(main) player.bulletHitsFrame++;
  }
}

class FFExplosion extends Explosion{
  boolean playerHit;
  FFExplosion(float posX,float posY,float dmg,float explosionRadius){
    super( posX, posY, dmg, explosionRadius);
  }
  public void draw(){
    timer--;
    if(timer<=0) bullets.remove(this);
    imageD(explosionI,posX,posY,size*2);
    posX+=speedX;
    posY+=speedY;
    float dist=distance(posX,posY,player.posX,player.posY) ;
    if(dist>1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        hits.add(prey.get(i));
        prey.get(i).getHit(this);
      }
    }
    if(!playerHit && dist < (size+player.size)/2 ){
        player.getDmg(dmg/enemyStrength*0.6f +dmg*0.4f);
        playerHit=true;
    }
  }
}
class PiercingBullet extends Bullet{

  ArrayList<Prey> hits = new ArrayList<Prey>();
  
  PiercingBullet(){
    super();
    this.size=5+player.bulletSize;
    knockback=0;
  }
  
  public void draw(){
    skin();
    move();
    
    if(distance(posX,posY,player.posX,player.posY) >maxRange)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        hits.add(prey.get(i));
        prey.get(i).getHit(this);
      }
    }
  }
  public void trigger(){
    if(main) player.bulletHitsFrame++;
  }
}

class RavenBulletPlayer extends Bullet{
  int curveTime;
  float curve;
  float multi;
  ArrayList<Prey> hits = new ArrayList<Prey>();
  
  RavenBulletPlayer(){
    float dist = distance(posX,posY,mouseX+camX,mouseY+camY);
    speedX=4*(mouseX+camX-posX)/dist;
    speedY=4*(mouseY+camY-posY)/dist;
    this.dmg=player.dmg*3.0f;
    this.size=55+player.bulletSize;
    knockback=0;
    multi=1;
    if(random(100)<50)multi=-1;
    
    curveTime=(int)(distance(this.posX,this.posY,mouseX+camX,mouseY+camY) /3);
    curve=-multi*curveTime*1.0f/120.0f;
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    posX-=speedY*curve;
    posY+=speedX*curve;
    curve+=2/120.0f *multi;
    curveTime--;
    
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        hits.add(prey.get(i));
        prey.get(i).getHit(this);
      }
    }
  }
  public void trigger(){
    if(main) player.bulletHitsFrame++;
  }
  public void skin(){
    turnImgD(ravenBulletI,posX,posY,size*2,size*2,PI+angle(0,0,speedX-speedY*curve,speedY+speedX*curve));
  }
}


class ElephantBullet extends Bullet{
  float angle;
  ElephantBullet(){
    super();
    this.size=15+player.maxHp*0.5f;
    dmg=player.maxHp*0.5f;
    knockback=50.0f*dmg/10.0f;
    speedX*=0.5f;
    speedY*=0.5f;
    maxRange=900;
  }
  
  public void hitPrey(){
    for(int i=0;i<prey.size();i++){    
      if(posX+size+prey.get(i).size>prey.get(i).posX && posX<prey.get(i).posX+size+prey.get(i).size && posY+size+prey.get(i).size>prey.get(i).posY && posY<prey.get(i).posY+size+prey.get(i).size &&
      distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        float newDmg=dmg-prey.get(i).hp;
        prey.get(i).getHit(this);
        if(newDmg>0){
          dmg=newDmg;
          size=15+newDmg;
        }else trigger();
      }
    }
  }
  
   public void skin(){
     if(speedX>0) angle+=1.3f*TWO_PI/120;
      else angle-=1.3f*TWO_PI/120;
    turnImgD(schrottBulletI,posX,posY,size*2,size*2,angle);
  }
}

class AxeBullet extends Bullet{
  boolean powerUp;
  float angle;
  ArrayList<Prey> hits = new ArrayList<Prey>();
  boolean landed;
  int timer;
  float targetX;
  float targetY;
  AxeThrow t;
  
  AxeBullet(AxeThrow t){
    super();
    this.t=t;
    this.size=60+player.bulletSize*2;
    dmg=player.dmg*2.5f;
    knockback=0;
    speedX*=0.6f;
    speedY*=0.6f;
    targetX=mouseX+camX;
    targetY=mouseY+camY;
    powerUp=true;
  }
  
  
  public void draw(){
    skin();

    if(landed){
      timer--;
      if(timer<0)bullets.remove(this);
      if(dist(player.posX,player.posY,posX,posY)<= (size+player.size)/2){
        bullets.remove(this);
        t.cdLeft-=120*12;
      }
    }else{
      move();
      if(speedX>0) angle+=4*TWO_PI/120;
      else angle-=4*TWO_PI/120;
      if(dist(posX,posY,targetX,targetY)< dist(0,0,speedX,speedY)){
        timer=120*15;
        landed=true;
      }
      for(int i=0;i<prey.size();i++){
        if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
          hits.add(prey.get(i));
          prey.get(i).getHit(this);
          if(powerUp && prey.get(i).hp>1){
            powerUp=false;
            prey.get(i).getHit(this);
          }
        }
      }
    }
  }
  
  public void trigger(){
    if(main) player.bulletHitsFrame++;
  }
  
  public void skin(){
    turnImgD(axeMapI,posX,posY,size*2,size*2,angle);
  }
}


class OrbitalBullet extends Bullet{
  float dist;
  float angle;
  int time;
  
  OrbitalBullet(){
    dist = 240;
    angle= random(TWO_PI);
    this.dmg=player.dmg*1.0f;
    this.size=45;
    knockback=0;
  }
  
  public void draw(){
    skin();
    angle+=TWO_PI/120/4;
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
    
    time++;
    if(time > 8*120)bullets.remove(this);
    
    hitPrey();
    
    for(int i=0;i<enemyBullets.size();i++){
      if(distance(posX,posY,enemyBullets.get(i).posX,enemyBullets.get(i).posY) < (size+enemyBullets.get(i).size)/2){
        bullets.remove(this);
        enemyBullets.remove((enemyBullets.get(i)));
        i--;
      }
    }
  }
  public void skin(){
    turnImgD(spikeBulletI,posX,posY,size*2,size*2,0);
  }
}


class EnemyBullet{
  
  float dmg;
  float posX;
  float posY;
  float speedX;
  float speedY;
  float size;
  float time;

  EnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    this.dmg=dmg;
    this.posX=posX;
    this.posY=posY;
    this.speedX=speedX*0.9f;
    this.speedY=speedY*0.9f;
    this.size=size;
    enemyBullets.add(this);
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        player.getDmg(dmg);
        enemyBullets.remove(this);
    }
  }
  
  public void skin(){
    imageD(blueBulletI,posX,posY,size*4);
  }
}

class RedEnemyBullet extends EnemyBullet{
  RedEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  public void skin(){
    imageD(redBulletI,posX,posY,size*4);
  }
}

class CrossEnemyBullet extends EnemyBullet{
  float angle;
  CrossEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  public void skin(){
    angle+=0.1f;
    turnImgD(crossBulletI,posX,posY,size*4,size*4,angle);
  }
}

class StraightEnemyBullet extends EnemyBullet{
  StraightEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  public void skin(){
    turnImgD(streamBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}


class EnemyPoisonBullet extends EnemyBullet{
  float timer;
  EnemyPoisonBullet(float dmg,float posX,float posY,float speedX,float speedY,float size,float timer){
    super( dmg, posX, posY, speedX, speedY, size);
    this.timer=timer;
  }
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        explode();
    }
    timer--;
    if(timer<=0)explode();
  }
  
  public void explode(){
    bigMap.add(new PoisonField(posX,posY));
    smallMap.add(new PoisonField(posX,posY));
    enemyBullets.remove(this);
  }
  
  public void skin(){
    turnImgD(greenBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}


class SplitEnemyBullet extends EnemyBullet{
  float timer;
  float maxTimer;
  float speedMulti;
  SplitEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size,float timer){
    super( dmg, posX, posY, speedX, speedY, size);
    this.timer=timer;
    maxTimer=timer;
    speedMulti=1.6f;
  }
  public void draw(){
    skin();
    speedMulti=0.5f+ 1.5f* timer/maxTimer;
    posX+=speedX*speedMulti;
    posY+=speedY*speedMulti;
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        player.getDmg(dmg);
        enemyBullets.remove(this);
    }
    timer--;
    if(timer<=0)explode();
  }
  
  public void explode(){
    new EnemyBullet(dmg, posX+speedY*3, posY-3*speedX  ,speedY*1.5f,-1.5f*speedX,size*0.5f);
    new EnemyBullet(dmg, posX-3*speedY, posY+speedX*3,-1.5f*speedY,speedX*1.5f,size*0.5f);
    
    enemyBullets.remove(this);
  }
  
  public void skin(){
    turnImgD(greenBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}
float mapSize=100100;


class MapObj implements Comparable<MapObj>{
  
  float posX;
  float posY;
  float size;
  int type;
  int c;
  boolean flip;
  float hp;
  float maxHp;
  float state;
  
  MapObj(){
    posX=random(-mapSize,mapSize);
    posY=random(-mapSize,mapSize);
    size=60;
    type=(int)random(5);
    if(random(10)>8){
      type=10+(int)random(2);
    }
    this.posY=posY-size/2;
  }
  
  public int compareTo(MapObj m){
    return (int)(10*(posY+size/2-m.posY-m.size/2)); 
  }
  
  public void newDay(){
  }
  
  public void draw(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    switch(type){
    case 0:
      image(floorI,0,0,size,size);
      break;
    case 1:
      image(floor1I,0,0,size,size);
      break;
    case 2:
      image(floor2I,0,0,size,size);
      break;
    case 3:
      image(floor3I,0,0,size,size);
      break;
    case 4:
      image(spikeI,0,0,size,size);
      break;
    case 10:
      image(stoneI,0,0,120,120);
      break;
    case 11:
      image(grasI,0,0,size,size);
      break;
    case 12:
      image(pilzI,0,0,200,200);
      break;
    case 14:
      image(pilzI,0,0,size,size);
      break;
    case 15:
      image(weirdGrasI,0,0,size,size);
      break;
    case 21:
      image(weirdPilzI,0,0,size,size);
      break;
    case 22:
      image(tree2I,0,0,size,size);
      break;
    case 23:
      image(treeI,0,0,size,size);
      break;
    case 24:
      image(weirdTreeI,0,0,size,size);
      break;
    }
    popMatrix();
  }
}

class Tree extends MapObj{
  Tree(){
    super();
    size=240;
    type=5;
    if(random(100)<25)state=1;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to harvest",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.hp+=0.05f*player.maxHp;
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    if(state==1) image(treeRipeI,0,0,size,size);
    else image(treeI,0,0,size,size);
    popMatrix();
  }
}


class CursedTree extends Tree{
  
  CursedTree(){
    type=6;
    state=0;
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(tree2I,0,0,size,size);
    popMatrix();
  }
  
  public void newDay(){
    if(random(100)<25) bigMap.remove(this);
  }
}

class Sapling extends Tree{
  
  Sapling(){
    super();
    size=100;
    type=7;
    state=0;
  }
  Sapling(float posX,float posY){
    super();
    size=100;
    this.posX=posX;
    this.posY=posY;
    type=7;
    state=0;
  }
  
  public void newDay(){
    if(random(100)<20){
      Tree t=new Tree();
      t.posX=this.posX;
      t.posY=this.posY;
      bigMap.add(t);
      bigMap.remove(this);
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(saplingI,0,0,size,size);
    popMatrix();
  }
}

class Mushroom extends MapObj{
  Mushroom(){
    super();
    size=150;
    state=1;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to harvest",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.mana+=player.manaRegen*120*10;
        smallMap.remove(this);
        bigMap.remove(this);
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(pilzI,0,0,size,size);
    popMatrix();
  }
}

class Chest extends MapObj{
  Chest(){
    super();
    size=140;
    state=1;
    type=9;
  }
  Chest(float posX,float posY){
    super();
    size=180;
    state=1;
    this.posX=posX;
    this.posY=posY;
    type=9;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to open",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        if(random(1)<greenChest) items.add(randomGoodLoot(posX,posY-50));
        else items.add(randomLoot(posX,posY-50));
      }
    }
  }
  
  public void newDay(){
    state=1;
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(state==1) image(chestI,0,0,size,size);
    else image(chestOpenI,0,0,size,size);
    popMatrix();
  }
}

class BloodChest extends Chest{
  BloodChest(){
    super();
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to open (-25% hp)",player.posX+60,player.posY+60,24,0);
      if(keys[5] && player.hp>5+player.maxHp*0.25f){
        state=0;
        player.hp-=player.maxHp*0.25f;
        if(random(1)<greenChest) items.add(randomGoodLoot(posX,posY-50));
        else items.add(randomLoot(posX,posY-50));
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(state==1) image(redChestI,0,0,size,size);
    else image(openredChestI,0,0,size,size);
    popMatrix();
  }
}

class Hive extends MapObj{
  float hp;
  float maxHp;
  int nextBee;
  ArrayList<Bullet> hits = new ArrayList<Bullet>();
  Hive(){
    super();
    size=120;
    type=19;
    maxHp=6;
    hp=maxHp;
    nextBee=120*4;
    float dist=8400;
    if(random(100)<80)dist*=2;
    float angle=random(TWO_PI);
    posX=dist*cos(angle);
    posY=dist*sin(angle);
  }
  
  public void draw(){
    skin();
    nextBee--;
    if(nextBee<=0 && dist(posX,posY,player.posX,player.posY)<1500){
      nextBee=120*4;
      new Bee(posX,posY);
    }
    for(int i=0;i<bullets.size();i++){
      if(!hits.contains(bullets.get(i)) && distance(posX,posY,bullets.get(i).posX,bullets.get(i).posY) < (size+bullets.get(i).size)/2 ){
        hits.add(bullets.get(i));
        hp-=1;
        bullets.get(i).trigger();
      }
    }
    if(hp<=0){
      bigMap.remove(this);
      smallMap.remove(this);
      for(int i=0;i<3;i++){
        new Bee(posX+random(-60,60),posY+random(-60,60));
        items.add(randomLoot(posX+random(-60,60),posY+random(-60,60)));
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(HiveI,0,0,size*2,size*2);
    popMatrix();
    
    fill(255);
    rectD(posX, posY+size/2+40,size,10);
    fill(0xffB40000);
    rectD(posX+(hp/(maxHp)-1)*size/2, posY+size/2+40,size*hp/(maxHp),10);
  }
}

class BossAltar extends MapObj{
  BossAltar(){
    super();
    size=280;
    
    do{float dist=5000;
    float angle=random(TWO_PI);
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
    }while(posX<-mapSize || posX>mapSize || posY<-mapSize || posY>mapSize);
    state=1;
    type=9;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to start bossfight",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        int r=(int)random(6);
        if(r==0) new RavenBoss();
        if(r==1) new CaveDweller();
        if(r==2) new Fish();
        if(r==3) new RatKing();
        if(r==4) new HiveBoss();
        if(r==5) new BeeBoss();
      }
    }
  }
  
  public void newDay(){
    state=1;
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(state==0) image(ravenTentI,0,0,size,size);
    else image(ravenTentI,0,0,size,size);
    popMatrix();
  }
}


class Bombfass extends MapObj{
  float hp;
  ArrayList<Bullet> hits = new ArrayList<Bullet>();
  Bombfass(){
    super();
    size=50;
    hp=1;
    if(random(100)<30){
      float angle=random(TWO_PI);
      float dist = 50;
      new Bombfass( this.posX +dist*cos(angle), this.posY +dist*sin(angle),angle);
    }
  }
  
  Bombfass(float posX,float posY, float dir){
    size=50;
    hp=1;
    this.posX=posX;
    this.posY=posY;
    if(random(100)<30){
      float angle=dir+random(-HALF_PI/2,HALF_PI/2);
      float dist = 100;
      bigMap.add(new Bombfass( this.posX +dist*cos(angle), this.posY +dist*sin(angle),angle));
    }
    }
  
  public void draw(){
    skin();
    for(int i=0;i<bullets.size();i++){
      if(!hits.contains(bullets.get(i)) && distance(posX,posY,bullets.get(i).posX,bullets.get(i).posY) < (size+bullets.get(i).size)/2 ){
        hits.add(bullets.get(i));
        hp-=bullets.get(i).dmg;
        bullets.get(i).trigger();
      }
    }
    if(hp<=0){
      bigMap.remove(this);
      smallMap.remove(this);
      new FFExplosion(posX,posY,50*enemyStrength,320);
    }
    pushEnemies();
    
  }
  
  public void pushEnemies(){
    
    for(int i=0;i<prey.size();i++){
      float distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
      if(distFromEnemy < (size+prey.get(i).size)/2+2){
        float weigthDiff=prey.get(i).weight/1;
        posX+=1*(posX-prey.get(i).posX)/distFromEnemy*weigthDiff;
        posY+=1*(posY-prey.get(i).posY)/distFromEnemy*weigthDiff;
        distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        while(distFromEnemy > (size+prey.get(i).size)/2+2){
          posX-=(posX-prey.get(i).posX)/distFromEnemy;
          posY-=(posY-prey.get(i).posY)/distFromEnemy;
          distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        }
      }
    }
    
    float distFromEnemy=distance(posX,posY,player.posX,player.posY);
      if(distFromEnemy < (size+player.size)/2+2){
        posX+=1*(posX-player.posX)/distFromEnemy*3;
        posY+=1*(posY-player.posY)/distFromEnemy*3;
        distFromEnemy=distance(posX,posY,player.posX,player.posY);
        while(distFromEnemy > (size+player.size)/2+2){
          posX-=(posX-player.posX)/distFromEnemy;
          posY-=(posY-player.posY)/distFromEnemy;
          distFromEnemy=distance(posX,posY,player.posX,player.posY);
        }
      }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(bombfassI,0,0,size*2,size*2);
    popMatrix();
  }
}

class PoisonField extends MapObj{
  PoisonField(){
    super();
    size=160;
    if(random(100)<70){
      float angle=random(TWO_PI);
      float dist = size/2;
      new PoisonField( this.posX +dist*cos(angle), this.posY +dist*sin(angle),angle);
    }
  }
  
  PoisonField(float posX,float posY, float dir){
    size=160;
    this.posX=posX;
    this.posY=posY;
    if(random(100)<60){
      float angle=dir+random(-HALF_PI/2,HALF_PI/2);
      float dist = size;
      bigMap.add(new PoisonField( this.posX +dist*cos(angle), this.posY +dist*sin(angle),angle));
    }
  }
  PoisonField(float posX,float posY){
    size=160;
    this.posX=posX;
    this.posY=posY;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2){
      textD("slowed",player.posX+60,player.posY+60,24,0);
      player.hp-=20.0f/120.0f * enemyDmg;
      poisonFrame+=20.0f/120.0f * enemyDmg;
      player.posX-=player.speedX*0.4f;
      player.posY-=player.speedY*0.4f;
    }
    
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getPoisond(2.5f/120.0f * enemyDmg);
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(posionI,0,0,size*2,size*2);
    popMatrix();
  }
}



class Trader extends MapObj{
  GoodItem tradeFrom;
  GoodItem tradeTo;
  
  Trader(){
    super();
    size=140;
    state=1;
    type=9;
    tradeFrom=randomGoodLoot(posX-50,posY);
    tradeTo=randomGoodLoot(posX+50,posY);
  }
  Trader(float posX,float posY){
    super();
    size=180;
    state=1;
    this.posX=posX;
    this.posY=posY;
    type=9;
  }
  
  public void draw(){
    
    GoodItem playersItem = null;
    for(int i=0;i<player.itemsGood.size();i++){
      if(player.itemsGood.get(i).getClass() == tradeFrom.getClass()){
        playersItem=player.itemsGood.get(i);
      }
    }
    if(playersItem!=null || state==0) skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1 && playersItem!=null){
      textD("e to trade",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.itemsGood.remove(playersItem);
        player.itemsGood.add(tradeTo);
      }
    }
  }
  
  public void newDay(){
    state=1;
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    image(traderI,0,0,size,size);
    popMatrix();
    if(state==1){
      tradeFrom.drawSkinMap();
      tradeTo.drawSkinMap();
    }
  }
}

class CrazyTrader extends MapObj{
  GoodItem tradeItem;
  
  CrazyTrader(){
    super();
    float dist=6200;
    if(random(100)<80)dist*=2;
    float angle=random(TWO_PI);
    posX=dist*cos(angle);
    posY=dist*sin(angle);
    size=280;
    state=1;
    type=9;
    tradeItem=tradeItem(posX+10,posY+25);
  }
  CrazyTrader(float posX,float posY){
    super();
    size=180;
    state=1;
    this.posX=posX;
    this.posY=posY;
    type=9;
  }
  
  public void draw(){

    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1 ){
      textD("e to trade",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.itemsGood.add(tradeItem);
      }
    }
  }
  
  public void newDay(){
    state=1;
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    image(crazyTraderI,0,0,size,size);
    popMatrix();
    if(state==1){
      tradeItem.drawSkinMap();
    }
  }
}

class Vase extends MapObj{
  float hp;
  Vase(){
    super();
    size=60;
    hp=5;
  }
  
  public void draw(){
    skin();
    for(int i=0;i<bullets.size();i++){
      if( distance(posX,posY,bullets.get(i).posX,bullets.get(i).posY) < (size+bullets.get(i).size)/2 ){
        hp-=bullets.get(i).dmg;
        bullets.get(i).trigger();
      }
    }
    if(hp<=0){
      bigMap.remove(this);
      smallMap.remove(this);
      int r=3+(int)random(5);
      for(int i=0;i<r;i++){
        pickups.add(new Coin(posX+random(-size/2,size/2),posY+random(-size/2,size/2)));
      }
      Rat rat=new Rat();
      rat.posX=posX;
      rat.posY=posY;
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(vaseI,0,0,size*2,size*2);
    popMatrix();
  }
}

class Camp extends MapObj{
  int timer;
  Camp(){
    super();
    size=320;
  }
  
  Camp(float posX,float posY){
    size=320;
    this.posX=posX;
    this.posY=posY;
  }
  
  public void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2){
      player.hp+=2.0f/120;
      player.mana+=2.0f/120;
      for(int i=0;i<prey.size();i++){
        if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
          prey.get(i).posX-=prey.get(i).speedX*0.5f;
          prey.get(i).posY-=prey.get(i).speedY*0.5f;
        }
      }
    }
  }
  
  public void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    if(distance(posX,posY,player.posX,player.posY)>size/2)tint(200,205);
    image(campMapI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}
class Player{
  float posX;
  float posY;
  float size;
  
  float speed;
  float speedX;
  float speedY;
  int menu;
  
  float attackSpeed;
  float nextShot = 60;
  int magsize;
  int bulletsMag=2;
  float reloadSpeed;
  float reloading = 120*2;
  float dmg;
  float stealth;
  int noStealth;
  float bulletSpeed;
  float bulletSize;
  float scope;
  float stunnDuration;
  float pepper;
  float bulletNotReuse;
  float slowDuration;
  float mixer;
  float magnet;
  float poison;
  float mutation;
  float firstShotDmg;
  float stunnDmg;
  float goldDrops;
  float markDmg;
  float markSlow;
  float markPercent;
  float firstShotStunn;
  float maxSpinny;
  float spinnSpeed;
  float stunned;
  float maxHp;
  float hp;
  float regen;
  float armor;
  float harvestSpeed; 
  float maxMana;
  float mana;
  float cdr;
  float ap;
  float manaRegen;
  float spray;
  float chargedPlug;
  boolean spamStopper;
  float accelerateBulet;
  int bulletHitsFrame;
  int cheese;
  int dashType;
  float dmgReduction;
  float dashRange;
  float meeleDmg=0;
  float bonusShotdmg;
  float knockback;
  float primeSkillUp;
  boolean shotFrame;
  float shield;
  float tempHp;
  boolean gotDamaged;
  int hitTimer;
  
  float spawnX;
  float spawnY;
  
  Skill rightClick = new RocketShooter(0,0);
  Skill Space;//NinjaDash(0,0);//Rage();
  Skill Qskill;
  
  Skin skin;

  int page = 0;
  ArrayList<Item> itemsEquipt = new ArrayList<Item>();
  ArrayList<GoodItem> itemsGood = new ArrayList<GoodItem>();
  /*ArrayList<Item> itemsUnlocked = new ArrayList<Item>();
  ArrayList<Item> itemsEquipt = new ArrayList<Item>();
  ArrayList<Consumable> consum = new ArrayList<Consumable>();
  ArrayList<Consumable> consumEquipt = new ArrayList<Consumable>();*/
  int classCode;
  
  Player(){
    size=30;
    posX=0;
    posY=0;
    spawnX=0;
    spawnY=0;
    maxHp=1000000;
    mana=10000000;
    hp=maxHp;
    skin=skins.get(skinPicked);
    //items.add(new BigElephant(posX,posY-50));
  }

  public void draw(){
    
    if(hp<=0)die();
    baseStats();
    for(int i=0;i<itemsEquipt.size();i++){
      itemsEquipt.get(i).stats1();
    } 
    
    for(int ii=0;ii<100;ii++){
      if(ii==0){
        for(int i=0;i<itemsGood.size();i++){
          itemsGood.get(i).stats1();
        }
      }
      
      if(rightClick!=null){
        rightClick.buff(ii);
      }
      if(Space!=null){
        Space.buff(ii);
      }
      if(Qskill!=null){
        Qskill.buff(ii);
      }
      
      for(int i=0;i<itemsGood.size();i++){
        itemsGood.get(i).stats2(ii);
      }
      
    }
    
    if(hp<maxHp)hp+=regen;
    if(hp>maxHp)hp=maxHp;
    
    if(mana<maxMana)mana+=manaRegen;
    if(mana>maxMana)mana=maxMana;
    
    
    fill(255);
    rectD(posX, posY+size/2+15,size*1.5f,6);
    fill(0xffB40000);
    rectD(posX+(hp/maxHp-1)*size/2*1.5f, posY+size/2+15,size*1.5f*hp/maxHp,6);
    fill(0xff60A2D3);
    if(shield>0) rectD(posX+(shield/maxHp-1)*size/2*1.5f, posY+size/2+15,size*1.5f*shield/maxHp,6);
    skin.drawBody(posX,posY,size);
    
    //move
    speedX=0;
    speedY=0;
    if(keys[0]){
      if(keys[1]||keys[3])speedY=-speed/1.4f;
      else speedY=-speed;
    }
    if(keys[1]){
      if(keys[0]||keys[2])speedX=-speed/1.4f;
      else speedX=-speed;
    }
    if(keys[2]){
      if(keys[1]||keys[3])speedY=speed/1.4f;
      else speedY=speed;
    }
    if(keys[3]){
      if(keys[0]||keys[2])speedX=speed/1.4f;
      else speedX=speed;
    }
    if(stunned>0){
      stunned--;
      speedX=0;
      speedY=0;
    }
    posX=posX+speedX;
    posY=posY+speedY;

    //skills
    if(rightClick!=null){
      if(keys[13])rightClick.activate();
    }
    if(Space!=null){
      if(keys[6])Space.activate();
    }
    if(Qskill!=null){
      if(keys[4])Qskill.activate();
    }
    
    //gun
    shotFrame=false;
    if(keys[9] && bulletsMag<magsize)bulletsMag=0;
    if(bulletsMag<=0){
      spinnSpeed=0;
      if(reloading<=0){
        //reloading
        reloading=120/reloadSpeed;
        if(reloading<120/attackSpeed) reloading=120/attackSpeed;
        bulletsMag=magsize;
        nextShot=0;
      }else reloading--;
    }else{
      
      //shot
      if(nextShot<=0){
        shot();
      }else nextShot--;
    }
    
    //arrow
    if(activeBoss==null)turnImgD(arrowI,posX,posY,140,140,HALF_PI+angle(posX, posY,activeAltar.posX,activeAltar.posY));
    else turnImgD(arrowI,posX,posY,140,140,HALF_PI+angle(posX, posY,activeBoss.posX,activeBoss.posY));
    
  }
  
  public void shot(){
    if(keys[12]){
      nextShot=120/attackSpeed;
      reloading=120/reloadSpeed;
      shotFrame=true;
      Bullet b = new Bullet();
      if(random(1)<bulletNotReuse)bulletsMag--;
    }
  }
  
  
  public void getDmg(float dmg){
    gotDamaged=true;
    hitTimer=120;
    float dmgLeft=dmg;
    if(tempHp>0){
      dmgLeft-=tempHp;
      tempHp-=dmg;
    }
    if(tempHp<0)tempHp=0;
    if(dmgLeft>0 && shield>0){
      float sDMGleft=shield;
      shield-=dmgLeft;
      dmgLeft-=sDMGleft;
    }
    if(shield<0)shield=0;
    if(dmgLeft>0)hp-=dmgLeft*dmgReduction;
    
  }
  
  public void die(){
    gameMode=0;
    if(highscore<bossKills)highscore=bossKills;
    for(int i = 0; i<marks.size();i++){
      marks.get(i).endGame();
    }
  }
  
  public void drawHud(){
    fill(255);
    rect(400,1000,600,40);
    noFill();
    rect(50,985,90,75);
    image(skin.img,50,985,size*2,size*2);
   
    fill(200);
    rect(100+300*hp/maxHp,1000,600*hp/maxHp,40);
    textC((int)hp+"/"+(int)maxHp,400,1000,20,0);
    
    fill(0xff60A2D3);
    if(shield>0) rect(5+300*shield/maxHp,920,600*shield/maxHp,20);
    fill(0xff59AA8D);
    if(tempHp>0) rect(5+600*shield/maxHp+300*tempHp/maxHp,920,600*tempHp/maxHp,20);
    
    itemHud();
    
    fill(255);
    rect(750,985,75,75);
    textC(bulletsMag+"/"+magsize,750,985,20,0);
    
    
    
    fill(255);
    rect(400,960,600,25);
    fill(0xff4C35D3);
    rect(100+300*mana/maxMana,960,600*mana/maxMana,25);
    
    if(rightClick!=null){
      rightClick.draw(1060);
    }
    if(Space!=null){
      Space.draw(970);
    }
    if(Qskill!=null){
      Qskill.draw(860);
    }
    
    image(ingameHudI,1920/2,1080/2);
    if(hitTimer>0){
      hitTimer--;
      tint(255,255*hitTimer/120);
      image(hitScreenI,1920/2,1080/2);
      tint(255,255);
    }
  }
  
  
  int kapaUsed;
  int redKapaUsed;
  
  public void drawMenu(){
    image(TabMenuI,1920/2,1080/2);
    textAlign(LEFT, CENTER);
    textC("Dmg: ",150,30,20,0); textC(""+dmg,300,30,20,0);
    textC("Mag: ",150,50,20,0); textC(""+magsize,300,50,20,0);
    textC("Attackspeed: ",150,70,20,0); textC(""+(attackSpeed-attackSpeed%0.01f),300,70,20,0);
    textC("Speed: ",150,90,20,0); textC(""+(speed-speed%0.01f),300,90,20,0);
    textC("Reloadspeed: ",150,110,20,0); textC(""+(reloadSpeed-reloadSpeed%0.01f),300,110,20,0);
    textC("Regen: ",150,130,20,0); textC(""+((120*regen-120*regen%0.0001f)),300,130,20,0); 
    textC("Mana: ",150,150,20,0); textC(""+((120*manaRegen-120*manaRegen%0.0001f)),300,150,20,0);
    textAlign(CENTER, CENTER);
    
    for(int i=0;i<itemsGood.size();i++){
      float posXM=120+80*(i%20);
      float posYM=300+80*(int)(i/20);
      itemsGood.get(i).drawSkinMenu(posXM,posYM);
      if(buttonHoover(posXM,posYM,60,60)) itemsGood.get(i).drawMenuText();
    }
  }
  
  
  public void baseStats(){
    attackSpeed=2;
    if(bulletsMag>magsize) bulletsMag=magsize;
    magsize=6;
    bulletSize=12;
    reloadSpeed=0.5f;
    knockback=50.0f*dmg/10.0f;
    dmg=10;
    bulletSpeed = 12;
    speed=1.51f;
    scope=0;
    stunnDuration=0;
    pepper=0;
    bulletNotReuse=1;
    slowDuration=0;
    poison=0;
    firstShotDmg=0;
    stunnDmg=0;
    maxHp=100;
    regen=0.5f/120;
    armor=0;
    harvestSpeed=10.0f/120;
    maxMana=100;
    cdr=0;
    ap=10;
    spray=0;
    chargedPlug=0;
    manaRegen=0.5f/120;
    accelerateBulet=0;
    dashType=0;
    dmgReduction=1;
    meeleDmg=0;
    dashRange=15;
    bonusShotdmg=0;
    
    primeSkillUp=1;
    if(shield>0){
      shield*=1-(0.15f/120) *100/maxHp;
      shield-=5.0f/120.0f *100/maxHp;
    }else shield=0;
    greenChest=0;
    extraStats();
  }
  public void extraStats(){}
}

int enemy1=1;
int enemy2=0;
int enemy3=0;
int enemy4=0;
float enemyDmg;
float dropChance;

public void spawnEnemy(boolean casual){

  spawnRate=0.7f+1.27f*(float)Math.log(1+0.2f*time/120.0f/60.0f);
  dropChance=0.7f/spawnRate ;
  if(prey.size()>1200 || activeBoss!=null){
    if(0.4f+0.03f*bossKills<1) spawnRate*=0.4f+0.03f*bossKills;
  }
  enemyStrength=0.33f*(float)Math.pow(1.11f,time/120.0f/60.0f) + 0.056f*time/120.0f/60.0f;
  enemyDmg=0.35f*0.33f+  0.65f*( 0.33f*(float)Math.pow(1.11f,time/120.0f/60.0f) + 0.056f*time/120.0f/60.0f );
  
  int enemyX=0;
  if(random(120)<0.0f/4.0f*spawnRate){
         new RedSpeedster();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
  }
  for(int i=0;i<=4;i++){
    if(i==0) enemyX=enemy1;
    if(i==1) enemyX=enemy2;
    if(i==2) enemyX=enemy3;
    if(i==3) enemyX=enemy4;

    if(random(120)<8.0f/10 || casual){
      float diffSpawn=1+1.4f*time/120.0f/60.0f;
      
      if(diffSpawn>15)diffSpawn=15;
      if(i==0) enemy1=(int)random(diffSpawn);
      if(i==1) enemy2=(int)random(diffSpawn);
      if(i==2) enemy3=(int)random(diffSpawn);
      if(i==3) enemy3=(int)random(diffSpawn);
    }
    
    
    
    if(enemyX==0){
      if(random(120)<1.0f/4.0f*spawnRate){
         new Crawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==1){
      if(random(120)<0.3f/4.0f*spawnRate){
         new Shooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==2){
      if(random(120)<0.18f/4.0f*spawnRate){
         new BigCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==3){
      if(random(120)<0.3f/4.0f*spawnRate){
         new RedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==4){
      if(random(120)<0.12f/4.0f*spawnRate){
         new Sprinkler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==5){
      if(random(120)<0.13f/4.0f*spawnRate){
         new TeleportEnemy();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==6){
      if(random(120)<0.2f/4.0f*spawnRate){
         new RedSpeedster();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==7){
      if(random(120)<0.07f/4.0f*spawnRate){
         new Waver();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==8){
      if(random(120)<0.07f/4.0f*spawnRate){
         new Bulldozer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==9){
      if(random(120)<0.26f/4.0f*spawnRate){
         new Splitter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==10){
      if(random(120)<2.1f/4.0f*spawnRate){
         new Rat();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==11){
      if(random(120)<0.14f/4.0f*spawnRate){
         new BigShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==12){
      if(random(120)<0.14f/4.0f*spawnRate){
         new BigRedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==13){
      if(random(120)<0.08f/4.0f*spawnRate){
         new Healer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==14){
      if(random(120)<0.07f/4.0f*spawnRate){
         new Sprayer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    } 
    if(enemyX==15){
      if(random(120)<0.1f/4.0f*spawnRate){
         new GiantCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==16){
      if(random(120)<0.14f/4.0f*spawnRate){
         new SpeedBuffer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
  }
}


class Prey{
  float posX;
  float posY;
  float size;
  float powersqr;
  
  float dmg;
  float hitTimer;
  float walkSpeed;
  float runSpeed;
  float sightRange;
  float angle;
  float hp;
  float maxHp;
  float armor;
  float xpSaved;
  int c;
  float droprate;
  float nextShot;
  float shotSpeed;
  
  float speedX;
  float speedY;
  
  boolean dodging;
  int d=1;
  int stunned;
  int slowed;
  boolean mutant=false;
  int hits=0;
  float markAngle;
  float vision;
  float ccRes;
  float knockbackX;
  float knockbackY;
  float knockbackTimer;
  float range;
  boolean removable=true;
  boolean stopwhenHitting;
  float weight;
  
  Prey(int c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate){
    reposition();
    prey.add(this);
    range=900;
    weight=1;
    stopwhenHitting=true;
    this.c=c;
    this.size= size;
    this.maxHp = maxHp*enemyStrength;
    this.xpSaved = xpSaved;
    this.walkSpeed = walkSpeed;
    this.runSpeed = runSpeed*1.0f;
    this.hp=this.maxHp;
    this.dmg=dmg*enemyDmg;
    this.vision=400;
    this.droprate=droprate;
  }
  
  public void reposition(){
    float dist=1130+size/2;
    angle=random(TWO_PI);
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
  }

  public void draw(){
    poisonedGot=0;
    if(distance(posX,posY,player.posX,player.posY)>2000 && removable)prey.remove(this);
    if(hits>=3 && player.markDmg>0){
      turnImgD(markI,posX,posY,size*2,size*2,markAngle);
      markAngle-=0.01f;
    }
    if(posX-camX>-size && posY-camY>-size && posX-camX<1920+size && posY-camY<1080+size) body();
    hpBar();
    hit();
    shoot();
    if(knockbackTimer>0){
      posX+=knockbackX;
      posY+=knockbackY;
      knockbackTimer--;
    }
    if(stunned>0) stunned--;
    if(hitTimer>0)hitTimer--;
    if((hitTimer<=0 || !stopwhenHitting) && stunned<=0 && knockbackTimer<=0)move();
    pushEnemies();
    extra();
  }
  
  public void pushEnemies(){
    for(int i=0;i<prey.size();i++){
      float distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
      if(distFromEnemy < (size+prey.get(i).size)/2+2 && !this.equals(prey.get(i))){
        float weigthDiff=prey.get(i).weight/weight;
        posX+=runSpeed*(posX-prey.get(i).posX)/distFromEnemy*weigthDiff;
        posY+=runSpeed*(posY-prey.get(i).posY)/distFromEnemy*weigthDiff;
        distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        while(distFromEnemy > (size+prey.get(i).size)/2+2){
          posX-=(posX-prey.get(i).posX)/distFromEnemy;
          posY-=(posY-prey.get(i).posY)/distFromEnemy;
          distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        }
      }
    }
  }
  
  public void hit(){
    if(hitTimer<=0 && distance(posX,posY,player.posX,player.posY) < (size+player.size)/2){
      player.getDmg(dmg);
      hitTimer=120;
    }
  }
  
  public void shoot(){}
  
  public void shoot1(float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=bulletSpeed*(player.posX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(player.posY-posY)/dist;
      new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  public void shoot2(int shots,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float angleS=0;
      for(int i=0;i<shots;i++){
        angleS+=TWO_PI/shots;
        float bulletSpeedX=bulletSpeed*cos(angleS);
        float bulletSpeedY=bulletSpeed*sin(angleS);
        new StraightEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
      }
    }
  }
  
  public void shoot3(int shots,float spread,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=bulletSpeed*(player.posX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(player.posY-posY)/dist;
      spread=spread*TWO_PI/360;
      float angleS=angle(0,0,bulletSpeedX,bulletSpeedY)-shots*spread/2+spread/2;
      for(int i=0;i<shots;i++){
        bulletSpeedX=bulletSpeed*cos(angleS);
        bulletSpeedY=bulletSpeed*sin(angleS);
        new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
        angleS+=spread;
      }
    }
  }
  
  public void shoot4(float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+player.speedX*distReal/bulletSpeed;
      float aimY=player.posY+player.speedY*distReal/bulletSpeed;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=bulletSpeed*(aimX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(aimY-posY)/dist;
      new RedEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  public void shootSpray(float bulletSize, float bulletSpeed,float spray){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float angle = angle(posX,posY,player.posX,player.posY);
      angle+=random(TWO_PI*spray/360) - TWO_PI*spray/360*0.5f;
      float bulletSpeedX=bulletSpeed*cos(angle);
      float bulletSpeedY=bulletSpeed*sin(angle);
      new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  public void shootSplit(float bulletSize, float bulletSpeed,float extraFrames){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*60/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+0.8f*player.speedX*distReal/bulletSpeed;
      float aimY=player.posY+0.8f*player.speedY*distReal/bulletSpeed;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=bulletSpeed*(aimX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(aimY-posY)/dist;
      new SplitEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize,dist/bulletSpeed+extraFrames);
    }
  }
  
  public void body(){
    /*pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(assasinI,0,0,size*2,size*2);
    popMatrix();*/
  } 
  
  public void hpBar(){
    fill(255);
    rectD(posX, posY+size/2+10,size,10);
    fill(0xffB40000);
    rectD(posX+(hp/maxHp-1)*size/2, posY+size/2+10,size*hp/maxHp,10);
  }
  
  public void move(){
    float speedMulti=1;
    run();
    if(slowed>0){
      slowed--;
      speedMulti*=0.75f;
    }
    posX+=speedX*speedMulti;
    posY+=speedY*speedMulti;
  }
  
  public void strayve(){
    float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
      if(dist<600){
        float saveX=speedX;
        speedX=-speedY;
        speedY=saveX;
    }
  }
  
  public void run(){
    float dist=distance(posX,posY,player.posX,player.posY);
    speedX = runSpeed*(player.posX-posX)/dist;
    speedY = runSpeed*(player.posY-posY)/dist;
  }
  
  public void extra(){}
  
  public void getHit(Bullet b){
    float dmgGot=b.dmg;
    float kbmulti=16.0f/maxHp;
    
    if(b.knockback*kbmulti-ccRes>0){
      float kbX=b.speedX;
      float kbY=b.speedY;
      knockbackX=(b.knockback-ccRes)*kbX/(float)Math.sqrt(Math.pow(b.speedX,2)+Math.pow(b.speedY,2))/12 *kbmulti;
      knockbackY=(b.knockback-ccRes)*kbY/(float)Math.sqrt(Math.pow(b.speedX,2)+Math.pow(b.speedY,2))/12 *kbmulti;
      knockbackTimer=12;
    }
    if(stunned-ccRes/50>0) dmgGot+=player.stunnDmg;
    if(hits>=3)dmgGot+=player.markDmg;
    if(b.main)hits++;
    if(hits==3 && b.main){
      slowed = (int)player.markSlow;
      dmgGot+=maxHp*player.markPercent;
      stunned = (int)player.stunnDuration;
    }
    if(hp>=maxHp*0.9f){
      dmgGot+=player.firstShotDmg;
      if(player.firstShotStunn>stunned)stunned=(int)player.firstShotStunn;
    }
    hp-=dmgGot;
    if(b.slowDuration > slowed) slowed = (int)b.slowDuration;
    
    if(hp<1)die();
  }
  float poisonedGot;
  public void getPoisond(float dmgGot){
    hp-=dmgGot;
    if(hp<1)die();
  }
  
  public void die(){
    enemiesKilled++;
    prey.remove(this);
    kills.add(this);
    if(random(100)<droprate*dropChance) items.add(randomLoot(posX,posY-50));
    if(time> 2*120*60 && random(100)<droprate*dropChance*0.27f) pickups.add(new GrayCrystal(posX-10,posY));
    if(random(100)<droprate*0.4f) pickups.add(new Coin(posX+10,posY-50));
    
    extraDrops();
  }
  public void extraDrops(){}
}


class Crawler extends Prey{
  
  Crawler(){
    super(255,50,17,0,0.5f,1,20,6.5f);//5*1.3
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(rocketI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class GiantCrawler extends Prey{
  
  GiantCrawler(){
    super(255,110,150,0,0.5f,0.7f,40,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=4;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(rocketI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class BigCrawler extends Prey{
  
  BigCrawler(){
    super(255,75,70,0,0.5f,0.8f,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(rocketI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}


class Shooter extends Prey{
  
  
  Shooter(){
    super(255,55,15,0,0.4f,0.7f,20,15);
    shotSpeed=26;
    weight=0.7f;
  }
  
  public void shoot(){
    shoot1(15,2.8f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(blueI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class BigShooter extends Prey{
  
  
  BigShooter(){
    super(255,75,70,0,0.5f,0.8f,35,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=46;
    weight=1.4f;
  }
  
  public void shoot(){
    shoot1(24,2.8f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(blueI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class Sprinkler extends Prey{
  
  
  Sprinkler(){
    super(255,65,40,0,0.4f,0.8f,20,40);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=30;
  }
  
  public void shoot(){
    shoot2(6,15,2.5f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(SpikerI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class Waver extends Prey{
  Waver(){
    super(255,55,70,0,0.4f,0.8f,30,70);
    shotSpeed=18;
  }
  
  public void shoot(){
    shoot3(3,18,18,3.2f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(assasinI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class Rat extends Prey{
  
  Rat(){
    super(255,40,2,0,0.5f,1.3f,15,2);
    weight=0.5f;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(RatI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  public void getPoisond(float dmgGot){
    hp+=dmgGot;
    if(hp>maxHp)hp=maxHp;
  }
}

class Bee extends Prey{
  
  Bee(float posX,float posY){
    super(255,35,25,0,0.5f,1.5f,20,5);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    this.posX=posX;
    this.posY=posY;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(BeeI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class RedShooter extends Prey{
  
  RedShooter(){
    super(255,55,15,0,0.4f,0.7f,20,15);
    shotSpeed=24;
    weight=0.7f;
  }
  
  public void shoot(){
    shoot4(15,2.8f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(redOctoI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class BigRedShooter extends Prey{
  
  BigRedShooter(){
    super(255,75,70,0,0.5f,0.8f,35,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=46;
    weight=1.4f;
  }
  
  public void shoot(){
    shoot4(24,2.8f);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(redOctoI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class RedSpeedster extends Prey{
  float orbitalAngle;
  float orbitalRange=300;
  float bulletSize=45;
  RedSpeedster(){
    super(255,70,50,0,0.5f,0.7f,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4f;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    turnImg(chainRedI,0,0,orbitalRange*2,orbitalRange*2,orbitalAngle);
    
    image(redBulletI,orbitalRange*cos(orbitalAngle),orbitalRange*sin(orbitalAngle),bulletSize*2,bulletSize*2);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(redSpeederI,0,0,size*2,size*2);
    noTint();
    popMatrix();
    
  }

  public void hit(){
    orbitalAngle+=0.2f*TWO_PI/120;
    if(hitTimer<=0 && distance(posX,posY,player.posX,player.posY) < (size+player.size)/2){
      player.getDmg(dmg);
      hitTimer=120;
    }
    if(hitTimer<=0 && distance(posX+orbitalRange*cos(orbitalAngle),posY+orbitalRange*sin(orbitalAngle),player.posX,player.posY) < (bulletSize+player.size)/2){
      player.getDmg(dmg);
      hitTimer=120;
    }
  }
}

class Bulldozer extends Prey{
  boolean powerTimer;
  float chargeX;
  float chargeY;
  Bulldozer(){
    super(255,85,70,0,0.5f,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=true;
    chargeX=player.posX+player.speedX*60;
    chargeY=player.posY+player.speedY*60;
    float dist=distance(posX,posY,player.posX,player.posY);
    float extraX = (player.posX-posX)/dist;
    float extraY = (player.posY-posY)/dist;
    new Bulldozer(posX+extraY*size*0.55f,posY-extraX*size*0.55f,   chargeX+extraY*size*0.55f,chargeY-extraX*size*0.55f);
    new Bulldozer(posX-extraY*size*0.55f,posY+extraX*size*0.55f,   chargeX-extraY*size*0.55f,chargeY+extraX*size*0.55f);
    this.posX-=extraX*1.3f;
    this.posY-=extraY*1.3f;
    
  }
  Bulldozer(float posX,float posY,float cX,float cY){
    super(255,85,70,0,0.5f,1,30,23);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    chargeX=cX;
    chargeY=cY;
    powerTimer=true;
    this.posX=posX;
    this.posY=posY;
  }
  public void body(){
    angle=angle(posX,posY,player.posX,player.posY);
    if(powerTimer) angle=angle(posX,posY,chargeX,chargeY);
    turnFlipImgD(BulldozerI,posX,posY,size*2,size*2,angle);
  }
  
  public void move(){
    weight=2;
    if(powerTimer){
      weight=5;
      float dist=distance(posX,posY,chargeX,chargeY);
      if(dist<=runSpeed*4){
        powerTimer=false;
      }
      speedX = 3.2f*runSpeed*(chargeX-posX)/dist;
      speedY = 3.2f*runSpeed*(chargeY-posY)/dist;
    }
    else run();

    posX+=speedX;
    posY+=speedY;
  }

}

class Wanderer extends Prey{
  float goalX;
  float goalY;
  Item item;
  
  Wanderer(float posX,float posY,float goalX,float goalY, Item item){
    super(255,110,150,0,0.5f,0.4f,40,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=4;
    removable=false;
    this.posX=posX;
    this.posY=posY;
    this.goalX=goalX;
    this.goalY=goalY;
    
    float dist=distance(posX,posY,goalX,goalY);
    this.posX+= time*runSpeed*(goalX-posX)/dist;
    this.posY+= time*runSpeed*(goalY-posY)/dist;
    this.item=item;
    this.maxHp =300 *dist(0,0,posX,posY)/20000;
    this.dmg=20 *dist(0,0,posX,posY)/20000;
    this.hp = maxHp;
    shotSpeed=40;
  }
  
  public void move(){
    float speedMulti=1;
    if(hp<maxHp*0.9f) run();
    else{
      float dist=distance(posX,posY,goalX,goalY);
      speedX = runSpeed*(goalX-posX)/dist;
      speedY = runSpeed*(goalY-posY)/dist;
      if(dist<=100){
        if(random(100)<5) hp*=0.8f;
        goalX=-goalX;
        goalY=-goalY;
      }
    }
    posX+=speedX*speedMulti;
    posY+=speedY*speedMulti;
  }
  
  public void shoot(){
    if(hp<maxHp && activeBoss==null) shoot2(16,16,2.5f);
  }
  
  public void extraDrops(){
    item.posX=posX;
    item.posY=posY;
    items.add(item);
  }
  public void getPoisond(float dmgGot){
  }
  
  public void body(){
    float side=-size*0.54f;
    if(speedX<0)side*=-1;

    item.posX=posX+side;
    item.posY=posY-size*0.31f;
    item.drawMap();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(wandererI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class Healer extends Prey{
  float range=360;

  Healer(){
    super(255,75,120,0,0.5f,0.8f,20,60);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.8f;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(KingI,0,0,size*2,size*2);
    image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  
  public void extra(){
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < range/2 ){
        if(!(prey.get(i) instanceof Healer) )prey.get(i).hp+=3*maxHp/180/120 + dmg/120/4;
        if(prey.get(i).hp>prey.get(i).maxHp)prey.get(i).hp=prey.get(i).maxHp;
      }
    }
  }
}

class SpeedBuffer extends Prey{
  float range=460;

  SpeedBuffer(){
    super(255,65,120,0,0.5f,1.3f,30,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.0f;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(guardianI,0,0,size*2,size*2);
    image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  
  public void extra(){
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < range/2 ){
        if(!(prey.get(i) instanceof SpeedBuffer) ){
          prey.get(i).posX+=prey.get(i).speedX*1.0f;
          prey.get(i).posY+=prey.get(i).speedY*1.0f;
        }
      }
    }
  }
}

class TeleportEnemy extends Prey{
  float range=450;
  int portalTimer;
  float portalX;
  float portalY;

  TeleportEnemy(){
    super(255,65,80,0,0.5f,0.7f,30,38);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4f;
    shotSpeed=5;
    nextShot=120*9;
    removable=false;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(teleportEnemyI,0,0,size*2,size*2);
    if(portalTimer>0) image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    shootPortal();
  }
  
  public void shootPortal(){
    nextShot--;
    if(nextShot<=0){
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=300*(player.posX-posX)/dist;
      float bulletSpeedY=300*(player.posY-posY)/dist;
      portalX=player.posX+bulletSpeedX;
      portalY=player.posY+bulletSpeedY;
      portalTimer=120*2;
      nextShot=portalTimer+120.0f*60/shotSpeed;
    }
    if(portalTimer>0){
      portalTimer--;
      pushMatrix();
      translate(portalX-camX,portalY-camY);
      tint(255,100);
      image(teleportEnemyI,0,0,size*2,size*2);
      image(rangeIndI,0,0,range*2,range*2);
      noTint();
      popMatrix();
      if(portalTimer<=0){
        for(int i=0;i<prey.size();i++){
          if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < range/2 ){
            prey.get(i).posX+=portalX-posX;
            prey.get(i).posY+=portalY-posY;
          }
        }
        posX=portalX;
        posY=portalY;
        
        int shots=10;
        float angleS=angle(posX,posY,player.posX,player.posY);
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          float bulletSpeedX=3*cos(angleS);
          float bulletSpeedY=3*sin(angleS);
          new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,24);
        }
      }
    }
  }
}

class Sprayer extends Prey{
  Sprayer(){
    super(255,110,150,0,0.4f,0.6f,8,70);
    shotSpeed=200;
    range=650;
  }
  
  public void shoot(){
    shootSpray(14,4.5f,60);
  }
  public void run(){
    float dist=distance(posX,posY,player.posX,player.posY);
    speedX = runSpeed*(player.posX-posX)/dist;
    speedY = runSpeed*(player.posY-posY)/dist;
    if(dist(posX,posY,player.posX,player.posY)>range*0.9f){
      speedX = 1.5f*runSpeed*(player.posX-posX)/dist;
      speedY = 1.5f*runSpeed*(player.posY-posY)/dist;
    }
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    //tint(#A6F764);
    image(techpriestI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

class Splitter extends Prey{

  Splitter(){
    super(255,55,18,0,0.4f,0.7f,20,19);
    range=1000;
    shotSpeed=22;
    weight=0.8f;
  }
  
  public void shoot(){
    shootSplit(34,2.6f,40);
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(0xffA6F764);
    image(blueI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}
public void addSkins(){
  skins.add(new SkinNorm());
  skins.add(new SkinEmp());
  skins.add(new SkinVamp());
  skins.add(new SkinHive());
  skins.add(new SkinShrek());
  skins.add(new SkinActualy());
  skins.add(new SkinPocorn());
  skins.add(new SkinHellokitty());
  skins.add(new SkinDisco());
  skins.add(new SkinSkull());
  skins.add(new SkinFish());
  skins.add(new SkinRaphi());
  skins.add(new SkinDeath());
  skins.add(new SkinBane());
  skins.add(new SkinMorpheus());
  skins.add(new SkinClown());
  skins.add(new SkinCharlyBrown());
  skins.add(new SkinGlitch());
  skins.add(new SkinBombSkin());
}

float spinnspeed;
float spinnPos;
public void newSkin(){

  if(buttonPressed(960, 650, 400, 100) && money>=100 && spinnspeed<=0){
    money-=100;
    spinnspeed=(10+random(10))/120;
    spinnPos=-0.5f+random(skins.size());
  }
    
  if(spinnspeed>0){
    spinnspeed*=1-(0.2f/120);
    spinnspeed-=0.01f/120;
    spinnPos+=spinnspeed;
    if((int)(spinnPos+0.5f)>=skins.size())spinnPos=-0.5f;
    
    image(skins.get(skinPicked).img,1920/2 -150 +300*((spinnPos+0.5f)%1),300,45,45);
    
    skinPicked=(int)(spinnPos+0.5f);
    if(spinnspeed<=0.0f){
      if(skins.get((int)(spinnPos+0.5f)).unlocked==true) diamonds++;
      skins.get((int)(spinnPos+0.5f)).unlocked=true;
      
    }
  }
}
class Mission{
  float score;
  boolean finished;
  
  public void menuText(){
  }
  
  public void checkFinished(){
    
  }
}



class KillEnemiesFrameMission extends Mission{
  int goal;
  
  public void menuText(){
    textC("kill "+goal+ " enemies at once ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(kills.size()>=goal){ finished=true; money+=100;}
    if(kills.size()>score) score=bossKills;
  }
}
class Kill10EnemiesFrameMission extends KillEnemiesFrameMission{
  Kill10EnemiesFrameMission(){
    goal=10;
  }
}


class KillBossesMission extends Mission{
  int goal;
  
  public void menuText(){
    textC("kill "+goal+ " bosses in one run ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(bossKills>=goal){ finished=true; money+=100;}
    if(bossKills>score) score=bossKills;
  }
}
class Kill1BossesM extends KillBossesMission{
  Kill1BossesM(){
    goal=1;
  }
}
class Kill5BossesM extends KillBossesMission{
  Kill5BossesM(){
    goal=5;
  }
}
class Kill10BossesM extends KillBossesMission{
  Kill10BossesM(){
    goal=10;
  }
}
class Kill20BossesM extends KillBossesMission{
  Kill20BossesM(){
    goal=20;
  }
}

class KillBossesTimeMission extends Mission{
  int goal;
  int timer;
  public void menuText(){
    textC("kill "+goal+ " bosses in "+timer+" min ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(time/120/60<timer && bossKills>=goal){ finished=true; money+=100;}
    if(time/120/60<timer && bossKills>score) score=bossKills;
  }
}
class Kill10Bosses15TimeMission extends KillBossesTimeMission{
  Kill10Bosses15TimeMission(){
    goal=10;
    timer=15;
  }
}

class SurviveMission extends Mission{
  int goal;
  
  public void menuText(){
    textC("survive for "+goal+ " minutes ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(time/120/60>=goal){ finished=true; money+=100;}
    if(time/120/60>score) score=time/120/60;
  }
}
class Survive10Mission extends SurviveMission{
  Survive10Mission(){
    goal=10;
  }
}

class Survive20Mission extends SurviveMission{
  Survive20Mission(){
    goal=20;
  }
}

class Survive30Mission extends SurviveMission{
  Survive30Mission(){
    goal=30;
  }
}


class SurviveNoItemsMission extends Mission{
  int goal;
  
  public void menuText(){
    textC("survive for "+goal+ " minutes without any items ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(player!=null){
      if(player.itemsEquipt.size()<=0 && player.itemsGood.size()<=0 && time/120/60>=goal){ finished=true; money+=100;}
      if(player.itemsEquipt.size()<=0 && player.itemsGood.size()<=0 && time/120/60>score) score=time/120/60;
    }
  }
}
class Survive10NoItemsMission extends SurviveNoItemsMission{
  Survive10NoItemsMission(){
    goal=10;
  }
}


class StatMission extends Mission{
  int goal;
  String stat;
  
  public void menuText(){
    textC("reach "+goal+" "+stat +" ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(player!=null){
      if(!finished&& getStat()>=goal){ finished=true; money+=100;}
      if(getStat()>score) score=getStat();
    }
  }
  public float getStat(){return 0;}
}
class StatDmgMission extends StatMission{
  StatDmgMission(){
    goal=60;
    stat="damage";
  }
  public float getStat(){return player.dmg;}
}
class StatASMission extends StatMission{
  StatASMission(){
    goal=12;
    stat="Attackspeed";
  }
  public float getStat(){return player.attackSpeed;}
}
class StatHpMission extends StatMission{
  StatHpMission(){
    goal=500;
    stat="Hp";
  }
  public float getStat(){return player.maxHp;}
}
class StatManaMission extends StatMission{
  StatManaMission(){
    goal=5;
    stat="Manaregen";
  }
  public float getStat(){return player.manaRegen;}
}
class StatRegenMission extends StatMission{
  StatRegenMission(){
    goal=4;
    stat="Regen";
  }
  public float getStat(){return player.regen;}
}
class StatMagMission extends StatMission{
  StatMagMission(){
    goal=50;
    stat="Magsize";
  }
  public float getStat(){return player.magsize;}
}

class RerollMissionextends extends Mission{
  int goal;
  RerollMissionextends(){
    goal=13;
  }
  public void menuText(){
    textC("use the ItemDice "+goal+ " times ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(score>=goal){ finished=true; money+=100;}
    if(itemDiced){
      score++;
      itemDiced=false;
    }
  }
}

float poisonFrame;
class PoisonMission extends Mission{
  int goal;
  PoisonMission(){
    goal=2000;
  }
  public void menuText(){
    textC("get "+goal+ " damage from Poison ("+(int)score+")",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(score>=goal){ finished=true; money+=100;}
    score+=poisonFrame;
    poisonFrame=0;
  }
}


class MapMission extends Mission{
  String stat;
  
  public void menuText(){
    textC("reach the end of the map",1500,150,26,255);
  }
  
  public void checkFinished(){
    if(player!=null){
      if(player.posX>mapSize || player.posX<-mapSize || player.posY>mapSize || player.posY<-mapSize){ finished=true; money+=100;}
    }
  }
  public float getStat(){return 0;}
}


class Skin{
  Mission m = new Kill10BossesM();
  boolean unlocked=false ;
  PImage img;
  
  public void drawMenu(){
    if(!unlocked)tint(20,100);
    image(img,1920/2,200,150,150);
    tint(255);
    if(unlocked){
      m.menuText();
      if(m.finished) image(crossBulletI,1920/2+80,200-50,100,100);
    }
  }
  
  public void drawBody(float posX, float posY, float size){
    imageD(img,posX,posY,size*2);
    drawWeapon(posX, posY, size);
    if(!m.finished) m.checkFinished();
    if(keys[8]) m.menuText();
  }
  
  public void drawWeapon(float posX, float posY, float size){
    float weaponAngle=angle(posX,posY,camX+mouseX,camY+mouseY);
    turnFlipImgD(sniperI,posX,posY+size*0.7f,size*2,size*2,weaponAngle);
  }
}
class SkinNorm extends Skin{
  SkinNorm(){
    unlocked=true;
    img=playerI;
    m= new Kill1BossesM();
  }
}

class SkinEmp extends Skin{
  SkinEmp(){
    img=emperorI;
    m= new Kill5BossesM();
  }
}

class SkinVamp extends Skin{
  SkinVamp(){
    img=vampireI;
    m= new Kill10BossesM();
  }
}

class SkinHive extends Skin{
  SkinHive(){
    img=hivemasterI;
    m= new Kill20BossesM();
  }
}

class SkinShrek extends Skin{
  SkinShrek(){
    img=shrekI;
    m=new Survive10Mission();
  }
}
class SkinActualy extends Skin{
  SkinActualy(){
    img=actualyI;
    m=new Survive20Mission();
  }
}
class SkinPocorn extends Skin{
  SkinPocorn(){
    img=pocornI;
    m=new Survive30Mission();
  }
}
class SkinHellokitty extends Skin{
  SkinHellokitty(){
    img=hellokittyI;
    m=new StatDmgMission();
  }
}
class SkinDisco extends Skin{
  SkinDisco(){
    img=discoI;
    m=new StatASMission();
  }
}

class SkinClown extends Skin{
  SkinClown(){
    img=clownI;
    m=new StatHpMission();
  }
}
class SkinFish extends Skin{
  SkinFish(){
    img=fishI;
    m=new StatManaMission();
  }
}
class SkinRaphi extends Skin{
  SkinRaphi(){
    img=raphiI;
    m=new StatRegenMission();
  }
}
class SkinDeath extends Skin{
  SkinDeath(){
    img=deathI;
    m=new Kill10EnemiesFrameMission();
  }
}
class SkinBane extends Skin{
  SkinBane(){
    img=baneI;
    m = new PoisonMission();
  }
}
class SkinMorpheus extends Skin{
  SkinMorpheus(){
    img=morpheusI;
    m = new RerollMissionextends();
  }
}
class SkinSkull extends Skin{
  SkinSkull(){
    img=skullI;
    m=new Kill10Bosses15TimeMission();
  }
}
class SkinGlitch extends Skin{
  SkinGlitch(){
    img=glitchI;
    m= new MapMission();
  }
}
class SkinCharlyBrown extends Skin{
  SkinCharlyBrown(){
    img=charlyBrownI;
    m=new Survive10NoItemsMission();
  }
}

class SkinBombSkin extends Skin{
  SkinBombSkin(){
    img=bombSkinI;
    m=new Kill10EnemiesFrameMission();
  }
} 
class Boss extends Prey{
  
  int phase=0;
  int maxPhase;
  int timeInPhase=0;
  int phaseCounter;
  
  Boss(int c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate){
    super(c,size,maxHp,xpSaved,walkSpeed,runSpeed,dmg,droprate);
    range=1100;
    weight=10;
    activeBoss=this;
    float dist=1130+size/2;
    angle=angle(0,0,player.posX,player.posY);
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
    this.maxHp=this.maxHp*1.1f*(float)Math.pow(1.12f,bossKills);//0.9*this.maxHp+0.1*this.maxHp*enemyStrength/0.3;//this.maxHp*0.5+his.maxHp*0.5/enemyStrength*0.3*Math.pow(1.35,bossesKilled)
    hp=this.maxHp;
    ccRes=20;
    //spawnRate*=0.9;
  }
  
  public void draw(){
    poisonedGot=0;
    phaseCounter--;
    if(phaseCounter<=0){
      phase=(phase+(int)random(1,maxPhase))%maxPhase;
      phaseCounter=timeInPhase;
    }
    if(hits>=3 && player.markDmg>0){
      turnImgD(markI,posX,posY,size*2,size*2,markAngle);
      markAngle-=0.01f;
    }
    body();
    hpBar();
    hit();
    shoot();
    if(stunned>0) stunned--;
    if(hitTimer>0)hitTimer--;
    if((hitTimer<=0 || !stopwhenHitting) && stunned<=0 )move();
    extra();
  }
  public void die(){
    prey.remove(this);
    kills.add(this);
    bossKills+=1;
    extraDrops();
    activeBoss=null;
    activeAltar = new BossAltar();
    bigMap.add(activeAltar);
    items.add(randomGoodLoot( posX, posY));
    if(bossKills%4==0) pickups.add(new GreenCrystal(posX+60,posY-80));
    else pickups.add(new GrayCrystal(posX+60,posY-80));
    pickups.add(new Coin(posX+random(-size/2,size/2),posY+random(-size/2,size/2)));
    for(int i=0;i<4;i++){
      if(random(100)<80) pickups.add(new Coin(posX+random(-size/2,size/2),posY+random(-size/2,size/2)));
    }
  }
  
  public void drawHP(){
    fill(255);
    rect(1920/2,80,800,40);
    fill(0xffB40000);
    rect(1920/2+(hp/maxHp-1)*800/2, 80,800*hp/maxHp,40);
    textC(""+(int)hp,1920/2,80,30,0);
  }
}



class CaveDweller extends Boss{
  
  CaveDweller(){
    super(0xff6F679D,140,700,0,1,1.2f,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=65;
    maxPhase=2;
    timeInPhase=120*5;
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(rightHandI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    if(phase==0){shootSplit(46,2.2f,15);}
    if(phase==1){shoot2(16,20,2.5f);}
  }
  
  public void run(){
    if(phase==0){
      speedX=0;speedY=0;
      if(distance(posX,posY,player.posX,player.posY)>1000)phase=1;
    }
    if(phase==1){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
      if(dist<600){
        float saveX=speedX;
        speedX=-speedY;
        speedY=saveX;
      }
    }
  }
}

class RavenBoss extends Boss{
  int wavesSpawned;
  
  RavenBoss(){
    super(0xff6F679D,140,700,0,1,0.9f,36,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=100;
    maxPhase=3;
    wavesSpawned=0;
    timeInPhase=120*5;
    phase=1;
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(ravenBossI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    if(phase==0)shoot3(4,20,15,2.1f);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){ shotSpeed=70;shootRaven();}else shotSpeed=100;
    if(phase==2)shoot4(15,2.6f);
  }
  
  public void shootRaven(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0f*60/shotSpeed*1.2f;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.4f*(player.posX-posX)/dist;
      float bulletSpeedY=2.4f*(player.posY-posY)/dist;
      new RavenBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,30);
    }
  }
  
  public void run(){
    if(phase==0){
      speedX=0;speedY=0;
    }
    if(phase==1){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
      if(dist<600){
        float saveX=speedX;
        speedX=-speedY;
        speedY=saveX;
      }
    }
    if(phase==2){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}

class RavenBullet extends EnemyBullet{
  int curveTime;
  float curve;
  float multi;
  boolean noCurve=false;
  
  RavenBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
    multi=1;
    if(random(100)<50)multi=-1;
    
    float speed=2.4f;
    curveTime=(int) (((distance(this.posX,this.posY,player.posX,player.posY)+120) / speed)); //6  frames it takes to hit target, half that time curve has to be 0
    if(curveTime<240) curveTime=240;
    curve=-multi*curveTime*speed/120.0f/2.0f;
    if(curveTime>300){
      noCurve=true;
      this.speedX*=2.5f;this.speedY*=2.5f;
      curve=0;
    }
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    if(!noCurve){
      posX-=speedY*curve;
      posY+=speedX*curve;
      curve+= 2.5f/120.0f*multi;
    }
    
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        player.getDmg(dmg);
        enemyBullets.remove(this);
    }
  }
  
  public void skin(){
    turnImgD(ravenBulletI,posX,posY,size*4,size*4,PI+angle(0,0,speedX-speedY*curve,speedY+speedX*curve));
  }
  
}


class Fish extends Boss{
  Body body;
  float chargeX;
  float chargeY;
  float charge;
  
  Fish(){
    super(0xff6F679D,140,800,0,1,1.2f,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    body = new Body(this);
    stopwhenHitting=false;
    shotSpeed=150;
    maxPhase=3;
    timeInPhase=120*3;
    phase=0;
    charge=120;
  }
  
  
  public void shoot(){
    if(phase==0)shootSpray(25,3.2f,50);
    if(phase==1);
    if(phase==2)charge-=1;
    if(charge<=0){
      phase=3;
      charge=120;
      phaseCounter=120*40;
    }
    if(charge==30){
      float dist=distance(posX,posY,player.posX,player.posY);
      chargeX=player.posX + 300*(player.posX-posX)/dist;//+ (player.posX-posX)/300
      chargeY=player.posY + 300*(player.posY-posY)/dist;
    }
    if(phase==3);
  }
  public void pushEnemies(){}
  
  public void run(){
    body.hitTimer=hitTimer;
    if(phase==0){
      strayve();
    }
    if(phase==1){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }
    if(phase==2){
      speedX=0;speedY=0;
    }
    if(phase==3){
      float dist=distance(posX,posY,chargeX,chargeY);
      if(dist<=runSpeed*4){
        phase=0;
        if(random(100)<50) phase=1;
        charge=120+hitTimer;
        phaseCounter=timeInPhase;
        if(random(100)<70)phase=2;
      }
      speedX = 3.6f*runSpeed*(chargeX-posX)/dist;
      speedY = 3.6f*runSpeed*(chargeY-posY)/dist;
      if(hitTimer>1)hitTimer++;
    }
  }
  public void body(){
    body.bodybefore();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishHeadI,0,0,size*2,size*2);
    popMatrix();
  }
  
  public void extraDrops(){
    body.die();
  }
}

class Body extends Prey{
  Fish fish;
  Tail tail;
  
  Body(Fish fish){
    super(0xff6F679D,140,300+400*enemyStrength,0,1,1.1f,45,0);
    this.fish=fish;
    this.posX = fish.posX;
    this.posY = fish.posY;
    this.size=fish.size*0.9f;
    stopwhenHitting=false;
    runSpeed=fish.runSpeed;
    tail = new Tail(this);
    boolean removable=false;
  }
    
  public void bodybefore(){
    tail.bodybefore();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishBodyI,0,0,size*2,size*2);
    popMatrix();
  }
  
  public void hpBar(){
  }
  
  public void move(){
    tail.hitTimer=hitTimer;
    runSpeed=fish.runSpeed;
    float dist=distance(posX,posY,fish.posX,fish.posY);
    if(dist>size){
      speedX = (fish.posX-posX)/10/dist *(dist-size) *runSpeed;
      speedY = (fish.posY-posY)/10/dist *(dist-size) *runSpeed;
      posX+=speedX;
      posY+=speedY;
    }
  }
  
  public void getHit(Bullet b){
    fish.getHit(b);
  }
  public void pushEnemies(){}
  
  public void die(){
    prey.remove(this);
    tail.die();
  }
}

class Tail extends Prey{
  Body body;
    
  Tail(Body body){
    super(0xff6F679D,140,700,0,1,1.1f,45,0);
    this.body=body;
    this.posX = body.posX;
    this.posY = body.posY;
    this.size=body.size;
    stopwhenHitting=false;
    boolean removable=false;
  }
  
  public void bodybefore(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishTailI,0,0,size*2,size*2);
    popMatrix();
  }
  public void hpBar(){
  }
  
  public void move(){
    runSpeed=body.runSpeed;
    float dist=distance(posX,posY,body.posX,body.posY);
    if(dist>size){
      speedX = (body.posX-posX)/10/dist *(dist-size) *runSpeed;
      speedY = (body.posY-posY)/10/dist *(dist-size) *runSpeed;
      posX+=speedX;
      posY+=speedY;
    }
  }
  public void pushEnemies(){}
  
  public void getHit(Bullet b){
    body.getHit(b);
  }
  
  public void die(){
    prey.remove(this);
  }
}

class RatKing extends Boss{
  int wavesSpawned;
  
  RatKing(){
    super(0xff6F679D,140,700,0,1,1,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=3;
    wavesSpawned=0;
    timeInPhase=120*4;
    phase=1;
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FlyI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    if(phase==0){
      nextShot--;
      if(nextShot<=0){
        nextShot=120.0f*60/shotSpeed*50/dmg;
        Rat r = new Rat();
        if(hp>10) hp-=2;
        float angle=random(TWO_PI);
        float dist = size/2+20;
        r.posX=posX+dist*cos(angle);
        r.posY=posY+dist*sin(angle);
        
      }
      
    }//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){
      poisonShot(3,15,25,2.5f);
    }
    if(phase==2)shoot4(27,2.2f);
  }
  
  public void poisonShot(int shots,float spread,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0f*2* 120/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+player.speedX*distReal/bulletSpeed;
      float aimY=player.posY+player.speedY*distReal/bulletSpeed;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=bulletSpeed*(aimX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(aimY-posY)/dist;
      
      spread=spread*TWO_PI/360*600/ (distance(posX,posY,aimX,aimY)+330) ;
      float angleS=angle(0,0,bulletSpeedX,bulletSpeedY)-shots*spread/2+spread/2;
      for(int i=0;i<shots;i++){
        bulletSpeedX=bulletSpeed*cos(angleS);
        bulletSpeedY=bulletSpeed*sin(angleS);
        new EnemyPoisonBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize,  (distance(posX,posY,aimX,aimY)+330)/bulletSpeed  );
        angleS+=spread;
      }
    }
  }
  
  public void run(){
    if(phase==0){
      speedX=0;speedY=0;
    }
    if(phase==1){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = 0.6f*runSpeed*(player.posX-posX)/dist;
      speedY = 0.6f*runSpeed*(player.posY-posY)/dist;
    }
    if(phase==2){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX =runSpeed*(player.posX-posX)/dist;
      speedY =runSpeed*(player.posY-posY)/dist;
    }
  }
  
  public void getPoisond(float dmgGot){
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}

class HiveBoss extends Boss{
  
  HiveBoss(){
    super(0xff6F679D,140,700,0,1,0.9f,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=50;
    maxPhase=3;
    timeInPhase=120*6;
    phase=0;
  }
  
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX>0) scale(-1,1);
    image(SpawnerBossI,0,0,size*2,size*2);
    image(rangeIndI,0,0,360*2,360*2);
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < 360/2 ){
        if(!(prey.get(i) instanceof HiveBoss) ) prey.get(i).hp+=2*enemyStrength/120 + dmg/120/8;
        if(prey.get(i).hp>prey.get(i).maxHp) prey.get(i).hp=prey.get(i).maxHp;
      }
    }
    
    if(phase==0)shoot3(4,18,15,2.1f);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){shootBlob();}
    if(phase==2){
      if(phaseCounter==120){
        for(int i=0;i<prey.size();i++){
          if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < 480/2 && !(prey.get(i) instanceof HiveBoss)){
            float dist = distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
            prey.get(i).knockbackX=3.5f*(prey.get(i).posX-posX)/dist;
            prey.get(i).knockbackY=3.5f*(prey.get(i).posY-posY)/dist;
            prey.get(i).knockbackTimer=120;
            prey.get(i).hp*=0.5f;
          }
        }
        
        float angleS=0;
        int shots=20;
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          float bulletSpeedX=3.5f*cos(angleS);
          float bulletSpeedY=3.5f*sin(angleS);
          new StraightEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,22);
        }
      }
    }
  }
  
  public void shootBlob(){
    nextShot--;
    if(nextShot<=0){
      phase=0;
      nextShot=120.0f*60/shotSpeed;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.6f*(player.posX-posX)/dist;
      float bulletSpeedY=2.6f*(player.posY-posY)/dist;
      new BlobBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,65);
    }
  }
  
  public void run(){
    if(phase==0){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }
    if(phase==1){
      speedX = 0;
      speedY = 0;
    }
    if(phase==2){
      speedX = 0;
      speedY = 0;
    }
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}

class BlobBullet extends EnemyBullet{
  float timer;
  
  BlobBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
    this.timer=(200+distance(posX,posY,player.posX,player.posY))/2.6f;
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        explode();
    }
    timer--;
    if(timer<=0)explode();
  }
  
  public void explode(){
    
    
    Prey e = null;
    
    for(int i=0;i<1+5*spawnRate;i++){
      e = null;
      int r = (int)random(5);
      if(i==0)r=0;
      if(r==0 && random(100)<100) e=new Crawler();
      if(r==1 && random(100)<40) e=new Shooter();
      if(r==2 && random(100)<30) e=new BigCrawler();
      if(r==3 && random(100)<40) e=new RedShooter();
      if(r==4 && random(100)<15) e=new Sprinkler();
      
      float dist=size/2;
      float angle=random(TWO_PI);
      if(e!=null){
        e.hp*=0.5f;
        e.posX=posX+dist*cos(angle);
        e.posY=posY+dist*sin(angle);
      }
    }
    enemyBullets.remove(this);
  }
  
  public void skin(){
    turnImgD(greenBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}


class BeeBoss extends Boss{ 
  HoneyPot honey;
  
  BeeBoss(){
    super(0xff6F679D,140,700,0,1,0.9f,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=60;
    maxPhase=4;
    timeInPhase=120*4;
    phase=0;
  }
  
  public void body(){
    if(honey!=null) honey.draw();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(BeeI,0,0,size*2,size*2);
    noFill();
    noTint();
    popMatrix();
  }
  
  public void shoot(){
    
    if(phase==0);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){shootBee();}
    if(phase==2){
      if(phaseCounter%120==100){ 
        int shots=20;
        float angleS=angle(posX,posY,player.posX,player.posY);
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          float bulletSpeedX=2.7f*cos(angleS);
          float bulletSpeedY=2.7f*sin(angleS);
          new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,30);
        }
      }
    }
    if(phase==3){
       phase=0;
       if(honey!=null){
         bigMap.add(new PoisonField(honey.posX,honey.posY));
         smallMap.add(new PoisonField(honey.posX,honey.posY));
       }
       float angleS=angle(posX,posY,player.posX,player.posY);
       honey=new HoneyPot(posX-size/2*cos(angleS),posY-size/2*sin(angleS),this);
       hp+=maxHp*0.05f+10;
       if(hp>maxHp)hp=maxHp;
    }
  }
  
  public void shootBee(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0f*60/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+player.speedX*distReal/3;
      float aimY=player.posY+player.speedY*distReal/3;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=3*(aimX-posX)/dist;
      float bulletSpeedY=3*(aimY-posY)/dist;
      Bee b=new Bee(posX+bulletSpeedX*23,posY+bulletSpeedY*23);
      b.knockbackX=bulletSpeedX;
      b.knockbackY=bulletSpeedY;
      b.knockbackTimer=dist/3;
      if(spawnRate<=2) b.hp*=spawnRate/2;
    }
  }
  
  public void run(){
    if(phase==0){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }
    if(phase==1){
      speedX = 0;
      speedY = 0;
    }
    if(phase==2){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }
  }
  
  public void honeyExplode(){
    hp-=maxHp*0.06f+20;
    if(hp<1) hp=2;
    honey=null;
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}

class HoneyPot{
  float posX;
  float posY;
  float size;
  BeeBoss b;

  HoneyPot(float posX,float posY,BeeBoss b ){
    this.posX=posX;
    this.posY=posY;
    this.b=b;
    size=80;
  }
  
  public void draw(){
    body();
    pushEnemies();
    for(int i=0;i<bullets.size();i++){
      if(distance(posX,posY,bullets.get(i).posX,bullets.get(i).posY) < (size+bullets.get(i).size)/2 ){
        bigMap.add(new PoisonField(posX,posY));
        smallMap.add(new PoisonField(posX,posY));
        bullets.get(i).trigger();
        b.honeyExplode();
      }
    }
  }
  
  public void pushEnemies(){
    
    for(int i=0;i<prey.size();i++){
      float distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
      if(distFromEnemy < (size+prey.get(i).size)/2+2){
        float weigthDiff=prey.get(i).weight/1;
        posX+=1*(posX-prey.get(i).posX)/distFromEnemy*weigthDiff;
        posY+=1*(posY-prey.get(i).posY)/distFromEnemy*weigthDiff;
        distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        while(distFromEnemy > (size+prey.get(i).size)/2+2){
          posX-=(posX-prey.get(i).posX)/distFromEnemy;
          posY-=(posY-prey.get(i).posY)/distFromEnemy;
          distFromEnemy=distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
        }
      }
    }
    
    float distFromEnemy=distance(posX,posY,player.posX,player.posY);
      if(distFromEnemy < (size+player.size)/2+2){
        posX+=1*(posX-player.posX)/distFromEnemy*3;
        posY+=1*(posY-player.posY)/distFromEnemy*3;
        distFromEnemy=distance(posX,posY,player.posX,player.posY);
        while(distFromEnemy > (size+player.size)/2+2){
          posX-=(posX-player.posX)/distFromEnemy;
          posY-=(posY-player.posY)/distFromEnemy;
          distFromEnemy=distance(posX,posY,player.posX,player.posY);
        }
      }
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    image(HoneyPotI,0,0,size*2,size*2);
    noFill();
    noTint();
    popMatrix();
  }
}
public Item randomLoot(float posX,float posY){
  ArrayList<Item> dropPool = new ArrayList<Item>();
  
  dropPool.add(new Ad(posX,posY));
  dropPool.add(new Mag(posX,posY));
  dropPool.add(new ASItem(posX,posY));
  dropPool.add(new SpeedItem(posX,posY));
  dropPool.add(new ReloadItem(posX,posY));
  dropPool.add(new Elephant(posX,posY));
  dropPool.add(new HeartItem(posX,posY));
  dropPool.add(new ManaItem(posX,posY));
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

public GoodItem randomGoodLoot(float posX,float posY){
  ArrayList<GoodItem> dropPool = new ArrayList<GoodItem>();
  
  dropPool.add(new RavenItem(posX,posY));
  dropPool.add(new Pepper(posX,posY));
  dropPool.add(new BlueCheese(posX,posY));
  dropPool.add(new Warrior(posX,posY));
  dropPool.add(new Trident(posX,posY));
  dropPool.add(new MagicMag(posX,posY));
  dropPool.add(new ShieldRhino(posX,posY));
  dropPool.add(new Monk(posX,posY));
  dropPool.add(new Firststrike(posX,posY));
  dropPool.add(new Marioshroom(posX,posY));
  dropPool.add(new BigElephant(posX,posY));
  dropPool.add(new Plug(posX,posY));
  dropPool.add(new ChainBall(posX,posY));
  dropPool.add(new MagicHat(posX,posY));
  dropPool.add(new Driller(posX,posY));
  dropPool.add(new Ghost(posX,posY));
  dropPool.add(new SpinnyGunn(posX,posY));
  dropPool.add(new Sandclock(posX,posY));
  dropPool.add(new StickyBombItem(posX,posY));
  dropPool.add(new Singed(posX,posY));
  dropPool.add(new SteakEater(posX,posY));
  dropPool.add(new CampingTools(posX,posY));
  dropPool.add(new Dagger(posX,posY));
  dropPool.add(new ChestBuffer(posX,posY));
  
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

public GoodItem tradeItem(float posX,float posY){
  ArrayList<GoodItem> dropPool = new ArrayList<GoodItem>();
  
  dropPool.add(new AsforAd(posX,posY));
  dropPool.add(new AdforAs(posX,posY));
  dropPool.add(new AsforHp(posX,posY));
  dropPool.add(new HpforReload(posX,posY));
  dropPool.add(new ReloadfoMag(posX,posY));
  dropPool.add(new MagforMana(posX,posY));
  dropPool.add(new ManaforRegen(posX,posY));
  
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

class Item{
  float posX;
  float posY;
  int kapa;
  int redKapa;
  int code;
  String name;
  String description;
  int tier;
  int stacks;
  
  Item(float posX,float posY){
    this.posX=posX;
    this.posY=posY;
    kapa=1;
    tier=1;
    redKapa=0;
    name="Name";
    description="does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" ;
  }
  public void drawBoxes(){}
  
  public void stats1(){ 
  }
  public void stats2(int itemWave){ 
  }
  
  public void drawMap(){
    drawSkinMap();
    
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<50){
      items.remove(this);
      player.itemsEquipt.add(this);
    }
  }
  
  public void drawSkinMap(){
    fill(200);
    rectD(posX,posY,60,40);
  }
  public void drawSkinMenu(){
  }
  
  public void drawMenuText(){
    textC(name,1160,50,30,0);
    textSize(18);
    text(description,1160,150,780,330);
  }
  
  public void drawMenu(){
  }
  
  public void reset(){
    stacks=0;
  }
}

class GoodItem extends Item{
  boolean rollable=true;
  GoodItem(float posX,float posY){
    super(posX,posY);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(ravenItemI,posMX,posMY,60,60);
  }
  
  public void drawMap(){
    drawSkinMap();
    
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<50){
      items.remove(this);
      player.itemsGood.add(this);
      pickup();
    }
  }
  
  public void pickup(){}
}
ArrayList<PImage> imgDrawnBox;
int boxesDrawn;
public void itemHud(){
  boxesDrawn=0;
  imgDrawnBox = new ArrayList<PImage>();
  
  for(int i=0;i<player.itemsGood.size();i++){
    player.itemsGood.get(i).drawBoxes();
  }
}

public void itemEffectsBoxes(PImage img,int number,int c){
  if(!imgDrawnBox.contains(img)){
    
  imgDrawnBox.add(img);
  float posX=250+60*boxesDrawn;
  float posY=870;
  
  image(img,posX,posY,50,50);
  textC(""+number,posX+12,posY+12,18,0);
  boxesDrawn++;
  }
}

class Pickup{
  float posX;
  float posY;
  float size;
 
  
  public void drawMap(){
    drawSkinMap();
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<(size+player.size+20)/2){
      pickups.remove(this);
      pickUp();
    }
  }
  public void pickUp(){}
  public void drawSkinMap(){}
}


class Coin extends Pickup{
  Coin(float posX,float posY){
    size=18;
    this.posX=posX;
    this.posY=posY;
  }
  
  public void pickUp(){
    money++;
    moneyRound++;
  }
  
  
  public void drawSkinMap(){
    imageD(coin1I,posX,posY,size*2);
  }
}

class GrayCrystal extends Pickup{
  GrayCrystal(float posX,float posY){
    size=50;
    this.posX=posX;
    this.posY=posY;
  }
  
  public void pickUp(){
    timeChrystal++;
  }
  
  
  public void drawSkinMap(){
    imageD(grayCrytalI,posX,posY,size*2);
  }
}

class GreenCrystal extends Pickup{
  GreenCrystal(float posX,float posY){
    size=70;
    this.posX=posX;
    this.posY=posY;
  }
  
  public void pickUp(){
    greenChrystal++;
  }
  
  
  public void drawSkinMap(){
    imageD(greenCrytalI,posX,posY,size*2);
  }
}

class Steak extends Pickup{
  boolean picked;
  int timer;
  Steak(float posX,float posY){
    size=38;
    this.posX=posX;
    this.posY=posY;
    pickups.add(this);
    timer=16*120;
  }
  
  public void pickUp(){
    picked=true;
    player.hp+=5;
    player.hp+=player.maxHp*0.05f;
    timer=timer/2;
    timer+=120;
    if(player.hp>player.maxHp)player.hp=player.maxHp;
  }
  
  public void drawSkinMap(){
    imageD(steakI,posX,posY,size*2);
  }
}
class Ad extends Item{
  
  Ad(float posX,float posY){
    super(posX,posY);
    tier=1;
    name="Tooth";
    description = "+1 damage\n"+//6
    "\n"+ //6
    "";
  }
  
  public void stats1(){
    player.dmg +=1;
  }
  
  public void drawSkinMap(){
    imageD(commonSharkbiteI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonSharkbiteI,posX,posY,60,60);
  }
}

class ASItem extends Item{
  
  ASItem(float posX,float posY){
    super(posX,posY);
    code=1;
    name="Triggerfinger Ring";
    description = "+0.3 attackspeed\n"+
    "";
  }
  
  public void stats1(){
    player.attackSpeed +=0.3f;
  }
  
  public void drawSkinMap(){
    imageD(commonASRingI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonASRingI,posX,posY,60,60);
  }
}


class Mag extends Item{
  
  Mag(float posX,float posY){
    super(posX,posY);
    tier=1;
    name="Bullet";
    description = "+1 magsize\n"+//6
    "\n"+ //6
    "";
  }
  
  public void stats1(){
    player.magsize +=1;
  }
  
  public void drawSkinMap(){
    imageD(commonMagUpI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonMagUpI,posX,posY,60,60);
  }
}

class ReloadItem extends Item{
  
  ReloadItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=5;
    name="Reload Oil";
    description = "+0.08 reloadspeed\n"+//1 
    "";
  }
  
  public void stats1(){
    player.reloadSpeed +=0.08f;
  }
  
  public void drawSkinMap(){
    imageD(commonOilI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonOilI,posX,posY,60,60);
  }
}

class Elephant extends Item{
  
  Elephant(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=25;
    name="Elephant Foot";
    description = "+2 bulletsize\n"+
    "+10 hp\n"+
    " ";
  }
  
  public void stats1(){
    player.maxHp+=10;
    player.bulletSize +=2;
  }
  
  public void drawSkinMap(){
    imageD(commonElefantI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonElefantI,posX,posY,60,60);
  }
}
class SpeedItem extends Item{
  
  SpeedItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=4;
    name="Energy Drink";
    description = "+0,04 speed\n"+
    "";
  }
  
  public void stats1(){
    player.speed +=0.04f;
  }
  
  public void drawSkinMap(){
    imageD(commonEnergyI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonEnergyI,posX,posY,60,60);
  }
}

class HeartItem extends Item{
  
  HeartItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=60;
    tier=1;
    name="Pigheart";
    description = "+0.2 regen/s\n"+
    "";
  }
  
  public void stats1(){
    player.regen+=0.2f/120;
  }
  
  public void drawSkinMap(){
    imageD(commonHeartI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonHeartI,posX,posY,60,60);
  }
}

class ManaItem extends Item{
  
  ManaItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=61;
    tier=1;
    name="Manapotion";
    description = "+0.2 mana/s\n"+
    " ";
  }
  
  public void stats1(){
    player.manaRegen+=0.2f/120;
  }
  
  public void drawSkinMap(){
    imageD(commonManaI,posX,posY,50);
  }
  public void drawSkinMenu(){
    image(commonManaI,posX,posY,60,60);
  }
}




//bossitems
///////////

class RavenItem extends GoodItem{
  float nextRaven;
  
  RavenItem(float posX,float posY){
    super(posX,posY);
    name="Ravenshot";
    nextRaven=random(120*5);
    description = "shoots 1 raven /s\n"+//6
    "every 4 seconds\n"+//6
    "ravens deal 300% dmg/s\n"+//6
    "";
  }

  public void stats2(int itemWave){
    if(itemWave==60){
      nextRaven--;
      if(nextRaven<=0){
        nextRaven=120*4;
        RavenBulletPlayer b = new RavenBulletPlayer();
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(ravenItemI,posX,posY,70);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(ravenItemI,posMX,posMY,60,60);
  }
}


class Pepper extends GoodItem{
  
  Pepper(float posX,float posY){
    super(posX,posY);
    name="Pepper";
    description = "shoots 1 pepperbullet /s\n"+//6
    "deals 10 dmg\n"+//6
    "/s\n"+//6
    "";
  }

  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame){
        SchrottBullet peprrr = new SchrottBullet(10);
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(rarePepperI,posX,posY,70);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rarePepperI,posMX,posMY,60,60);
  }
}

class BlueCheese extends GoodItem{
  
  BlueCheese(float posX,float posY){
    super(posX,posY);
    name="Bluecheese";
    description = "+0.6 regen/s\n"+//6
    "when you are full life\n"+//6
    "shoot a wave of 10 bullets\n"+//6
    "but lose 10 hp\n"+//6
    " ";
  }
   public void stats1(){
     player.regen+=0.6f/120;
   }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.hp>=player.maxHp){
        player.hp-=10;
        float angleS=0;
        for(int i=0;i<10;i++){
          angleS+=TWO_PI/10;
          CheesBullet b=new CheesBullet();
          b.speedX=player.bulletSpeed/5*cos(angleS);
          b.speedY=player.bulletSpeed/5*sin(angleS);
        }
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(rareBlueCheesI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareBlueCheesI,posMX,posMY,60,60);
  }
}

class Warrior extends GoodItem{
  
  Warrior(float posX,float posY){
    super(posX,posY);
    name="Warrior";
    description = "+20 hp\n"+
    "for each 20 missing hp gain:/s\n"+//6
    "+0.3 attackspeed\n"+//6
    "+1 dmg\n"+//6
    //6
    "";
  }
  
  public void stats1(){
    player.maxHp+=20;
  }
  public void stats2(int itemWave){
    if(itemWave==30){
      float missingHp=(player.maxHp-player.hp)/20;
        player.attackSpeed+=0.3f*missingHp;
        player.dmg+=1*missingHp;
        stacks=(int)missingHp;
    }
  }
  
  public void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(rareWarriorI,stacks,255);
    }
  }
  
  public void drawSkinMap(){
    imageD(rareWarriorI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareWarriorI,posMX,posMY,60,60);
  }
}

boolean tridentHit=false;
class Trident extends GoodItem{
  
  Trident(float posX,float posY){
    super(posX,posY);
    name="Trident";
    description = "50% chance to\n"+//6
    "shoot bullets in 4 direction/s\n"+//6
    "+4 bulletsize\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    tridentHit=false;
    player.bulletSize+=4;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame && !tridentHit && random(100)<50){
        float speedSave;
        Bullet b = new Bullet();speedSave=b.speedX; b.speedX=b.speedY; b.speedY=-speedSave; 
        b = new Bullet();speedSave=b.speedX; b.speedX=-b.speedY; b.speedY=speedSave; 
        b = new Bullet(); b.speedX=-b.speedX; b.speedY=-b.speedY;
        tridentHit=true;
      }
    }
  }
  
  public void drawBoxes(){
  }
  
  public void drawSkinMap(){
    imageD(commonTridentI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(commonTridentI,posMX,posMY,60,60);
  }
}


class MagicMag extends GoodItem{
  int timer;
  
  MagicMag(float posX,float posY){
    super(posX,posY);
    name="Warrior";
    description = "killing an enemy gives:/s\n"+//6
    "+1 bullet in the mag\n"+//6
    "+5 dmg for 0.5 sec\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    if(kills.size()>0 && player.bulletsMag>0){
      player.bulletsMag+=1;
      timer = 60;
    }
    if(timer>=0){
      timer--;
      player.dmg+=5;
    }
  }
  
  public void drawBoxes(){
    if(timer>0){
      itemEffectsBoxes(superRareMagInAHatI,5,255);
    }
  }
  
  public void drawSkinMap(){
    imageD(superRareMagInAHatI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(superRareMagInAHatI,posMX,posMY,60,60);
  }
}

class ShieldRhino extends GoodItem{
  int stacks;
  
  ShieldRhino(float posX,float posY){
    super(posX,posY);
    name="Rhinos horn";
    description = "killing 20 enemy gives/s\n"+//6
    "+60 shield\n"+//6
    "while shielded gain +3 dmg\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    stacks+=kills.size();
    if(stacks>=20){
      stacks=0;
      player.shield+=60;
    }
    if(player.shield >0){
      player.dmg+=3.0f;
    }
  }
  
  public void drawBoxes(){
    itemEffectsBoxes(rareRhinoI,stacks,255);
  }
  
  public void drawSkinMap(){
    imageD(rareRhinoI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareRhinoI,posMX,posMY,60,60);
  }
}

class Firststrike extends GoodItem{
  
  Firststrike(float posX,float posY){
    super(posX,posY);
    name="Firststrike";
    description = "first shot in your mag explodes/s\n"+//6
    "100% explosion dmg\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(stacks>0 && player.shotFrame){
        new Rocket();
      }
      
    
      if(player.bulletsMag==player.magsize){
        stacks=1;
      }else stacks=0;
    }
  }
  
  public void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(commonScopeI,1,255);
    }
  }
  
  public void drawSkinMap(){
    imageD(commonScopeI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(commonScopeI,posMX,posMY,60,60);
  }
}

class Monk extends GoodItem{
  int timer;
  
  Monk(float posX,float posY){
    super(posX,posY);
    name="Monk";
    description = "not shooting gives up to 10 stacks/s\n"+//6
    "shooting releases the stacks\n"+//6
    "dealing big dmg\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.nextShot<=0 || player.bulletsMag==0)timer--;
      if(timer<0){
        timer=90;
        if(stacks<10) stacks+=1;
      }
      if(player.shotFrame){
        if(stacks>0){
          new Bubble();
          new Bubble();
          stacks--;
        }
      }
    }
  }
  
  public void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(rareNinjaI,stacks,255);
    }
  }
  
  public void drawSkinMap(){
    imageD(rareNinjaI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareNinjaI,posMX,posMY,60,60);
  }
}

class Marioshroom extends GoodItem{
  
  Marioshroom(float posX,float posY){
    super(posX,posY);
    name="Big Shroom";
    description = "all stats up\n"+//6
    "\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    player.maxHp+=10;
    player.dmg+=1;
    player.magsize+=1;
    player.attackSpeed+=0.3f;
    player.reloadSpeed+=0.08f;
    player.speed+=0.04f;
    player.regen+=0.2f/120;
    player.manaRegen+=0.2f/120;
  }
  
  public void drawSkinMap(){
    imageD(allStatsUpI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(allStatsUpI,posMX,posMY,60,60);
  }
}

class BigElephant extends GoodItem{
  int timer;
  BigElephant(float posX,float posY){
    super(posX,posY);
    name="Elephant";
    description = "+50 hp\n"+//6
    "every 12 sec shoot\n"+//6
    "a powerfull shot\n"+//6
    "(50% maxHp dmg)\n"+//6
    "";
    timer=(int)random(120*15);
  }
  
  public void stats1(){
    player.maxHp+=50;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      if(timer<=0){
        timer=120*12;
        new ElephantBullet();
      }
    }
  }
  
  public void drawBoxes(){
    itemEffectsBoxes(elephantI,(int)(timer/120),255);
  }
  
  public void drawSkinMap(){
    imageD(elephantI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(elephantI,posMX,posMY,60,60);
  }
}

class Plug extends GoodItem{
  Plug(float posX,float posY){
    super(posX,posY);
    name="Elephant";
    description = "+1.0 attackspeed\n"+//6
    "create targetet\n"+//6
    "bullets each shot\n"+//6
    "(3 dmg)\n"+//6
    "";
  }
  
  public void stats1(){
    player.attackSpeed+=1.0f;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame){
        BoltBullet peprrr = new BoltBullet(3, player.posX,player.posY,600);
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(commonPlugI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(commonPlugI,posMX,posMY,60,60);
  }
}

class ChainBall extends GoodItem{
  ChainBall(float posX,float posY){
    super(posX,posY);
    name="Ball and Chain";
    description = "+0.3 reloadspeed\n"+//6
    "reloading creates\n"+//6
    "1 orbital bullets\n"+//6
    "200% dmg)\n"+//6
    "";
  }
  
  public void stats1(){
    player.reloadSpeed+=0.3f;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.reloading>1 && player.reloading<=2){
        OrbitalBullet peprrr = new OrbitalBullet();
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(commonSlowI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(commonSlowI,posMX,posMY,60,60);
  }
}

class MagicHat extends GoodItem{
  MagicHat(float posX,float posY){
    super(posX,posY);
    name="Wizard Hat";
    description = "+25 max mana\n"+//6
    "gain more mana\n"+//6
    "the more missing mana you have\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    player.maxMana+=25;
  }
  
  public void stats2(int itemWave){
    if(itemWave==40){
      player.mana+= 2 *(player.maxMana-player.mana)/100.0f/120;
    }
  }
  
  public void drawSkinMap(){
    imageD(rareMagicHatI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareMagicHatI,posMX,posMY,60,60);
  }
}

class Driller extends GoodItem{
  int timer;
  Driller(float posX,float posY){
    super(posX,posY);
    name="Driller";
    description = "every 3s shoot\n"+//6
    "piercing spikes in every directon\n"+//6
    "kills reduce by 0.5s\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      timer-=kills.size()*60;
      if(timer<=0){
        timer=120*3;
        float angleS=0;
        int shots=12;
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          PiercingBullet b=new PiercingBullet();
          b.dmg=player.dmg*0.5f+5;
          b.maxRange=360+player.bulletSize*3;
          b.size=14;
          b.speedX=player.bulletSpeed/2*cos(angleS);
          b.speedY=player.bulletSpeed/2*sin(angleS);
        }
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(drillI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(drillI,posMX,posMY,60,60);
  }
}

class Ghost extends GoodItem{
  int timer;
  Ghost(float posX,float posY){
    super(posX,posY);
    name="Ghost";
    description = "+1 stack for each 8 second not damaged\n"+//6
    "stacks give:\n"+//6
    "+10 hp\n"+//6
    "+1 magSize\n"+//6
    "+0.2 regen";
  }
  
  public void stats1(){
    if(stacks<10)timer--;
    if(timer<=0){
      stacks++;
      timer=8*120;
    }
    else{
      player.maxHp+=10 *stacks;
      player.regen+=0.2f/120 *stacks;
      player.magsize+=1*stacks;
    }
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.gotDamaged){
        timer=5*120;
        stacks=0;
      }
    }
  }
  public void drawBoxes(){
    if(stacks>0) itemEffectsBoxes(rareStealthI,stacks,255);
  }
  
  public void drawSkinMap(){
    imageD(rareStealthI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareStealthI,posMX,posMY,60,60);
  }
}

class SpinnyGunn extends GoodItem{
  int stacks;
  int timer;
  SpinnyGunn(float posX,float posY){
    super(posX,posY);
    name="Spinnygun";
    description = "+1 magsize\n"+//6
    "shooting gives:\n"+//6
    "+0.2 attackspeed\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    if(player.shotFrame)stacks++;
    player.attackSpeed+=0.2f*stacks;
    player.magsize+=1;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.bulletsMag<=0)stacks=0;
      if(player.nextShot<=0)timer++;
      else timer=0;
      if(timer>0.5f*120.0f/stacks){
        stacks--;
        if(player.bulletsMag==0)stacks=0;
        timer=0;
      }
    }
  }
  public void drawBoxes(){
    if(stacks>0)itemEffectsBoxes(goldSpinnyGunI,stacks,255);
  }
  
  public void drawSkinMap(){
    imageD(goldSpinnyGunI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(goldSpinnyGunI,posMX,posMY,60,60);
  }
}

class Sandclock extends GoodItem{
  int timer;
  boolean ad;
  Sandclock(float posX,float posY){
    super(posX,posY);
    name="Sandclock";
    description = "either gives\n"+//6
    "+10 dmg\n"+//6
    "-20% attackspeed\n"+//6
    "or\n"+//6
    "+2.5 attackspeed\n"+//6
    "-20% dmg";
  }
  
  public void stats1(){
    if(ad){ 
      player.dmg+=5;
    }else player.attackSpeed+=1.5f;
    timer--;
    if(timer<=0){
      timer=120*15;
      ad=!ad;
    }
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      if(ad){ 
        player.attackSpeed*=0.8f;
        player.dmg*=1.0f/0.8f;
      }else{
        player.dmg*=0.8f;
        player.attackSpeed*=1.0f/0.8f;
      }
    }
  }
  public void drawBoxes(){
    itemEffectsBoxes(sandclockI,timer/120,255);
  }
  
  public void drawSkinMap(){
    imageD(sandclockI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(sandclockI,posMX,posMY,60,60);
  }
}

class StickyBombItem extends GoodItem{
  int timer;

  StickyBombItem(float posX,float posY){
    super(posX,posY);
    name="Sticky Bombs";
    description = "either gives\n"+//6
    "+5 dmg\n"+//6
    "or\n"+//6
    "+1.5 attackspeed\n"+//6
    "";
  }
  
  
  public void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      if(player.shotFrame && timer<=0 && random(100)<50){
        timer=0;
        new StickyBomb();
      }
    }
  }
  public void drawBoxes(){
  }
  
  public void drawSkinMap(){
    imageD(stickyBombI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(stickyBombI,posMX,posMY,60,60);
  }
}


class Singed extends GoodItem{
  float timer;

  Singed(float posX,float posY){
    super(posX,posY);
    name="Poison Gas";
    description = "leave a trail of\n"+//6
    "poison behind you\n"+//6
    "50% dmg/s\n"+//6
    "+0.1 speed\n"+//6
    "";
  }
  public void stats1(){
    player.speed+=0.1f;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      timer-= distance(0,0,player.speedX,player.speedY);
      if(timer<=0){
        timer=90;
        new Gas(player.posX-5*player.speedX,player.posY-5*player.speedY,player.dmg*0.5f,50);
      }
    }
  }
  public void drawBoxes(){
  }
  
  public void drawSkinMap(){
    imageD(superRarePoisonI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(superRarePoisonI,posMX,posMY,60,60);
  }
}

class SteakEater extends GoodItem{
  ArrayList<Steak> steaks = new ArrayList<Steak>();
  int bonusAd;
  SteakEater(float posX,float posY){
    super(posX,posY);
    name="Ribeye steak";
    description = "enemies have a chance to drop\n"+//6
    "a steak giving\n"+//6
    "+5 dmg for 5 seconds\n"+//6
    "+10 hp\n"+//6
    "";
  }
  public void stats1(){
    bonusAd=0;
    for(int i=0;i<steaks.size();i++){
      if(steaks.get(i).picked)bonusAd+=5;
      steaks.get(i).timer--;
        if(steaks.get(i).timer<=0){
          pickups.remove(steaks.get(i));
          steaks.remove(i);
        }
    }
    player.dmg+=bonusAd;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      for(int i=0;i<kills.size();i++){
        if(random(100)<10 +5*(player.maxHp-player.hp)/100) steaks.add(new Steak(kills.get(i).posX,kills.get(i).posY));
      }
    }
  }
  public void drawBoxes(){
    if(bonusAd>0) itemEffectsBoxes(commonFoodI,bonusAd,255);
  }
  
  public void drawSkinMap(){
    imageD(commonFoodI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(commonFoodI,posMX,posMY,60,60);
  }
}

class CampingTools extends GoodItem{
  int timer;
  CampingTools(float posX,float posY){
    super(posX,posY);
    name="Camping Tools";
    description = "every 60 seconds\n"+//6
    "create a camp. standing on it gives:\n"+//6
    "+2 healing/s\n"+//6
    "+2 mana/s\n"+//6
    "slows enemies/s";
  }
  public void stats1(){
    timer--;
    if(timer<=0){
      timer=60*120;
      Camp c= new Camp(player.posX,player.posY);
      bigMap.add(c);
      smallMap.add(c);
    }
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
    }
  }
  public void drawBoxes(){
  }
  
  public void drawSkinMap(){
    imageD(rareCampI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareCampI,posMX,posMY,60,60);
  }
}
class Dagger extends GoodItem{
  int timer;
  float rememberX = 1.5f;
  float rememberY;
  Dagger(float posX,float posY){
    super(posX,posY);
    name="Dagger";
    description = "shoots 10 daggers per second\n"+//6
    "\n"+//6
    "(30% dmg)\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
      if(player.bulletsMag>0)timer--;
      if(player.speedX!=0 || player.speedY!=0){
          rememberX=player.speedX;
          rememberY=player.speedY;
      }
      if(timer<=0){
        timer=12;
        new DaggerBullet(angle(0,0,rememberX/player.speed,rememberY/player.speed));
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(rareDaggerI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareDaggerI,posMX,posMY,60,60);
  }
}

float greenChest;
class ChestBuffer extends GoodItem{
  ChestBuffer(float posX,float posY){
    super(posX,posY);
    name="Pirates chest";
    description = "25% chance to get a green\n"+//6
    "item out of a chest\n"+//6
    "also closes 50% of opend chests\n"+//6
    "\n"+//6
    "";
    
    
  }
  
  public void stats1(){
    greenChest+=0.25f;
  }
  
  public void stats2(int itemWave){
    if(itemWave==60){
    }
  }
  
  public void drawSkinMap(){
    imageD(rareChestI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareChestI,posMX,posMY,60,60);
  }
  public void pickup(){
    for(int i=0;i<bigMap.size();i++){
      if(random(100)<50 && bigMap.get(i) instanceof Chest) ((Chest)bigMap.get(i)).state=1;
    }
  }
}

///itemTrades

class AsforAd extends GoodItem{
  AsforAd(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(){
    player.attackSpeed*= 2;
    player.dmg*= 0.5f;
  }
  
  public void drawSkinMap(){
    imageD(commonASRingI,posX+50,posY,50);
    imageD(commonSharkbiteI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonASRingI,posMX+16,posMY+10,30,30);
    image(commonSharkbiteI,posMX-16,posMY+10,30,30);
  }
}

class AdforAs extends GoodItem{
  AdforAs(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      player.dmg*= 2;
      player.attackSpeed*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonSharkbiteI,posX+50,posY,50);
    imageD(commonASRingI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonSharkbiteI,posMX+16,posMY+10,30,30);
    image(commonASRingI,posMX-16,posMY+10,30,30);
  }
}

class AsforHp extends GoodItem{
  AsforHp(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 attackspeed\n"+//6
    "half hp\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      player.attackSpeed*= 2;
      player.maxHp*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonASRingI,posX+50,posY,50);
    imageD(commonElefantI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonASRingI,posMX+16,posMY+10,30,30);
    image(commonElefantI,posMX-16,posMY+10,30,30);
  }
}

class HpforReload extends GoodItem{
  HpforReload(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      player.maxHp*= 2;
      player.reloadSpeed*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonElefantI,posX+50,posY,50);
    imageD(commonOilI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonElefantI,posMX+16,posMY+10,30,30);
    image(commonOilI,posMX-16,posMY+10,30,30);
  }
}

class ReloadfoMag extends GoodItem{
  ReloadfoMag(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==21){
      player.reloadSpeed*= 2;
      player.magsize*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonOilI,posX+50,posY,50);
    imageD(commonMagUpI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonOilI,posMX+16,posMY+10,30,30);
    image(commonMagUpI,posMX-16,posMY+10,30,30);
  }
}

class MagforMana extends GoodItem{
  MagforMana(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      player.magsize*= 2;
      player.manaRegen*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonMagUpI,posX+50,posY,50);
    imageD(commonManaI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonMagUpI,posMX+16,posMY+10,30,30);
    image(commonManaI,posMX-16,posMY+10,30,30);
  }
}

class ManaforRegen extends GoodItem{
  ManaforRegen(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats2(int itemWave){
    if(itemWave==20){
      player.manaRegen*= 2;
      player.regen*= 0.5f;
    }
  }
  
  public void drawSkinMap(){
    imageD(commonManaI,posX+50,posY,50);
    imageD(commonHeartI,posX-50,posY,50);
  }
  public void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonManaI,posMX+16,posMY+10,30,30);
    image(commonHeartI,posMX-16,posMY+10,30,30);
  }
}
PImage menuI;
PImage ingameHudI;
PImage TabMenuI;
PImage hitScreenI;
PImage diamondBuyI;

PImage floorI;
PImage floor1I;
PImage floor2I;
PImage floor3I;
PImage stoneI;
PImage treeI;
PImage treeRipeI;
PImage saplingI;
PImage stone2I;
PImage tree2I;
PImage weirdTreeI;
PImage weirdPilzI;
PImage weirdGrasI;
PImage grasI;
PImage pilzI;
PImage spikeI;
PImage dungeonI;
PImage caveClearedI;
PImage chestI;
PImage chestOpenI;
PImage scrapperI;
PImage shopI;
PImage tentI;
PImage scrapMillI;
PImage cutWheatI;
PImage wheatI;
PImage redChestI;
PImage openredChestI;
PImage vaseI;
PImage graveI;
PImage graveOpendI;
PImage ripBagI;
PImage tuckaI;
PImage bombfassI;
PImage posionI;
PImage traderI;
PImage crazyTraderI;

PImage commonASRingI;
PImage commonEnergyI;
PImage commonMagUpI;
PImage commonOilI;
PImage commonSharkbiteI;
PImage commonScopeI;
PImage commonCammoShortsI;
PImage commonFoodI;
PImage steakI;
PImage commonSlowI;
PImage commonSilencerI;
PImage commonElefantI;
PImage commonTalonI;
PImage commonTridentI;
PImage commonHeartI;
PImage commonPlugI;
PImage commonManaI;
PImage rareTrippleDaggerI;
PImage rareDaggerI;
PImage rareBloodI;
PImage rareCampI;
PImage campMapI;
PImage rareDartsI;
PImage rarePepperI;
PImage rareRhinoI;
PImage rareSneakerI;
PImage rareDiamondI;
PImage rareMixerI;
PImage rareMagnetI;
PImage rareCrawI;
PImage rareMeatI;
PImage rareSlowI;
PImage rareExtendoI;
PImage rareStealthI;
PImage rareSpeedI;
PImage rareOilI;
PImage rareSilincerI;
PImage rareChestI;
PImage rareNinjaI;
PImage rareWarriorI;
PImage rareRevolverI;
PImage rareTridentI;
PImage rareMagicHatI;
PImage rareBlueCheesI;
PImage rarePunchI;
PImage ravenItemI;
PImage sandclockI;
PImage sniperItemI;
PImage allStatsUpI;
PImage elephantI;
PImage stickyBombI;
PImage superRareMagInAHatI;
PImage superRareGoldDaggerI;
PImage superRareKalashnikovI;
PImage superRarePoisonI;
PImage superRareBiohazardI;
PImage superRareDartsI;
PImage superRareShotgunI;
PImage superRareSneakerI;
PImage superRareSniperI;
PImage superRareTentI;
PImage superRareBloodI;
PImage superRareTridentI;
PImage goldSpinnyGunI;
PImage goldShotgunI;
PImage goldSniperI;
PImage goldAKI;
PImage customDNAI;
PImage customDNA2I;
PImage customNuggetI;
PImage flute1I;
PImage flute2I;
PImage flute3I;
PImage drillI;

PImage classMadManI;
PImage redAttackspeedRageI;
PImage redMassacreI;
PImage redbulletRainI;
PImage magicDieI;
PImage ninjaDashI;
PImage axeThrowI;
PImage axeMapI;
PImage whiteMonsterI;

PImage playerI;
PImage sniperI;
PImage markI;

PImage assasinI; 
PImage blueI;
PImage redOctoI;
PImage rocketI; 
PImage rightHandI; 
PImage guardianI;
PImage teleportEnemyI;
PImage FishHeadI; 
PImage FishBodyI; 
PImage FishTailI; 
PImage KingI;
PImage FlyI;
PImage SpikerI;
PImage BulldozerI;
PImage arrowI;
PImage RatI;
PImage HiveI;
PImage BeeI;
PImage redSpeederI;
PImage ravenBossI;
PImage ravenBulletI;
PImage ravenTentI;
PImage wandererI;
PImage SpawnerBossI;
PImage HoneyPotI;
PImage rangeIndI;
PImage chainRedI;

PImage blueBulletI;
PImage redBulletI;
PImage grayBulletI;
PImage crossBulletI;
PImage streamBulletI;
PImage schrottBulletI;
PImage explosionI;
PImage rocketTechI;
PImage cheeseBulletI;
PImage greenBulletI;
PImage spikeBulletI;
PImage DaggerBulletI;

PImage emperorI;
PImage techpriestI;
PImage vampireI;
PImage hivemasterI;
PImage shrekI;
PImage actualyI;
PImage pocornI;
PImage hellokittyI;
PImage discoI;
PImage skullI;
PImage fishI;
PImage raphiI;
PImage deathI;
PImage baneI;
PImage morpheusI;
PImage clownI;
PImage charlyBrownI;
PImage glitchI;
PImage bombSkinI;

PImage coin1I;
PImage scrap1I;
PImage appleI;
PImage grayCrytalI;
PImage greenCrytalI;

public void loadImg(){
  menuI=loadImage("menu.PNG");
  ingameHudI=loadImage("ingameHud.PNG");
  TabMenuI=loadImage("TabMenu.PNG");
  hitScreenI=loadImage("hitScreen.PNG");
  diamondBuyI=loadImage("diamondBuy.PNG");
  
  emperorI=loadImage("emperor.png");
  techpriestI=loadImage("techpriest.png");
  vampireI=loadImage("vampire.png");
  hivemasterI=loadImage("hivemaster.png");
  shrekI=loadImage("shrek.png");
  actualyI=loadImage("actualy.png");
  pocornI=loadImage("pocorn.png");
  hellokittyI=loadImage("hellokitty.png");
  discoI=loadImage("disco.png");
  skullI=loadImage("skull.png");
  fishI=loadImage("fish.png");
  raphiI=loadImage("raphi.png");
  deathI=loadImage("death.png");
  baneI=loadImage("bane.png");
  morpheusI=loadImage("morpheus.png");
  clownI=loadImage("clown.png");
  charlyBrownI=loadImage("charlyBrown.png");
  glitchI=loadImage("glitch.png");  
  bombSkinI=loadImage("bombSkin.png");  
  
  arrowI=loadImage("arrow.png");
  floorI = loadImage("Floor.png");
  floor1I = loadImage("Floor1.png");
  floor2I = loadImage("Floor2.png");
  floor3I = loadImage("Floor3.png");
  stoneI = loadImage("stone.png");
  treeI = loadImage("tree.png");
  treeRipeI = loadImage("treeRipe.png");
  saplingI = loadImage("sapling.png");
  stone2I = loadImage("stone2.png");
  tree2I = loadImage("tree2.png");
  weirdTreeI = loadImage("weirdTree.png");
  weirdPilzI = loadImage("bigPilz.png");
  weirdGrasI = loadImage("weirdGras.png");
  grasI = loadImage("gras.png");
  pilzI = loadImage("pilz.png");
  spikeI = loadImage("spike.png");
  dungeonI = loadImage("dungeon.png");
  caveClearedI = loadImage("caveCleared.png");
  chestI = loadImage("chest.png");
  chestOpenI = loadImage("chestOpen.png");
  scrapperI = loadImage("scrapper.png");
  shopI = loadImage("shop.png");
  tentI = loadImage("tent.png");
  scrapMillI = loadImage("scrapMill.png");
  cutWheatI = loadImage("cutWheat.png");
  wheatI = loadImage("wheat.png");
  redChestI = loadImage("redChest.png");
  openredChestI = loadImage("openredChest.png");
  vaseI = loadImage("vase.png");
  graveI = loadImage("grave.png");
  graveOpendI = loadImage("graveOpened.png");
  ripBagI= loadImage("RipBag.png");
  tuckaI= loadImage("tucka.png");
  bombfassI= loadImage("bombfass.png");
  posionI= loadImage("posion.png");
  traderI= loadImage("trader.png");
  crazyTraderI= loadImage("crazyTrader.png");
  
  commonASRingI = loadImage("commonASRing.png");
  commonEnergyI = loadImage("commonEnergy.png");
  commonMagUpI = loadImage("commonMagUp.png");
  commonOilI = loadImage("commonOil.png");
  commonSharkbiteI = loadImage("commonSharkbite.png");
  commonScopeI = loadImage("commonScope.png");
  commonCammoShortsI = loadImage("commonCammoShorts.png");
  commonFoodI = loadImage("commonFood.png");
  steakI = loadImage("steak.png");
  commonSlowI = loadImage("slow.png");
  commonSilencerI = loadImage("silencer.png");
  commonElefantI = loadImage("elefant.png");
  commonTalonI = loadImage("talon.png");
  commonTridentI = loadImage("trident2.png");
  commonHeartI = loadImage("commonHeart.png");
  commonPlugI = loadImage("commonPlug.png");
  commonManaI = loadImage("commonManaUp.png");
  rareTrippleDaggerI = loadImage("trippleDagger.png");
  rareDaggerI = loadImage("dagger.png");
  rareBloodI = loadImage("blood.png");
  rareCampI = loadImage("camp.png");
  campMapI = loadImage("campMap.png");
  rareDartsI = loadImage("darts.png");
  rarePepperI = loadImage("pepper.png");
  rareRhinoI = loadImage("rhino.png");
  rareSneakerI = loadImage("sneaker.png");
  rareDiamondI = loadImage("diamond.png");
  rareMixerI = loadImage("mixer.png");
  rareMagnetI = loadImage("magnet.png");
  rareCrawI = loadImage("craw.png");
  rareMeatI = loadImage("meat2.png");
  rareSlowI = loadImage("slow2.png");
  rareExtendoI = loadImage("extendo.png");
  rareStealthI = loadImage("stealth2.png");
  rareSpeedI = loadImage("speed2.png");
  rareOilI = loadImage("reload2.png");
  rareSilincerI = loadImage("silencer2.png");
  rareChestI = loadImage("tresureChest.png");
  rareNinjaI = loadImage("ninja.png");
  rareRevolverI = loadImage("revolver.png");
  rareTridentI = loadImage("trident.png");
  rareMagicHatI = loadImage("magicHat.png");
  rareBlueCheesI = loadImage("blueChees.png");
  rarePunchI = loadImage("punch.png");
  rareWarriorI = loadImage("warrior.png");
  ravenItemI = loadImage("ravenItem.png");
  drillI = loadImage("drill.png");
  stickyBombI = loadImage("stickyBomb.png");
  sandclockI = loadImage("sandclock.png");
  sniperItemI = loadImage("hyperScope.png");
  allStatsUpI = loadImage("allStatsUp.png");
  elephantI = loadImage("Elephant.png");
  superRareMagInAHatI = loadImage("magInAHat.png");
  superRareGoldDaggerI= loadImage("goldDagger.png");
  superRareKalashnikovI = loadImage("Kalashnikov.png");
  superRarePoisonI = loadImage("poison.png");
  superRareBiohazardI = loadImage("biohazard.png");
  superRareDartsI = loadImage("redDarts.png");
  superRareShotgunI = loadImage("redShotgun.png");
  superRareSneakerI = loadImage("redSneaker.png");
  superRareSniperI = loadImage("redSniper.png");
  superRareTentI = loadImage("redTent.png");
  superRareBloodI = loadImage("redBlood.png");
  superRareTridentI = loadImage("trident3.png");
  goldSpinnyGunI = loadImage("spinnyGun.png");
  goldShotgunI = loadImage("goldShotgun.png");
  goldSniperI = loadImage("goldSniper.png");
  goldAKI = loadImage("goldAK.png");
  customDNAI = loadImage("dna.png");
  customDNA2I = loadImage("DNA2.png");
  customNuggetI = loadImage("nugget.png");
  
  classMadManI = loadImage("madMan.png");
  redAttackspeedRageI = loadImage("redAttackspeedRage.png");
  redMassacreI = loadImage("redMassacre.png");
  redbulletRainI = loadImage("redbulletRain.png");
  magicDieI=loadImage("magicDie.png");
  ninjaDashI=loadImage("ninjaDash.png");
  axeThrowI=loadImage("axeThrow.png");
  axeMapI=loadImage("axeMap.png");
  whiteMonsterI=loadImage("whiteMonster.png");
  
  flute1I= loadImage("fishFlute.png");
  flute2I= loadImage("swarmFlute.png");
  flute3I= loadImage("spikeFlute.png");
  
  playerI = loadImage("player.png");
  sniperI = loadImage("Sniper.png");
  markI = loadImage("mark.png");
  
  assasinI = loadImage("Assasin.png");
  blueI = loadImage("blue.png");
  rocketI = loadImage("rocket.png");
  rightHandI = loadImage("RightHand.png");
  guardianI = loadImage("Guardian2.png");
  FishHeadI = loadImage("FishHead.png"); 
  FishBodyI = loadImage("FishBody.png");  
  FishTailI = loadImage("FishTail.png");
  KingI = loadImage("King.png");
  FlyI = loadImage("Fly.png");
  teleportEnemyI = loadImage("teleportEnemy.png");
  BulldozerI = loadImage("bulldozer.png");
  SpikerI = loadImage("spiker.png");
  RatI = loadImage("Rat.png");
  HiveI = loadImage("hiveRaid.png");
  BeeI = loadImage("bee.png");
  redOctoI = loadImage("redOcto.png");
  redSpeederI = loadImage("zombie.png");
  ravenBossI = loadImage("raven.png");
  ravenBulletI= loadImage("ravenBullet.png");
  ravenTentI= loadImage("ravenTent.png");
  wandererI= loadImage("wanderer.png");
  SpawnerBossI= loadImage("SpawnerBoss.png");
  HoneyPotI= loadImage("HoneyPot.png");
  rangeIndI= loadImage("rangeInd.png");
  chainRedI= loadImage("chainRed.png");
  
  blueBulletI = loadImage("blueBullet.png");
  redBulletI = loadImage("redBullet.png");
  grayBulletI = loadImage("grayBullet.png");
  crossBulletI = loadImage("krossBullet.png");
  streamBulletI = loadImage("streamBullet.png");
  schrottBulletI = loadImage("schrottBullet.png");
  explosionI = loadImage("explosion.png");
  rocketTechI = loadImage("rocketTech.png");
  cheeseBulletI = loadImage("cheeseBullet.png");
  greenBulletI = loadImage("greenBullet.png");
  spikeBulletI = loadImage("spikeBullet.png");
  DaggerBulletI = loadImage("DaggerBullet.png");
  
  coin1I = loadImage("coin1.png");
  scrap1I = loadImage("scrap1.png");
  appleI = loadImage("Apple.png");
  grayCrytalI = loadImage("grayCrytal.png");
  greenCrytalI = loadImage("greenCrytal.png");
}

public void loadGameState(){
  table= loadTable("player.csv","header,csv");
  money=table.getInt(1,"value");
  diamonds=table.getInt(2,"value");
  timeChrystal=table.getInt(3,"value");
  greenChrystal=table.getInt(4,"value");
  skinPicked=table.getInt(5,"value");
  highscore=table.getInt(6,"value");
  
  for(int i=0;i<skins.size();i++){
    if(table.getInt(10+i,"value") ==1) skins.get(i).unlocked=true;
    else skins.get(i).unlocked=false;
    
    if(table.getInt(10+i,"posY") ==1) skins.get(i).m.finished=true;
    else skins.get(i).m.finished=false;
    
    skins.get(i).m.score=table.getFloat(10+i,"posX");
  }
}

public void save(){
  table= loadTable("player.csv","header,csv");
  table.getRow(1).setFloat("value",money);
  table.getRow(2).setFloat("value",diamonds);
  table.getRow(3).setFloat("value",timeChrystal);
  table.getRow(4).setFloat("value",greenChrystal);
  table.getRow(5).setFloat("value",skinPicked);
  table.getRow(6).setFloat("value",highscore);
  
  for(int i=0;i<skins.size();i++){
    if(skins.get(i).unlocked) table.getRow(10+i).setInt("value",1);
    else table.getRow(10+i).setInt("value",0);
    
    if(skins.get(i).m.finished) table.getRow(10+i).setInt("posY",1);
    else table.getRow(10+i).setInt("posY",0);
    
    table.getRow(10+i).setFloat("posX",skins.get(i).m.score);
  }
  saveTable(table, "data/player.csv");
}
class Mark{
  
  Mark(){
  }
  public void startGame(){
  }
  public void endGame(){
  }
  public void drawScreen(int i){
    float posX=100+70*(i%14);
    float posY=500+(int)(i/14);
    image(FishHeadI,posX,posY,60,60);
  }
  public void drawIngame(){
  }
}

class FirstBossKill extends Mark{
  
  FirstBossKill(){
  }
  
  public void drawScreen(int i){
    float posX=100+70*(i%14);
    float posY=500+(int)(i/14);
    image(FishHeadI,posX,posY,60,60);
  }
  public void startGame(){
    player.itemsEquipt.add(new Ad(0,0));
    player.itemsEquipt.add(new Ad(0,0));
    player.itemsEquipt.add(new ASItem(0,0));
    player.itemsEquipt.add(new ASItem(0,0));
    spawnRate+=0.35f;
  }
  
  public void endGame(){
    marks.remove(this);
  }
}
public boolean buttonPressed(float posX, float posY, float sizeX, float sizeY) {
  if (keys[12] && firstFrameClick && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

public boolean buttonHoover(float posX, float posY, float sizeX, float sizeY) {
  if (mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

public boolean buttonRightClick(float posX, float posY, float sizeX, float sizeY) {
  if (keys[13] && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

public boolean buttonHold(float posX, float posY, float sizeX, float sizeY) {
  if (mousePressed && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

public void turnImg(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x,y);
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}

public void turnImgD(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x-camX,y-camY);
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}

public void turnFlipImgD(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x-camX,y-camY);
  if(degr>=HALF_PI || degr<=-HALF_PI){
    scale(-1,1);
    degr=PI-degr;
  }
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}
public void ellipseD(float posX, float posY, float size) {
  ellipse(posX-camX, posY-camY, size, size);
}

public void imageD(PImage img,float posX, float posY, float size) {
  image(img, posX-camX, posY-camY, size, size);
}

public void rectD(float posX, float posY, float size) {
  rect(posX-camX, posY-camY, size, size);
}

public void rectD(float posX, float posY, float sizeX,float sizeY) {
  rect(posX-camX, posY-camY, sizeX, sizeY);
}

public void textC(String text,float x,float y, float size, int c){
  textSize(size);
  fill(c);
  text(text,x,y);
}

public void textD(String text,float x,float y, float size, int c){
  textSize(size);
  fill(c);
  text(text,x-camX, y-camY);
}

public float w(){
  return width/1000.0f;
}
public float h(){
  return height/800.0f;
}

public float distance(float x1, float y1, float x2, float y2) {
  float distance=(float)Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
  return distance;
}

public float angle(float x1, float y1, float x2, float y2){
  float angle=0;
  angle=(float)Math.atan2( (y2-y1), (x2-x1));
  /*if(x1>x2)angle=TWO_PI-angle;
  if(x2-x1<=1 && x2-x1>=-1){
    if(y1<y2)angle=HALF_PI;
    if(y1>y2)angle=TWO_PI-HALF_PI;
  }
  */
  return angle;
}

public float getPosX(float r,float size){
  if(r<1200){
      return r;
    }else if(r<2400){
      return r-1200;
    }else if(r<3400){
      return -size;
    }else if(r<4400){
      return 1200+size;
    }
    return 0;
}
public float getPosY(float r,float size){
  if(r<1200){
      return -size;
    }else if(r<2400){
      return 1000+size;
    }else if(r<3400){
      return r-2400;
    }else if(r<4400){
      return r-3400;
    }
    return 0;
}
public float getSpeedX(float r,float speed){
  if(r<1200){
      return random(-speed,speed);
    }else if(r<2400){
      return random(-speed,speed);
    }else if(r<3400){
      return random(speed/4,speed);
    }else if(r<4400){
      return random(-speed/4,-speed);
    }
    return 10;
}
public float getSpeedY(float r,float speed){
  if(r<1200){
      return random(speed/4,speed);
    }else if(r<2400){
      return random(-speed/4,-speed);
    }else if(r<3400){
      return random(-speed,speed);
    }else if(r<4400){
      return random(-speed,speed);
    }
    return 10;
}


public void keyPressed() {
  if (key=='w'||key=='W')keys[0]=true;
  if (key=='a'||key=='A')keys[1]=true;
  if (key=='s'||key=='S')keys[2]=true;
  if (key=='d'||key=='D')keys[3]=true;
  if (key=='q'||key=='Q')keys[4]=true;
  if (key=='e'||key=='E')keys[5]=true;
  if (key==' ')          keys[6]=true;
  if (key=='p'||key=='P')keys[7]=true;
  if (key=='m'||key=='M')keys[8]=true;
  if (key=='r'||key=='R')keys[9]=true;
  if (key==ESC) {
    key=0;
    save();
  }
  if (key==TAB) {
    key=0;
    keys[11]=true;
  }
}

public void mousePressed() {
  if (mouseButton==LEFT)keys[12]=true;
  if (mouseButton==RIGHT)keys[13]=true;
}

public void keyReleased() {
  if (key=='w'||key=='W')keys[0]=false;
  if (key=='a'||key=='A')keys[1]=false;
  if (key=='s'||key=='S')keys[2]=false;
  if (key=='d'||key=='D')keys[3]=false;
  if (key=='q'||key=='Q')keys[4]=false;
  if (key=='e'||key=='E')keys[5]=false;
  if (key==' ')          keys[6]=false;
  if (key=='p'||key=='P')keys[7]=false;
  if (key=='m'||key=='M')keys[8]=false;
  if (key=='r'||key=='R')keys[9]=false;
  if (key==TAB) {
    key=0;
    keys[11]=false;
  }
}

public void mouseReleased() {
  if (mouseButton==LEFT)keys[12]=false;
  if (mouseButton==RIGHT)keys[13]=false;
}
class Skill extends Item{
  float cooldown;
  float cdLeft=120;
  float manaUse;
  PImage img;
  Skill(float posX,float posY){
    super( posX, posY);
  }
  public void draw(float posX){
    if(cdLeft>0)cdLeft--;
    if(cdLeft<0)cdLeft=0;
    image(img,posX,985,75,75);
    noFill();
    rect(posX,985,75,75);
    fill(100,100);
    rect(posX-37.5f+37.5f*cdLeft/cooldown,985,75*cdLeft/cooldown,75);
    bonusDraw();
  }
  public void bonusDraw(){}
  
  public void buff(int wave){}
  
  public void activate(){
    if(player.mana>=manaUse && cdLeft<=0){
      cdLeft=cooldown;
      player.mana-=manaUse;
      baseicActivate();
    }
  }
  public void baseicActivate(){
  }
  
  public void drawMap(){
    drawSkinMap();
    
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<50){
      textC("press Q, Space or right mouse",960,540,20,0);
      if(keys[13]){
        if(player.rightClick!=null){
          player.rightClick.posX=player.posX;
          player.rightClick.posY=player.posY+60;
          items.add(player.rightClick);
        }
        player.rightClick=this;
        items.remove(this);
      }
      if(keys[6]){
        if(player.Space!=null){
          player.Space.posX=player.posX;
          player.Space.posY=player.posY+60;
          items.add(player.Space);
        }
        player.Space=this;
        items.remove(this);
      }
      if(keys[4]){
        if(player.Qskill!=null){
          player.Qskill.posX=player.posX;
          player.Qskill.posY=player.posY+60;
          items.add(player.Qskill);
        }
        player.Qskill=this;
        items.remove(this);
      }
    }
  }
  
  public void drawSkinMap(){
    imageD(img,posX,posY,60);
  }
}

class Rage extends Skill{
  boolean active;
  Rage(float posX, float posY){
    super(posX,posY);
    img=redbulletRainI;
    manaUse=25.0f;
    cooldown=30;
  }
  
  public void activate(){
    if(player.mana>=manaUse && cdLeft<=0 && player.bulletsMag>0){
      cdLeft=cooldown;
      player.mana-=manaUse;
      float angleS=0;
      float bulletsShot=player.bulletsMag*2;
      player.bulletsMag=0;
      for(int i=0;i<bulletsShot;i++){
        angleS+=TWO_PI/bulletsShot;
        PiercingBullet b = new PiercingBullet();
        b.speedX=player.bulletSpeed/2*cos(angleS);
        b.speedY=player.bulletSpeed/2*sin(angleS);
      }
    }
  }
}

class RocketShooter extends Skill{
  RocketShooter(float posX, float posY){
    super(posX,posY);
    img=redMassacreI;
    manaUse=20.0f;
    cooldown=120*3;
  }
  
  public void baseicActivate(){
    Rocket r= new Rocket();
    r.posX+=1*r.speedY;
    r.posY+=-1*r.speedX;
    r= new Rocket();
    r.posX+=-1*r.speedY;
    r.posY+=1*r.speedX;
  }
}

class Dash extends Skill{
  int timer;
  float dashX;
  float dashY;
  float dashSpeed=8;
  
  Dash(float posX, float posY){
    super(posX,posY);
    img=classMadManI;
    manaUse=15.0f;
    cooldown=120*10;
  }
  
  public void bonusDraw(){
    if(timer>=0){
      timer--;
      player.posX+=dashX;
      player.posY+=dashY;
    }
  }
  
  public void baseicActivate(){
    timer=22;
    float dist = distance(player.posX,player.posY,mouseX+camX,mouseY+camY);
    dashX=dashSpeed*(mouseX+camX-player.posX)/dist;
    dashY=dashSpeed*(mouseY+camY-player.posY)/dist;
    player.shield+=player.maxHp;
  }
}

class NinjaDash extends Skill{
  float maxRange=200;
  NinjaDash(float posX, float posY){
    super(posX,posY);
    img=ninjaDashI;
    manaUse=20.0f;
    cooldown=120*12;
  }
  public void bonusDraw(){
    cdLeft-=kills.size()*120;
  }
  
  public void baseicActivate(){
    float angleS=0;
    float bulletsShot=16;
    for(int i=0;i<bulletsShot;i++){
      angleS+=TWO_PI/bulletsShot;
      Bullet b = new Bullet();
      b.speedX=player.bulletSpeed/2*cos(angleS);
      b.speedY=player.bulletSpeed/2*sin(angleS);
    }
    
    float dist = distance(player.posX,player.posY,mouseX+camX,mouseY+camY);
    if(dist<maxRange){
      player.posX=mouseX+camX;
      player.posY=mouseY+camY;
    }else{
      player.posX+=maxRange*(mouseX+camX-player.posX)/dist;
      player.posY+=maxRange*(mouseY+camY-player.posY)/dist;
    }
  }
}

boolean itemDiced;
class ItemDice extends Skill{

  ItemDice(float posX, float posY){
    super(posX,posY);
    img=magicDieI;
    manaUse=80.0f;
    cooldown=120*2*60;
  }
  
  public void baseicActivate(){
    itemDiced=true;
    int greens=0;
    for(int i=0;i<player.itemsGood.size();i++){
      if(player.itemsGood.get(i).rollable){
        greens++;
        player.itemsGood.remove(i);
        i--;
      }
    }
    for(int i=0;i<greens;i++){
      player.itemsGood.add(randomGoodLoot(0,0));
    }
  }
}

class AxeThrow extends Skill{
  AxeThrow(float posX, float posY){
    super(posX,posY);
    img=axeThrowI;
    manaUse=20.0f;
    cooldown=120*20;
  }
  
  public void baseicActivate(){
    AxeBullet r= new AxeBullet(this);
  }
}

class WhiteMonster extends Skill{
  float timer;
  WhiteMonster(float posX, float posY){
    super(posX,posY);
    img=whiteMonsterI;
    manaUse=40.0f;
    cooldown=120*60;
  }
  
  public void buff(int wave){
    if(player.itemsGood.size()>0 && timer>0){
      
      if(wave==0)timer--;
      for(int i=0;i<3;i++){
        if(wave==0){
          player.itemsGood.get(player.itemsGood.size()-1).stats1();
        }
        player.itemsGood.get(player.itemsGood.size()-1).stats2(wave);
      }
    }
  }
  
  public void baseicActivate(){
    timer=6*120;
  }
}
  public void settings() {  fullScreen(P2D);  smooth(8); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "fieldmage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
