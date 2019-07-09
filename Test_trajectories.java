import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Test_trajectories extends PApplet {

//Sample code by Joe Winter submitted with application to the Recurse Center

//This program is written to (roughly) visualize the dynamical system known as the perturbed 
//doubling map, which is a function on points in the complex plane (which 
//we can also think of as 2D space)

//the map is the point z -> z^2 + c, where c is a complex number which we call
//the perturbation constant

//when the program is run (in the Processing environment), CLICK THE MOUSE to iterate
//the map once. Each time you click, it will run the map on the grid of sample points 
//again. The goal of the visualization is to see how changing the perturbation constant effects the map, 
//how points "move around the space" and how many iterations it takes for most points
//to either fly away to infinity or collapse to the fixed-point/attractor of the system

//you can adjust the perturbation constant in the code. It's the first variable below: 

//ptb[] is the perturbation constant c in the doubling map z^2 + c. 
//ADJUST THIS IF YOU WANT TO SEE THE EFFECT ON THE DYNAMICS
//DON'T go above 1.0 in either coordinate otherwise the points will just fly off the grid

float ptb[] = {0.0f, 0.0f};  

float gridW = 800; 
float gridH = 800; 

//what range of 2D space to represent (centered at 0,0) 
float xrange = 3.0f; 
float yrange = 3.0f; 

//convert pixel space on computer screen to real number space on x and y axis
float pointspaceX = gridW/xrange; 
float pointspaceY = gridH/yrange; 

int sizeW = PApplet.parseInt(gridW); 
int sizeH = PApplet.parseInt(gridH); 

//one pixel represents this much "distance" on x or y axis
float unitDistance = gridW/xrange;  



float meshspacing; 
float pointsize;

int pointRow = 200; //how many points in a row of the grid

//total number of points to render
int samplePoints = 1; 

int Ygrid = 20;
int Xgrid = 20; 


float pointSpaceX = gridW/pointRow; 
float pointSpaceY = gridH/pointRow; 


//an array of points. the int samplePoints defines the number of points to store
//entry contains a 2 element array (for x and y coordinate of point) 

float pointArray2[][] = new float[samplePoints][2]; 

Orbit testOrbit, testOrbit2, testOrbitA, testOrbitB, testOrbitC; 
int c; 

float[][] testMesh; 
float[][] lastIterate; 

Orbit[] orbit_grid; 

//float pointArray[][] = {{-.5, sqrt(3.)/2.0}, {0., 1.}, {-1., 0}, {0., -1}};
//float pointArray2[][]; 
public void setup() {

  testMesh = rect_mesh(Xgrid, Ygrid, 1.0f, 1.0f, -.5f, -.5f); 
  orbit_grid = new Orbit[Xgrid*Ygrid]; //initialize a set of orbits corresponding to the mesh 
  lastIterate = new float[Xgrid*Ygrid][2]; 
  c = color(0, 0, 255);
  
  //initialize the orbits with start points contained in mesh
  
  for (int i =0; i < (Xgrid*Ygrid); i++) {
    //make a new orbit that starts at each point in the pixel mesh
    orbit_grid[i] = new Orbit(testMesh[i][0], testMesh[i][1], 20, c); 
    orbit_grid[i].iterate(19); 
    lastIterate[i] = orbit_grid[i].getPt(19); 
  }
   
  testOrbit = new Orbit(.5f, -.65f, 10, c); 
  testOrbitA = new Orbit(.5f, .65f, 10, c); 
  
  testOrbitB = new Orbit(-.5f, -.65f, 10, c); 
  testOrbitC = new Orbit(-.5f, .65f, 10, c); 
  
  testOrbit2 = new Orbit(.5f, .5f, 10, c); 
  pointArray2[0][0] = 0.0f; 
  pointArray2[0][1] = 0.0f; 
  
  testOrbit.iterate(9);
  testOrbitA.iterate(9);
  testOrbitB.iterate(9);
  testOrbitC.iterate(9);
  
  
  
   

  rectMode(CENTER); 
  
  //how big to make points
  pointsize = 2; 
  
  //meshspacing = 10;
  
  //initPointArray(); 
  
  noLoop(); 
}

public void draw() {
  //white background
  background(255); 
  
  //draw axes
  
  //axes are red
  stroke(255, 0, 0); 
  line(0, gridH/2, gridW, gridH/2);
  line(gridW/2, 0, gridW/2, gridH);
  line(gridW/2+unitDistance, gridH/2-20, gridW/2+unitDistance, gridH/2+20);
  line(gridW/2-unitDistance, gridH/2-20, gridW/2-unitDistance, gridH/2+20);
  line(gridW/2-20, gridH/2-unitDistance, gridW/2+20, gridH/2-unitDistance);
  line(gridW/2-20, gridH/2+unitDistance, gridW/2+20, gridH/2+unitDistance);
  
  noFill(); 
  //ellipse(400, 400, 533, 533);
  
  String PointLoc = "Pertubation Constant: \n" + pointArray2[0][0] + "+" + pointArray2[0][1]+"i";  
  fill(0, 0, 0); 
  stroke(0,0,0); 
  rect(90, 33, 170, 35); 
  fill(230, 230, 230); 
  text( PointLoc , 10, 30); 
  
  //graph the grid of test points in black
  stroke(0, 0,0); 
  drawPointArray(); 
  /*testOrbit.draw(); 
  testOrbit2.draw(); 
  testOrbitA.draw(); 
  testOrbitB.draw(); 
  testOrbitC.draw(); */
  
  for (int i = 0; i < (Xgrid*Ygrid); i++) {
    if (i==0) {
      fill(0); 
    }
    
    else fill(255, 0, 0); 
    //ellipse(real2Pix(testMesh[i])[0], real2Pix(testMesh[i])[1], pointsize, pointsize);
    orbit_grid[i].draw(); 
  }
 
  
}

//method to initialize array containing x and y coordinates of points being graphed
//evenly distributes the number of points (stored as samplePoints) across
//the pixel grid

public void initPointArray() {
  for (int i = 0; i < samplePoints; i++) {
    pointArray2[i][0] =  (i%pointRow)*(xrange/pointRow) -.5f*xrange; 
    pointArray2[i][1] = (i/pointRow)*(yrange/pointRow) -.5f*yrange;  
  }
}

//draw the array of points being graphed
//converts the coordinates of the point in R^2 to 
//the correct pixel coordinates
public void drawPointArray() {
  for (int i = 0; i < samplePoints; i++) {
    float pointX = gridW/2 +pointArray2[i][0]*pointspaceX;  
    float pointY = gridW/2 -pointArray2[i][1]*pointspaceY; 
    ellipse(pointX, pointY, pointsize, pointsize);
  }
}


public void iteratePointArray() {
  for (int i = 0; i < samplePoints; i++) {
    pointArray2[i] = iteratePoint(pointArray2[i][0], pointArray2[i][1]);
  }
}


//function to take a single point in 2 dimensions and apply whatever map/function you like
public float[] iteratePoint(float X, float Y) { 
  
  float Point[] = {X, Y};  
  
  //for doubling map z -> z^2 + c
  //(x,y) -> (x^2+y^2 +c_r, 2ab + c_i) 
  // note: c is an imaginary number. c_r is the real component, c_i is imaginary component
  
  
  //defining the map here for now...ie, what math do we do with the point being iterated
  float ItdPoint[] = {X*X -(Y*Y)+ptb[0], 2*Y*X+ptb[1]};  
  return ItdPoint;
}

public float[] iteratePoint2(float[] thePoint) { 
  
  float X = thePoint[0]; 
  float Y = thePoint[1];  
  
  //for doubling map z -> z^2 + c
  //(x,y) -> (x^2+y^2 +c_r, 2ab + c_i) 
  // note: c is an imaginary number. c_r is the real component, c_i is imaginary component
  
  
  //defining the map here for now...ie, what math do we do with the point being iterated
  float ItdPoint[] = {X*X -(Y*Y)+ptb[0], 2*Y*X+ptb[1]};  
  return ItdPoint;
}


//CLICK MOUSE TO ITERATE THE MAP AND SEE WHAT HAPPENS TO THE GRID OF POINTS 
public void mousePressed() {
  pointArray2[0][0] = (mouseX/(gridW))*xrange - (xrange/2.0f); 
  pointArray2[0][1] = (yrange/2.0f)-(mouseY/(gridW))*yrange; 
  
  testOrbit2.setStart((mouseX/(gridW))*xrange - (xrange/2.0f), (yrange/2.0f)-(mouseY/(gridW))*yrange); 
  
  ptb[0] = (mouseX/(gridW))*xrange - (xrange/2.0f); 
  ptb[1] = (yrange/2.0f)-(mouseY/(gridW))*yrange; 
  
  for (int i =0; i < (Xgrid*Ygrid); i++) {
    //make a new orbit that starts at each point in the pixel mesh
    //orbit_grid[i] = new Orbit(testMesh[i][0], testMesh[i][1], 20, c); 
    orbit_grid[i].iterate(19); 
    lastIterate[i] = orbit_grid[i].getPt(19); 
  }
  
  redraw();
  
  
}

public void  keyPressed() {
 
  if (key==ENTER||key==RETURN) {
  
    //iteratePointArray(); 
    //testOrbit2.nextPoint(); 
    //redraw();
    
    float[] thepoint = pointAvg(lastIterate); 
    println("Possible fixed point: " + thepoint[0] +", " + thepoint[1]); 

  } 
}




//class Orbit contains a list of points in 2D space visited starting at a given point
//being iterated period times 




//method to convert real number to pixel location in one dimension
public float[] real2Pix(float input[]) {
  float xPix = gridW/2 +input[0]*pointspaceX; 
  float yPix = gridW/2 -input[1]*pointspaceY;
  float[] output = {xPix, yPix}; 
  return output;
}


class Orbit {
  float startX, startY; //starting point
  int period; //how long is period of orbit (ie, how many iterations to store) 
  float[][] points;  //main array storing iterated point locations. 
  int oColor; 
  boolean visible; 
  
  int numIterations; //count how many times we've iterated so far. 
  
  Orbit (float x, float y, int p, int theColor) {
    startX = x; 
    startY = y; 
    period = p; 
    numIterations = 0; 
    oColor = theColor; 
    visible = true; 
    
    
    points = new float[period][2]; 
    points[0][0] = startX;
    points[0][1] = startY; 
  }
  
    Orbit (int p, int theColor) {
    period = p; 
    numIterations = 0; 
    oColor = theColor; 
    
    
    points = new float[period][2]; 
  }
  
  public float[] getPt(int index) {
    return points[index]; 
  }
  
  public void draw() {
    int opacity = 20; 
    if (visible) {
    stroke(oColor, opacity); 
    fill(oColor); 
    //draw an ellipse at the original point location
    //ellipse(real2Pix(points[0])[0], real2Pix(points[0])[1], pointsize, pointsize); 
    for (int i = 0; i < numIterations; i++ ) {
        //opacity += 255/numIterations; 
        stroke(oColor, opacity); 
        line(real2Pix(points[i])[0], real2Pix(points[i])[1], real2Pix(points[i+1])[0], real2Pix(points[i+1])[1]); 
        //println("line from:" + real2Pix(points[i])[0] +", " + real2Pix(points[i])[1] + "to " + real2Pix(points[i+1])[0] +", " + real2Pix(points[i+1])[1]); 
      }
    }
  }
  
  public void show() {
    visible = true; 
  }
  
  public void hide() {
    visible = false; 
  }
  
  
  public void setStart(float x, float y) {
    points[0][0] = x;
    points[0][1] = y; 
    
  }
  
  public void nextPoint() {
    points[numIterations+1] = iteratePoint2(points[numIterations]); 
    numIterations +=1; 
    
  }
  
  public void iterate(int iterations) {
    numIterations = 0; 
    for (int i = 0; i < iterations; i++) {
      this.nextPoint(); 
    }
  }
}



//method to construct a mesh of test points 
public float[][] rect_mesh(int numX, int numY, float W, float H, float BLx, float BLy) {
  int numPoints = (numX*numY); 
  float[][] the_mesh = new float[numPoints][2]; 
  float spacingX = W/PApplet.parseFloat(numX); 
  float spacingY = H/PApplet.parseFloat(numY); 
  
   for (int i = 0; i < numPoints; i++) {
   the_mesh[i][0] =  BLx + (i%numX)*spacingX +.5f*spacingX; //x coordinate of point in mesh (as a real number)
    the_mesh[i][1] = BLy + (i/numX)*spacingY +.5f*spacingY; //y coordinate of point in mesh (as a real number)
    }
  
  return the_mesh; //returns an array of points in real 2D space
}


//method to take a bunch of points and get their "average"... useful for estimating fixed point. 
public float[] pointAvg(float[][] points) {
  
  float[] avPoint = new float[2]; 
  
  float x = 0.f; 
  float y = 0.f; 
  
  for (int i=0; i < points.length; i++) {
    x += points[i][0]; 
    y += points[i][1]; 
  }
  
  x = x/PApplet.parseFloat(points.length); 
  y = y/PApplet.parseFloat(points.length);
  
  avPoint[0] = x; 
  avPoint[1] = y; 
  
  return avPoint;
}

  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Test_trajectories" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
