package interview;

public class LTest {
    //input: 5
    //1 * 2 * 3 * 4

    public static int mutiple(int n){
        if (n <= 1){
            return 1;
        }
        if (n == 2){
            return 2;
        }
        return mutiple(n-1) * n;
    }

    public static void main(String[] args) {
        System.out.println(mutiple(5));
    }


}
