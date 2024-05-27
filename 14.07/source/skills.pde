class Skill{
  float cooldown;
  float cdLeft;
  float manaUse;
  PImage img;
  
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
    }
  }
}

class Rage extends Skill{
  boolean active;
  Rage(){
    img=classMadManI;
    manaUse=20.0;
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
        angleS+=TWO_PI/bulletsShot+random(-0.1,0.1);
        PiercingBullet b = new PiercingBullet();
        b.speedX=player.bulletSpeed/2*cos(angleS);
        b.speedY=player.bulletSpeed/2*sin(angleS);
      }
    }
  }
}
