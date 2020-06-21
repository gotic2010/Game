import java.util.Scanner;

public class Game {
    private static char[][] fieldOfFire;
    private static final int size = 3;
    private static final char empty = '•';
    private static final char dagger = 'X';
    private static final char zero = 'O';
    private static Scanner scanner = new Scanner(System.in);
    private static int[][][] winningCombination = new int[8][3][2];
    private static int[][] weightCage = new int[size][size];
    public static void main(String[] args) {
        fill();
        print();
        fillingInWinningCombinations();
        while(true){
            courseOfHuman();
            if(end(dagger)){
                break;
            }
            courseOfComputer();
            if(end(zero)){
                break;
            }
        }
    }
    //Заполнение массива пустыми областями
    static void fill(){
        fieldOfFire = new char[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                fieldOfFire[i][j] = empty;
            }
        }
    }
    //Вывести игровое поле в консоль
    static void print(){
        for(int i = 0; i <= size; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for(int i = 0; i < size; i++){
            System.out.print(i + 1 + " ");
            for(int j = 0; j < size; j++) {
                System.out.print(fieldOfFire[i][j] + " ");

            }
            System.out.println();
        }
    }
    //Ход человека
    static void courseOfHuman() {
        int horizontal;
        int vertical;
        do {
            System.out.println("Введите координаты клетки");
            horizontal=scanner.nextInt()-1;
            vertical=scanner.nextInt()-1;

        } while (!correct(horizontal,vertical));
        fieldOfFire[vertical][horizontal]=dagger;
        print();

    }
    //Правильно ли ввели координаты
    static boolean correct(int x,int y){
        if((x < size && y < size) && (x >= 0 && y >= 0) && fieldOfFire[y][x] == empty){
            return true;
        }
        return false;
    }
    //Условие конца игры
    static boolean end ( char ch){
        if(victory(ch)){
            System.out.println("Выйграли: " + ch);
            return true;
        }
        else if (isFilled()){
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }
    //Ход компьютера
    static void courseOfComputer() {
        System.out.println("Ход компьютера");
        if(zeroVictory()){
            print();
        }
        else if(daggerVictory()){
            print();
        }
        else {
            zeroingAnArrayOfWeightValues();
            calculatingOptimalStroke();
            print();

        }
    }
    static boolean zeroVictory(){
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if(fieldOfFire[i][j]==empty){
                    fieldOfFire[i][j]=zero;
                    if(victory(zero)){
                        return true;
                    }
                    else {
                        fieldOfFire[i][j]=empty;
                    }
                }
            }
        }
        return false;
    }
    static boolean daggerVictory(){
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if(fieldOfFire[i][j]==empty){
                    fieldOfFire[i][j]=dagger;
                    if(victory(dagger)){
                        fieldOfFire[i][j]=zero;
                        return true;
                    }
                    else {
                        fieldOfFire[i][j]=empty;
                    }
                }
            }
        }
        return false;
    }
    //Заполнены ли все клетки
    static boolean isFilled(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(fieldOfFire[i][j]==empty){
                    return false;
                }
            }
        }
        return true;
    }
    //Проверка выигрышной комбинации
    static boolean victory(char symbol){
        return (fieldOfFire[0][0] == symbol && fieldOfFire[0][1] == symbol && fieldOfFire[0][2] == symbol) ||
                (fieldOfFire[1][0] == symbol && fieldOfFire[1][1] == symbol && fieldOfFire[1][2] == symbol) ||
                (fieldOfFire[2][0] == symbol && fieldOfFire[2][1] == symbol && fieldOfFire[2][2] == symbol) ||
                (fieldOfFire[0][0] == symbol && fieldOfFire[1][0] == symbol && fieldOfFire[2][0] == symbol) ||
                (fieldOfFire[0][1] == symbol && fieldOfFire[1][1] == symbol && fieldOfFire[2][1] == symbol) ||
                (fieldOfFire[0][2] == symbol && fieldOfFire[1][2] == symbol && fieldOfFire[2][2] == symbol) ||
                (fieldOfFire[0][0] == symbol && fieldOfFire[1][1] == symbol && fieldOfFire[2][2] == symbol) ||
                (fieldOfFire[2][0] == symbol && fieldOfFire[1][1] == symbol && fieldOfFire[0][2] == symbol);
    }
    //заполнение выигрышных комбинаций
    static void fillingInWinningCombinations(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 2; k++){
                    if(i<3){
                        if(k==0) {
                            winningCombination[i][j][k] = j;
                        }
                        else {
                            winningCombination[i][j][k] = i;
                        }
                    }
                    else if(i < 6){
                        if(k==0){
                            winningCombination[i][j][k] = i-3;
                        }
                        else {
                            winningCombination[i][j][k] = j;
                        }
                    }
                    else if(i==6){
                        winningCombination[i][j][k] = j;
                    }
                    else {
                        if(k==0){
                            if(j==0){
                                winningCombination[i][j][k]=2;
                            }
                            else if(j==1){
                                winningCombination[i][j][k]=1;
                            }
                            else{
                                winningCombination[i][j][k]=0;
                            }
                        }
                        else {
                            winningCombination[i][j][k] = j;
                        }
                    }
                }
            }
        }
    }
    //расчет оптимального хода, на основе больших доступных выигрышных комбинаций
    static void calculatingOptimalStroke(){
        int max = 0;
        int x = 2;
        int y = 2;
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if(fieldOfFire[i][j]==empty){
                    for (int u = 0; u < 8; u++) {
                        for (int g = 0; g < 3; g++) {
                            if(winningCombination[u][g][0] == j && winningCombination[u][g][1] == i){
                                if(checkingWinningCombination(u)){
                                    weightCage[i][j]+=1;
                                }
                            }

                        }
                    }
                }
            }
        }
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if(weightCage[i][j] >= max){
                    max=weightCage[i][j];
                    x = j;
                    y = i;
                }
            }
        }
        fieldOfFire[y][x]=zero;
    }
    //проверяет подходит ли выигрышная комбинация
    static boolean checkingWinningCombination(int i){
        if(fieldOfFire[winningCombination[i][0][1]][winningCombination[i][0][0]]==dagger||
           fieldOfFire[winningCombination[i][1][1]][winningCombination[i][1][0]]==dagger||
           fieldOfFire[winningCombination[i][2][1]][winningCombination[i][2][0]]==dagger)
            return false;
        else {
            return true;
        }
    }
    //обнуление массива с весовыми значениями оптимального хода нулями
    static void zeroingAnArrayOfWeightValues(){
        for(int i = 0; i > size; i++){
            for (int j = 0; j > size; j++){
                weightCage[i][j] = 0;
            }
        }
    }
}
