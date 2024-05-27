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
      size=15;
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
    speed=random(1)+1.2;
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
  }
  
  void skin(){
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
  
  void draw(){
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
    explosionRadius=150+player.bulletSize*6;
    maxRange=1000;
    main=true;
    posX+=speedX*2;
    posY+=speedY*2;
  }
  
  void draw(){
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
  void trigger(){
    bullets.remove(this);
    new Explosion(posX,posY,dmg,explosionRadius);
  }
  void skin(){
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
  
  void draw(){
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
    this.dmg=player.dmg*2.5;
    this.size=38+player.bulletSize;
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
