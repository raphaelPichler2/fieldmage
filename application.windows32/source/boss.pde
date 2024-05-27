class Boss extends Prey{
  
  int phase=0;
  int maxPhase;
  int timeInPhase=0;
  int phaseCounter;
  
  Boss(color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate){
    super(c,size,maxHp,xpSaved,walkSpeed,runSpeed,dmg,droprate);
    range=1100;
    weight=10;
    activeBoss=this;
    float dist=1130+size/2;
    angle=angle(0,0,player.posX,player.posY);
    posX=player.posX+dist*cos(angle);
    posY=player.posY+dist*sin(angle);
    this.maxHp=this.maxHp*1.1*(float)Math.pow(1.1,bossKills);//0.9*this.maxHp+0.1*this.maxHp*enemyStrength/0.3;//this.maxHp*0.5+his.maxHp*0.5/enemyStrength*0.3*Math.pow(1.35,bossesKilled)
    hp=this.maxHp;
    ccRes=20;
    //spawnRate*=0.9;
  }
  
  void draw(){
    poisonedGot=0;
    phaseCounter--;
    if(phaseCounter<=0){
      phase=(phase+(int)random(1,maxPhase))%maxPhase;
      phaseCounter=timeInPhase;
    }
    if(hits>=3 && player.markDmg>0){
      turnImgD(markI,posX,posY,size*2,size*2,markAngle);
      markAngle-=0.01;
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
  void die(){
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
    int r=1+(int)random(4);
      for(int i=0;i<r;i++){
        pickups.add(new Coin(posX+random(-size/2,size/2),posY+random(-size/2,size/2)));
      }
  }
  
  void drawHP(){
    fill(255);
    rect(1920/2,80,800,40);
    fill(#B40000);
    rect(1920/2+(hp/maxHp-1)*800/2, 80,800*hp/maxHp,40);
    textC(""+(int)hp,1920/2,80,30,0);
  }
}



class CaveDweller extends Boss{
  
  CaveDweller(){
    super(#6F679D,140,700,0,1,1.2,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=65;
    maxPhase=2;
    timeInPhase=120*5;
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(rightHandI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  void shoot(){
    if(phase==0){shootSplit(46,2.2,15);}
    if(phase==1){shoot2(16,20,2.5);}
  }
  
  void run(){
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
    super(#6F679D,140,700,0,1,0.9,36,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=100;
    maxPhase=3;
    wavesSpawned=0;
    timeInPhase=120*5;
    phase=1;
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(ravenBossI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  void shoot(){
    if(phase==0)shoot3(4,20,15,2.1);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){ shotSpeed=70;shootRaven();}else shotSpeed=100;
    if(phase==2)shoot4(15,2.6);
  }
  
  void shootRaven(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0*60/shotSpeed*1.2;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.4*(player.posX-posX)/dist;
      float bulletSpeedY=2.4*(player.posY-posY)/dist;
      new RavenBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,30);
    }
  }
  
  void run(){
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
    
    float speed=2.4;
    curveTime=(int) (((distance(this.posX,this.posY,player.posX,player.posY)+120) / speed)); //6  frames it takes to hit target, half that time curve has to be 0
    if(curveTime<240) curveTime=240;
    curve=-multi*curveTime*speed/120.0/2.0;
    if(curveTime>300){
      noCurve=true;
      this.speedX*=2.5;this.speedY*=2.5;
      curve=0;
    }
  }
  
  void draw(){
    skin();
    posX+=speedX;
    posY+=speedY;
    if(!noCurve){
      posX-=speedY*curve;
      posY+=speedX*curve;
      curve+= 2.5/120.0*multi;
    }
    
    float dist = distance(posX,posY,player.posX,player.posY);
    if(dist > 2500)enemyBullets.remove(this);
    
    if( dist < (size+player.size)/2 ){
        player.getDmg(dmg);
        enemyBullets.remove(this);
    }
  }
  
  void skin(){
    turnImgD(ravenBulletI,posX,posY,size*4,size*4,PI+angle(0,0,speedX-speedY*curve,speedY+speedX*curve));
  }
  
}


class Fish extends Boss{
  Body body;
  float chargeX;
  float chargeY;
  float charge;
  
  Fish(){
    super(#6F679D,140,800,0,1,1.2,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    body = new Body(this);
    stopwhenHitting=false;
    shotSpeed=150;
    maxPhase=3;
    timeInPhase=120*3;
    phase=0;
    charge=120;
  }
  
  
  void shoot(){
    if(phase==0)shootSpray(25,3.2,50);
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
  void pushEnemies(){}
  
  void run(){
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
      speedX = 3.6*runSpeed*(chargeX-posX)/dist;
      speedY = 3.6*runSpeed*(chargeY-posY)/dist;
      if(hitTimer>1)hitTimer++;
    }
  }
  void body(){
    body.bodybefore();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishHeadI,0,0,size*2,size*2);
    popMatrix();
  }
  
  void extraDrops(){
    body.die();
  }
}

class Body extends Prey{
  Fish fish;
  Tail tail;
  
  Body(Fish fish){
    super(#6F679D,140,300+400*enemyStrength,0,1,1.1,45,0);
    this.fish=fish;
    this.posX = fish.posX;
    this.posY = fish.posY;
    this.size=fish.size*0.9;
    stopwhenHitting=false;
    runSpeed=fish.runSpeed;
    tail = new Tail(this);
    boolean removable=false;
  }
    
  void bodybefore(){
    tail.bodybefore();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishBodyI,0,0,size*2,size*2);
    popMatrix();
  }
  
  void hpBar(){
  }
  
  void move(){
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
  
  void getHit(Bullet b){
    fish.getHit(b);
  }
  void pushEnemies(){}
  
  void die(){
    prey.remove(this);
    tail.die();
  }
}

class Tail extends Prey{
  Body body;
    
  Tail(Body body){
    super(#6F679D,140,700,0,1,1.1,45,0);
    this.body=body;
    this.posX = body.posX;
    this.posY = body.posY;
    this.size=body.size;
    stopwhenHitting=false;
    boolean removable=false;
  }
  
  void bodybefore(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FishTailI,0,0,size*2,size*2);
    popMatrix();
  }
  void hpBar(){
  }
  
  void move(){
    runSpeed=body.runSpeed;
    float dist=distance(posX,posY,body.posX,body.posY);
    if(dist>size){
      speedX = (body.posX-posX)/10/dist *(dist-size) *runSpeed;
      speedY = (body.posY-posY)/10/dist *(dist-size) *runSpeed;
      posX+=speedX;
      posY+=speedY;
    }
  }
  void pushEnemies(){}
  
  void getHit(Bullet b){
    body.getHit(b);
  }
  
  void die(){
    prey.remove(this);
  }
}

class RatKing extends Boss{
  int wavesSpawned;
  
  RatKing(){
    super(#6F679D,140,700,0,1,1,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=3;
    wavesSpawned=0;
    timeInPhase=120*4;
    phase=1;
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(FlyI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
  
  void shoot(){
    if(phase==0){
      nextShot--;
      if(nextShot<=0){
        nextShot=120.0*60/shotSpeed*50/dmg;
        Rat r = new Rat();
        if(hp>10) hp-=2;
        float angle=random(TWO_PI);
        float dist = size/2+20;
        r.posX=posX+dist*cos(angle);
        r.posY=posY+dist*sin(angle);
        
      }
      
    }//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){
      poisonShot(3,15,25,2.5);
    }
    if(phase==2)shoot4(27,2.2);
  }
  
  void poisonShot(int shots,float spread,float bulletSize, float bulletSpeed){
    nextShot--;
    if(nextShot<=0 && distance(posX,posY,player.posX,player.posY)<range){
      nextShot=120.0*2* 120/shotSpeed;
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
  
  void run(){
    if(phase==0){
      speedX=0;speedY=0;
    }
    if(phase==1){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX = 0.6*runSpeed*(player.posX-posX)/dist;
      speedY = 0.6*runSpeed*(player.posY-posY)/dist;
    }
    if(phase==2){
      float dist=distance(posX,posY,player.posX,player.posY);
      speedX =runSpeed*(player.posX-posX)/dist;
      speedY =runSpeed*(player.posY-posY)/dist;
    }
  }
  
  void getPoisond(float dmgGot){
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}

class HiveBoss extends Boss{
  
  HiveBoss(){
    super(#6F679D,140,700,0,1,0.9,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=50;
    maxPhase=3;
    timeInPhase=120*6;
    phase=0;
  }
  
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX>0) scale(-1,1);
    image(SpawnerBossI,0,0,size*2,size*2);
    image(rangeIndI,0,0,360*2,360*2);
    noTint();
    popMatrix();
  }
  
  void shoot(){
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < 360/2 ){
        if(!(prey.get(i) instanceof HiveBoss) ) prey.get(i).hp+=2*enemyStrength/120 + dmg/120/8;
        if(prey.get(i).hp>prey.get(i).maxHp) prey.get(i).hp=prey.get(i).maxHp;
      }
    }
    
    if(phase==0)shoot3(4,18,15,2.1);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){shootBlob();}
    if(phase==2){
      if(phaseCounter==120){
        for(int i=0;i<prey.size();i++){
          if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < 480/2 && !(prey.get(i) instanceof HiveBoss)){
            float dist = distance(posX,posY,prey.get(i).posX,prey.get(i).posY);
            prey.get(i).knockbackX=3.5*(prey.get(i).posX-posX)/dist;
            prey.get(i).knockbackY=3.5*(prey.get(i).posY-posY)/dist;
            prey.get(i).knockbackTimer=120;
            prey.get(i).hp*=0.5;
          }
        }
        
        float angleS=0;
        int shots=20;
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          float bulletSpeedX=3.5*cos(angleS);
          float bulletSpeedY=3.5*sin(angleS);
          new StraightEnemyBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,22);
        }
      }
    }
  }
  
  void shootBlob(){
    nextShot--;
    if(nextShot<=0){
      phase=0;
      nextShot=120.0*60/shotSpeed;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.6*(player.posX-posX)/dist;
      float bulletSpeedY=2.6*(player.posY-posY)/dist;
      new BlobBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,65);
    }
  }
  
  void run(){
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
    this.timer=(200+distance(posX,posY,player.posX,player.posY))/2.6;
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
        e.hp*=0.5;
        e.posX=posX+dist*cos(angle);
        e.posY=posY+dist*sin(angle);
      }
    }
    enemyBullets.remove(this);
  }
  
  void skin(){
    turnImgD(greenBulletI,posX,posY,size*3,size*3,angle(0,0,speedX,speedY));
  }
}


class BeeBoss extends Boss{ 
  HoneyPot honey;
  
  BeeBoss(){
    super(#6F679D,140,700,0,1,0.9,45,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=60;
    maxPhase=4;
    timeInPhase=120*4;
    phase=0;
  }
  
  void body(){
    if(honey!=null) honey.draw();
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(speedX<0) scale(-1,1);
    image(BeeI,0,0,size*2,size*2);
    noFill();
    noTint();
    popMatrix();
  }
  
  void shoot(){
    
    if(phase==0);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){shootBee();}
    if(phase==2){
      if(phaseCounter%120==100){ 
        int shots=20;
        float angleS=angle(posX,posY,player.posX,player.posY);
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          float bulletSpeedX=2.7*cos(angleS);
          float bulletSpeedY=2.7*sin(angleS);
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
       hp+=maxHp*0.05+10;
       if(hp>maxHp)hp=maxHp;
    }
  }
  
  void shootBee(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0*60/shotSpeed;
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
  
  void run(){
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
  
  void honeyExplode(){
    hp-=maxHp*0.06+20;
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
  
  void draw(){
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
  
  void pushEnemies(){
    
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
  void body(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    image(HoneyPotI,0,0,size*2,size*2);
    noFill();
    noTint();
    popMatrix();
  }
}
