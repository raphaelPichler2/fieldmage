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
    this.maxHp=0.9*this.maxHp+0.1*this.maxHp*enemyStrength/0.3;
    hp=this.maxHp;
    //spawnRate*=0.9;
  }
  
  void draw(){
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
    extraDrops();
    activeBoss=null;
    activeAltar = new BossAltar();
    bigMap.add(activeAltar);
    if(bossKills==0) marks.add(new FirstBossKill());
    bossKills+=1;
  }
  
  void drawHP(){
    fill(255);
    rect(1920/2,80,800,40);
    fill(#B40000);
    rect(1920/2+(hp/maxHp-1)*800/2, 80,800*hp/maxHp,40);
    textC(""+(int)hp,1920/2,80,30,0);
  }
  
  void extraDrops(){
    items.add(bossLoot(posX,posY-50));
  }
}

Item bossLoot(float posX,float posY){
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
    super(#6F679D,140,700,0,1,1.2,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=2;
    ccRes=100;
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
    if(phase==0)shoot1(30,3);
    if(phase==1)shoot2(14,20,2.5);
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
    super(#6F679D,140,700,0,1,0.9,40,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=110;
    maxPhase=3;
    ccRes=100;
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
    if(phase==0)shoot3(4,15,15,2.4);//int shots,float spread,float bulletSize, float bulletSpeed
    if(phase==1){ shotSpeed=80;shootRaven();}else shotSpeed=110;
    if(phase==2)shoot4(15,3);
  }
  
  void shootRaven(){
    nextShot--;
    if(nextShot<=0){
      nextShot=120.0*60/shotSpeed*1.2;
      float dist = distance(posX,posY,player.posX,player.posY);
      float bulletSpeedX=2.6*(player.posX-posX)/dist;
      float bulletSpeedY=2.6*(player.posY-posY)/dist;
      new RavenBullet(dmg,posX,posY,bulletSpeedX,bulletSpeedY,25);
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
    
    float speed=2.6;
    curveTime=(int) (((distance(this.posX,this.posY,player.posX,player.posY)+120) / speed)); //6  frames it takes to hit target, half that time curve has to be 0
    if(curveTime<180) curveTime=180;
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
    super(#6F679D,140,700,0,1,1.2,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    body = new Body(this);
    stopwhenHitting=false;
    shotSpeed=100;
    maxPhase=3;
    ccRes=100;
    timeInPhase=120*3;
    phase=0;
    charge=120;
  }
  
  
  void shoot(){
    if(phase==0)shoot1(30,3.3);
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
    items.add(bossLoot(posX,posY-50));
  }
}

class Body extends Prey{
  Fish fish;
  Tail tail;
  
  Body(Fish fish){
    super(#6F679D,140,300+400*enemyStrength,0,1,1.1,50,0);
    this.fish=fish;
    this.posX = fish.posX;
    this.posY = fish.posY;
    this.size=fish.size*0.9;
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
    super(#6F679D,140,700,0,1,1.1,50,0);
    this.body=body;
    this.posX = body.posX;
    this.posY = body.posY;
    this.size=body.size;
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
    super(#6F679D,140,700,0,1,1,50,0);//color c,float size,float maxHp,float xpSaved,float walkSpeed,float runSpeed, float dmg, float droprate
    shotSpeed=120;
    maxPhase=3;
    ccRes=100;
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
        nextShot=120.0*60/shotSpeed*1.5;
        Rat r = new Rat();
        if(hp>r.hp) hp-=r.hp*hp/maxHp;
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
    hp+=dmgGot;
    if(hp>maxHp)hp=maxHp;
  }
  
  /*void extraDrops(){
    if(random(100)<40) items.add(new RavenItem(posX,posY-50));
    else items.add(bossLoot(posX,posY-50));
  }*/
}
