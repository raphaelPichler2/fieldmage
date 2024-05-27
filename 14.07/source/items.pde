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
  void stats2(){ 
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
    fill(255);
    rect(960,540,800,400);
    textC(name,960,390,30,0);
    textSize(16);
    text(description,960,590,780,330);
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
  textC(""+number,posX+12,posY+12,18,c);
  boxesDrawn++;
  }
}
