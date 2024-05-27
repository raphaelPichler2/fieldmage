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
    posY=player.posY+player.size*0.7;
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
      posY=player.posY+player.size*0.7;
      float angle = angle(posX,posY,camX+mouseX,camY+mouseY);
      angle+=random(TWO_PI*0.12) - TWO_PI*0.12*0.5;
      speedX=player.bulletSpeed*cos(angle);
      speedY=player.bulletSpeed*sin(angle);
      posX=posX + speedX*2;
      posY=posY + speedY*2;
    }
    knockback=0;
    main=false;
    bullets.add(this);
  }
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY) >maxRange)bullets.remove(this);
    move();
    hitPrey();
  }
  
  void move(){
    posX+=speedX;
    posY+=speedY;
  }
  
  void skin(){
    turnImgD(grayBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
  
  void hitPrey(){
    for(int i=0;i<prey.size();i++){    
      if(posX+size+prey.get(i).size>prey.get(i).posX && posX<prey.get(i).posX+size+prey.get(i).size && posY+size+prey.get(i).size>prey.get(i).posY && posY<prey.get(i).posY+size+prey.get(i).size &&
      distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getHit(this);
        trigger();
      }
    }
  }
  
  void trigger(){
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
    speedX*=0.6;
    speedY*=0.6;
  }
  
  void skin(){
    angle+=0.1;
    tint(255, 100);
    turnImgD(explosionI,posX,posY,size*2,size*2,angle);
    tint(255, 255);
  }
}

class DaggerBullet extends Bullet{
  
  DaggerBullet(float angle){
    size=player.bulletSize+6;
    dmg=player.dmg*0.3;
    angle+=random(TWO_PI*0.08) - TWO_PI*0.08*0.5;
    speedX=0.6*player.bulletSpeed*cos(angle);
    speedY=0.6*player.bulletSpeed*sin(angle);
    posX=player.posX + speedX*2;
    posY=player.posY + speedY*2;
  }
  
  void skin(){
    turnImgD(DaggerBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}

class Bubble extends PiercingBullet{
  float speed;
  float curveture;
  float ccurve;
  
  Bubble(){
    maxRange=1000;
    speed=random(1.1)+1.1;
    size=player.bulletSize+random(12);
    curveture= (random(10)-5.0)/120.0;
    ccurve=0;
  }
  
  void move(){
    posX+=speedX*speed;
    posY+=speedY*speed;
    
    posX+=speedY*ccurve*speed;
    posY+=-speedX*ccurve*speed;
    ccurve+=curveture;
    
    speed*=1-(7.0/120.0);
    if(speed<0.04)bullets.remove(this);
  }
  
  void skin(){
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
  
  void skin(){
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
  
  void newEnemy(){
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
  
  void draw(){
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
  void skin(){
    turnImgD(streamBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
}

class BloodSpark extends BoltBullet{
  BloodSpark(float dmg,float dist){
    super(dmg,player.posX,player.posY,dist);
  }
  void skin(){
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
  
  void draw(){
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
  void trigger(){
    bullets.remove(this);
    new Explosion(posX,posY,dmg,explosionRadius);
  }
  void skin(){
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
  
  void draw(){
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

  void skin(){
    turnImgD(spikeBulletI,posX,posY,size*2,size*2,angle(0,0,speedX,speedY));
  }
  
  void trigger(){
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
  
  void draw(){
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
  void trigger(){
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
    timer=2.7*120;
    knockback=0;
  }
  
  void draw(){
    timer--;
    if(timer<=0) bullets.remove(this);
    size+=100.0/120/2.7;
    
    tint(255,5+180*timer/2.7/120);
    imageD(posionI,posX,posY,size*2);
    noTint();
    posX+=speedX;
    posY+=speedY;
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if( distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getPoisond(dmg/120.0);
      }
    }
  }
  void trigger(){
    if(main) player.bulletHitsFrame++;
  }
}

class FFExplosion extends Explosion{
  boolean playerHit;
  FFExplosion(float posX,float posY,float dmg,float explosionRadius){
    super( posX, posY, dmg, explosionRadius);
  }
  void draw(){
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
        player.getDmg(dmg/enemyStrength*0.6 +dmg*0.4);
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
  
  void draw(){
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
  void trigger(){
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
    this.dmg=player.dmg*3.0;
    this.size=55+player.bulletSize;
    knockback=0;
    multi=1;
    if(random(100)<50)multi=-1;
    
    curveTime=(int)(distance(this.posX,this.posY,mouseX+camX,mouseY+camY) /3);
    curve=-multi*curveTime*1.0/120.0;
  }
  
  void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    posX-=speedY*curve;
    posY+=speedX*curve;
    curve+=2/120.0 *multi;
    curveTime--;
    
    if(distance(posX,posY,player.posX,player.posY) >1500)bullets.remove(this);
    
    for(int i=0;i<prey.size();i++){
      if(!hits.contains(prey.get(i)) && distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        hits.add(prey.get(i));
        prey.get(i).getHit(this);
      }
    }
  }
  void trigger(){
    if(main) player.bulletHitsFrame++;
  }
  void skin(){
    turnImgD(ravenBulletI,posX,posY,size*2,size*2,PI+angle(0,0,speedX-speedY*curve,speedY+speedX*curve));
  }
}


class ElephantBullet extends Bullet{
  float angle;
  ElephantBullet(){
    super();
    this.size=15+player.maxHp*0.5;
    dmg=player.maxHp*0.5;
    knockback=50.0*dmg/10.0;
    speedX*=0.5;
    speedY*=0.5;
    maxRange=900;
  }
  
  void hitPrey(){
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
  
   void skin(){
     if(speedX>0) angle+=1.3*TWO_PI/120;
      else angle-=1.3*TWO_PI/120;
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
    dmg=player.dmg*2.5;
    knockback=0;
    speedX*=0.6;
    speedY*=0.6;
    targetX=mouseX+camX;
    targetY=mouseY+camY;
    powerUp=true;
  }
  
  
  void draw(){
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
  
  void trigger(){
    if(main) player.bulletHitsFrame++;
  }
  
  void skin(){
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
    this.dmg=player.dmg*1.0;
    this.size=45;
    knockback=0;
  }
  
  void draw(){
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
  void skin(){
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
    this.speedX=speedX*0.9;
    this.speedY=speedY*0.9;
    this.size=size;
    enemyBullets.add(this);
  }
  
  void draw(){
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
  
  void skin(){
    imageD(blueBulletI,posX,posY,size*4);
  }
}

class RedEnemyBullet extends EnemyBullet{
  RedEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  void skin(){
    imageD(redBulletI,posX,posY,size*4);
  }
}

class CrossEnemyBullet extends EnemyBullet{
  float angle;
  CrossEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  void skin(){
    angle+=0.1;
    turnImgD(crossBulletI,posX,posY,size*4,size*4,angle);
  }
}

class StraightEnemyBullet extends EnemyBullet{
  StraightEnemyBullet(float dmg,float posX,float posY,float speedX,float speedY,float size){
    super( dmg, posX, posY, speedX, speedY, size);
  }
  void skin(){
    turnImgD(streamBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}


class EnemyPoisonBullet extends EnemyBullet{
  float timer;
  EnemyPoisonBullet(float dmg,float posX,float posY,float speedX,float speedY,float size,float timer){
    super( dmg, posX, posY, speedX, speedY, size);
    this.timer=timer;
  }
  void draw(){
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
  
  void explode(){
    bigMap.add(new PoisonField(posX,posY));
    smallMap.add(new PoisonField(posX,posY));
    enemyBullets.remove(this);
  }
  
  void skin(){
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
    speedMulti=1.6;
  }
  void draw(){
    skin();
    speedMulti=0.5+ 1.5* timer/maxTimer;
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
  
  void explode(){
    new EnemyBullet(dmg, posX+speedY*3, posY-3*speedX  ,speedY*1.5,-1.5*speedX,size*0.5);
    new EnemyBullet(dmg, posX-3*speedY, posY+speedX*3,-1.5*speedY,speedX*1.5,size*0.5);
    
    enemyBullets.remove(this);
  }
  
  void skin(){
    turnImgD(greenBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}
