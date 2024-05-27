
int enemy1=1;
int enemy2=0;
int enemy3=0;
int enemy4=0;
float enemyDmg;
float dropChance;

void spawnEnemy(boolean casual){

  spawnRate=0.7+3.0*(float)Math.log(1+ (1/(3.0*5)) *time/120.0/60.0);
  dropChance=0.5/spawnRate ;
  if(chillPhase>0){
    chillPhase--;
    spawnRate=0;
  }
  if(prey.size()>1200 || activeBoss!=null){
    if(0.4+0.03*bossKills<1) spawnRate*=0.4+0.03*bossKills;
  }
  enemyStrength=0.33*(float)Math.pow(1.11,time/120.0/60.0) + 0.056*time/120.0/60.0;
  enemyDmg=0.35*0.33+  0.65*( 0.33*(float)Math.pow(1.11,time/120.0/60.0) + 0.056*time/120.0/60.0 );
  
  int enemyX=0;
  if(random(120)<0.0/4.0*spawnRate){
         new SpeedBuffer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
  }
  for(int i=0;i<=4;i++){
    if(i==0) enemyX=enemy1;
    if(i==1) enemyX=enemy2;
    if(i==2) enemyX=enemy3;
    if(i==3) enemyX=enemy4;

    if(random(120)<8.0/10 || casual){
      float diffSpawn=1+1.5*time/120.0/60.0;
      
      if(diffSpawn>17)diffSpawn=17;
      if(i==0) enemy1=(int)random(diffSpawn);
      if(i==1) enemy2=(int)random(diffSpawn);
      if(i==2) enemy3=(int)random(diffSpawn);
      if(i==3) enemy3=(int)random(diffSpawn);
    }
    
    
    
    if(enemyX==0){
      if(random(120)<1.0/4.0*spawnRate){
         new Crawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==1){
      if(random(120)<0.3/4.0*spawnRate){
         new Shooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==2){
      if(random(120)<0.18/4.0*spawnRate){
         new BigCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==3){
      if(random(120)<0.3/4.0*spawnRate){
         new RedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==4){
      if(random(120)<0.12/4.0*spawnRate){
         new Sprinkler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==5){
      if(random(120)<0.13/4.0*spawnRate){
         new TeleportEnemy();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==6){
      if(random(120)<0.2/4.0*spawnRate){
         new RedSpeedster();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==7){
      if(random(120)<0.07/4.0*spawnRate){
         new Waver();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }

    if(enemyX==8){
      if(random(120)<0.07/4.0*spawnRate){
         new Bulldozer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==9){
      if(random(120)<0.26/4.0*spawnRate){
         new Splitter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    
    if(enemyX==10){
      if(random(120)<2.1/4.0*spawnRate){
         new Rat();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==11){
      if(random(120)<0.14/4.0*spawnRate){
         new BigShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==12){
      if(random(120)<0.14/4.0*spawnRate){
         new BigRedShooter();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==13){
      if(random(120)<0.08/4.0*spawnRate){
         new Healer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==14){
      if(random(120)<0.07/4.0*spawnRate){
         new Sprayer();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    } 
    if(enemyX==15){
      if(random(120)<0.1/4.0*spawnRate){
         new GiantCrawler();//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
      }
    }
    if(enemyX==16){
      if(random(120)<0.14/4.0*spawnRate){
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
    range=900;
    weight=1;
    stopwhenHitting=true;
    this.c=c;
    this.size= size;
    this.maxHp = maxHp*enemyStrength;
    this.xpSaved = xpSaved;
    this.walkSpeed = walkSpeed;
    this.runSpeed = runSpeed*1.0;
    this.hp=this.maxHp;
    this.dmg=dmg*enemyDmg;
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
    poisonedGot=0;
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
    if((hitTimer<=0 || !stopwhenHitting) && stunned<=0 && knockbackTimer<=0)move();
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
      hitTimer=120;
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
  
  void shootSpray(float bulletSize, float bulletSpeed,float spray){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
      float angle = angle(posX,posY,player.posX,player.posY);
      angle+=random(TWO_PI*spray/360) - TWO_PI*spray/360*0.5;
      float bulletSpeedX=bulletSpeed*cos(angle);
      float bulletSpeedY=bulletSpeed*sin(angle);
      new EnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize);
    }
  }
  
  void shootSplit(float bulletSize, float bulletSpeed,float extraFrames){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*60/shotSpeed;
      float distReal = distance(posX,posY,player.posX,player.posY);
      float aimX=player.posX+0.8*player.speedX*distReal/bulletSpeed;
      float aimY=player.posY+0.8*player.speedY*distReal/bulletSpeed;
      float dist = distance(posX,posY,aimX,aimY);
      
      float bulletSpeedX=bulletSpeed*(aimX-posX)/dist;
      float bulletSpeedY=bulletSpeed*(aimY-posY)/dist;
      new SplitEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,bulletSize,dist/bulletSpeed+extraFrames);
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
    float kbmulti=16.0/maxHp;
    
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
    if(hp>=maxHp*0.9){
      dmgGot+=player.firstShotDmg;
      if(player.firstShotStunn>stunned)stunned=(int)player.firstShotStunn;
    }
    hp-=dmgGot;
    if(b.slowDuration > slowed) slowed = (int)b.slowDuration;
    
    if(hp<1)die();
  }
  float poisonedGot;
  void getPoisond(float dmgGot){
    hp-=dmgGot;
    if(hp<1)die();
  }
  
  void die(){
    enemiesKilled++;
    prey.remove(this);
    kills.add(this);
    if(random(100)<droprate*dropChance) items.add(randomLoot(posX,posY-50));
    if(time> 2*120*60 && random(100)<droprate*dropChance*0.27) pickups.add(new GrayCrystal(posX-10,posY));
    if(random(100)<droprate*0.45) pickups.add(new Coin(posX+10,posY-50));
    
    extraDrops();
  }
  void extraDrops(){}
  void cloone(){}
}

ArrayList<Mutation> CrawlerMu = new ArrayList<Mutation>();
class Crawler extends Prey{
  
  Crawler(){
    super(255,50,17,0,0.5,1,20,6.5);//5*1.3
    for(int i=0;i<CrawlerMu.size();i++){
      CrawlerMu.get(i).buff(this);
    }  
  }
  void cloone(){  }
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

ArrayList<Mutation> GiantCrawlerMu = new ArrayList<Mutation>();
class GiantCrawler extends Prey{
  
  GiantCrawler(){
    super(255,110,150,0,0.5,0.7,40,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=4;
    for(int i=0;i<GiantCrawlerMu.size();i++){
      GiantCrawlerMu.get(i).buff(this);
    }
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

ArrayList<Mutation> BigCrawlerMu = new ArrayList<Mutation>();
class BigCrawler extends Prey{
  
  BigCrawler(){
    super(255,75,70,0,0.5,0.8,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    for(int i=0;i<BigCrawlerMu.size();i++){
      BigCrawlerMu.get(i).buff(this);
    }
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

ArrayList<Mutation> ShooterMu = new ArrayList<Mutation>();
class Shooter extends Prey{
 
  Shooter(){
    super(255,55,15,0,0.4,0.7,20,15);
    shotSpeed=26;
    weight=0.7;
    for(int i=0;i<ShooterMu.size();i++){
      ShooterMu.get(i).buff(this);
    }
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

ArrayList<Mutation> BigShooterMu = new ArrayList<Mutation>();
class BigShooter extends Prey{
  
  BigShooter(){
    super(255,75,70,0,0.5,0.8,35,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=46;
    weight=1.4;
    for(int i=0;i<BigShooterMu.size();i++){
      BigShooterMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shoot1(24,2.8);
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

ArrayList<Mutation> SprinklerMu = new ArrayList<Mutation>();
class Sprinkler extends Prey{
  
  Sprinkler(){
    super(255,65,40,0,0.4,0.8,20,40);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=30;
    for(int i=0;i<SprinklerMu.size();i++){
      SprinklerMu.get(i).buff(this);
    }
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

ArrayList<Mutation> WaverMu = new ArrayList<Mutation>();
class Waver extends Prey{
  Waver(){
    super(255,55,70,0,0.4,0.8,30,70);
    shotSpeed=18;
    for(int i=0;i<WaverMu.size();i++){
      WaverMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shoot3(3,18,18,3.2);
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

ArrayList<Mutation> RatMu = new ArrayList<Mutation>();
class Rat extends Prey{
  
  Rat(){
    super(255,40,2,0,0.5,1.3,15,2);
    weight=0.5;
    for(int i=0;i<RatMu.size();i++){
      RatMu.get(i).buff(this);
    }
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

ArrayList<Mutation> BeeMu = new ArrayList<Mutation>();
class Bee extends Prey{
  
  Bee(float posX,float posY){
    super(255,35,25,0,0.5,1.5,20,5);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    this.posX=posX;
    this.posY=posY;
    for(int i=0;i<BeeMu.size();i++){
      BeeMu.get(i).buff(this);
    }
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

ArrayList<Mutation> RedShooterMu = new ArrayList<Mutation>();
class RedShooter extends Prey{
  
  RedShooter(){
    super(255,55,15,0,0.4,0.7,20,15);
    shotSpeed=24;
    weight=0.7;
    for(int i=0;i<RedShooterMu.size();i++){
      RedShooterMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shoot4(15,2.8);
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

ArrayList<Mutation> BigRedShooterMu = new ArrayList<Mutation>();
class BigRedShooter extends Prey{
  
  BigRedShooter(){
    super(255,75,70,0,0.5,0.8,35,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=46;
    weight=1.4;
    for(int i=0;i<BigRedShooterMu.size();i++){
      BigRedShooterMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shoot4(24,2.8);
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

ArrayList<Mutation> RedSpeedsterMu = new ArrayList<Mutation>();
class RedSpeedster extends Prey{
  float orbitalAngle;
  float orbitalRange=300;
  float bulletSize=45;
  RedSpeedster(){
    super(255,70,50,0,0.5,0.7,30,25);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4;
    for(int i=0;i<RedSpeedsterMu.size();i++){
      RedSpeedsterMu.get(i).buff(this);
    }
  }
  void body(){
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
  
  void run(){
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist>orbitalRange) dist*=0.5;
    speedX = runSpeed*(player.posX-posX)/dist;
    speedY = runSpeed*(player.posY-posY)/dist;
  }

  void hit(){
    orbitalAngle+=0.2*TWO_PI/120;
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

ArrayList<Mutation> BulldozerMu = new ArrayList<Mutation>();
class Bulldozer extends Prey{
  boolean powerTimer;
  float chargeX;
  float chargeY;
  Bulldozer(){
    super(255,85,70,0,0.5,1,30,18);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    powerTimer=true;
    chargeX=player.posX+player.speedX*60;
    chargeY=player.posY+player.speedY*60;
    float dist=distance(posX,posY,player.posX,player.posY);
    float extraX = (player.posX-posX)/dist;
    float extraY = (player.posY-posY)/dist;
    new Bulldozer(posX+extraY*size*0.55,posY-extraX*size*0.55,   chargeX+extraY*size*0.55,chargeY-extraX*size*0.55);
    new Bulldozer(posX-extraY*size*0.55,posY+extraX*size*0.55,   chargeX-extraY*size*0.55,chargeY+extraX*size*0.55);
    this.posX-=extraX*1.3;
    this.posY-=extraY*1.3;
    ccRes=12;
    for(int i=0;i<BulldozerMu.size();i++){
      BulldozerMu.get(i).buff(this);
    }
  }
  Bulldozer(float posX,float posY,float cX,float cY){
    super(255,85,70,0,0.5,1,30,23);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=2;
    chargeX=cX;
    chargeY=cY;
    powerTimer=true;
    this.posX=posX;
    this.posY=posY;
    ccRes=12;
    for(int i=0;i<BulldozerMu.size();i++){
      BulldozerMu.get(i).buff(this);
    }
  }
  void body(){
    angle=angle(posX,posY,player.posX,player.posY);
    if(powerTimer) angle=angle(posX,posY,chargeX,chargeY);
    turnFlipImgD(BulldozerI,posX,posY,size*2,size*2,angle);
  }
  
  void move(){
    weight=2;
    if(powerTimer){
      weight=5;
      float dist=distance(posX,posY,chargeX,chargeY);
      if(dist<=runSpeed*4){
        powerTimer=false;
      }
      speedX = 3.2*runSpeed*(chargeX-posX)/dist;
      speedY = 3.2*runSpeed*(chargeY-posY)/dist;
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
    super(255,110,150,0,0.5,0.4,40,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
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
  
  void move(){
    float speedMulti=1;
    if(hp<maxHp*0.8) run();
    else{
      float dist=distance(posX,posY,goalX,goalY);
      speedX = runSpeed*(goalX-posX)/dist;
      speedY = runSpeed*(goalY-posY)/dist;
      if(dist<=100){
        if(random(100)<10) hp*=0.8;
        goalX=-goalX;
        goalY=-goalY;
      }
    }
    posX+=speedX*speedMulti;
    posY+=speedY*speedMulti;
  }
  
  void shoot(){
    if(hp<maxHp && activeBoss==null) shoot2(16,16,2.5);
  }
  
  void extraDrops(){
    item.posX=posX;
    item.posY=posY;
    items.add(item);
  }
  void getPoisond(float dmgGot){
  }
  
  void body(){
    float side=-size*0.54;
    if(speedX<0)side*=-1;

    item.posX=posX+side;
    item.posY=posY-size*0.31;
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

ArrayList<Mutation> HealerMu = new ArrayList<Mutation>();
class Healer extends Prey{
  float range=360;

  Healer(){
    super(255,75,120,0,0.5,0.8,30,60);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.8;
    for(int i=0;i<HealerMu.size();i++){
      HealerMu.get(i).buff(this);
    }
  }
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(KingI,0,0,size*2,size*2);
    image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  
  void extra(){
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < range/2 ){
        if(!(prey.get(i) instanceof Healer) && !(prey.get(i) instanceof Boss))prey.get(i).hp+=5*enemyStrength/120 + 0.5*dmg/120;
        if(prey.get(i).hp>prey.get(i).maxHp)prey.get(i).hp=prey.get(i).maxHp;
      }
    }
  }
}

ArrayList<Mutation> SpeedBufferMu = new ArrayList<Mutation>();
class SpeedBuffer extends Prey{
  float range=550;

  SpeedBuffer(){
    super(255,65,120,0,0.5,0.65,30,35);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=4.0;
    shotSpeed=6;
    nextShot=120*8;
    for(int i=0;i<SpeedBufferMu.size();i++){
      SpeedBufferMu.get(i).buff(this);
    }
  }
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(guardianI,0,0,size*2,size*2);
    if(nextShot<100) image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  void shoot(){
    shootPush(400);
  }
  
  void run(){
    if(nextShot>100){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = runSpeed*(player.posX-posX)/dist;
      speedY = runSpeed*(player.posY-posY)/dist;
    }else{
      speedX=0;
      speedY=0;
    }
  }
  
  void shootPush(float knDist){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0*60/shotSpeed;
      for(int i=0;i<prey.size();i++){
        if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (range+prey.get(i).size)/2 && !(prey.get(i) instanceof Boss) && (prey.get(i)!=this)){
          float dist = distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
          prey.get(i).knockbackX=3.5*(prey.get(i).posX-posX)/dist;
          prey.get(i).knockbackY=3.5*(prey.get(i).posY-posY)/dist;
          prey.get(i).knockbackTimer=knDist/3.5;
          prey.get(i).hp*=0.8;
        }
      }
      
      float angleS=0;
      int shots=16;
      for(int i=0;i<shots;i++){
        angleS+=TWO_PI/shots;
        float bulletSpeedX=3.5*cos(angleS);
        float bulletSpeedY=3.5*sin(angleS);
        new StraightEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,22);
      }
    }
  }
}

ArrayList<Mutation> TeleportEnemyMu = new ArrayList<Mutation>();
class TeleportEnemy extends Prey{
  float range=450;
  int portalTimer;
  float portalX;
  float portalY;

  TeleportEnemy(){
    super(255,65,80,0,0.5,0.7,30,38);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    weight=1.4;
    shotSpeed=5;
    nextShot=120*9;
    removable=false;
    for(int i=0;i<TeleportEnemyMu.size();i++){
      TeleportEnemyMu.get(i).buff(this);
    }
  }
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(c);
    image(teleportEnemyI,0,0,size*2,size*2);
    if(portalTimer>0) image(rangeIndI,0,0,range*2,range*2);
    noTint();
    popMatrix();
  }
  
  void shoot(){
    shootPortal();
  }
  
  void shootPortal(){
    nextShot--;
    if(nextShot<=0){
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=300*(player.posX-posX)/dist;
      float bulletSpeedY=300*(player.posY-posY)/dist;
      portalX=player.posX+bulletSpeedX;
      portalY=player.posY+bulletSpeedY;
      portalTimer=120*2;
      nextShot=portalTimer+120.0*60/shotSpeed;
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

ArrayList<Mutation> SprayerMu = new ArrayList<Mutation>();
class Sprayer extends Prey{
  Sprayer(){
    super(255,110,150,0,0.4,0.7,8,70);
    shotSpeed=200;
    range=650;
    for(int i=0;i<SprayerMu.size();i++){
      SprayerMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shootSpray(14,4.5,60);
  }
  void run(){
    float dist=distance(posX,posY,player.posX,player.posY);
    speedX = runSpeed*(player.posX-posX)/dist;
    speedY = runSpeed*(player.posY-posY)/dist;
    if(dist(posX,posY,player.posX,player.posY)>range*0.9){
      speedX = 1.5*runSpeed*(player.posX-posX)/dist;
      speedY = 1.5*runSpeed*(player.posY-posY)/dist;
    }
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    //tint(#A6F764);
    image(techpriestI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}

ArrayList<Mutation> SplitterMu = new ArrayList<Mutation>();
class Splitter extends Prey{

  Splitter(){
    super(255,55,18,0,0.4,0.7,20,19);
    range=1000;
    shotSpeed=22;
    weight=0.8;
    for(int i=0;i<SplitterMu.size();i++){
      SplitterMu.get(i).buff(this);
    }
  }
  
  void shoot(){
    shootSplit(34,2.6,40);
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    tint(#A6F764);
    image(blueI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}
