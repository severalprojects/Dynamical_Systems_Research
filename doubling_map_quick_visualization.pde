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

float ptb[] = {-.5, .5};  

float gridW = 800; 
float gridH = 800; 

//what range of 2D space to represent (centered at 0,0) 
float xrange = 3.0; 
float yrange = 3.0; 

//convert pixel space on computer screen to real number space on x and y axis
float pointspaceX = gridW/xrange; 
float pointspaceY = gridH/yrange; 

int sizeW = int(gridW); 
int sizeH = int(gridH); 

//one pixel represents this much "distance" on x or y axis
float unitDistance = gridW/xrange;  



float meshspacing; 
float pointsize;

int pointRow = 200; //how many points in a row of the grid

//total number of points to render
int samplePoints = (pointRow*pointRow); 

float pointSpaceX = gridW/pointRow; 
float pointSpaceY = gridH/pointRow; 


//an array of points. the int samplePoints defines the number of points to store
//entry contains a 2 element array (for x and y coordinate of point) 

float pointArray2[][] = new float[samplePoints][2]; 

//float pointArray[][] = {{-.5, sqrt(3.)/2.0}, {0., 1.}, {-1., 0}, {0., -1}};
//float pointArray2[][]; 
void setup() {

  size(800, 800); 

  rectMode(CENTER); 
  
  //how big to make points
  pointsize = 1; 
  
  //meshspacing = 10;
  
  initPointArray(); 
  
  noLoop(); 
}

void draw() {
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
  
  //graph the grid of test points in black
  stroke(0, 0,0); 
  drawPointArray(); 
 
  
}

//method to initialize array containing x and y coordinates of points being graphed
//evenly distributes the number of points (stored as samplePoints) across
//the pixel grid

void initPointArray() {
  for (int i = 0; i < samplePoints; i++) {
    pointArray2[i][0] =  (i%pointRow)*(xrange/pointRow) -.5*xrange; 
    pointArray2[i][1] = (i/pointRow)*(yrange/pointRow) -.5*yrange;  
  }
}

//draw the array of points being graphed
//converts the coordinates of the point in R^2 to 
//the correct pixel coordinates
void drawPointArray() {
  for (int i = 0; i < samplePoints; i++) {
    float pointX = gridW/2 +pointArray2[i][0]*pointspaceX;  
    float pointY = gridW/2 -pointArray2[i][1]*pointspaceY; 
    ellipse(pointX, pointY, pointsize, pointsize);
  }
}


void iteratePointArray() {
  for (int i = 0; i < samplePoints; i++) {
    pointArray2[i] = iteratePoint(pointArray2[i][0], pointArray2[i][1]);
  }
}


//function to take a single point in 2 dimensions and apply whatever map/function you like
float[] iteratePoint(float X, float Y) { 
  
  float Point[] = {X, Y};  
  
  //for doubling map z -> z^2 + c
  //(x,y) -> (x^2+y^2 +c_r, 2ab + c_i) 
  // note: c is an imaginary number. c_r is the real component, c_i is imaginary component
  
  
  //defining the map here for now...ie, what math do we do with the point being iterated
  float ItdPoint[] = {X*X -(Y*Y)+ptb[0], 2*Y*X+ptb[1]};  
  return ItdPoint;
}


//CLICK MOUSE TO ITERATE THE MAP AND SEE WHAT HAPPENS TO THE GRID OF POINTS 
void mousePressed() {
  iteratePointArray();  
  redraw();
}

void keyPressed() { 
 saveFrame("output_0_0/JS_####.png");
}
