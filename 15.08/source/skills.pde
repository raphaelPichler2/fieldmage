class Skill extends Item{
  float cooldown;
  float cdLeft=60;
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
  
  void buff(){}
  
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
          player.rightClick.posY=player.posY;
          items.add(player.rightClick);
        }
        player.rightClick=this;
        items.remove(this);
      }
      if(keys[6]){
        if(player.Space!=null){
          player.Space.posX=player.posX;
          player.Space.posY=player.posY;
          items.add(player.Space);
        }
        player.Space=this;
        items.remove(this);
      }
      if(keys[4]){
        if(player.Qskill!=null){
          player.Qskill.posX=player.posX;
          player.Qskill.posY=player.posY;
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
  Rage(){
    super(0,0);
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
    manaUse=15.0;
    cooldown=30;
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
    manaUse=10.0;
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
