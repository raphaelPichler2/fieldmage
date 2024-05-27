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

class Skin{
  boolean unlocked=false ;
  PImage img;
  
  void drawMenu(){
    if(!unlocked)tint(20,100);
    image(img,1920/2,200,150,150);
    tint(255);
  }
  
  void drawBody(float posX, float posY, float size){
    imageD(img,posX,posY,size*2);
    drawWeapon(posX, posY, size);
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
  }
}

class SkinEmp extends Skin{
  SkinEmp(){
    img=emperorI;
  }
}

class SkinVamp extends Skin{
  SkinVamp(){
    img=vampireI;
  }
}

class SkinHive extends Skin{
  SkinHive(){
    img=hivemasterI;
  }
}

class SkinShrek extends Skin{
  SkinShrek(){
    img=shrekI;
  }
}
class SkinActualy extends Skin{
  SkinActualy(){
    img=actualyI;
  }
}
class SkinPocorn extends Skin{
  SkinPocorn(){
    img=pocornI;
  }
}
class SkinHellokitty extends Skin{
  SkinHellokitty(){
    img=hellokittyI;
  }
}
class SkinDisco extends Skin{
  SkinDisco(){
    img=discoI;
  }
}

class SkinClown extends Skin{
  SkinClown(){
    img=clownI;
  }
}
class SkinFish extends Skin{
  SkinFish(){
    img=fishI;
  }
}
class SkinRaphi extends Skin{
  SkinRaphi(){
    img=raphiI;
  }
}
class SkinDeath extends Skin{
  SkinDeath(){
    img=deathI;
  }
}
class SkinBane extends Skin{
  SkinBane(){
    img=baneI;
  }
}
class SkinMorpheus extends Skin{
  SkinMorpheus(){
    img=morpheusI;
  }
}
class SkinSkull extends Skin{
  SkinSkull(){
    img=skullI;
  }
}
class SkinGlitch extends Skin{
  SkinGlitch(){
    img=glitchI;
  }
}
class SkinCharlyBrown extends Skin{
  SkinCharlyBrown(){
    img=charlyBrownI;
  }
}
