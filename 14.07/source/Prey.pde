float seaMonsterSpawn=0;
float swarmSpawn=0;
float spikeSpawn=0;

int enemy1=1;
int enemy2=0;
int enemy3=0;
int enemy4=0;

void spawnEnemy(boolean casual){
  
  spawnRate=0.7+1.04*(float)Math.log(1+0.55*time/120.0/60.0);
  enemyStrength=0.3*(float)Math.pow(1.1,time/120.0/60.0) + 0.08*time/120.0/60.0;
  
  int enemyX=0;
  for(int i=0;i<=4;i++){
    if(i==0) enemyX=enemy1;
    if(i==1) enemyX=enemy2;
    if(i==2) enemyX=enemy3;
    if(i==3) enemyX=enemy4;

    if(random(120)<1.35/10 || casual){
      float diffSpawn=enemyStrength*12.0/1.8 -2.0;
      if(diffSpawn>10)diffSpawn=10;
      if(i==0) enemy1=(int)random(diffSpawn);
      if(i==1) enemy2=(int)random(diffSpawn);
      if(i==2) enemy3=(int)random(diffSpawn);
      if(i==3) enemy4=(int)random(diffSpawn);
    }
    
    if(enemyX==0){
      if(random(120)<1.0/4*spawnRate){
         new Crawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==1){
      if(random(120)<0.3/4*spawnRate){
         new Shooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==2){
      if(random(120)<0.18/4*spawnRate){
         new BigCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==3){
      if(random(120)<0.2/4*spawnRate){
         new RedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==4){
      if(random(120)<1.5/4*spawnRate){
         new Rat();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==5){
      if(random(120)<0.12/4*spawnRate){
         new Sprinkler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==6){
      if(random(120)<0.2/4*spawnRate){
         new RedSpeedster();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==7){
      if(random(120)<0.07/4*spawnRate){
         new Waver();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==8){
      if(random(120)<0.1/4*spawnRate){
         new GiantCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==9){
      if(random(120)<0.07/4*spawnRate){
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
  color c;
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
  
  Prey(color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate){
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
  
  void reposition(){
    float dist=1130+size/2;
    angle=random(TWO_PI);
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
  }

  void draw(){
    if(distance(posX,posY,player.posX,player.posY)>2000 && removable)prey.remove(this);
    if(hits>=3 && player.markDmg>0){
      turnImgD(markI,posX,posY,size*2,size*2,markAngle);
      markAngle-=0.01;
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
  
  void pushEnemies(){
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
  
  void hit(){
    if(hitTimer<=0 && distance(posX,posY,player.posX,player.posY) < (size+player.size)/2){
      player.getDmg(dmg);
      hitTimer=60;
    }
  }
  
  void shoot(){}
  
  void shoot1(float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=bulletSpeed*(player.posX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(player.posY-posY)/dist;
      new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  void shoot2(int shots,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
      float angleS=0;
      for(int i=0;i<shots;i++){
        angleS+=TWO_PI/shots;
        float bulletSpeedX=bulletSpeed*cos(angleS);
        float bulletSpeedY=bulletSpeed*sin(angleS);
        new StraightEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
      }
    }
  }
  
  void shoot3(int shots,float spread,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
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
  
  void shoot4(float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+player.speedX*distReal/bulletSpeed;
      float aimY=player.posY+player.speedY*distReal/bulletSpeed;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=bulletSpeed*(aimX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(aimY-posY)/dist;
      new RedEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  void body(){
    /*pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(assasinI,0,0,size*2,size*2);
    popMatrix();*/
  }
  
  void hpBar(){
    fill(255);
    rectD(posX, posY+size/2+10,size,10);
    fill(#B40000);
    rectD(posX+(hp/maxHp-1)*size/2, posY+size/2+10,size*hp/maxHp,10);
  }
  
  void move(){
    float speedMulti=1;
    run();
    if(slowed>0){
      slowed--;
      speedMulti*=0.75;
    }
    posX+=speedX*speedMulti;
    posY+=speedY*speedMulti;
  }
  
  void strayve(){
    float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
      if(dist<600){
        float saveX=speedX;
        speedX=-speedY;
        speedY=saveX;
    }
  }
  
  void run(){
    float dist=distance(posX,posY,player.posX,player.posY);
    speedX = runSpeed*(player.posX-posX)/dist;
    speedY = runSpeed*(player.posY-posY)/dist;
  }
  
  void extra(){}
  
  void getHit(Bullet b){
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
    if(hp>=maxHp*0.9){
      dmgGot+=player.firstShotDmg;
      if(player.firstShotStunn>stunned)stunned=(int)player.firstShotStunn;
    }
    hp-=dmgGot;
    if(b.slowDuration > slowed) slowed = (int)b.slowDuration;
    
    if(hp<1)die();
  }
  void getPoisond(float dmgGot){
    hp-=dmgGot;
    if(hp<1)die();
  }
  
  void die(){
    prey.remove(this);
    kills.add(this);
    if(random(100)<droprate/spawnRate*0.8)items.add(randomLoot(posX,posY-50));
    extraDrops();
  }
  void extraDrops(){}
}


class Crawler extends Prey{
  
  Crawler(){
    super(255,50,17,0,0.5,1,20,9);
  }
  void body(){
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
    super(255,110,150,0,0.5,0.7,30,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    ccRes=30;
    weight=4;
  }
  void body(){
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
    super(255,75,70,0,0.5,0.8,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    ccRes=15;
    weight=2;
  }
  void body(){
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
    super(255,55,15,0,0.4,0.7,20,15);
    shotSpeed=26;
    weight=0.7;
  }
  
  void shoot(){
    shoot1(15,2.8);
  }
  
  void body(){
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
    super(255,65,40,0,0.4,0.8,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=30;
  }
  
  void shoot(){
    shoot2(6,15,2.5);
  }
  
  void body(){
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
    super(255,55,70,0,0.4,0.8,40,35);
    shotSpeed=18;
  }
  
  void shoot(){
    shoot3(3,15,18,3.5);
  }
  
  void body(){
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
    super(255,40,5,0,0.5,1.3,20,2);
    weight=0.5;
  }
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(RatI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  void getPoisond(float dmgGot){
    hp+=dmgGot;
    if(hp>maxHp)hp=maxHp;
  }
}

class Bee extends Prey{
  
  Bee(float posX,float posY){
    super(255,35,25,0,0.5,1.5,20,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    this.posX=posX;
    this.posY=posY;
  }
  void body(){
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
    super(255,55,15,0,0.4,0.7,20,15);
    shotSpeed=24;
    weight=0.7;
  }
  
  void shoot(){
    shoot4(15,2.5);
  }
  
  void body(){
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
    super(255,60,50,0,0.5,0.9,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4;
  }
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(redSpeederI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  void extra(){
      float dist=distance(posX,posY,player.posX,player.posY);
      chargeX = chargeX*0.99+runSpeed*(player.posX-posX)/dist*0.01;
      chargeY = chargeY*0.99+runSpeed*(player.posY-posY)/dist*0.01;
      posX+=chargeX;
      posY+=chargeY;
  }
}

class Bulldozer extends Prey{
  int powerTimer;
  Bulldozer(){
    super(255,85,70,0,0.5,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=120*10;
    float dist=distance(posX,posY,player.posX,player.posY);
    float extraX = (player.posX-posX)/dist;
    float extraY = (player.posY-posY)/dist;
    new Bulldozer(posX+extraY*size/2,posY-extraX*size/2);
    new Bulldozer(posX-extraY*size/2,posY+extraX*size/2);
    this.posX-=extraX*1.3;
    this.posY-=extraY*1.3;
    
  }
  Bulldozer(float posX,float posY){
    super(255,85,70,0,0.5,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=120*10;
    this.posX=posX;
    this.posY=posY;
  }
  void body(){
    angle=angle(posX,posY,player.posX,player.posY);
    turnFlipImgD(BulldozerI,posX,posY,size*2,size*2,angle);
  }
  
  void extra(){
     runSpeed=1;
     weight=2;
     if(powerTimer>=0){
       powerTimer--;
       runSpeed=1.8;
       weight=5;
     }
  }
}
