void addSkins(){
  skins.add(new SkinNorm());
  skins.add(new SkinEmp());
  skins.add(new SkinVamp());
  skins.add(new SkinHive());
  skins.add(new SkinShrek());
  skins.add(new SkinActualy());
  skins.add(new SkinPocorn());
  skins.add(new SkinHellokitty());
  skins.add(new SkinDisco());
  skins.add(new SkinSkull());
  skins.add(new SkinFish());
  skins.add(new SkinRaphi());
  skins.add(new SkinDeath());
  skins.add(new SkinBane());
  skins.add(new SkinMorpheus());
  skins.add(new SkinClown());
  skins.add(new SkinCharlyBrown());
  skins.add(new SkinGlitch());
  skins.add(new SkinBombSkin());
}

float spinnspeed;
float spinnPos;
void newSkin(){

  if(buttonPressed(960, 650, 400, 100) && money>=100 && spinnspeed<=0){
    money-=100;
    spinnspeed=(10+random(10))/120;
    spinnPos=-0.5+random(skins.size());
  }
    
  if(spinnspeed>0){
    spinnspeed*=1-(0.2/120);
    spinnspeed-=0.01/120;
    spinnPos+=spinnspeed;
    if((int)(spinnPos+0.5)>=skins.size())spinnPos=-0.5;
    
    image(skins.get(skinPicked).img,1920/2 -150 +300*((spinnPos+0.5)%1),300,45,45);
    
    skinPicked=(int)(spinnPos+0.5);
    if(spinnspeed<=0.0){
      if(skins.get((int)(spinnPos+0.5)).unlocked==true) diamonds++;
      skins.get((int)(spinnPos+0.5)).unlocked=true;
      
    }
  }
}
class Mission{
  float score;
  boolean finished;
  
  void menuText(){
  }
  
  void checkFinished(){
    
  }
}



class KillEnemiesFrameMission extends Mission{
  int goal;
  
  void menuText(){
    textC("kill "+goal+ " enemies at once ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(kills.size()>=goal){ finished=true; money+=100;}
    if(kills.size()>score) score=bossKills;
  }
}
class Kill10EnemiesFrameMission extends KillEnemiesFrameMission{
  Kill10EnemiesFrameMission(){
    goal=10;
  }
}


class KillBossesMission extends Mission{
  int goal;
  
  void menuText(){
    textC("kill "+goal+ " bosses in one run ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(bossKills>=goal){ finished=true; money+=100;}
    if(bossKills>score) score=bossKills;
  }
}
class Kill1BossesM extends KillBossesMission{
  Kill1BossesM(){
    goal=1;
  }
}
class Kill5BossesM extends KillBossesMission{
  Kill5BossesM(){
    goal=5;
  }
}
class Kill10BossesM extends KillBossesMission{
  Kill10BossesM(){
    goal=10;
  }
}
class Kill20BossesM extends KillBossesMission{
  Kill20BossesM(){
    goal=20;
  }
}

class KillBossesTimeMission extends Mission{
  int goal;
  int timer;
  void menuText(){
    textC("kill "+goal+ " bosses in "+timer+" min ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(time/120/60<timer && bossKills>=goal){ finished=true; money+=100;}
    if(time/120/60<timer && bossKills>score) score=bossKills;
  }
}
class Kill10Bosses15TimeMission extends KillBossesTimeMission{
  Kill10Bosses15TimeMission(){
    goal=10;
    timer=15;
  }
}

class SurviveMission extends Mission{
  int goal;
  
  void menuText(){
    textC("survive for "+goal+ " minutes ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(time/120/60>=goal){ finished=true; money+=100;}
    if(time/120/60>score) score=time/120/60;
  }
}
class Survive10Mission extends SurviveMission{
  Survive10Mission(){
    goal=10;
  }
}

class Survive20Mission extends SurviveMission{
  Survive20Mission(){
    goal=20;
  }
}

class Survive30Mission extends SurviveMission{
  Survive30Mission(){
    goal=30;
  }
}


class SurviveNoItemsMission extends Mission{
  int goal;
  
  void menuText(){
    textC("survive for "+goal+ " minutes without any items ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(player!=null){
      if(player.itemsEquipt.size()<=0 && player.itemsGood.size()<=0 && time/120/60>=goal){ finished=true; money+=100;}
      if(player.itemsEquipt.size()<=0 && player.itemsGood.size()<=0 && time/120/60>score) score=time/120/60;
    }
  }
}
class Survive10NoItemsMission extends SurviveNoItemsMission{
  Survive10NoItemsMission(){
    goal=10;
  }
}


class StatMission extends Mission{
  int goal;
  String stat;
  
  void menuText(){
    textC("reach "+goal+" "+stat +" ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(player!=null){
      if(!finished&& getStat()>=goal){ finished=true; money+=100;}
      if(getStat()>score) score=getStat();
    }
  }
  float getStat(){return 0;}
}
class StatDmgMission extends StatMission{
  StatDmgMission(){
    goal=60;
    stat="damage";
  }
  float getStat(){return player.dmg;}
}
class StatASMission extends StatMission{
  StatASMission(){
    goal=12;
    stat="Attackspeed";
  }
  float getStat(){return player.attackSpeed;}
}
class StatHpMission extends StatMission{
  StatHpMission(){
    goal=500;
    stat="Hp";
  }
  float getStat(){return player.maxHp;}
}
class StatManaMission extends StatMission{
  StatManaMission(){
    goal=5;
    stat="Manaregen";
  }
  float getStat(){return player.manaRegen;}
}
class StatRegenMission extends StatMission{
  StatRegenMission(){
    goal=4;
    stat="Regen";
  }
  float getStat(){return player.regen;}
}
class StatMagMission extends StatMission{
  StatMagMission(){
    goal=50;
    stat="Magsize";
  }
  float getStat(){return player.magsize;}
}

class RerollMissionextends extends Mission{
  int goal;
  RerollMissionextends(){
    goal=13;
  }
  void menuText(){
    textC("use the ItemDice "+goal+ " times ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(score>=goal){ finished=true; money+=100;}
    if(itemDiced){
      score++;
      itemDiced=false;
    }
  }
}

float poisonFrame;
class PoisonMission extends Mission{
  int goal;
  PoisonMission(){
    goal=2000;
  }
  void menuText(){
    textC("get "+goal+ " damage from Poison ("+(int)score+")",1500,150,26,255);
  }
  
  void checkFinished(){
    if(score>=goal){ finished=true; money+=100;}
    score+=poisonFrame;
    poisonFrame=0;
  }
}


class MapMission extends Mission{
  String stat;
  
  void menuText(){
    textC("reach the end of the map",1500,150,26,255);
  }
  
  void checkFinished(){
    if(player!=null){
      if(player.posX>mapSize || player.posX<-mapSize || player.posY>mapSize || player.posY<-mapSize){ finished=true; money+=100;}
    }
  }
  float getStat(){return 0;}
}


class Skin{
  Mission m = new Kill10BossesM();
  boolean unlocked=false ;
  PImage img;
  
  void drawMenu(){
    if(!unlocked)tint(20,100);
    image(img,1920/2,200,150,150);
    tint(255);
    if(unlocked){
      m.menuText();
      if(m.finished) image(crossBulletI,1920/2+80,200-50,100,100);
    }
  }
  
  void drawBody(float posX, float posY, float size){
    imageD(img,posX,posY,size*2);
    drawWeapon(posX, posY, size);
    if(!m.finished) m.checkFinished();
    if(keys[8]) m.menuText();
  }
  
  void drawWeapon(float posX, float posY, float size){
    float weaponAngle=angle(posX,posY,camX+mouseX,camY+mouseY);
    turnFlipImgD(sniperI,posX,posY+size*0.7,size*2,size*2,weaponAngle);
  }
}
class SkinNorm extends Skin{
  SkinNorm(){
    unlocked=true;
    img=playerI;
    m= new Kill1BossesM();
  }
}

class SkinEmp extends Skin{
  SkinEmp(){
    img=emperorI;
    m= new Kill5BossesM();
  }
}

class SkinVamp extends Skin{
  SkinVamp(){
    img=vampireI;
    m= new Kill10BossesM();
  }
}

class SkinHive extends Skin{
  SkinHive(){
    img=hivemasterI;
    m= new Kill20BossesM();
  }
}

class SkinShrek extends Skin{
  SkinShrek(){
    img=shrekI;
    m=new Survive10Mission();
  }
}
class SkinActualy extends Skin{
  SkinActualy(){
    img=actualyI;
    m=new Survive20Mission();
  }
}
class SkinPocorn extends Skin{
  SkinPocorn(){
    img=pocornI;
    m=new Survive30Mission();
  }
}
class SkinHellokitty extends Skin{
  SkinHellokitty(){
    img=hellokittyI;
    m=new StatDmgMission();
  }
}
class SkinDisco extends Skin{
  SkinDisco(){
    img=discoI;
    m=new StatASMission();
  }
}

class SkinClown extends Skin{
  SkinClown(){
    img=clownI;
    m=new StatHpMission();
  }
}
class SkinFish extends Skin{
  SkinFish(){
    img=fishI;
    m=new StatManaMission();
  }
}
class SkinRaphi extends Skin{
  SkinRaphi(){
    img=raphiI;
    m=new StatRegenMission();
  }
}
class SkinDeath extends Skin{
  SkinDeath(){
    img=deathI;
    m=new Kill10EnemiesFrameMission();
  }
}
class SkinBane extends Skin{
  SkinBane(){
    img=baneI;
    m = new PoisonMission();
  }
}
class SkinMorpheus extends Skin{
  SkinMorpheus(){
    img=morpheusI;
    m = new RerollMissionextends();
  }
}
class SkinSkull extends Skin{
  SkinSkull(){
    img=skullI;
    m=new Kill10Bosses15TimeMission();
  }
}
class SkinGlitch extends Skin{
  SkinGlitch(){
    img=glitchI;
    m= new MapMission();
  }
}
class SkinCharlyBrown extends Skin{
  SkinCharlyBrown(){
    img=charlyBrownI;
    m=new Survive10NoItemsMission();
  }
}

class SkinBombSkin extends Skin{
  SkinBombSkin(){
    img=bombSkinI;
    m=new Kill10EnemiesFrameMission();
  }
} 
