Item randomLoot(float posX,float posY){
  ArrayList<Item> dropPool = new ArrayList<Item>();
  
  dropPool.add(new Ad(posX,posY));
  dropPool.add(new Mag(posX,posY));
  dropPool.add(new ASItem(posX,posY));
  dropPool.add(new SpeedItem(posX,posY));
  dropPool.add(new ReloadItem(posX,posY));
  dropPool.add(new Elephant(posX,posY));
  dropPool.add(new HeartItem(posX,posY));
  dropPool.add(new ManaItem(posX,posY));
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

GoodItem randomGoodLoot(float posX,float posY){
  ArrayList<GoodItem> dropPool = new ArrayList<GoodItem>();
  
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
  dropPool.add(new BigElephant(posX,posY));
  dropPool.add(new Plug(posX,posY));
  dropPool.add(new ChainBall(posX,posY));
  dropPool.add(new MagicHat(posX,posY));
  dropPool.add(new Driller(posX,posY));
  dropPool.add(new Ghost(posX,posY));
  dropPool.add(new SpinnyGunn(posX,posY));
  dropPool.add(new Sandclock(posX,posY));
  dropPool.add(new StickyBombItem(posX,posY));
  dropPool.add(new Singed(posX,posY));
  dropPool.add(new SteakEater(posX,posY));
  dropPool.add(new CampingTools(posX,posY));
  dropPool.add(new Dagger(posX,posY));
  
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

GoodItem tradeItem(float posX,float posY){
  ArrayList<GoodItem> dropPool = new ArrayList<GoodItem>();
  
  dropPool.add(new AsforAd(posX,posY));
  dropPool.add(new AdforAs(posX,posY));
  dropPool.add(new AsforHp(posX,posY));
  dropPool.add(new HpforReload(posX,posY));
  dropPool.add(new ReloadfoMag(posX,posY));
  dropPool.add(new MagforMana(posX,posY));
  dropPool.add(new ManaforRegen(posX,posY));
  
  return dropPool.get( (int)random(dropPool.size()) ) ;
}

class Item{
  float posX;
  float posY;
  int kapa;
  int redKapa;
  int code;
  String name;
  String description;
  int tier;
  int stacks;
  
  Item(float posX,float posY){
    this.posX=posX;
    this.posY=posY;
    kapa=1;
    tier=1;
    redKapa=0;
    name="Name";
    description="does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" + 
    "does nothing and sits in your inventory, wasting space. get fucked.\n" ;
  }
  void drawBoxes(){}
  
  void stats1(){ 
  }
  void stats2(int itemWave){ 
  }
  
  void drawMap(){
    drawSkinMap();
    
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<50){
      items.remove(this);
      player.itemsEquipt.add(this);
    }
  }
  
  void drawSkinMap(){
    fill(200);
    rectD(posX,posY,60,40);
  }
  void drawSkinMenu(){
  }
  
  void drawMenuText(){
    textC(name,1160,50,30,0);
    textSize(18);
    text(description,1160,150,780,330);
  }
  
  void drawMenu(){
  }
  
  void reset(){
    stacks=0;
  }
}

class GoodItem extends Item{
  GoodItem(float posX,float posY){
    super(posX,posY);
  }
  void drawSkinMenu(float posMX, float posMY){
    image(ravenItemI,posMX,posMY,60,60);
  }
  
  void drawMap(){
    drawSkinMap();
    
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<50){
      items.remove(this);
      player.itemsGood.add(this);
    }
  }
  
}
ArrayList<PImage> imgDrawnBox;
int boxesDrawn;
void itemHud(){
  boxesDrawn=0;
  imgDrawnBox = new ArrayList<PImage>();
  
  for(int i=0;i<player.itemsGood.size();i++){
    player.itemsGood.get(i).drawBoxes();
  }
}

void itemEffectsBoxes(PImage img,int number,color c){
  if(!imgDrawnBox.contains(img)){
    
  imgDrawnBox.add(img);
  float posX=250+60*boxesDrawn;
  float posY=870;
  
  image(img,posX,posY,50,50);
  textC(""+number,posX+12,posY+12,18,0);
  boxesDrawn++;
  }
}

class Pickup{
  float posX;
  float posY;
  float size;
 
  
  void drawMap(){
    drawSkinMap();
    float dist=distance(posX,posY,player.posX,player.posY);
    if(dist<(size+player.size)/2){
      pickups.remove(this);
      pickUp();
    }
  }
  void pickUp(){}
  void drawSkinMap(){}
}


class Coin extends Pickup{
  Coin(float posX,float posY){
    size=18;
    this.posX=posX;
    this.posY=posY;
  }
  
  void pickUp(){
    money++;
    moneyRound++;
  }
  
  
  void drawSkinMap(){
    imageD(coin1I,posX,posY,size*2);
  }
}

class GrayCrystal extends Pickup{
  GrayCrystal(float posX,float posY){
    size=50;
    this.posX=posX;
    this.posY=posY;
  }
  
  void pickUp(){
    timeChrystal++;
  }
  
  
  void drawSkinMap(){
    imageD(grayCrytalI,posX,posY,size*2);
  }
}

class GreenCrystal extends Pickup{
  GreenCrystal(float posX,float posY){
    size=70;
    this.posX=posX;
    this.posY=posY;
  }
  
  void pickUp(){
    greenChrystal++;
  }
  
  
  void drawSkinMap(){
    imageD(greenCrytalI,posX,posY,size*2);
  }
}

class Steak extends Pickup{
  boolean picked;
  int timer;
  Steak(float posX,float posY){
    size=38;
    this.posX=posX;
    this.posY=posY;
    pickups.add(this);
    timer=16*120;
  }
  
  void pickUp(){
    picked=true;
    player.hp+=10;
    timer=timer/2;
    timer+=120;
    if(player.hp>player.maxHp)player.hp=player.maxHp;
  }
  
  void drawSkinMap(){
    imageD(steakI,posX,posY,size*2);
  }
}
