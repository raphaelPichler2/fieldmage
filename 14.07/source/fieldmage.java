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


public void setup(){
  
  
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

public void draw(){
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
    
    float camSpeedX=(player.posX-960-camX+(mouseX-960)*0.2f)/40;//(float)Math.sqrt(Math.pow(player1.posX-980-camX,2)+Math.pow(player1.posY-540-camY,2))/60;
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
      
      
      textC("fps:"+PApplet.parseInt(frameRate),50, 30, 18, 0);
      textC("x:"+PApplet.parseInt(player.posX),50, 50, 18, 0);
      textC("y:"+PApplet.parseInt(player.posY),50, 70, 18, 0);
      textC("timer: "+PApplet.parseInt(time/120/60)+":"+PApplet.parseInt((time/120)%60),50, 90, 18, 0);
    }
    
    if(gameMode==5){
      //playermenu
      if(keys[11] && firstFrameClick && player.kapaUsed<=level*2 && player.redKapaUsed<=(int)(level/5)){ gameMode=1;firstFrameClick=false;}
      player.drawMenu();
    }
    
  if (!keyPressed && !mousePressed)firstFrameClick=true;
  if (keyPressed || mousePressed )firstFrameClick=false;
}

public void createMap(){
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

public void startGame(){
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
      size=15;
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
    turnImgD(explosionI,posX,posY,size*3,size*3,angle);
    tint(255, 255);
  }
}

class Bubble extends Bullet{
  float speed;
  float curveture;
  float ccurve;
  
  Bubble(){
    maxRange=850;
    speed=random(1)+1.2f;
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
  }
  
  public void skin(){
    //angle+=0.01;
    turnImgD(cheeseBulletI,posX,posY,size*2,size*2,angle);
  }
}

class BoltBullet extends Bullet{
  Prey p;

  BoltBullet(float dmg, float posX,float posY,Prey p){
    this.dmg=dmg;
    this.p=p;
    this.posX=posX;
    this.posY=posY;
    this.size=20;
    knockback=0;
    main=false;
  }
  BoltBullet(float dmg, float posX,float posY,float dist){
    this.dmg=dmg;
    if(prey.size()>0){
      Prey nearest=prey.get(0);
      for(int j = 1; j<prey.size();j++){
        if( distance(player.posX,player.posY,prey.get(j).posX,prey.get(j).posY) < distance(player.posX,player.posY,nearest.posX,nearest.posY)){
          nearest=prey.get(j);
        }
      }
      if(distance(player.posX,player.posY,nearest.posX,nearest.posY)<dist){
        this.p=nearest;
      }else{
        bullets.remove(this);
      }
    }else bullets.remove(this);
    this.posX=posX;
    this.posY=posY;
    this.size=20;
    knockback=0;
    main=false;
  }
  
  public void draw(){
    skin();
    if(!prey.contains(p))bullets.remove(this);
    float dist = distance(posX,posY,p.posX,p.posY);
    speedX=6*(p.posX-posX)/dist;
    speedY=6*(p.posY-posY)/dist;
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
    explosionRadius=150+player.bulletSize*6;
    maxRange=1000;
    main=true;
    posX+=speedX*2;
    posY+=speedY*2;
  }
  
  public void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >2500)bullets.remove(this);
    
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
        player.getDmg(dmg);
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

class RavenBulletPlayer extends Bullet{
  int curveTime;
  float curve;
  float multi;
  ArrayList<Prey> hits = new ArrayList<Prey>();
  
  RavenBulletPlayer(){
    float dist = distance(posX,posY,mouseX+camX,mouseY+camY);
    speedX=4*(mouseX+camX-posX)/dist;
    speedY=4*(mouseY+camY-posY)/dist;
    this.dmg=player.dmg*2.5f;
    this.size=38+player.bulletSize;
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
    this.speedX=speedX;
    this.speedY=speedY;
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
      textC("e to harvest",960,540,20,0);
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
  }
  Sapling(float posX,float posY){
    super();
    size=100;
    this.posX=posX;
    this.posY=posY;
    type=7;
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
      textC("e to harvest",960,540,20,0);
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
      textC("e to open",960,540,20,0);
      if(keys[5]){
        state=0;
        items.add(randomLoot(posX,posY-50));
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

class Hive extends MapObj{
  float hp;
  int nextBee;
  ArrayList<Bullet> hits = new ArrayList<Bullet>();
  Hive(){
    super();
    size=120;
    type=19;
    hp=100;
    nextBee=120*4;
    float dist=8000;
    if(random(100)<80)dist*=2;
    float angle=random(TWO_PI);
    posX=dist*cos(angle);
    posY=dist*sin(angle);
  }
  
  public void draw(){
    skin();
    nextBee--;
    if(nextBee<=0){
      nextBee=120*4;
      new Bee(posX,posY);
    }
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
    rectD(posX+(hp/100-1)*size/2, posY+size/2+40,size*hp/100,10);
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
      textC("e to open",960,540,20,0);
      if(keys[5]){
        state=0;
        int r=(int)random(4);
        if(r==0) new RavenBoss();
        if(r==1) new CaveDweller();
        if(r==2) new Fish();
        if(r==3) new RatKing();
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
      textC("slowed",960,540,20,0);
      player.hp-=10.0f/120.0f * enemyStrength;
      player.posX-=player.speedX*0.4f;
      player.posY-=player.speedY*0.4f;
    }
    
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getPoisond(1.0f/120.0f * enemyStrength);
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
  
  float spawnX;
  float spawnY;
  
  Skill rightClick;
  Skill Space = new Rage();
  Skill Qskill;

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
    /*for(int i=0;i<10;i++){
    items.add(new RavenItem(posX,posY-50));
    items.add(new Pepper(posX,posY-50));
    items.add(new BlueCheese(posX,posY-50));
    items.add(new ShieldRhino(posX,posY-50));
    items.add(new Warrior(posX,posY-50));
    items.add(new ShieldRhino(posX,posY-50));
    items.add(new MagicMag(posX,posY-50));
    items.add(new Firststrike(posX,posY-50));
    items.add(new Monk(posX,posY-50));
    }*/
    
    
  }

  public void draw(){
    
    if(hp<=0)die();
    baseStats();
    for(int i=0;i<itemsEquipt.size();i++){
      itemsEquipt.get(i).stats1();
    }
    for(int i=0;i<itemsEquipt.size();i++){
      itemsEquipt.get(i).stats2();
    }
    for(int i=0;i<itemsGood.size();i++){
      itemsGood.get(i).stats1();
    }
    
    if(rightClick!=null){
      rightClick.buff();
    }
    if(Space!=null){
      Space.buff();
    }
    if(Qskill!=null){
      Qskill.buff();
    }
    
    for(int i=0;i<itemsGood.size();i++){
      itemsGood.get(i).stats2();
    }
    
    if(hp<maxHp)hp+=regen;
    if(hp>maxHp)hp=maxHp;
    
    if(mana<maxMana)mana+=manaRegen;
    if(mana>maxMana)mana=maxMana;
    
    drawBody();
    
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
    if(activeBoss==null)turnImgD(arrowI,posX,posY,100,100,HALF_PI+angle(posX, posY,activeAltar.posX,activeAltar.posY));
    else turnImgD(arrowI,posX,posY,100,100,HALF_PI+angle(posX, posY,activeBoss.posX,activeBoss.posY));
    
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
  public void drawBody(){
    imageD(playerI,posX,posY,size*2);
    float weaponAngle=angle(posX,posY,camX+mouseX,camY+mouseY);
    turnFlipImgD(sniperI,posX,posY+size*0.7f,size*2,size*2,weaponAngle);
  }
  
  public void getDmg(float dmg){
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
    for(int i = 0; i<marks.size();i++){
      marks.get(i).endGame();
    }
  }
  
  public void drawHud(){
    fill(255);
    rect(400,1000,600,40);
    noFill();
    rect(50,985,90,75);
    textC(""+(int)level,50,985,30,0);
   
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
  }
  
  
  int kapaUsed;
  int redKapaUsed;
  
  public void drawMenu(){
    background(200);
    textAlign(LEFT, CENTER);
    textC("Dmg: ",150,30,20,0); textC(""+dmg,300,30,20,0);
    textC("Mag: ",150,50,20,0); textC(""+magsize,300,50,20,0);
    textC("Attackspeed: ",150,70,20,0); textC(""+attackSpeed,300,70,20,0);
    textC("Speed: ",150,90,20,0); textC(""+speed,300,90,20,0);
    textC("Reloadspeed: ",150,110,20,0); textC(""+reloadSpeed,300,110,20,0);
    textC("Regen: ",150,130,20,0); textC(""+(120*regen),300,130,20,0); 
    textC("Mana: ",150,150,20,0); textC(""+(120*manaRegen),300,150,20,0);
    textAlign(CENTER, CENTER);
    
    for(int i=0;i<itemsGood.size();i++){
      float posXM=120+80*(i%20);
      float posYM=300+80*(int)(i/20);
      itemsGood.get(i).drawSkinMenu(posXM,posYM);
    }
  }
  
  public float levelMulti(){
    return (float)Math.pow(1.06f,level);
  }
  
  public void baseStats(){
    attackSpeed=2;
    if(bulletsMag>magsize) bulletsMag=magsize;
    magsize=6;
    bulletSize=12;
    reloadSpeed=0.5f;
    dmg=10;
    bulletSpeed = 12;
    speed=1.5f;
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
    knockback=50;
    primeSkillUp=1;
    if(shield>0){
      shield*=1-(0.5f/120) *100/maxHp;
      shield-=4.0f/120.0f *100/maxHp;
    }else shield=0;
    extraStats();
  }
  public void extraStats(){}
}
float seaMonsterSpawn=0;
float swarmSpawn=0;
float spikeSpawn=0;

int enemy1=1;
int enemy2=0;
int enemy3=0;
int enemy4=0;

public void spawnEnemy(boolean casual){
  
  spawnRate=0.7f+1.04f*(float)Math.log(1+0.55f*time/120.0f/60.0f);
  enemyStrength=0.3f*(float)Math.pow(1.1f,time/120.0f/60.0f) + 0.08f*time/120.0f/60.0f;
  
  int enemyX=0;
  for(int i=0;i<=4;i++){
    if(i==0) enemyX=enemy1;
    if(i==1) enemyX=enemy2;
    if(i==2) enemyX=enemy3;
    if(i==3) enemyX=enemy4;

    if(random(120)<1.35f/10 || casual){
      float diffSpawn=enemyStrength*12.0f/1.8f -2.0f;
      if(diffSpawn>10)diffSpawn=10;
      if(i==0) enemy1=(int)random(diffSpawn);
      if(i==1) enemy2=(int)random(diffSpawn);
      if(i==2) enemy3=(int)random(diffSpawn);
      if(i==3) enemy4=(int)random(diffSpawn);
    }
    
    if(enemyX==0){
      if(random(120)<1.0f/4*spawnRate){
         new Crawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==1){
      if(random(120)<0.3f/4*spawnRate){
         new Shooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==2){
      if(random(120)<0.18f/4*spawnRate){
         new BigCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==3){
      if(random(120)<0.2f/4*spawnRate){
         new RedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==4){
      if(random(120)<1.5f/4*spawnRate){
         new Rat();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==5){
      if(random(120)<0.12f/4*spawnRate){
         new Sprinkler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==6){
      if(random(120)<0.2f/4*spawnRate){
         new RedSpeedster();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==7){
      if(random(120)<0.07f/4*spawnRate){
         new Waver();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==8){
      if(random(120)<0.1f/4*spawnRate){
         new GiantCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==9){
      if(random(120)<0.07f/4*spawnRate){
         new Bulldozer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
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
    range=840;
    weight=1;
    stopwhenHitting=true;
    this.c=c;
    this.size= size;
    this.maxHp = maxHp*enemyStrength;
    this.xpSaved = xpSaved;
    this.walkSpeed = walkSpeed;
    this.runSpeed = runSpeed;
    this.hp=this.maxHp;
    this.dmg=dmg*enemyStrength;
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
    if((hitTimer<=0 || !stopwhenHitting) && stunned<=0 )move();
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
      hitTimer=60;
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
    
    if(b.knockback-ccRes>0){
      float kbX=b.speedX;
      float kbY=b.speedY;
      knockbackX=(b.knockback-ccRes)*kbX/(float)Math.sqrt(Math.pow(b.speedX,2)+Math.pow(b.speedY,2))/12;
      knockbackY=(b.knockback-ccRes)*kbY/(float)Math.sqrt(Math.pow(b.speedX,2)+Math.pow(b.speedY,2))/12;
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
  public void getPoisond(float dmgGot){
    hp-=dmgGot;
    if(hp<1)die();
  }
  
  public void die(){
    prey.remove(this);
    kills.add(this);
    if(random(100)<droprate/spawnRate*0.8f)items.add(randomLoot(posX,posY-50));
    extraDrops();
  }
  public void extraDrops(){}
}


class Crawler extends Prey{
  
  Crawler(){
    super(255,50,17,0,0.5f,1,20,9);
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
    super(255,110,150,0,0.5f,0.7f,30,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    ccRes=30;
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
    super(255,75,70,0,0.5f,0.8f,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    ccRes=15;
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

class Sprinkler extends Prey{
  
  
  Sprinkler(){
    super(255,65,40,0,0.4f,0.8f,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
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
    super(255,55,70,0,0.4f,0.8f,40,35);
    shotSpeed=18;
  }
  
  public void shoot(){
    shoot3(3,15,18,3.5f);
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
    super(255,40,5,0,0.5f,1.3f,20,2);
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
    super(255,35,25,0,0.5f,1.5f,20,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
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
    shoot4(15,2.5f);
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
  float chargeX;
  float chargeY;
  RedSpeedster(){
    super(255,60,50,0,0.5f,0.9f,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4f;
  }
  public void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(redSpeederI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  public void extra(){
      float dist=distance(posX,posY,player.posX,player.posY);
      chargeX = chargeX*0.99f+runSpeed*(player.posX-posX)/dist*0.01f;
      chargeY = chargeY*0.99f+runSpeed*(player.posY-posY)/dist*0.01f;
      posX+=chargeX;
      posY+=chargeY;
  }
}

class Bulldozer extends Prey{
  int powerTimer;
  Bulldozer(){
    super(255,85,70,0,0.5f,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=120*10;
    float dist=distance(posX,posY,player.posX,player.posY);
    float extraX = (player.posX-posX)/dist;
    float extraY = (player.posY-posY)/dist;
    new Bulldozer(posX+extraY*size/2,posY-extraX*size/2);
    new Bulldozer(posX-extraY*size/2,posY+extraX*size/2);
    this.posX-=extraX*1.3f;
    this.posY-=extraY*1.3f;
    
  }
  Bulldozer(float posX,float posY){
    super(255,85,70,0,0.5f,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=120*10;
    this.posX=posX;
    this.posY=posY;
  }
  public void body(){
    angle=angle(posX,posY,player.posX,player.posY);
    turnFlipImgD(BulldozerI,posX,posY,size*2,size*2,angle);
  }
  
  public void extra(){
     runSpeed=1;
     weight=2;
     if(powerTimer>=0){
       powerTimer--;
       runSpeed=1.8f;
       weight=5;
     }
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
    this.maxHp=0.9f*this.maxHp+0.1f*this.maxHp*enemyStrength/0.3f;
    hp=this.maxHp;
    //spawnRate*=0.9;
  }
  
  public void draw(){
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
    extraDrops();
    activeBoss=null;
    activeAltar = new BossAltar();
    bigMap.add(activeAltar);
    if(bossKills==0) marks.add(new FirstBossKill());
    bossKills+=1;
  }
  
  public void drawHP(){
    fill(255);
    rect(1920/2,80,800,40);
    fill(0xffB40000);
    rect(1920/2+(hp/maxHp-1)*800/2, 80,800*hp/maxHp,40);
    textC(""+(int)hp,1920/2,80,30,0);
  }
  
  public void extraDrops(){
    items.add(bossLoot(posX,posY-50));
  }
}

public Item bossLoot(float posX,float posY){
  ArrayList<Item> dropPool = new ArrayList<Item>();
  
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

  return dropPool.get( (int)random(dropPool.size()) ) ;
}



class CaveDweller extends Boss{
  
  CaveDweller(){
    super(0xff6F679D,140,700,0,1,1.2f,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=2;
    ccRes=100;
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
    if(phase==0)shoot1(30,3);
    if(phase==1)shoot2(14,20,2.5f);
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
    super(0xff6F679D,140,700,0,1,0.9f,40,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=110;
    maxPhase=3;
    ccRes=100;
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
    if(phase==0)shoot3(4,15,15,2.4f);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){ shotSpeed=80;shootRaven();}else shotSpeed=110;
    if(phase==2)shoot4(15,3);
  }
  
  public void shootRaven(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0f*60/shotSpeed*1.2f;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.6f*(player.posX-posX)/dist;
      float bulletSpeedY=2.6f*(player.posY-posY)/dist;
      new RavenBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,25);
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
    
    float speed=2.6f;
    curveTime=(int) (((distance(this.posX,this.posY,player.posX,player.posY)+120) / speed)); //6  frames it takes to hit target, half that time curve has to be 0
    if(curveTime<180) curveTime=180;
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
    super(0xff6F679D,140,700,0,1,1.2f,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    body = new Body(this);
    stopwhenHitting=false;
    shotSpeed=100;
    maxPhase=3;
    ccRes=100;
    timeInPhase=120*3;
    phase=0;
    charge=120;
  }
  
  
  public void shoot(){
    if(phase==0)shoot1(30,3.3f);
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
        charge=120;
        phaseCounter=timeInPhase;
        if(random(100)<70)phase=2;
      }
      speedX = 4*runSpeed*(chargeX-posX)/dist;
      speedY = 4*runSpeed*(chargeY-posY)/dist;
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
    items.add(bossLoot(posX,posY-50));
  }
}

class Body extends Prey{
  Fish fish;
  Tail tail;
  
  Body(Fish fish){
    super(0xff6F679D,140,300+400*enemyStrength,0,1,1.1f,50,0);
    this.fish=fish;
    this.posX = fish.posX;
    this.posY = fish.posY;
    this.size=fish.size*0.9f;
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
    super(0xff6F679D,140,700,0,1,1.1f,50,0);
    this.body=body;
    this.posX = body.posX;
    this.posY = body.posY;
    this.size=body.size;
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
    super(0xff6F679D,140,700,0,1,1,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=3;
    ccRes=100;
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
        nextShot=120.0f*60/shotSpeed*1.5f;
        Rat r = new Rat();
        if(hp>r.hp) hp-=r.hp*hp/maxHp;
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
    hp+=dmgGot;
    if(hp>maxHp)hp=maxHp;
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
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
  public void stats2(){ 
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
    fill(255);
    rect(960,540,800,400);
    textC(name,960,390,30,0);
    textSize(16);
    text(description,960,590,780,330);
  }
  
  public void drawMenu(){
  }
  
  public void reset(){
    stacks=0;
  }
}

class GoodItem extends Item{
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
    }
  }
  
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
  textC(""+number,posX+12,posY+12,18,c);
  boxesDrawn++;
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
    description = "+1 bulletsize\n"+
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
    nextRaven=120;
    description = "shoots 1 raven /s\n"+//6
    "every 5 seconds\n"+//6
    "ravens deal 150% dmg/s\n"+//6
    "";
  }

  public void stats2(){
    nextRaven--;
    if(nextRaven<=0){
      nextRaven=120*5;
      RavenBulletPlayer b = new RavenBulletPlayer();
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

  public void stats1(){
    if(player.shotFrame){
      SchrottBullet peprrr = new SchrottBullet(10);
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
    description = "+1 regen/s\n"+//6
    "when you are full life\n"+//6
    "shoot a wave of 10 bullets\n"+//6
    "but lose 10% hp\n"+//6
    " ";
  }
  
  public void stats1(){
    player.regen+=1.0f/120;
    if(player.hp>=player.maxHp){
      player.hp-=player.maxHp*0.1f;
      float angleS=0;
      for(int i=0;i<10;i++){
        angleS+=TWO_PI/10+random(-0.1f,0.1f);
        CheesBullet b=new CheesBullet();
        b.speedX=player.bulletSpeed/5*cos(angleS);
        b.speedY=player.bulletSpeed/5*sin(angleS);
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
    "if under 60%hp gain:/s\n"+//6
    "+2 attackspeed\n"+//6
    "+0.1 speed\n"+//6
    //6
    "";
  }
  
  public void stats1(){
    player.maxHp+=20;
    if(player.hp<=player.maxHp*0.6f){
      player.attackSpeed+=2;
      player.speed+=0.1f;
      stacks=10;
    }else stacks=0;
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
    "\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    tridentHit=false;
  }
  
  public void stats2(){
    if(player.shotFrame && !tridentHit && random(100)<50){
      float speedSave;
      Bullet b = new Bullet();speedSave=b.speedX; b.speedX=b.speedY; b.speedY=-speedSave; 
      b = new Bullet();speedSave=b.speedX; b.speedX=-b.speedY; b.speedY=speedSave; 
      b = new Bullet(); b.speedX=-b.speedX; b.speedY=-b.speedY;
      tridentHit=true;
    }
  }
  
  public void drawBoxes(){
  }
  
  public void drawSkinMap(){
    imageD(rareTridentI,posX,posY,60);
  }
  public void drawSkinMenu(float posMX, float posMY){
    image(rareTridentI,posMX,posMY,60,60);
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
    if(kills.size()>0){
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
  int timer;
  
  ShieldRhino(float posX,float posY){
    super(posX,posY);
    name="Rhinos horn";
    description = "killing an enemy gives/s\n"+//6
    "+9 shield\n"+//6
    "while shielded gain +3 dmg\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    if(kills.size()>0){
      player.shield+=9;
    }
    if(player.shield >0){
      player.dmg+=3;
    }
  }
  
  public void drawBoxes(){
    if(player.shield>0){
      itemEffectsBoxes(rareRhinoI,3,255);
    }
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
  
  public void stats2(){
    if(stacks>0 && player.shotFrame){
      new Rocket();
    }
    
    if(player.bulletsMag==player.magsize){
      stacks=1;
    }else stacks=0;
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
    description = "not shooting gives up to 12 stacks/s\n"+//6
    "shooting releases the stacks\n"+//6
    "dealing big dmg\n"+//6
    "\n"+//6
    "";
  }
  
  public void stats1(){
    if(player.nextShot<=0 || player.bulletsMag==0)timer--;
    if(timer<0){
      timer=60;
      if(stacks<12) stacks+=1;
    }
    if(player.shotFrame){
      timer=60;
      if(stacks>0){
        new Bubble();
        new Bubble();
        stacks--;
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
    player.maxHp+=20;
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

PImage commonASRingI;
PImage commonEnergyI;
PImage commonMagUpI;
PImage commonOilI;
PImage commonSharkbiteI;
PImage commonScopeI;
PImage commonCammoShortsI;
PImage commonFoodI;
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
PImage sniperItemI;
PImage allStatsUpI;
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

PImage classMadManI;
PImage redAttackspeedRageI;
PImage redMassacreI;
PImage redbulletRainI;

PImage playerI;
PImage sniperI;
PImage markI;

PImage assasinI; 
PImage blueI;
PImage redOctoI;
PImage rocketI; 
PImage rightHandI; 
PImage guardianI;
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

PImage emperorI;
PImage techpriestI;
PImage vampireI;
PImage hivemasterI;

PImage coin1I;
PImage scrap1I;
PImage appleI;

public void loadImg(){
  
  emperorI=loadImage("emperor.png");
  techpriestI=loadImage("techpriest.png");
  vampireI=loadImage("vampire.png");
  hivemasterI=loadImage("hivemaster.png");
  
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
  
  commonASRingI = loadImage("commonASRing.png");
  commonEnergyI = loadImage("commonEnergy.png");
  commonMagUpI = loadImage("commonMagUp.png");
  commonOilI = loadImage("commonOil.png");
  commonSharkbiteI = loadImage("commonSharkbite.png");
  commonScopeI = loadImage("commonScope.png");
  commonCammoShortsI = loadImage("commonCammoShorts.png");
  commonFoodI = loadImage("commonFood.png");
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
  sniperItemI = loadImage("hyperScope.png");
  allStatsUpI = loadImage("allStatsUp.png");
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
  
  coin1I = loadImage("coin1.png");
  scrap1I = loadImage("scrap1.png");
  appleI = loadImage("Apple.png");
}

public void loadGameState(){
  table= loadTable("player.csv","header,csv");
  money=table.getInt(2,"value");
}

public void save(){
  table= loadTable("player.csv","header,csv");
  saveTable(table, "data/player.csv");
  
  table= loadTable("map.csv","header,csv");
  saveTable(table, "data/map.csv");
  
  table= loadTable("items.csv","header,csv");
  saveTable(table, "data/items.csv");
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
    if(player!=null){
      save();
    }
    exit();
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
class Skill{
  float cooldown;
  float cdLeft;
  float manaUse;
  PImage img;
  
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
  
  public void buff(){}
  
  public void activate(){
    if(player.mana>=manaUse && cdLeft<=0){
      cdLeft=cooldown;
      player.mana-=manaUse;
    }
  }
}

class Rage extends Skill{
  boolean active;
  Rage(){
    img=classMadManI;
    manaUse=20.0f;
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
        angleS+=TWO_PI/bulletsShot+random(-0.1f,0.1f);
        PiercingBullet b = new PiercingBullet();
        b.speedX=player.bulletSpeed/2*cos(angleS);
        b.speedY=player.bulletSpeed/2*sin(angleS);
      }
    }
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
