import java.util.ArrayList;
import java.util.List;
import java.util.Random;//можно видос запилить бекоз мало чего на джава фх особенно на русском особенно с участием меня)))
//
//надо сделать по кнопке рестарт, препятсвия
//главное крч сделать чтобы если ты вправо движешься нельзя было нажать влево. типо как у телки варианта только два
//потом тетрис и бомбера и надо заканчивать с javafx
/*scene.setOnKeyPressed(new EventHandler<KeyEvent>(){ или scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> { чтобы клавишу нажатую на сцене обработать
public void handle(KeyEvent event) { если второй варик эту штуку не надо..
и пишим if (event/key.getCode() == KeyCode.W тогда.
ВТОРОЙ ВАРИК ЛУЧШЕ. НО СУТЬ В ТОМ ЧТО ЭТО ОБРАБОТКА КЛАВИШ!!
*/
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
public class MySnake extends Application {
    static Image image=new Image("http://i.mirozor.ru/u/36/fd209654e111e7981ec8b9a2a3b318/-/favicon_335.png");
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;//короче это тут по другому работает и означает что 20 клеток(размером cornersize)
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;//это крч размер одной клетки (яблока)
    static List <Corner> zabor = new ArrayList<>();
    static List<Corner> snake = new ArrayList<>();
     static Dir dir = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();
    static CheckBox pause=new CheckBox();
    static boolean restart = true;
    public enum Dir {
        left, right, up, down
    }
    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public void start(Stage stage) {
        try {

            Label label=new Label("Space - restart the game\n" + "Enter - pause the game");
            label.setFont(new Font("", 18));
            pause.setSelected(true);
        newFood();
        VBox root =new VBox();
        Canvas c =new Canvas(width*cornersize,height*cornersize);
        GraphicsContext gc =c.getGraphicsContext2D();
        root.getChildren().add(c);
       //background
            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }
                    if ((now - lastTick > 1000000000 /speed)&&pause.isSelected()) {
                        lastTick = now;
                        tick(gc);
                    }
                }

            }.start();
            FlowPane flowPane=new FlowPane(root,label);flowPane.setPrefWidth(width*cornersize+200);
        Scene scene =new Scene(new Group(flowPane),width*cornersize+200,height*cornersize);
        scene.setOnKeyPressed(event -> {
            KeyCode key=event.getCode();
            if(key == KeyCode.UP&&dir!=Dir.down){
                dir=Dir.up;
            } else if(key == KeyCode.DOWN&&dir!=Dir.up){
                dir=Dir.down;
            } else if(key == KeyCode.LEFT&&dir!=Dir.right){
                dir=Dir.left;
            } else if(key == KeyCode.RIGHT&&dir!=Dir.left){
                dir=Dir.right;
            } if (key==KeyCode.ENTER)pause.fire();
            if (key==KeyCode.SPACE){
                gameOver=false;
                firstSnake();
                zabor();
                speed=5;
                newFood();

            }
        });
            firstSnake();
            zabor();
            stage.setScene(scene); stage.setResizable(false);
            stage.setTitle("SNAKE GAME");stage.show();
}catch (Exception e){
    e.printStackTrace();
}
}

    public static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(50));
            gc.fillText("Game Over", 100, 250);
            return;
        }
        //movement of the tail
        for(int i = snake.size()-1; i>=1;i--){
            snake.get(i).x =snake.get(i-1).x;
            snake.get(i).y=snake.get(i-1).y;
        }//movement of the head
        switch (dir) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

        }

        //eat Food
        if(foodY == snake.get(0).y && foodX == snake.get(0).x){
            snake.add(new Corner(-1,-1));
            newFood();
        }
        //slam
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }//crashed into a fence
        for (int i = 0; i <zabor.size() ; i++) {
            if(snake.get(0).x==zabor.get(i).x&&snake.get(0).y==zabor.get(i).y){
                gameOver=true;
            }

        }


        //frontend
        //all field//rect-прямоугольник
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width*cornersize,height*cornersize);

        //Score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score: " + (speed - 6), 10, 30);

        //color food
        Color cc=Color.WHITE;
        switch (foodcolor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        gc.setFill(cc);//отрисовка яблока(овала)
        gc.fillOval(foodX*cornersize,foodY*cornersize,cornersize,cornersize);
                        //1 и 2 это от какой точки начать рисование(видимо начало снизу справа яблока)
                        //3 и 4 это размер (соответсвенно корнер сайз это одно поле как бы)) как часть змейки
        //snake
        for (int i = 1; i <snake.size() ; i++) {
            gc.setFill(Color.LIGHTSEAGREEN);
            gc.fillRect(snake.get(i).x*cornersize,snake.get(i).y * cornersize,cornersize-1,cornersize-1);
            gc.setFill(Color.GOLD);
            gc.fillRect(snake.get(i).x*cornersize,snake.get(i).y * cornersize,cornersize-2,cornersize-2);
        }

        gc.drawImage(image,snake.get(0).x*cornersize,snake.get(0).y * cornersize,cornersize-1,cornersize-1);
        //fence
        for(Corner c: zabor){
            gc.setFill(Color.BLUEVIOLET);
            gc.fillRect(c.x*cornersize,c.y * cornersize,cornersize-1,cornersize-1);//-1 для чтоб видно границы
        }


    }



    public static void newFood(){
        for (int i = 0; i < zabor.size(); i++) {


        if(((foodX=rand.nextInt(height))!=zabor.get(i).x)&&(foodY=rand.nextInt(width))!=zabor.get(i).y){
    }}
        foodcolor = rand.nextInt(5);
        speed++;
    }

    public static void firstSnake () {
        snake.clear();
        snake.add(0,new Corner(width / 2,height /2));
        snake.add(1,new Corner(width / 2,height /2));
        snake.add(2,new Corner(width / 2,height /2));
    }
    public static void zabor (){
        zabor.clear();
        zabor.add(new Corner(rand.nextInt(width),rand.nextInt(height)));
        zabor.add(new Corner(zabor.get(0).x-1,zabor.get(0).y));
        zabor.add(new Corner(zabor.get(0).x-2,zabor.get(0).y));
    }
    public static void main(String[] args) {
        launch(args);
    }
}
