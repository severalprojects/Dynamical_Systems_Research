float w = 3;
float h = (w * height) / (width);

//these variables store corners of 'window' for zooming.. 
int wStartX = 0; 
int wStartY = 0; 
int wEndX = 0; 
int wEndY = 0; 
float zBoxSize = 0;

//SET PERTURBATION CONSTANT c HERE
float rPerturb = -.7; 
float iPerturb = .2; 

// Start at negative half the width and height // centers picture at the origin to start
float xmin = -w/2;
float xmax = xmin + w;
float ymin = -h/2;
float ymax = ymin + h;

float gridW = 800; 
float gridH = 800; 

float pointspaceX = gridW/w; 
float pointspaceY = gridH/h; 

void setup() {
size(800, 800);
noLoop();


// Establish a range of values on the complex plane
// A different range will allow us to "zoom" in or out on the fractal

// It all starts with the width, try higher or lower values
}
//float xmin = .65;
//float ymin = .65;

// Make sure we can write to the pixels[] array.
// Only need to do this once since we don't do any other drawing.
void draw() {
background(255);
loadPixels();

// Maximum number of iterations for each point on the complex plane
int maxiterations = 100;

// x goes from xmin to xmax

// y goes from ymin to ymax


// Calculate amount we increment x,y for each pixel
float dx = (xmax - xmin) / (width);
float dy = (ymax - ymin) / (height);

// Start y
float y = ymin;
for (int j = 0; j < height; j++) {
  // Start x
  float x = xmin;
  for (int i = 0; i < (width); i++) {
    //test the map here!!!!!
    // Now we test, as we iterate z = z^2 + cm does z tend towards infinity?
    float a = x;
    float b = y;
    int n = 0;
    while (n < maxiterations) {
      float aa = a * a;
      float bb = b * b;
      float twoab = 2.0 * a * b;
      a = aa - bb + rPerturb;
      b = twoab + iPerturb;
      // Infinty in our finite world is simple, let's just consider it 16
      if (dist(a, b, 0, 0) > 100000) {
        break;  // Bail
      }
      n++;
    }

    // We color each pixel based on how long it takes to get to infinity
    // If we never got there, let's pick the color black
    if (n == maxiterations) { //if we get through max iterations without going off, we are probably in julia set! 
      pixels[i+j*(width)] = color(0,0,0);
    } else {
      // Gosh, we could make fancy colors here if we wanted
      float norm = map(n, 0, maxiterations, 0, 1);
      float colorCode = map(n, 0, maxiterations, 0, 2000); 
      float redV = 0; 
      float greenV = 0; 
      float blueV =0; 
      
      //redV = colorCode % 255; 
      if (colorCode <= 255) {
        redV = colorCode; 
      }
      else if (colorCode <= 510) {
        redV = 255; 
        greenV = max(0, colorCode - 255); 
      }
      else {
        redV = 255; 
        greenV = 255; 
        blueV = max(0, colorCode - 510); 
      }
      //pixels[i+j*width] = color(map(sqrt(norm), 0, 1, 0, 255), 0, map(sqrt(norm), 0, 1, 0, 255));
      
      //new color idea*/
      pixels[i+j*(width)] = color(redV, greenV, blueV);
      
      //println("R "+ redV + "G: "+ greenV + "B: " +blueV);
    }
    x += dx;
  }
  y += dy;
}
//text("testing", 10, 10);
updatePixels();

fill(0);
rect(10, 10, 450, 70); 
textSize(12);
String rangeText = "range: [" + xmin + ", " + xmax + "] X [" + ymin + ", " + ymax + "]";  
fill(0, 180, 180); 
text( rangeText , 15, 30); 
text("window width: "+ (xmax - xmin), 15, 50); 
text("perturbation constant:" + rPerturb + " + " + iPerturb + "i", 15, 70); 
//text("x: "+ (xmin + (((xmax - xmin)*mouseX) / (width))) + " y: "+ (ymin+(((ymax - ymin)*mouseY) / (width))), 10, 70); 
/*textSize(12);
String rangeText = "range: [" + xmin + ", " + xmax + "] X [" + ymin + ", " + ymax + "]";  
fill(0, 180, 180); 
text( rangeText , 10, 30); 
text("window width: "+ (xmax - xmin), 10, 50); */

//ellipse(0,0, 400,400); 
}
void mousePressed() {
  println("mouse pressed. X:" + mouseX + "Y: " +mouseY); 
  wStartX = mouseX; 
  wStartY = mouseY; 

} 

void mouseReleased() {
  println("mouse released. X:" + mouseX + "Y: " +mouseY); 
  wEndX = mouseX; 
  wEndY = mouseY; 
  zBoxSize = max(abs(wStartX - wEndX), abs(wStartY - wEndY)); 
  println("zbox size: " +zBoxSize); 
  xmin = map(wStartX, 0, width, xmin, xmax); //convert pixel starting pointX to real number on x-axis
  ymin = map(wStartY, 0, height, ymin, ymax); //convert pixel starting pointY to real number on y-axis
  println("xmin: "+xmin); 
  println("ymin: "+ymin); 
  w = (zBoxSize/( width) )*w ;  //scale pixel width of zoom box to range on x-axis
  h = (zBoxSize/height)*h; //scale pixel height of zoom boz to range on y-axis
  
  xmax = xmin + w; 
  ymax = ymin + h; 
  println("new Width: "+w); 
  println("new Height: "+h);
  
  redraw(); 
  
//float xmin = -w/2;
//float ymin = -h/2;
  

} 

void keyPressed() { 
 saveFrame("output_0_0/JS_####.png");
}
