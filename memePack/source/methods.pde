boolean buttonPressed(float posX, float posY, float sizeX, float sizeY) {
  if (keys[12] && firstFrameClick && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

boolean buttonHoover(float posX, float posY, float sizeX, float sizeY) {
  if (mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

boolean buttonRightClick(float posX, float posY, float sizeX, float sizeY) {
  if (keys[13] && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

boolean buttonHold(float posX, float posY, float sizeX, float sizeY) {
  if (mousePressed && mouseX<posX+sizeX/2 && mouseX>posX-sizeX/2 && mouseY<posY+sizeY/2 && mouseY>posY-sizeY/2) {
    return true;
  }
  return false;
}

void turnImg(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x,y);
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}

void turnImgD(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x-camX,y-camY);
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}

void turnFlipImgD(PImage img,float x,float y,float sizeX,float sizeY,float degr){
  pushMatrix();
  translate(x-camX,y-camY);
  if(degr>=HALF_PI || degr<=-HALF_PI){
    scale(-1,1);
    degr=PI-degr;
  }
  rotate(degr);
  image(img,0,0,sizeX,sizeY);
  popMatrix();
}
void ellipseD(float posX, float posY, float size) {
  ellipse(posX-camX, posY-camY, size, size);
}

void imageD(PImage img,float posX, float posY, float size) {
  image(img, posX-camX, posY-camY, size, size);
}

void rectD(float posX, float posY, float size) {
  rect(posX-camX, posY-camY, size, size);
}

void rectD(float posX, float posY, float sizeX,float sizeY) {
  rect(posX-camX, posY-camY, sizeX, sizeY);
}

void textC(String text,float x,float y, float size, color c){
  textSize(size);
  fill(c);
  text(text,x,y);
}

void textD(String text,float x,float y, float size, color c){
  textSize(size);
  fill(c);
  text(text,x-camX, y-camY);
}

float w(){
  return width/1000.0;
}
float h(){
  return height/800.0;
}

float distance(float x1, float y1, float x2, float y2) {
  float distance=(float)Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
  return distance;
}

float angle(float x1, float y1, float x2, float y2){
  float angle=0;
  angle=(float)Math.atan2( (y2-y1), (x2-x1));
  /*if(x1>x2)angle=TWO_PI-angle;
  if(x2-x1<=1 && x2-x1>=-1){
    if(y1<y2)angle=HALF_PI;
    if(y1>y2)angle=TWO_PI-HALF_PI;
  }
  */
  return angle;
}

float getPosX(float r,float size){
  if(r<1200){
      return r;
    }else if(r<2400){
      return r-1200;
    }else if(r<3400){
      return -size;
    }else if(r<4400){
      return 1200+size;
    }
    return 0;
}
float getPosY(float r,float size){
  if(r<1200){
      return -size;
    }else if(r<2400){
      return 1000+size;
    }else if(r<3400){
      return r-2400;
    }else if(r<4400){
      return r-3400;
    }
    return 0;
}
float getSpeedX(float r,float speed){
  if(r<1200){
      return random(-speed,speed);
    }else if(r<2400){
      return random(-speed,speed);
    }else if(r<3400){
      return random(speed/4,speed);
    }else if(r<4400){
      return random(-speed/4,-speed);
    }
    return 10;
}
float getSpeedY(float r,float speed){
  if(r<1200){
      return random(speed/4,speed);
    }else if(r<2400){
      return random(-speed/4,-speed);
    }else if(r<3400){
      return random(-speed,speed);
    }else if(r<4400){
      return random(-speed,speed);
    }
    return 10;
}


void keyPressed() {
  if (key=='w'||key=='W')keys[0]=true;
  if (key=='a'||key=='A')keys[1]=true;
  if (key=='s'||key=='S')keys[2]=true;
  if (key=='d'||key=='D')keys[3]=true;
  if (key=='q'||key=='Q')keys[4]=true;
  if (key=='e'||key=='E')keys[5]=true;
  if (key==' ')          keys[6]=true;
  if (key=='p'||key=='P')keys[7]=true;
  if (key=='m'||key=='M')keys[8]=true;
  if (key=='r'||key=='R')keys[9]=true;
  if (key==ESC) {
    key=0;
    save();
  }
  if (key==TAB) {
    key=0;
    keys[11]=true;
  }
}

void mousePressed() {
  if (mouseButton==LEFT)keys[12]=true;
  if (mouseButton==RIGHT)keys[13]=true;
}

void keyReleased() {
  if (key=='w'||key=='W')keys[0]=false;
  if (key=='a'||key=='A')keys[1]=false;
  if (key=='s'||key=='S')keys[2]=false;
  if (key=='d'||key=='D')keys[3]=false;
  if (key=='q'||key=='Q')keys[4]=false;
  if (key=='e'||key=='E')keys[5]=false;
  if (key==' ')          keys[6]=false;
  if (key=='p'||key=='P')keys[7]=false;
  if (key=='m'||key=='M')keys[8]=false;
  if (key=='r'||key=='R')keys[9]=false;
  if (key==TAB) {
    key=0;
    keys[11]=false;
  }
}

void mouseReleased() {
  if (mouseButton==LEFT)keys[12]=false;
  if (mouseButton==RIGHT)keys[13]=false;
}
