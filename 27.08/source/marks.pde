class Mark{
  
  Mark(){
  }
  void startGame(){
  }
  void endGame(){
  }
  void drawScreen(int i){
    float posX=100+70*(i%14);
    float posY=500+(int)(i/14);
    image(FishHeadI,posX,posY,60,60);
  }
  void drawIngame(){
  }
}

class FirstBossKill extends Mark{
  
  FirstBossKill(){
  }
  
  void drawScreen(int i){
    float posX=100+70*(i%14);
    float posY=500+(int)(i/14);
    image(FishHeadI,posX,posY,60,60);
  }
  void startGame(){
    player.itemsEquipt.add(new Ad(0,0));
    player.itemsEquipt.add(new Ad(0,0));
    player.itemsEquipt.add(new ASItem(0,0));
    player.itemsEquipt.add(new ASItem(0,0));
    spawnRate+=0.35;
  }
  
  void endGame(){
    marks.remove(this);
  }
}
