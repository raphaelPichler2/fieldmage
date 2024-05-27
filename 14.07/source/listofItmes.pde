class Ad extends Item{
  
  Ad(float posX,float posY){
    super(posX,posY);
    tier=1;
    name="Tooth";
    description = "+1 damage\n"+//6
    "\n"+ //6
    "";
  }
  
  void stats1(){
    player.dmg +=1;
  }
  
  void drawSkinMap(){
    imageD(commonSharkbiteI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonSharkbiteI,posX,posY,60,60);
  }
}

class ASItem extends Item{
  
  ASItem(float posX,float posY){
    super(posX,posY);
    code=1;
    name="Triggerfinger Ring";
    description = "+0.3 attackspeed\n"+
    "";
  }
  
  void stats1(){
    player.attackSpeed +=0.3;
  }
  
  void drawSkinMap(){
    imageD(commonASRingI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonASRingI,posX,posY,60,60);
  }
}


class Mag extends Item{
  
  Mag(float posX,float posY){
    super(posX,posY);
    tier=1;
    name="Bullet";
    description = "+1 magsize\n"+//6
    "\n"+ //6
    "";
  }
  
  void stats1(){
    player.magsize +=1;
  }
  
  void drawSkinMap(){
    imageD(commonMagUpI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonMagUpI,posX,posY,60,60);
  }
}

class ReloadItem extends Item{
  
  ReloadItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=5;
    name="Reload Oil";
    description = "+0.08 reloadspeed\n"+//1 
    "";
  }
  
  void stats1(){
    player.reloadSpeed +=0.08;
  }
  
  void drawSkinMap(){
    imageD(commonOilI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonOilI,posX,posY,60,60);
  }
}

class Elephant extends Item{
  
  Elephant(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=25;
    name="Elephant Foot";
    description = "+1 bulletsize\n"+
    "+10 hp\n"+
    " ";
  }
  
  void stats1(){
    player.maxHp+=10;
    player.bulletSize +=2;
  }
  
  void drawSkinMap(){
    imageD(commonElefantI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonElefantI,posX,posY,60,60);
  }
}
class SpeedItem extends Item{
  
  SpeedItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=4;
    name="Energy Drink";
    description = "+0,04 speed\n"+
    "";
  }
  
  void stats1(){
    player.speed +=0.04;
  }
  
  void drawSkinMap(){
    imageD(commonEnergyI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonEnergyI,posX,posY,60,60);
  }
}

class HeartItem extends Item{
  
  HeartItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=60;
    tier=1;
    name="Pigheart";
    description = "+0.2 regen/s\n"+
    "";
  }
  
  void stats1(){
    player.regen+=0.2/120;
  }
  
  void drawSkinMap(){
    imageD(commonHeartI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonHeartI,posX,posY,60,60);
  }
}

class ManaItem extends Item{
  
  ManaItem(float posX,float posY){
    super(posX,posY);
    kapa=1;
    code=61;
    tier=1;
    name="Manapotion";
    description = "+0.2 mana/s\n"+
    " ";
  }
  
  void stats1(){
    player.manaRegen+=0.2/120;
  }
  
  void drawSkinMap(){
    imageD(commonManaI,posX,posY,50);
  }
  void drawSkinMenu(){
    image(commonManaI,posX,posY,60,60);
  }
}




//bossitems
///////////

class RavenItem extends GoodItem{
  float nextRaven;
  
  RavenItem(float posX,float posY){
    super(posX,posY);
    name="Ravenshot";
    nextRaven=120;
    description = "shoots 1 raven /s\n"+//6
    "every 5 seconds\n"+//6
    "ravens deal 150% dmg/s\n"+//6
    "";
  }

  void stats2(){
    nextRaven--;
    if(nextRaven<=0){
      nextRaven=120*5;
      RavenBulletPlayer b = new RavenBulletPlayer();
    } 
  }
  
  void drawSkinMap(){
    imageD(ravenItemI,posX,posY,70);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(ravenItemI,posMX,posMY,60,60);
  }
}


class Pepper extends GoodItem{
  
  Pepper(float posX,float posY){
    super(posX,posY);
    name="Pepper";
    description = "shoots 1 pepperbullet /s\n"+//6
    "deals 10 dmg\n"+//6
    "/s\n"+//6
    "";
  }

  void stats1(){
    if(player.shotFrame){
      SchrottBullet peprrr = new SchrottBullet(10);
    }
  }
  
  void drawSkinMap(){
    imageD(rarePepperI,posX,posY,70);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rarePepperI,posMX,posMY,60,60);
  }
}

class BlueCheese extends GoodItem{
  
  BlueCheese(float posX,float posY){
    super(posX,posY);
    name="Bluecheese";
    description = "+1 regen/s\n"+//6
    "when you are full life\n"+//6
    "shoot a wave of 10 bullets\n"+//6
    "but lose 10% hp\n"+//6
    " ";
  }
  
  void stats1(){
    player.regen+=1.0/120;
    if(player.hp>=player.maxHp){
      player.hp-=player.maxHp*0.1;
      float angleS=0;
      for(int i=0;i<10;i++){
        angleS+=TWO_PI/10+random(-0.1,0.1);
        CheesBullet b=new CheesBullet();
        b.speedX=player.bulletSpeed/5*cos(angleS);
        b.speedY=player.bulletSpeed/5*sin(angleS);
      }
    }
  }
  
  void drawSkinMap(){
    imageD(rareBlueCheesI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareBlueCheesI,posMX,posMY,60,60);
  }
}

class Warrior extends GoodItem{
  
  Warrior(float posX,float posY){
    super(posX,posY);
    name="Warrior";
    description = "+20 hp\n"+
    "if under 60%hp gain:/s\n"+//6
    "+2 attackspeed\n"+//6
    "+0.1 speed\n"+//6
    //6
    "";
  }
  
  void stats1(){
    player.maxHp+=20;
    if(player.hp<=player.maxHp*0.6){
      player.attackSpeed+=2;
      player.speed+=0.1;
      stacks=10;
    }else stacks=0;
  }
  
  void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(rareWarriorI,stacks,255);
    }
  }
  
  void drawSkinMap(){
    imageD(rareWarriorI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareWarriorI,posMX,posMY,60,60);
  }
}

boolean tridentHit=false;
class Trident extends GoodItem{
  
  Trident(float posX,float posY){
    super(posX,posY);
    name="Trident";
    description = "50% chance to\n"+//6
    "shoot bullets in 4 direction/s\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    tridentHit=false;
  }
  
  void stats2(){
    if(player.shotFrame && !tridentHit && random(100)<50){
      float speedSave;
      Bullet b = new Bullet();speedSave=b.speedX; b.speedX=b.speedY; b.speedY=-speedSave; 
      b = new Bullet();speedSave=b.speedX; b.speedX=-b.speedY; b.speedY=speedSave; 
      b = new Bullet(); b.speedX=-b.speedX; b.speedY=-b.speedY;
      tridentHit=true;
    }
  }
  
  void drawBoxes(){
  }
  
  void drawSkinMap(){
    imageD(rareTridentI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareTridentI,posMX,posMY,60,60);
  }
}


class MagicMag extends GoodItem{
  int timer;
  
  MagicMag(float posX,float posY){
    super(posX,posY);
    name="Warrior";
    description = "killing an enemy gives:/s\n"+//6
    "+1 bullet in the mag\n"+//6
    "+5 dmg for 0.5 sec\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    if(kills.size()>0){
      player.bulletsMag+=1;
      timer = 60;
    }
    if(timer>=0){
      timer--;
      player.dmg+=5;
    }
  }
  
  void drawBoxes(){
    if(timer>0){
      itemEffectsBoxes(superRareMagInAHatI,5,255);
    }
  }
  
  void drawSkinMap(){
    imageD(superRareMagInAHatI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(superRareMagInAHatI,posMX,posMY,60,60);
  }
}

class ShieldRhino extends GoodItem{
  int timer;
  
  ShieldRhino(float posX,float posY){
    super(posX,posY);
    name="Rhinos horn";
    description = "killing an enemy gives/s\n"+//6
    "+9 shield\n"+//6
    "while shielded gain +3 dmg\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    if(kills.size()>0){
      player.shield+=9;
    }
    if(player.shield >0){
      player.dmg+=3;
    }
  }
  
  void drawBoxes(){
    if(player.shield>0){
      itemEffectsBoxes(rareRhinoI,3,255);
    }
  }
  
  void drawSkinMap(){
    imageD(rareRhinoI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareRhinoI,posMX,posMY,60,60);
  }
}

class Firststrike extends GoodItem{
  
  Firststrike(float posX,float posY){
    super(posX,posY);
    name="Firststrike";
    description = "first shot in your mag explodes/s\n"+//6
    "100% explosion dmg\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(){
    if(stacks>0 && player.shotFrame){
      new Rocket();
    }
    
    if(player.bulletsMag==player.magsize){
      stacks=1;
    }else stacks=0;
  }
  
  void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(commonScopeI,1,255);
    }
  }
  
  void drawSkinMap(){
    imageD(commonScopeI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(commonScopeI,posMX,posMY,60,60);
  }
}

class Monk extends GoodItem{
  int timer;
  
  Monk(float posX,float posY){
    super(posX,posY);
    name="Monk";
    description = "not shooting gives up to 12 stacks/s\n"+//6
    "shooting releases the stacks\n"+//6
    "dealing big dmg\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    if(player.nextShot<=0 || player.bulletsMag==0)timer--;
    if(timer<0){
      timer=60;
      if(stacks<12) stacks+=1;
    }
    if(player.shotFrame){
      timer=60;
      if(stacks>0){
        new Bubble();
        new Bubble();
        stacks--;
      }
    }
  }
  
  void drawBoxes(){
    if(stacks>0){
      itemEffectsBoxes(rareNinjaI,stacks,255);
    }
  }
  
  void drawSkinMap(){
    imageD(rareNinjaI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareNinjaI,posMX,posMY,60,60);
  }
}

class Marioshroom extends GoodItem{
  
  Marioshroom(float posX,float posY){
    super(posX,posY);
    name="Big Shroom";
    description = "all stats up\n"+//6
    "\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    player.maxHp+=20;
    player.dmg+=1;
    player.magsize+=1;
    player.attackSpeed+=0.3;
    player.reloadSpeed+=0.08;
    player.speed+=0.04;
    player.regen+=0.2/120;
    player.manaRegen+=0.2/120;
  }
  
  void drawSkinMap(){
    imageD(allStatsUpI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(allStatsUpI,posMX,posMY,60,60);
  }
}
