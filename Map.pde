float mapSize=100100;


class MapObj implements Comparable<MapObj>{
  
  float posX;
  float posY;
  float size;
  int type;
  color c;
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
  
  int compareTo(MapObj m){
    return (int)(10*(posY+size/2-m.posY-m.size/2)); 
  }
  
  void newDay(){
  }
  
  void draw(){
    skin();
  }
  
  void skin(){
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
  
  Tree(float posX,float posY){
    super();
    size=240;
    this.posX=posX;
    this.posY=posY;
    type=7;
    if(random(100)<25)state=1;
  }
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to harvest",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.hp+=0.05*player.maxHp;
      }
    }
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    if(state==1) image(treeRipeI,0,0,size,size);
    else image(treeI,0,0,size,size);
    popMatrix();
  }
}

class MagicTree extends MapObj{
  MagicTree(){
    super();
    size=240;
    type=5;
    state=1;
  }
  MagicTree(float posX,float posY){
    super();
    size=240;
    this.posX=posX;
    this.posY=posY;
    type=7;
    state=1;
  }
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to harvest",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.hp+=100;
        player.shield+=100;
        player.mana+=100;
        player.bulletsMag=player.magsize;
      }
    }
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    if(state==1) image(magicTreeAppleI,0,0,size,size);
    else image(magicTreeI,0,0,size,size);
    popMatrix();
  }
}


class CursedTree extends MapObj{
  
  CursedTree(){
    type=6;
    size=240;
    state=0;
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(tree2I,0,0,size,size);
    popMatrix();
  }
  
  void newDay(){
    if(random(100)<25) bigMap.remove(this);
  }
}

class Sapling extends MapObj{
  
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
  
  void newDay(){
    if(random(100)<20){
      Tree t=new Tree();
      t.posX=this.posX;
      t.posY=this.posY;
      bigMap.add(t);
      bigMap.remove(this);
    }
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(saplingI,0,0,size,size);
    popMatrix();
  }
}

class TreeStump extends MapObj{
  
  TreeStump(){
    super();
    size=240;
    type=7;
    state=0;
  }
  TreeStump(float posX,float posY){
    super();
    size=240;
    this.posX=posX;
    this.posY=posY;
    type=7;
    state=0;
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(treeStumpI,0,0,size,size);
    popMatrix();
  }
}



class Mushroom extends MapObj{
  Mushroom(){
    super();
    size=150;
    state=1;
  }
  Mushroom(float posX,float posY){
    super();
    size=150;
    this.posX=posX;
    this.posY=posY;
    type=7;
    state=1;
  }
  
  void draw(){
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
  
  void skin(){
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
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to open",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        if(random(1)>greenChest) items.add(randomGoodLoot(posX,posY-50));
        else items.add(randomLoot(posX,posY-50));
      }
    }
  }
  
  void newDay(){
    state=1;
  }
  
  void skin(){
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
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1){
      textD("e to open (-25% hp)",player.posX+60,player.posY+60,24,0);
      if(keys[5] && player.hp>5+player.maxHp*0.25){
        state=0;
        player.hp-=player.maxHp*0.25;
        if(random(1)>greenChest) items.add(randomGoodLoot(posX,posY-50));
        else items.add(randomLoot(posX,posY-50));
      }
    }
  }
  
  void skin(){
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
  
  void draw(){
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
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    image(HiveI,0,0,size*2,size*2);
    popMatrix();
    
    fill(255);
    rectD(posX, posY+size/2+40,size,10);
    fill(#B40000);
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
  
  void draw(){
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
  
  void newDay(){
    state=1;
  }
  
  void skin(){
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
  
  void draw(){
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
  
  void skin(){
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
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2){
      textD("slowed",player.posX+60,player.posY+60,24,0);
      player.hp-=20.0/120.0 * enemyDmg;
      poisonFrame+=20.0/120.0 * enemyDmg;
      player.posX-=player.speedX*0.4;
      player.posY-=player.speedY*0.4;
    }
    
    for(int i=0;i<prey.size();i++){
      if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
        prey.get(i).getPoisond(2.5/120.0 * enemyDmg);
      }
    }
  }
  
  void skin(){
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
  
  void draw(){
    
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
  
  void newDay(){
    state=1;
  }
  
  void skin(){
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
  
  void draw(){

    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2 && state==1 ){
      textD("e to trade",player.posX+60,player.posY+60,24,0);
      if(keys[5]){
        state=0;
        player.itemsGood.add(tradeItem);
      }
    }
  }
  
  void newDay(){
    state=1;
  }
  
  void skin(){
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
  
  void draw(){
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
      int r=2+(int)random(4);
      for(int i=0;i<r;i++){
        pickups.add(new Coin(posX+random(-size/2,size/2),posY+random(-size/2,size/2)));
      }
      Rat rat=new Rat();
      rat.posX=posX;
      rat.posY=posY;
    }
  }
  
  void skin(){
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
  
  void draw(){
    skin();
    if(distance(posX,posY,player.posX,player.posY)<size/2){
      player.hp+=3.0/120;
      player.mana+=3.0/120;
      for(int i=0;i<prey.size();i++){
        if(distance(posX,posY,prey.get(i).posX,prey.get(i).posY) < (size+prey.get(i).size)/2 ){
          prey.get(i).posX-=prey.get(i).speedX*0.5;
          prey.get(i).posY-=prey.get(i).speedY*0.5;
        }
      }
    }
  }
  
  void skin(){
    pushMatrix();
    translate(posX-camX,posY-camY);
    if(flip)scale(-1,1);
    if(distance(posX,posY,player.posX,player.posY)>size/2)tint(200,205);
    image(campMapI,0,0,size*2,size*2);
    noTint();
    popMatrix();
  }
}
