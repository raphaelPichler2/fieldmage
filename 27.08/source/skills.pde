class Skill extends Item{
  float cooldown;
  float cdLeft=120;
  float manaUse;
  PImage img;
  Skill(float posX,float posY){
    super( posX, posY);
  }
  void draw(float posX){
    if(cdLeft>0)cdLeft--;
    if(cdLeft<0)cdLeft=0;
    image(img,posX,985,75,75);
    noFill();
    rect(posX,985,75,75);
    fill(100,100);
    rect(posX-37.5+37.5*cdLeft/cooldown,985,75*cdLeft/cooldown,75);
    bonusDraw();
  }
  void bonusDraw(){}
  
  void buff(int wave){}
  
  void activate(){
    if(player.mana>=manaUse && cdLeft<=0){
      cdLeft=cooldown;
      player.mana-=manaUse;
      baseicActivate();
    }
  }
  void baseicActivate(){
  }
  
  void drawMap(){
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
  
  void drawSkinMap(){
    imageD(img,posX,posY,60);
  }
}

class Rage extends Skill{
  boolean active;
  Rage(float posX, float posY){
    super(posX,posY);
    img=redbulletRainI;
    manaUse=25.0;
    cooldown=30;
  }
  
  void activate(){
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
    manaUse=20.0;
    cooldown=120*3;
  }
  
  void baseicActivate(){
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
    manaUse=15.0;
    cooldown=120*10;
  }
  
  void bonusDraw(){
    if(timer>=0){
      timer--;
      player.posX+=dashX;
      player.posY+=dashY;
    }
  }
  
  void baseicActivate(){
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
    manaUse=20.0;
    cooldown=120*12;
  }
  void bonusDraw(){
    cdLeft-=kills.size()*120;
  }
  
  void baseicActivate(){
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
    manaUse=80.0;
    cooldown=120*2*60;
  }
  
  void baseicActivate(){
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
    manaUse=20.0;
    cooldown=120*20;
  }
  
  void baseicActivate(){
    AxeBullet r= new AxeBullet(this);
  }
}

class WhiteMonster extends Skill{
  float timer;
  WhiteMonster(float posX, float posY){
    super(posX,posY);
    img=whiteMonsterI;
    manaUse=40.0;
    cooldown=120*60;
  }
  
  void buff(int wave){
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
  
  void baseicActivate(){
    timer=6*120;
  }
}
