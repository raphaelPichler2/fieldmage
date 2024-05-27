class Player{
  float posX;
  float posY;
  float size;
  
  float speed;
  float speedX;
  float speedY;
  int menu;
  
  float attackSpeed;
  float nextShot = 60;
  int magsize;
  int bulletsMag=2;
  float reloadSpeed;
  float reloading = 120*2;
  float dmg;
  float stealth;
  int noStealth;
  float bulletSpeed;
  float bulletSize;
  float scope;
  float stunnDuration;
  float pepper;
  float bulletNotReuse;
  float slowDuration;
  float mixer;
  float magnet;
  float poison;
  float mutation;
  float firstShotDmg;
  float stunnDmg;
  float goldDrops;
  float markDmg;
  float markSlow;
  float markPercent;
  float firstShotStunn;
  float maxSpinny;
  float spinnSpeed;
  float stunned;
  float maxHp;
  float hp;
  float regen;
  float armor;
  float harvestSpeed; 
  float maxMana;
  float mana;
  float cdr;
  float ap;
  float manaRegen;
  float spray;
  float chargedPlug;
  boolean spamStopper;
  float accelerateBulet;
  int bulletHitsFrame;
  int cheese;
  int dashType;
  float dmgReduction;
  float dashRange;
  float meeleDmg=0;
  float bonusShotdmg;
  float knockback;
  float primeSkillUp;
  boolean shotFrame;
  float shield;
  float tempHp;
  
  float spawnX;
  float spawnY;
  
  Skill rightClick;
  Skill Space = new Rage();
  Skill Qskill;

  int page = 0;
  ArrayList<Item> itemsEquipt = new ArrayList<Item>();
  ArrayList<GoodItem> itemsGood = new ArrayList<GoodItem>();
  /*ArrayList<Item> itemsUnlocked = new ArrayList<Item>();
  ArrayList<Item> itemsEquipt = new ArrayList<Item>();
  ArrayList<Consumable> consum = new ArrayList<Consumable>();
  ArrayList<Consumable> consumEquipt = new ArrayList<Consumable>();*/
  int classCode;
  
  Player(){
    
    size=30;
    posX=0;
    posY=0;
    spawnX=0;
    spawnY=0;
    maxHp=1000000;
    mana=10000000;
    hp=maxHp;
    /*for(int i=0;i<10;i++){
    items.add(new RavenItem(posX,posY-50));
    items.add(new Pepper(posX,posY-50));
    items.add(new BlueCheese(posX,posY-50));
    items.add(new ShieldRhino(posX,posY-50));
    items.add(new Warrior(posX,posY-50));
    items.add(new ShieldRhino(posX,posY-50));
    items.add(new MagicMag(posX,posY-50));
    items.add(new Firststrike(posX,posY-50));
    items.add(new Monk(posX,posY-50));
    }*/
    
    
  }

  void draw(){
    
    if(hp<=0)die();
    baseStats();
    for(int i=0;i<itemsEquipt.size();i++){
      itemsEquipt.get(i).stats1();
    }
    for(int i=0;i<itemsEquipt.size();i++){
      itemsEquipt.get(i).stats2();
    }
    for(int i=0;i<itemsGood.size();i++){
      itemsGood.get(i).stats1();
    }
    
    if(rightClick!=null){
      rightClick.buff();
    }
    if(Space!=null){
      Space.buff();
    }
    if(Qskill!=null){
      Qskill.buff();
    }
    
    for(int i=0;i<itemsGood.size();i++){
      itemsGood.get(i).stats2();
    }
    
    if(hp<maxHp)hp+=regen;
    if(hp>maxHp)hp=maxHp;
    
    if(mana<maxMana)mana+=manaRegen;
    if(mana>maxMana)mana=maxMana;
    
    drawBody();
    
    //move
    speedX=0;
    speedY=0;
    if(keys[0]){
      if(keys[1]||keys[3])speedY=-speed/1.4;
      else speedY=-speed;
    }
    if(keys[1]){
      if(keys[0]||keys[2])speedX=-speed/1.4;
      else speedX=-speed;
    }
    if(keys[2]){
      if(keys[1]||keys[3])speedY=speed/1.4;
      else speedY=speed;
    }
    if(keys[3]){
      if(keys[0]||keys[2])speedX=speed/1.4;
      else speedX=speed;
    }
    if(stunned>0){
      stunned--;
      speedX=0;
      speedY=0;
    }
    posX=posX+speedX;
    posY=posY+speedY;

    //skills
    if(rightClick!=null){
      if(keys[13])rightClick.activate();
    }
    if(Space!=null){
      if(keys[6])Space.activate();
    }
    if(Qskill!=null){
      if(keys[4])Qskill.activate();
    }
    
    //gun
    shotFrame=false;
    if(keys[9] && bulletsMag<magsize)bulletsMag=0;
    if(bulletsMag<=0){
      spinnSpeed=0;
      if(reloading<=0){
        //reloading
        reloading=120/reloadSpeed;
        if(reloading<120/attackSpeed) reloading=120/attackSpeed;
        bulletsMag=magsize;
        nextShot=0;
      }else reloading--;
    }else{
      
      //shot
      if(nextShot<=0){
        shot();
      }else nextShot--;
    }
    
    //arrow
    if(activeBoss==null)turnImgD(arrowI,posX,posY,100,100,HALF_PI+angle(posX, posY,activeAltar.posX,activeAltar.posY));
    else turnImgD(arrowI,posX,posY,100,100,HALF_PI+angle(posX, posY,activeBoss.posX,activeBoss.posY));
    
  }
  
  void shot(){
    if(keys[12]){
      nextShot=120/attackSpeed;
      reloading=120/reloadSpeed;
      shotFrame=true;
      Bullet b = new Bullet();
      if(random(1)<bulletNotReuse)bulletsMag--;
    }
  }
  void drawBody(){
    imageD(playerI,posX,posY,size*2);
    float weaponAngle=angle(posX,posY,camX+mouseX,camY+mouseY);
    turnFlipImgD(sniperI,posX,posY+size*0.7,size*2,size*2,weaponAngle);
  }
  
  void getDmg(float dmg){
    float dmgLeft=dmg;
    if(tempHp>0){
      dmgLeft-=tempHp;
      tempHp-=dmg;
    }
    if(tempHp<0)tempHp=0;
    if(dmgLeft>0 && shield>0){
      float sDMGleft=shield;
      shield-=dmgLeft;
      dmgLeft-=sDMGleft;
    }
    if(shield<0)shield=0;
    if(dmgLeft>0)hp-=dmgLeft*dmgReduction;
    
  }
  
  void die(){
    gameMode=0;
    for(int i = 0; i<marks.size();i++){
      marks.get(i).endGame();
    }
  }
  
  void drawHud(){
    fill(255);
    rect(400,1000,600,40);
    noFill();
    rect(50,985,90,75);
    textC(""+(int)level,50,985,30,0);
   
    fill(200);
    rect(100+300*hp/maxHp,1000,600*hp/maxHp,40);
    textC((int)hp+"/"+(int)maxHp,400,1000,20,0);
    
    fill(#60A2D3);
    if(shield>0) rect(5+300*shield/maxHp,920,600*shield/maxHp,20);
    fill(#59AA8D);
    if(tempHp>0) rect(5+600*shield/maxHp+300*tempHp/maxHp,920,600*tempHp/maxHp,20);
    
    itemHud();
    
    fill(255);
    rect(750,985,75,75);
    textC(bulletsMag+"/"+magsize,750,985,20,0);
    
    
    
    fill(255);
    rect(400,960,600,25);
    fill(#4C35D3);
    rect(100+300*mana/maxMana,960,600*mana/maxMana,25);
    
    if(rightClick!=null){
      rightClick.draw(1060);
    }
    if(Space!=null){
      Space.draw(970);
    }
    if(Qskill!=null){
      Qskill.draw(860);
    }
  }
  
  
  int kapaUsed;
  int redKapaUsed;
  
  void drawMenu(){
    background(200);
    textAlign(LEFT, CENTER);
    textC("Dmg: ",150,30,20,0); textC(""+dmg,300,30,20,0);
    textC("Mag: ",150,50,20,0); textC(""+magsize,300,50,20,0);
    textC("Attackspeed: ",150,70,20,0); textC(""+attackSpeed,300,70,20,0);
    textC("Speed: ",150,90,20,0); textC(""+speed,300,90,20,0);
    textC("Reloadspeed: ",150,110,20,0); textC(""+reloadSpeed,300,110,20,0);
    textC("Regen: ",150,130,20,0); textC(""+(120*regen),300,130,20,0); 
    textC("Mana: ",150,150,20,0); textC(""+(120*manaRegen),300,150,20,0);
    textAlign(CENTER, CENTER);
    
    for(int i=0;i<itemsGood.size();i++){
      float posXM=120+80*(i%20);
      float posYM=300+80*(int)(i/20);
      itemsGood.get(i).drawSkinMenu(posXM,posYM);
    }
  }
  
  float levelMulti(){
    return (float)Math.pow(1.06,level);
  }
  
  void baseStats(){
    attackSpeed=2;
    if(bulletsMag>magsize) bulletsMag=magsize;
    magsize=6;
    bulletSize=12;
    reloadSpeed=0.5;
    dmg=10;
    bulletSpeed = 12;
    speed=1.5;
    scope=0;
    stunnDuration=0;
    pepper=0;
    bulletNotReuse=1;
    slowDuration=0;
    poison=0;
    firstShotDmg=0;
    stunnDmg=0;
    maxHp=100;
    regen=0.5/120;
    armor=0;
    harvestSpeed=10.0/120;
    maxMana=100;
    cdr=0;
    ap=10;
    spray=0;
    chargedPlug=0;
    manaRegen=0.5/120;
    accelerateBulet=0;
    dashType=0;
    dmgReduction=1;
    meeleDmg=0;
    dashRange=15;
    bonusShotdmg=0;
    knockback=50;
    primeSkillUp=1;
    if(shield>0){
      shield*=1-(0.5/120) *100/maxHp;
      shield-=4.0/120.0 *100/maxHp;
    }else shield=0;
    extraStats();
  }
  void extraStats(){}
}
