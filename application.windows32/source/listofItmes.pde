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
    description = "+2 bulletsize\n"+
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
    nextRaven=random(120*5);
    description = "shoots 1 raven /s\n"+//6
    "every 4 seconds\n"+//6
    "ravens deal 300% dmg/s\n"+//6
    "";
  }

  void stats2(int itemWave){
    if(itemWave==60){
      nextRaven--;
      if(nextRaven<=0){
        nextRaven=120*4;
        RavenBulletPlayer b = new RavenBulletPlayer();
      }
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

  void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame){
        SchrottBullet peprrr = new SchrottBullet(10);
      }
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
    description = "+0.6 regen/s\n"+//6
    "when you are full life\n"+//6
    "shoot a wave of 10 bullets\n"+//6
    "but lose 10 hp\n"+//6
    " ";
  }
   void stats1(){
     player.regen+=0.6/120;
   }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.hp>=player.maxHp){
        player.hp-=10;
        float angleS=0;
        for(int i=0;i<10;i++){
          angleS+=TWO_PI/10;
          CheesBullet b=new CheesBullet();
          b.speedX=player.bulletSpeed/5*cos(angleS);
          b.speedY=player.bulletSpeed/5*sin(angleS);
        }
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
    "for each 20 missing hp gain:/s\n"+//6
    "+0.3 attackspeed\n"+//6
    "+1 dmg\n"+//6
    //6
    "";
  }
  
  void stats1(){
    player.maxHp+=20;
  }
  void stats2(int itemWave){
    if(itemWave==30){
      float missingHp=(int)( (player.maxHp-player.hp)/20) ;
        player.attackSpeed+=0.3*missingHp;
        player.dmg+=1*missingHp;
        stacks=(int)missingHp;
    }
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
    "+4 bulletsize\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    tridentHit=false;
    player.bulletSize+=4;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame && !tridentHit && random(100)<50){
        float speedSave;
        Bullet b = new Bullet();speedSave=b.speedX; b.speedX=b.speedY; b.speedY=-speedSave; 
        b = new Bullet();speedSave=b.speedX; b.speedX=-b.speedY; b.speedY=speedSave; 
        b = new Bullet(); b.speedX=-b.speedX; b.speedY=-b.speedY;
        tridentHit=true;
      }
    }
  }
  
  void drawBoxes(){
  }
  
  void drawSkinMap(){
    imageD(commonTridentI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(commonTridentI,posMX,posMY,60,60);
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
    if(kills.size()>0 && player.bulletsMag>0){
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
  int stacks;
  
  ShieldRhino(float posX,float posY){
    super(posX,posY);
    name="Rhinos horn";
    description = "killing 20 enemy gives/s\n"+//6
    "+60 shield\n"+//6
    "while shielded gain +3 dmg\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    stacks+=kills.size();
    if(stacks>=20){
      stacks=0;
      player.shield+=60;
    }
    if(player.shield >0){
      player.dmg+=3.0;
    }
  }
  
  void drawBoxes(){
    itemEffectsBoxes(rareRhinoI,stacks,255);
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
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(stacks>0 && player.shotFrame){
        new Rocket();
      }
      
    
      if(player.bulletsMag==player.magsize){
        stacks=1;
      }else stacks=0;
    }
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
    description = "not shooting gives up to 10 stacks/s\n"+//6
    "shooting releases the stacks\n"+//6
    "dealing big dmg\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.nextShot<=0 || player.bulletsMag==0)timer--;
      if(timer<0){
        timer=90;
        if(stacks<10) stacks+=1;
      }
      if(player.shotFrame){
        if(stacks>0){
          new Bubble();
          new Bubble();
          stacks--;
        }
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
    player.maxHp+=10;
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

class BigElephant extends GoodItem{
  int timer;
  BigElephant(float posX,float posY){
    super(posX,posY);
    name="Elephant";
    description = "+50 hp\n"+//6
    "every 12 sec shoot\n"+//6
    "a powerfull shot\n"+//6
    "(50% maxHp dmg)\n"+//6
    "";
    timer=(int)random(120*15);
  }
  
  void stats1(){
    player.maxHp+=50;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      if(timer<=0){
        timer=120*12;
        new ElephantBullet();
      }
    }
  }
  
  void drawBoxes(){
    itemEffectsBoxes(elephantI,(int)(timer/120),255);
  }
  
  void drawSkinMap(){
    imageD(elephantI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(elephantI,posMX,posMY,60,60);
  }
}

class Plug extends GoodItem{
  Plug(float posX,float posY){
    super(posX,posY);
    name="Elephant";
    description = "+1.0 attackspeed\n"+//6
    "create targetet\n"+//6
    "bullets each shot\n"+//6
    "(3 dmg)\n"+//6
    "";
  }
  
  void stats1(){
    player.attackSpeed+=1.0;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.shotFrame){
        BoltBullet peprrr = new BoltBullet(3, player.posX,player.posY,600);
      }
    }
  }
  
  void drawSkinMap(){
    imageD(commonPlugI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(commonPlugI,posMX,posMY,60,60);
  }
}

class ChainBall extends GoodItem{
  ChainBall(float posX,float posY){
    super(posX,posY);
    name="Ball and Chain";
    description = "+0.3 reloadspeed\n"+//6
    "reloading creates\n"+//6
    "1 orbital bullets\n"+//6
    "200% dmg)\n"+//6
    "";
  }
  
  void stats1(){
    player.reloadSpeed+=0.3;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.reloading>1 && player.reloading<=2){
        OrbitalBullet peprrr = new OrbitalBullet();
      }
    }
  }
  
  void drawSkinMap(){
    imageD(commonSlowI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(commonSlowI,posMX,posMY,60,60);
  }
}

class MagicHat extends GoodItem{
  MagicHat(float posX,float posY){
    super(posX,posY);
    name="Wizard Hat";
    description = "+25 max mana\n"+//6
    "gain more mana\n"+//6
    "the more missing mana you have\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    player.maxMana+=25;
  }
  
  void stats2(int itemWave){
    if(itemWave==40){
      player.mana+= 2 *(player.maxMana-player.mana)/100.0/120;
    }
  }
  
  void drawSkinMap(){
    imageD(rareMagicHatI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareMagicHatI,posMX,posMY,60,60);
  }
}

class Driller extends GoodItem{
  int timer;
  Driller(float posX,float posY){
    super(posX,posY);
    name="Driller";
    description = "every 3s shoot\n"+//6
    "piercing spikes in every directon\n"+//6
    "kills reduce by 0.5s\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      timer-=kills.size()*60;
      if(timer<=0){
        timer=120*3;
        float angleS=0;
        int shots=12;
        for(int i=0;i<shots;i++){
          angleS+=TWO_PI/shots;
          PiercingBullet b=new PiercingBullet();
          b.dmg=player.dmg*0.5+5;
          b.maxRange=360+player.bulletSize*3;
          b.size=14;
          b.speedX=player.bulletSpeed/2*cos(angleS);
          b.speedY=player.bulletSpeed/2*sin(angleS);
        }
      }
    }
  }
  
  void drawSkinMap(){
    imageD(drillI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(drillI,posMX,posMY,60,60);
  }
}

class Ghost extends GoodItem{
  int timer;
  Ghost(float posX,float posY){
    super(posX,posY);
    name="Ghost";
    description = "+1 stack for each 8 second not damaged\n"+//6
    "stacks give:\n"+//6
    "+10 hp\n"+//6
    "+1 magSize\n"+//6
    "+0.2 regen";
  }
  
  void stats1(){
    if(stacks<10)timer--;
    if(timer<=0){
      stacks++;
      timer=8*120;
    }
    else{
      player.maxHp+=10 *stacks;
      player.regen+=0.2/120 *stacks;
      player.magsize+=1*stacks;
    }
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.gotDamaged){
        timer=5*120;
        stacks=0;
      }
    }
  }
  void drawBoxes(){
    if(stacks>0) itemEffectsBoxes(rareStealthI,stacks,255);
  }
  
  void drawSkinMap(){
    imageD(rareStealthI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareStealthI,posMX,posMY,60,60);
  }
}

class SpinnyGunn extends GoodItem{
  int stacks;
  int timer;
  SpinnyGunn(float posX,float posY){
    super(posX,posY);
    name="Spinnygun";
    description = "+1 magsize\n"+//6
    "shooting gives:\n"+//6
    "+0.2 attackspeed\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
    if(player.shotFrame)stacks++;
    player.attackSpeed+=0.2*stacks;
    player.magsize+=1;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.bulletsMag<=0)stacks=0;
      if(player.nextShot<=0)timer++;
      else timer=0;
      if(timer>0.5*120.0/stacks){
        stacks--;
        if(player.bulletsMag==0)stacks=0;
        timer=0;
      }
    }
  }
  void drawBoxes(){
    if(stacks>0)itemEffectsBoxes(goldSpinnyGunI,stacks,255);
  }
  
  void drawSkinMap(){
    imageD(goldSpinnyGunI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(goldSpinnyGunI,posMX,posMY,60,60);
  }
}

class Sandclock extends GoodItem{
  int timer;
  boolean ad;
  Sandclock(float posX,float posY){
    super(posX,posY);
    name="Sandclock";
    description = "either gives\n"+//6
    "+10 dmg\n"+//6
    "-20% attackspeed\n"+//6
    "or\n"+//6
    "+2.5 attackspeed\n"+//6
    "-20% dmg";
  }
  
  void stats1(){
    if(ad){ 
      player.dmg+=5;
    }else player.attackSpeed+=1.5;
    timer--;
    if(timer<=0){
      timer=120*15;
      ad=!ad;
    }
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      if(ad){ 
        player.attackSpeed*=0.8;
        player.dmg*=1.0/0.8;
      }else{
        player.dmg*=0.8;
        player.attackSpeed*=1.0/0.8;
      }
    }
  }
  void drawBoxes(){
    itemEffectsBoxes(sandclockI,timer/120,255);
  }
  
  void drawSkinMap(){
    imageD(sandclockI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(sandclockI,posMX,posMY,60,60);
  }
}

class StickyBombItem extends GoodItem{
  int timer;

  StickyBombItem(float posX,float posY){
    super(posX,posY);
    name="Sticky Bombs";
    description = "either gives\n"+//6
    "+5 dmg\n"+//6
    "or\n"+//6
    "+1.5 attackspeed\n"+//6
    "";
  }
  
  
  void stats2(int itemWave){
    if(itemWave==60){
      timer--;
      if(player.shotFrame && timer<=0 && random(100)<50){
        timer=0;
        new StickyBomb();
      }
    }
  }
  void drawBoxes(){
  }
  
  void drawSkinMap(){
    imageD(stickyBombI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(stickyBombI,posMX,posMY,60,60);
  }
}


class Singed extends GoodItem{
  float timer;

  Singed(float posX,float posY){
    super(posX,posY);
    name="Poison Gas";
    description = "leave a trail of\n"+//6
    "poison behind you\n"+//6
    "50% dmg/s\n"+//6
    "+0.1 speed\n"+//6
    "";
  }
  void stats1(){
    player.speed+=0.1;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      timer-= distance(0,0,player.speedX,player.speedY);
      if(timer<=0){
        timer=90;
        new Gas(player.posX-5*player.speedX,player.posY-5*player.speedY,player.dmg*0.5,50);
      }
    }
  }
  void drawBoxes(){
  }
  
  void drawSkinMap(){
    imageD(superRarePoisonI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(superRarePoisonI,posMX,posMY,60,60);
  }
}

class SteakEater extends GoodItem{
  ArrayList<Steak> steaks = new ArrayList<Steak>();
  int bonusAd;
  SteakEater(float posX,float posY){
    super(posX,posY);
    name="Ribeye steak";
    description = "enemies have a chance to drop\n"+//6
    "a steak giving\n"+//6
    "+5 dmg for 5 seconds\n"+//6
    "+10 hp\n"+//6
    "";
  }
  void stats1(){
    bonusAd=0;
    for(int i=0;i<steaks.size();i++){
      if(steaks.get(i).picked)bonusAd+=5;
      steaks.get(i).timer--;
        if(steaks.get(i).timer<=0){
          pickups.remove(steaks.get(i));
          steaks.remove(i);
        }
    }
    player.dmg+=bonusAd;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      for(int i=0;i<kills.size();i++){
        if(random(100)<10 +5*(player.maxHp-player.hp)/100) steaks.add(new Steak(kills.get(i).posX,kills.get(i).posY));
      }
    }
  }
  void drawBoxes(){
    if(bonusAd>0) itemEffectsBoxes(commonFoodI,bonusAd,255);
  }
  
  void drawSkinMap(){
    imageD(commonFoodI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(commonFoodI,posMX,posMY,60,60);
  }
}

class CampingTools extends GoodItem{
  int timer;
  CampingTools(float posX,float posY){
    super(posX,posY);
    name="Camping Tools";
    description = "every 60 seconds\n"+//6
    "create a camp. standing on it gives:\n"+//6
    "+2 healing/s\n"+//6
    "+2 mana/s\n"+//6
    "slows enemies/s";
    timer=(int)random(60*120);
  }
  void stats1(){
    timer--;
    if(timer<=0){
      timer=60*120;
      Camp c= new Camp(player.posX,player.posY);
      bigMap.add(c);
      smallMap.add(c);
    }
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
    }
  }
  void drawBoxes(){
  }
  
  void drawSkinMap(){
    imageD(rareCampI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareCampI,posMX,posMY,60,60);
  }
}
class Dagger extends GoodItem{
  int timer;
  float rememberX = 1.5;
  float rememberY;
  Dagger(float posX,float posY){
    super(posX,posY);
    name="Dagger";
    description = "shoots 10 daggers per second\n"+//6
    "\n"+//6
    "(30% dmg)\n"+//6
    "\n"+//6
    "";
  }
  
  void stats1(){
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
      if(player.bulletsMag>0)timer--;
      if(player.speedX!=0 || player.speedY!=0){
          rememberX=player.speedX;
          rememberY=player.speedY;
      }
      if(timer<=0){
        timer=12;
        new DaggerBullet(angle(0,0,rememberX/player.speed,rememberY/player.speed));
      }
    }
  }
  
  void drawSkinMap(){
    imageD(rareDaggerI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareDaggerI,posMX,posMY,60,60);
  }
}

float greenChest;
class ChestBuffer extends GoodItem{
  ChestBuffer(float posX,float posY){
    super(posX,posY);
    name="Pirates chest";
    description = "20% chance to get a green\n"+//6
    "item out of a chest\n"+//6
    "also closes 50% of opend chests\n"+//6
    "\n"+//6
    "";
    
    
  }
  
  void stats1(){
    greenChest+=0.20;
  }
  
  void stats2(int itemWave){
    if(itemWave==60){
    }
  }
  
  void drawSkinMap(){
    imageD(rareChestI,posX,posY,60);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(rareChestI,posMX,posMY,60,60);
  }
  void pickup(){
    for(int i=0;i<bigMap.size();i++){
      if(random(100)<50 && bigMap.get(i) instanceof Chest) ((Chest)bigMap.get(i)).state=1;
    }
  }
}

///itemTrades

class AsforAd extends GoodItem{
  AsforAd(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(){
    player.attackSpeed*= 2;
    player.dmg*= 0.5;
  }
  
  void drawSkinMap(){
    imageD(commonASRingI,posX+50,posY,50);
    imageD(commonSharkbiteI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonASRingI,posMX+16,posMY+10,30,30);
    image(commonSharkbiteI,posMX-16,posMY+10,30,30);
  }
}

class AdforAs extends GoodItem{
  AdforAs(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      player.dmg*= 2;
      player.attackSpeed*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonSharkbiteI,posX+50,posY,50);
    imageD(commonASRingI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonSharkbiteI,posMX+16,posMY+10,30,30);
    image(commonASRingI,posMX-16,posMY+10,30,30);
  }
}

class AsforHp extends GoodItem{
  AsforHp(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 attackspeed\n"+//6
    "half hp\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      player.attackSpeed*= 2;
      player.maxHp*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonASRingI,posX+50,posY,50);
    imageD(commonElefantI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonASRingI,posMX+16,posMY+10,30,30);
    image(commonElefantI,posMX-16,posMY+10,30,30);
  }
}

class HpforReload extends GoodItem{
  HpforReload(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      player.maxHp*= 2;
      player.reloadSpeed*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonElefantI,posX+50,posY,50);
    imageD(commonOilI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonElefantI,posMX+16,posMY+10,30,30);
    image(commonOilI,posMX-16,posMY+10,30,30);
  }
}

class ReloadfoMag extends GoodItem{
  ReloadfoMag(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==21){
      player.reloadSpeed*= 2;
      player.magsize*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonOilI,posX+50,posY,50);
    imageD(commonMagUpI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonOilI,posMX+16,posMY+10,30,30);
    image(commonMagUpI,posMX-16,posMY+10,30,30);
  }
}

class MagforMana extends GoodItem{
  MagforMana(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      player.magsize*= 2;
      player.manaRegen*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonMagUpI,posX+50,posY,50);
    imageD(commonManaI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonMagUpI,posMX+16,posMY+10,30,30);
    image(commonManaI,posMX-16,posMY+10,30,30);
  }
}

class ManaforRegen extends GoodItem{
  ManaforRegen(float posX,float posY){
    super(posX,posY);
    rollable=false;
    name="Ad for As";
    description = "x2 dmg\n"+//6
    "half attackspeed\n"+//6
    "\n"+//6
    "\n"+//6
    "";
  }
  
  void stats2(int itemWave){
    if(itemWave==20){
      player.manaRegen*= 2;
      player.regen*= 0.5;
    }
  }
  
  void drawSkinMap(){
    imageD(commonManaI,posX+50,posY,50);
    imageD(commonHeartI,posX-50,posY,50);
  }
  void drawSkinMenu(float posMX, float posMY){
    textC(">>>",posMX, posMY-22, 18, 0);
    image(commonManaI,posMX+16,posMY+10,30,30);
    image(commonHeartI,posMX-16,posMY+10,30,30);
  }
}
